/*
 * Licensed to The Apereo Foundation under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * The Apereo Foundation licenses this file to you under the Apache License,
 * Version 2.0, (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import * as OEQ from "@openequella/rest-api-client";

export const getMimeTypeFilters: OEQ.SearchFilterSettings.MimeTypeFilter[] = [
  {
    id: "fe79c485-a6dd-4743-81e8-52de66494632",
    name: "Image filter",
    mimeTypes: ["Image/png", "Image/jpeg"],
  },
  {
    id: "fe79c485-a6dd-4743-81e8-52de66494631",
    name: "PDF filter",
    mimeTypes: ["Application/pdf"],
  },
];
