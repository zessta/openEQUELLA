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

package com.tle.core.institution.migration;

import com.dytech.devlib.PropBagEx;
import com.tle.core.filesystem.SubTemporaryFile;
import com.tle.core.filesystem.TemporaryFileHandle;
import com.tle.core.institution.convert.ConverterParams;

public interface ItemXmlMigrator
{
	void beforeMigrate(ConverterParams params, TemporaryFileHandle staging, SubTemporaryFile file) throws Exception;

	boolean migrate(ConverterParams params, PropBagEx xml, SubTemporaryFile file, String filename) throws Exception;

	void afterMigrate(ConverterParams params, SubTemporaryFile file) throws Exception;
}
