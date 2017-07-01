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

package ws.ament.hammock.jwt.servlet;

import ws.ament.hammock.jwt.JWTPrincipal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.security.Principal;

public class JWTRequest extends HttpServletRequestWrapper {
    private final JWTPrincipal JWTPrincipal;

    public JWTRequest(JWTPrincipal JWTPrincipal, HttpServletRequest request) {
        super(request);
        this.JWTPrincipal = JWTPrincipal;
    }

    @Override
    public String getAuthType() {
        return "jwt";
    }

    @Override
    public boolean isUserInRole(String role) {
        return JWTPrincipal.isUserInRole(role);
    }

    @Override
    public Principal getUserPrincipal() {
        return JWTPrincipal;
    }
}
