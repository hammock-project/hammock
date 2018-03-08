/*
 * Copyright 2017 Hammock and its contributors
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

package ws.ament.hammock.jwt;

import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import ws.ament.hammock.security.api.NotLoggedInException;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Dependent
@WebServlet(urlPatterns = {"/*"}, name = "DefaultServlet")
public class DefaultServlet extends HttpServlet{
    @Inject
    private RequiredLoggedIn requiredLoggedIn;
    @Inject
    @Claim(standard = Claims.aud)
    private JsonArray aud;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            requiredLoggedIn.shouldRequireLogin();
            resp.getWriter().println(req.getUserPrincipal().getName());
        }
        catch (NotLoggedInException e) {
            resp.sendError(401);
        }
    }
}
