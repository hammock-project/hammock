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

import ws.ament.hammock.security.api.Identity;

import javax.enterprise.inject.Vetoed;

@Vetoed
public class JWTIdentity implements Identity{
    private final JWTPrincipal principal;

    JWTIdentity() {
        this(null);
    }

    public JWTIdentity(JWTPrincipal principal) {
        this.principal = principal;
    }

    @Override
    public JWTPrincipal getPrincipal() {
        return principal;
    }

    @Override
    public boolean isLoggedIn() {
        return principal.getName() != null;
    }

    @Override
    public boolean hasRole(String role) {
        return principal.isUserInRole(role);
    }
}
