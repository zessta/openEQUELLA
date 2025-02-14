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
import { API_BASE_URL } from "../AppConfig";

export const getSearchSettingsFromServer =
  (): Promise<OEQ.SearchSettings.Settings> =>
    OEQ.SearchSettings.getSearchSettings(API_BASE_URL);

export const saveSearchSettingsToServer = (
  settings: OEQ.SearchSettings.Settings
): Promise<void> =>
  OEQ.SearchSettings.updateSearchSettings(API_BASE_URL, settings);

export const defaultSearchSettings: OEQ.SearchSettings.Settings = {
  searchingShowNonLiveCheckbox: false,
  searchingDisableGallery: false,
  searchingDisableVideos: false,
  searchingDisableOwnerFilter: false,
  searchingDisableDateModifiedFilter: false,
  fileCountDisabled: false,
  defaultSearchSort: "rank",
  authenticateFeedsByDefault: false,

  urlLevel: 0,
  titleBoost: 0,
  descriptionBoost: 0,
  attachmentBoost: 0,
};
