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

import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.enterprise.inject.Vetoed;
import javax.json.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toList;

@Vetoed
public class JWTPrincipal implements JsonWebToken {
    private final JsonObject jwt;
    private final String stringForm;
    private final List<String> roles;

    public JWTPrincipal() {
        this(Json.createObjectBuilder().build(), "");
    }

    public JWTPrincipal(JsonObject jwt, String stringForm) {
        this.jwt = jwt;
        this.stringForm = stringForm;
        JsonObject realmAccess = jwt.getJsonObject("realm_access");
        if (realmAccess == null) {
            roles = emptyList();
        }
        else {
            JsonArray roles = realmAccess.getJsonArray("roles");
            if(roles == null) {
                this.roles = emptyList();
            }
            else {
                this.roles = roles.getValuesAs(JsonString.class)
                        .stream()
                        .map(JsonString::getString)
                        .collect(toList());
            }
        }
    }

    @Override
    public String getName() {
        return jwt.getString(Claims.preferred_username.name(), null);
    }

    @Override
    public String getRawToken() {
        return stringForm;
    }

    @Override
    public String getIssuer() {
        return jwt.getString(Claims.iss.name());
    }

    @Override
    public Set<String> getAudience() {
        return singleton(jwt.getString(Claims.aud.name()));
    }

    @Override
    public String getSubject() {
        return jwt.getString(Claims.sub.name());
    }

    @Override
    public String getTokenID() {
        return jwt.getString(Claims.jti.name());
    }

    @Override
    public long getExpirationTime() {
        return jwt.getJsonNumber(Claims.exp.name()).longValue();
    }

    @Override
    public long getIssuedAtTime() {
        return jwt.getJsonNumber(Claims.auth_time.name()).longValue();
    }

    @Override
    public Set<String> getGroups() {
        JsonValue groups = jwt.get(Claims.groups.name());
        if(groups == null) {
            return null;
        }
        if(groups instanceof JsonString) {
            return singleton(((JsonString) groups).getString());
        }
        else if(groups instanceof JsonArray) {
            JsonArray jsonArray = (JsonArray)groups;
            return new HashSet<>(jsonArray.getValuesAs(jsonValue -> jsonValue == null ? null : jsonValue.toString()));
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
    public <T> T getClaim(String s) {
        return (T)jwt.get(s);
    }

    public boolean isUserInRole(String role) {
        return roles.contains(role);
    }

    public JsonObject getJwt() {
        return jwt;
    }

    public String getGivenName() {
        return jwt.getString("given_name");
    }

    public String getFamilyName() {
        return jwt.getString("family_name");
    }

    public String getEmail() {
        return jwt.getString("email");
    }
}
