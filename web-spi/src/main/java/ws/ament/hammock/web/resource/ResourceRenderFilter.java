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

package ws.ament.hammock.web.resource;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@RequestScoped
public class ResourceRenderFilter implements Filter {

   @Inject
   private ResourceManager resourceManager;

   @Override
   public void init(FilterConfig filterConfig) throws ServletException {

   }

   @Override
   public void doFilter(ServletRequest servletRequest,
                        ServletResponse servletResponse,
                        FilterChain filterChain) throws IOException, ServletException {
      HttpServletResponse response = (HttpServletResponse)servletResponse;
      HttpServletRequest request = (HttpServletRequest)servletRequest;
      String path = request.getRequestURI();
      InputStream stream = resourceManager.load(path);
      if(stream != null) {
         ServletOutputStream outputStream = response.getOutputStream();
         IOUtils.copy(stream, outputStream);
      }
      else {
         filterChain.doFilter(servletRequest, servletResponse);
      }

   }

   @Override
   public void destroy() {

   }
}
