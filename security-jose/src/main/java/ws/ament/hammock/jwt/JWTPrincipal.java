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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singleton;

public class JWTPrincipal implements org.eclipse.microprofile.jwt.JWTPrincipal {
    private final Map<String,Object> jwt;
    private final String stringForm;
    private final List<String> roles;

    public JWTPrincipal(Map<String,Object> jwt, String stringForm) {
        this.jwt = jwt;
        this.stringForm = stringForm;
        Map<String, List<String>> realmAccess = (Map<String, List<String>>) jwt.getOrDefault("realm_access", emptyMap());
        roles = realmAccess.getOrDefault("roles", emptyList());
    }

    @Override
    public String getName() {
        String username = (String)jwt.get(PRINCIPAL_NAME);
        if(username == null) {
            return (String)jwt.get("preferred_username");
        }
        else {
            return username;
        }
    }

    @Override
    public String getRawToken() {
        return stringForm;
    }

    @Override
    public String getIssuer() {
        return (String)jwt.get(ISSUER);
    }

    @Override
    public Set<String> getAudience() {
        return singleton((String)jwt.get(AUDIENCE));
    }

    @Override
    public String getSubject() {
        return (String)jwt.get(SUBJECT);
    }

    @Override
    public String getTokenID() {
        return (String)jwt.get(TOKEN_ID);
    }

    @Override
    public long getExpirationTime() {
        return (Long)jwt.get(EXPIRY);
    }

    @Override
    public long getIssuedAtTime() {
        return (Long)jwt.get(ISSURE_TIME);
    }

    @Override
    public Set<String> getGroups() {
        Object groups = jwt.get(GROUPS);
        if(groups == null) {
            return null;
        }
        if(groups instanceof String) {
            return singleton((String)groups);
        }
        else if(groups instanceof Set) {
            return (Set<String>)groups;
        }
        else if(groups instanceof List) {
            return new HashSet<>((List<String>)groups);
        }
        else {
            throw new IllegalStateException("Unable to parse groups "+groups);
        }
    }

    @Override
    public Set<String> getClaimNames() {
        return jwt.keySet();
    }

    @Override
    public Object getClaim(String s) {
        return jwt.get(s);
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
