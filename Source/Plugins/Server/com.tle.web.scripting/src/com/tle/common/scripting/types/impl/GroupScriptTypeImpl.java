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

package com.tle.common.scripting.types.impl;

import com.dytech.edge.common.valuebean.DefaultGroupBean;
import com.dytech.edge.common.valuebean.GroupBean;
import com.tle.common.scripting.types.GroupScriptType;

/**
 * @author aholland
 */
public class GroupScriptTypeImpl extends DefaultGroupBean implements GroupScriptType
{
	private static final long serialVersionUID = 1L;

	public GroupScriptTypeImpl(GroupBean group)
	{
		super(group.getUniqueID(), group.getName());
	}
}
