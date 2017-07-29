/*
 * Copyright 2016 Hammock and its contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ws.ament.hammock.web.cors;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ws.ament.hammock.web.base.BaseAlwaysPostRequestFilter;

@Dependent
public class CORSFilter extends BaseAlwaysPostRequestFilter {

   @Inject
   private CORSConfiguration corsConfiguration;

   @Override
   protected void doFilter(HttpServletRequest request, HttpServletResponse response) {
      if(corsConfiguration.isEnabled()) {
         response.addHeader("Access-Control-Allow-Origin", corsConfiguration.getOrigin());
         response.addHeader("Access-Control-Allow-Headers", corsConfiguration.getHeaders());
         response.addHeader("Access-Control-Allow-Credentials", corsConfiguration.getCredentials());
         response.addHeader("Access-Control-Allow-Methods", corsConfiguration.getMethods());
         response.addHeader("Access-Control-Max-Age", corsConfiguration.getMaxAge());
      }
   }
}
