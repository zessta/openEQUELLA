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

package com.tle.core.workflow.operations;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.tle.beans.item.Item;

public class SetItemThumbnailOperation extends AbstractWorkflowOperation
{
	private final String thumb;

	@AssistedInject
	private SetItemThumbnailOperation(@Assisted String thumb)
	{
		this.thumb = thumb;
	}

	@Override
	public boolean execute()
	{
		Item item = getItem();
		item.setThumb(thumb);
		params.setUpdateSecurity(true);
		return true;
	}
}
