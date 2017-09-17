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
import org.junit.Test;

import javax.json.Json;
import javax.json.JsonObject;
import java.util.Collections;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class JWTPrincipalTest {
    @Test
    public void shouldBeValid() throws Exception{
        JsonObject jsonObject = Json.createObjectBuilder()
        .add(Claims.upn.name(), "secure_user")
        .add(Claims.iss.name(),"http;//issuer.com")
        .add(Claims.sub.name(),"24400320")
        .add(Claims.aud.name(),"s6BhdRkqt3")
        .add("nonce","n-0S6_WzA2Mj")
        .add("exp",1311281970l)
        .add("iat",1311280970l)
        .add("auth_time",1311280970l)
        .add("given_name","Bob")
        .add("family_name","Admin")
        .add("email","bob.admin@company.com")
        .add("roles", Json.createArrayBuilder(asList("role1","role2")))
        .build();

        JWTPrincipal jwtPrincipal = new JWTPrincipal(jsonObject, null, new RoleProcessor(Collections.emptySet()));
        assertThat(jwtPrincipal.getName()).isEqualTo("secure_user");
        assertThat(jwtPrincipal.isUserInRole("role1")).isTrue();
        assertThat(jwtPrincipal.isUserInRole("role2")).isTrue();
        assertThat(jwtPrincipal.isUserInRole("role3")).isFalse();
        assertThat(jwtPrincipal.getGivenName()).isEqualTo("Bob");
        assertThat(jwtPrincipal.getFamilyName()).isEqualTo("Admin");
        assertThat(jwtPrincipal.getEmail()).isEqualTo("bob.admin@company.com");
        assertThat(jwtPrincipal.getJwt()).isSameAs(jsonObject);
    }

    @Test
    public void handlesNoRealmAccess() {
        JWTPrincipal JWTPrincipal = new JWTPrincipal(Json.createObjectBuilder().build(), null, new RoleProcessor(Collections.emptySet()));
        assertThat(JWTPrincipal.isUserInRole("role3")).isFalse();
    }
}