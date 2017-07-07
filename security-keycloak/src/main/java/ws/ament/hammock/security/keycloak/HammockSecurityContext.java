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

package ws.ament.hammock.security.keycloak;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.Collection;

class HammockSecurityContext implements SecurityContext{
    private final Principal principal;
    private final Collection<String> roles;
    private final boolean secure;

    HammockSecurityContext(Principal principal, Collection<String> roles, boolean secure) {
        this.principal = principal;
        this.roles = roles;
        this.secure = secure;
    }

    @Override
    public Principal getUserPrincipal() {
        return principal;
    }

    @Override
    public boolean isUserInRole(String s) {
        return roles.contains(s);
    }

    @Override
    public boolean isSecure() {
        return secure;
    }

    @Override
    public String getAuthenticationScheme() {
        return "OAUTH";
    }
}
