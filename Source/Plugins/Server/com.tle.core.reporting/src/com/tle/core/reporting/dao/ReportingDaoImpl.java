/*
 * Copyright 2017 Apereo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tle.core.reporting.dao;

import java.util.List;

import javax.inject.Singleton;

import com.tle.beans.entity.report.Report;
import com.tle.core.dao.impl.AbstractEntityDaoImpl;
import com.tle.core.guice.Bind;
import com.tle.core.user.CurrentInstitution;

@Bind(ReportingDao.class)
@Singleton
public class ReportingDaoImpl extends AbstractEntityDaoImpl<Report> implements ReportingDao
{
	public ReportingDaoImpl()
	{
		super(Report.class);
	}

	@Override
	@SuppressWarnings({"unchecked", "nls"})
	public Report findByReportFilename(String filename)
	{
		int folder = filename.lastIndexOf('/');
		if( folder != -1 )
		{
			filename = filename.substring(folder + 1);
		}
		List<Report> reportsByName = getHibernateTemplate().findByNamedParam(
			"from Report where (filename = :filename or filename like :filelike) and institution = :inst",
			new String[]{"filename", "filelike", "inst"},
			new Object[]{filename, "%/" + filename, CurrentInstitution.get()});
		if( reportsByName.isEmpty() )
		{
			return null;
		}
		return reportsByName.iterator().next();
	}
}
