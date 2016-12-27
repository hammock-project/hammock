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

package ws.ament.hammock.web.tck;

import ws.ament.hammock.web.api.ServletDescriptor;
import ws.ament.hammock.web.spi.WebParam;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ApplicationScoped
public class DefaultServlet extends HttpServlet {
    @Inject
    private MessageProvider messageProvider;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println(messageProvider.getMessage());
        resp.getWriter().println(", "+getInitParameter("name"));
    }

    @Produces
    @Dependent
    private ServletDescriptor descriptor = new ServletDescriptor("Default",null,new String[]{"/"},1,new WebInitParam[]{new WebParam("name","value")},true,DefaultServlet.class);
}
