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
    String origin = "*";

    // System.out.println("starting");
    if(servletRequest != null){
    String requestedOrigin = servletRequest.getHeader("Origin");
    System.out.println("end"+servletRequest.getHeader("Origin"));
    String bgswtalentedgeDomain=".bgswtalentedge-bosch.com";
    String careershaperDomain =".career-shaper.com";
    String edtechDomain =".hcl-edtech.com";
 System.out.println("requestedOrigin == "+ requestedOrigin);
    if ( requestedOrigin != null && requestedOrigin != "null" && !requestedOrigin.isEmpty()){
      if(requestedOrigin.contains(bgswtalentedgeDomain) ||
      requestedOrigin.contains(careershaperDomain) ||
      requestedOrigin.contains(edtechDomain)) {
        origin = requestedOrigin;
      } else {
        origin= "http://localhost:8080";
      }
    }
    System.out.println("origin == "+ origin);
    System.out.println("requestedOrigin == "+ requestedOrigin);
    }
    metadata.putSingle("Access-Control-Allow-Origin", origin);
    metadata.putSingle("Access-Control-Expose-Headers", "Location");
  }
}
