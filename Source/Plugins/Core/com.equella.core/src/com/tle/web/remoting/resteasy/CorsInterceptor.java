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

import java.util.Enumeration;
import java.util.Iterator;
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
    String origin = "http://localhost:3009";

    System.out.println("chandu");
    MultivaluedMap<String, Object> orderedMap = response.getHeaders();
    Iterator<String> mapIterator = orderedMap.keySet().iterator();

    Enumeration<String> headerNames = servletRequest.getHeaderNames();

    if (headerNames != null) {
      while (headerNames.hasMoreElements()) {
        System.out.println("Origin: " + servletRequest.getHeader("Origin"));
        System.out.println("origin: " + servletRequest.getHeader("origin"));
        System.out.println(
            "Header: "
                + headerNames.nextElement()
                + servletRequest.getHeader(headerNames.nextElement()));
      }
    }
    // iterate over the map
    while (mapIterator.hasNext()) {
      String key = mapIterator.next();
      System.out.println("key:" + key + ", values=" + orderedMap.get(key));
      // Collection<String> values = orderedMap.getCollection(key);
      // // iterate over the entries for this key in the map
      // for(Iterator<String> entryIterator = values.iterator(); entryIterator.hasNext();) {
      // 	String value = entryIterator.next();
      // 	System.out.println("    value:" + value);
      // }
    }

    if (servletRequest.getHeader("Origin").equals("http://localhost:3000")) {
      origin = servletRequest.getHeader("Origin");
    } else if (servletRequest.getHeader("Origin").equals("http://localhost:3001")) {
      origin = servletRequest.getHeader("Origin");
    }
    metadata.putSingle("Access-Control-Allow-Origin", origin);
    metadata.putSingle("Access-Control-Expose-Headers", "Location");
  }
}
