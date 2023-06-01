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

package com.tle.web.core.filter;

import com.google.inject.Inject;
import com.tle.core.guice.Bind;
import com.tle.web.dispatcher.FilterResult;
import java.io.IOException;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.configuration.PropertiesConfiguration;

/** @author jmaginnis */
@Bind
@Singleton
public class RequestFilter extends OncePerRequestFilter {
  @Inject(optional = true)
  @Named("strictTransportSecurity.maxage")
  private int stsMaxAge = -1;

  PropertiesConfiguration corsConfiguration = null;
  String allowedOrigins = "";

  public RequestFilter() {
    // Get CORS Properties
    try {
      // Use abosulte path in dev mode like
      // /home/zadmin/codebase/hcl/min-cr/openEQUELLA/docker/cors.properties
      corsConfiguration = new PropertiesConfiguration("cors.properties");
      //  Default Origins
      allowedOrigins = corsConfiguration.getProperty("content-security-policy.default") + "";
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @SuppressWarnings("nls")
  @Override
  protected FilterResult doFilterInternal(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response = new IgnoreContentWrapper(response);
    response.addHeader("P3P", "CP=\"CAO PSA OUR\"");
    response.setHeader("X-Content-Type-Options", "nosniff");
    // Chrome sucks.
    // http://dev.equella.com/issues/8025
    // http://dev.equella.com/issues/5612
    String ua = request.getHeader("User-Agent");
    if (ua != null && ua.contains("Chrome")) {
      response.addHeader("X-XSS-Protection", "0");
    } else {
      response.setHeader("X-XSS-Protection", "1; mode=block");
    }
    if (stsMaxAge != -1) {
      response.setHeader(
          "Strict-Transport-Security", "max-age=" + stsMaxAge + "; includeSubDomains");
    }
    response.addHeader("Strict-Transport-Security", "max-age=63072000; includeSubdomains;");
    response.addHeader("Content-Security-Policy", "frame-ancestors 'self' " + allowedOrigins + ";");
    return new FilterResult(response);
  }
}
