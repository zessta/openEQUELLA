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

package com.tle.admin.gui.common.actions;

import javax.swing.Action;
import javax.swing.JButton;

/**
 * @author Nicholas Read
 */
public class JTextlessButton extends JButton
{
	private static final long serialVersionUID = 1L;

	public JTextlessButton(Action action)
	{
		putClientProperty("hideActionText", true); //$NON-NLS-1$
		setAction(action);
	}
}
