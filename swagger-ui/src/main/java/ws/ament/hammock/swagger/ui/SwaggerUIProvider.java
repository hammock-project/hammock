/*
 * Copyright 2018 Hammock and its contributors
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

package ws.ament.hammock.swagger.ui;

import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ws.ament.hammock.web.api.ServletDescriptor;

@ApplicationScoped
public class SwaggerUIProvider {

    static private final String UI_MATCH_FORWARD = "/*";
    static private final String UI_REDIRECT_LINK = "/index.html?url=";
    static private final String UI_WEBJARS_BASE = "/webjars/swagger-ui/";

    @Inject
    private SwaggerUIConfiguration config;

    @Produces
    public ServletDescriptor swaggerUIDispatcherServlet() {
        String name = SwaggerUIDispatcherServlet.class.getSimpleName();
        String[] uris = new String[] { config.getSwaggerUIUrl(), config.getSwaggerUIUrl() + UI_MATCH_FORWARD };
        WebInitParam[] params = null;
        return new ServletDescriptor(name, uris, uris, 1, params, false, SwaggerUIDispatcherServlet.class);
    }

    @SuppressWarnings("serial")
    static public class SwaggerUIDispatcherServlet extends HttpServlet {

        @Inject
        private SwaggerUIConfiguration config;

        private String uiBootRedirect;

        private String uiBaseForward;

        @Override
        public void init() throws ServletException {
            uiBootRedirect = config.getSwaggerUIUrl() + UI_REDIRECT_LINK + config.getSwaggerUIApi();
            uiBaseForward = UI_WEBJARS_BASE + config.getSwaggerUIVersion();
        }

        @Override
        protected void service(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            if (!config.isSwaggerUIEnable()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            if (request.getPathInfo() == null) {
                response.sendRedirect(uiBootRedirect);
                return;
            }
            RequestDispatcher disp = request.getRequestDispatcher(uiBaseForward + request.getPathInfo());
            if (disp == null) {
                response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
                return;
            }
            disp.forward(request, response);
        }
    }
}
