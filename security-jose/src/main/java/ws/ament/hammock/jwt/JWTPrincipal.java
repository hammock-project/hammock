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

import net.minidev.json.JSONObject;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

public class JWTPrincipal implements Principal {
    private final Map<String,Object> jwt;
    private final List<String> roles;

    public JWTPrincipal(Map<String,Object> jwt) {
        this.jwt = jwt;
        Map<String, List<String>> realmAccess = (Map<String, List<String>>) jwt.getOrDefault("realm_access", emptyMap());
        roles = realmAccess.getOrDefault("roles", emptyList());
    }

    @Override
    public String getName() {
        return (String)jwt.get("preferred_username");
    }

    public boolean isUserInRole(String role) {
        return roles.contains(role);
    }

    public Map<String,Object> getJwt() {
        return jwt;
    }

    public String getGivenName() {
        return (String)jwt.get("given_name");
    }

    public String getFamilyName() {
        return (String) jwt.get("family_name");
    }

    public String getEmail() {
        return (String) jwt.get("email");
    }
}
