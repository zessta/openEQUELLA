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
import {
  ListItem,
  ListItemSecondaryAction,
  ListItemText,
} from "@material-ui/core";
import * as React from "react";
import { ReactNode } from "react";
import { makeStyles } from "@material-ui/core/styles";

interface SettingsListControlProps {
  /**
   * Whether or not there is a divider at the bottom of this control.
   */
  divider?: boolean;
  /**
   *  Text to appear on the top line of the row.
   */
  primaryText: string;
  /**
   * Text to appear on the bottom line(s) of the row.
   */
  secondaryText?: string;
  /**
   * The controllable component to be rendered on the right hand side of the row.
   */
  control: ReactNode;
}

const useStyles = makeStyles({
  listItemText: {
    maxWidth: "40%",
    minHeight: "38px",
  },
  secondaryAction: {
    width: "55%",
    display: "flex",
    justifyContent: "flex-end",
  },
});

/**
 * This component is used to define a row inside a SettingsList to be used in the page/settings/* pages.
 * It should be placed within a SettingsList.
 */
export default function SettingsListControl({
  divider,
  primaryText,
  secondaryText,
  control,
}: SettingsListControlProps) {
  const classes = useStyles();
  return (
    <ListItem alignItems={"flex-start"} divider={divider}>
      <ListItemText
        className={classes.listItemText}
        primary={primaryText}
        secondary={secondaryText}
      />
      <ListItemSecondaryAction className={classes.secondaryAction}>
        {control}
      </ListItemSecondaryAction>
    </ListItem>
  );
}
