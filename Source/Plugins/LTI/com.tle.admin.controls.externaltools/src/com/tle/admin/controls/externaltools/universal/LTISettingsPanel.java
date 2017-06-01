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

package com.tle.admin.controls.externaltools.universal;

import com.tle.admin.controls.universal.UniversalControlSettingPanel;
import com.tle.common.wizard.controls.universal.UniversalSettings;

@SuppressWarnings("nls")
public class LTISettingsPanel extends UniversalControlSettingPanel
{

	public LTISettingsPanel()
	{
		super();
	}

	@Override
	protected String getTitleKey()
	{
		return "com.tle.admin.controls.externaltools.settings.title";
	}

	@Override
	public void load(UniversalSettings state)
	{
		// Nothing
	}

	@Override
	public void removeSavedState(UniversalSettings state)
	{
		// Nothing
	}

	@Override
	public void save(UniversalSettings state)
	{
		// Nothing
	}
}

