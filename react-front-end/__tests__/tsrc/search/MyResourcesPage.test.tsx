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
import { MuiThemeProvider } from "@material-ui/core";
import { createTheme } from "@material-ui/core/styles";
import * as OEQ from "@openequella/rest-api-client";
import { CurrentUserDetails } from "@openequella/rest-api-client/dist/LegacyContent";
import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { createMemoryHistory } from "history";
import * as React from "react";
import { Router } from "react-router-dom";
import { getSearchResult } from "../../../__mocks__/SearchResult.mock";
import { getCurrentUserMock } from "../../../__mocks__/UserModule.mock";
import { AppContext } from "../../../tsrc/mainui/App";
import { nonDeletedStatuses } from "../../../tsrc/modules/SearchModule";
import { defaultSearchSettings } from "../../../tsrc/modules/SearchSettingsModule";
import { guestUser } from "../../../tsrc/modules/UserModule";
import MyResourcesPage from "../../../tsrc/search/MyResourcesPage";
import type { MyResourcesType } from "../../../tsrc/search/MyResourcesPageHelper";
import { defaultSearchPageOptions } from "../../../tsrc/search/SearchPageHelper";
import { languageStrings } from "../../../tsrc/util/langstrings";
import {
  initialiseEssentialMocks,
  mockCollaborators,
  queryRefineSearchComponent,
  waitForSearch,
} from "./SearchPageTestHelper";
import "@testing-library/jest-dom/extend-expect";

const history = createMemoryHistory();
const defaultTheme = createTheme({
  props: { MuiWithWidth: { initialWidth: "md" } },
});
const buildMyResourcesSearchPageOptions = (
  status: OEQ.Common.ItemStatus[]
) => ({
  ...defaultSearchPageOptions,
  status,
  owner: getCurrentUserMock,
  sortOrder: "RANK",
});

const {
  mockCollections,
  mockCurrentUser,
  mockListClassification,
  mockSearch,
  mockSearchSettings,
} = mockCollaborators();
initialiseEssentialMocks({
  mockCollections,
  mockCurrentUser,
  mockListClassification,
  mockSearchSettings,
});
mockSearchSettings.mockResolvedValue({
  ...defaultSearchSettings,
  searchingShowNonLiveCheckbox: true,
});

const searchPromise = mockSearch.mockResolvedValue(getSearchResult);

describe("<MyResourcesPage/>", () => {
  const renderMyResourcesPage = async (
    resourceType: MyResourcesType = "Published",
    currentUser: OEQ.LegacyContent.CurrentUserDetails = getCurrentUserMock
  ) => {
    history.push("/page/myresources", {
      customData: {
        myResourcesType: resourceType,
      },
    });
    const page = render(
      <MuiThemeProvider theme={defaultTheme}>
        <Router history={history}>
          <AppContext.Provider
            value={{
              refreshUser: jest.fn(),
              appErrorHandler: jest.fn(),
              currentUser,
            }}
          >
            <MyResourcesPage updateTemplate={jest.fn()} />
          </AppContext.Provider>
        </Router>
      </MuiThemeProvider>
    );

    await waitForSearch(searchPromise);
    return page;
  };

  describe("supports views of different My resources types", () => {
    it.each<[string, MyResourcesType, OEQ.Common.ItemStatus[]]>([
      ["live", "Published", ["LIVE", "REVIEW"]],
      ["draft", "Drafts", ["DRAFT"]],
      ["personal", "Scrapbook", ["PERSONAL"]],
      ["moderating", "Moderation queue", ["MODERATING", "REJECTED", "REVIEW"]],
      ["archived", "Archive", ["ARCHIVED"]],
      ["all", "All resources", nonDeletedStatuses],
    ])(
      "shows a list of %s Items",
      async (
        _: string,
        resourcesType: MyResourcesType,
        statuses: OEQ.Common.ItemStatus[]
      ) => {
        await renderMyResourcesPage(resourcesType);
        expect(mockSearch).toHaveBeenLastCalledWith({
          ...buildMyResourcesSearchPageOptions(statuses),
          includeAttachments: false,
        });
      }
    );
  });

  describe("Refine search panel", () => {
    it("always displays MyResourcesSelector", async () => {
      const { container } = await renderMyResourcesPage();
      expect(
        queryRefineSearchComponent(container, "MyResourcesSelector")
      ).toBeInTheDocument();
    });

    it("always hides Advanced search selector, Remote search selector and Owner selector", async () => {
      const { container } = await renderMyResourcesPage();
      [
        "AdvancedSearchSelector",
        "RemoteSearchSelector",
        "OwnerSelector",
      ].forEach((componentSuffix) =>
        expect(
          queryRefineSearchComponent(container, componentSuffix)
        ).not.toBeInTheDocument()
      );

      expect.assertions(3);
    });

    it("hides Collection selector for Scrapbook", async () => {
      const { container } = await renderMyResourcesPage(
        "Scrapbook",
        getCurrentUserMock
      );
      expect(
        queryRefineSearchComponent(container, "CollectionSelector")
      ).not.toBeInTheDocument();
    });

    it.each([["Moderation queue"], ["All resources"]])(
      "shows Item status selector for %s",
      async (resourceType: string) => {
        const { container } = await renderMyResourcesPage(
          resourceType as MyResourcesType
        );
        expect(
          queryRefineSearchComponent(container, "StatusSelector")
        ).toBeInTheDocument();
      }
    );

    it.each([
      ["Resource type selector", "MIMETypeSelector"],
      ["Date range selector", "DateRangeSelector"],
    ])(
      "displays %s based on Search settings",
      async (_: string, componentSuffix: string) => {
        const { container } = await renderMyResourcesPage();
        expect(
          queryRefineSearchComponent(container, componentSuffix)
        ).toBeInTheDocument();
      }
    );
  });

  describe("Browser history data", () => {
    it("saves currently selected My resources type in the browser history", async () => {
      await renderMyResourcesPage("Archive");
      expect(history.location.state).toEqual({
        searchPageOptions: buildMyResourcesSearchPageOptions(["ARCHIVED"]),
        customData: {
          myResourcesType: "Archive",
        },
      });
    });
  });

  describe("Access to Scrapbook", () => {
    it.each([
      ["shows", "enabled", getCurrentUserMock, true],
      ["hides", "disabled", guestUser, false],
    ])(
      "%s the option of Scrapbook if access to Scrapbook is %s",
      async (
        _: string,
        status: string,
        user: CurrentUserDetails | undefined,
        expecting: boolean
      ) => {
        const { getByText } = await renderMyResourcesPage("Published", user);

        userEvent.click(
          getByText(languageStrings.myResources.resourceType.published)
        );
        const scrapbookOptionFound = !!screen.queryByText("Scrapbook", {
          selector: "li",
        });

        expect(scrapbookOptionFound).toBe(expecting);
      }
    );
  });
});
