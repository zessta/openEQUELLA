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

package com.tle.core.xslt.ext;

import javax.inject.Inject;

import org.w3c.dom.Node;

import com.dytech.edge.common.valuebean.UserBean;
import com.tle.common.util.UserXmlUtils;
import com.tle.core.services.user.UserService;

public class Users
{
	@Inject
	private static UserService userService;

	public Node getUserById(String id)
	{
		UserBean bean = userService.getInformationForUser(id);
		return UserXmlUtils.getUserAsXml(bean).getRootElement();
	}
}
