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
import { Card, CardContent, Typography } from "@mui/material";
import * as React from "react";

export interface LightboxMessageProps {
  /**
   * The message to be displayed
   */
  message: string;
}

/**
 * Used for displaying messages in the <Lightbox/>.
 */
export const LightboxMessage = ({ message }: LightboxMessageProps) => (
  <Card>
    <CardContent>
      <Typography variant="h5" component="h2">
        {message}
      </Typography>
    </CardContent>
  </Card>
);

export default LightboxMessage;
