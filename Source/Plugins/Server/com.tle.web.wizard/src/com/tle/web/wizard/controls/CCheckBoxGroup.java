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

package com.tle.web.wizard.controls;

import com.dytech.edge.wizard.beans.control.RadioGroup;
import com.dytech.edge.wizard.beans.control.WizardControl;
import com.tle.core.wizard.controls.WizardPage;

/**
 * Provides a base data model for CheckBox-like controls.
 * 
 * @author Nicholas Read
 */
public class CCheckBoxGroup extends OptionCtrl
{
	private static final long serialVersionUID = 1L;
	private String type;

	public CCheckBoxGroup(WizardPage page, int controlNumber, int nestingLevel, WizardControl controlBean)
	{
		super(page, controlNumber, nestingLevel, controlBean);
		if( controlBean instanceof RadioGroup )
		{
			type = "radio"; //$NON-NLS-1$
		}
		else
		{
			type = "checkbox"; //$NON-NLS-1$
		}
	}

	public String getType()
	{
		return type;
	}
}
