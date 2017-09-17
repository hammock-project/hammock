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

import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toSet;

@Vetoed
public class JWTPrincipal implements JsonWebToken {
    private final JsonObject jwt;
    private final String stringForm;
    private final Set<String> roles;
    private final Set<String> groups;
    public JWTPrincipal() {
        this(Json.createObjectBuilder().build(), "", new RoleProcessor(emptySet()));
    }

    public JWTPrincipal(JsonObject jwt, String stringForm, RoleProcessor roleProcessor) {
        this.jwt = jwt;
        this.stringForm = stringForm;
        this.groups = parseClaimAsSet(Claims.groups.name());
        Set<String> roles = parseClaimAsSet("roles");
        this.roles = roleProcessor.parseRoles(roles, this.groups);
    }

    @Override
    public String getName() {
        return jwt.getString(Claims.upn.name(), null);
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
        JsonValue audValue = jwt.get(Claims.aud.name());
        if(audValue == null) {
            return null;
        }
        else if(audValue instanceof JsonString) {
            return singleton(((JsonString) audValue).getString());
        }
        else if(audValue instanceof JsonArray) {
            List<JsonString> jsonStrings = ((JsonArray) audValue).getValuesAs(JsonString.class);
            return jsonStrings.stream().map(JsonString::getString).collect(toSet());
        }
        else {
            throw new IllegalStateException("Invalid aud "+audValue);
        }
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
        return groups;
    }

    @Override
    public Set<String> getClaimNames() {
        return jwt.keySet();
    }

    @Override
    public <T> T getClaim(String s) {
        return (T) jwt.get(s);
    }

    public boolean isUserInRole(String role) {
        return roles.contains(role) || groups.contains(role);
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

    private Set<String> parseClaimAsSet(String claim) {
        JsonValue claimValue = jwt.get(claim);
        if (claimValue == null) {
            return emptySet();
        }
        if (claimValue instanceof JsonString) {
            return singleton(((JsonString) claimValue).getString());
        } else if (claimValue instanceof JsonArray) {
            JsonArray jsonArray = (JsonArray) claimValue;
            return new HashSet<>(jsonArray.getValuesAs(JsonString.class).stream().map(JsonString::getString).collect(toSet()));
        } else {
            return emptySet();
        }
    }
}
