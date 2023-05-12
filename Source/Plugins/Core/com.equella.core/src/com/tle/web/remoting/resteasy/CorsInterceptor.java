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

package com.tle.web.remoting.resteasy;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.interception.PostProcessInterceptor;

@Provider
@ServerInterceptor
@SuppressWarnings("nls")
public class CorsInterceptor implements PostProcessInterceptor {

  @Override
  public void postProcess(ServerResponse response) {
    process(response);
  }

  @Context static HttpServletRequest servletRequest;

  public static void runPostProcess(ServerResponse response) {
    process(response);
  }

  private static void process(ServerResponse response) {
    final MultivaluedMap<String, Object> metadata = response.getMetadata();
    PropertiesConfiguration corsConfiguration = null;

    // Get CORS Properties
    try {
      // Use abosulte path in dev mode like
      // /home/zadmin/codebase/hcl/min-cr/openEQUELLA/docker/cors.properties
      corsConfiguration = new PropertiesConfiguration("cors.properties");
    } catch (Exception e) {
      e.printStackTrace();
    }

    //  Default Origins
    String origin = corsConfiguration.getProperty("cors.default") + "";

    if (servletRequest != null) {

      // Requested Origin
      String requestedOrigin = servletRequest.getHeader("Origin");

      // Check is requested Origin is allowed or not
      if (requestedOrigin != null && requestedOrigin != "null" && !requestedOrigin.isEmpty()) {
        String combinedString = corsConfiguration.getProperty("cors.allowedOrigins") + "";
        String[] corsDomains = combinedString.split(",");
        for (int i = 0; i < corsDomains.length; i++) {
          String corsDomain = "";
          if (i == 0) {
            String temp = corsDomains[i];
            corsDomain = temp.substring(1, temp.length());
          } else if (i == corsDomains.length - 1) {
            String temp = corsDomains[i];
            corsDomain = temp.substring(0, temp.length() - 1);
          } else {
            String temp = corsDomains[i];
            corsDomain = temp.substring(1, temp.length());
          }
          if (requestedOrigin.contains(corsDomain)) {
            origin = requestedOrigin;
          }
        }
      }
    }
    metadata.putSingle("Access-Control-Allow-Origin", origin);
    metadata.putSingle("Access-Control-Expose-Headers", "Location");
  }
}
