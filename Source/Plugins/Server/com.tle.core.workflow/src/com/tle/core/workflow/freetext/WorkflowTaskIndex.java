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

package com.tle.core.workflow.freetext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Singleton;

import org.apache.lucene.document.Document;

import com.dytech.edge.queries.FreeTextQuery;
import com.tle.beans.item.ItemIdKey;
import com.tle.common.searching.Search;
import com.tle.core.guice.Bind;
import com.tle.core.services.item.TaskResult;
import com.tle.freetext.index.MultipleIndex;

@Bind
@Singleton
public class WorkflowTaskIndex extends MultipleIndex<TaskResult>
{
	@Override
	protected Set<String> getKeyFields()
	{
		return new HashSet<String>(Arrays.asList(FreeTextQuery.FIELD_UNIQUE, FreeTextQuery.FIELD_ID,
			FreeTextQuery.FIELD_WORKFLOW_TASKID));
	}

	@Override
	public String getIndexId()
	{
		return Search.INDEX_TASK;
	}

	@Override
	protected TaskResult createResult(ItemIdKey key, Document doc, float relevance, boolean sortByRelevance)
	{
		return new TaskResult(key, doc.get(FreeTextQuery.FIELD_WORKFLOW_TASKID), relevance, sortByRelevance);
	}
}
