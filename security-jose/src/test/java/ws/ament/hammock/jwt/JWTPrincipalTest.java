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
import org.junit.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;

public class JWTPrincipalTest {
    @Test
    public void shouldBeValid() throws Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JWTPrincipal.PRINCIPAL_NAME, "secure_user");
        jsonObject.put(JWTPrincipal.ISSUER,"http;//issuer.com");
        jsonObject.put(JWTPrincipal.SUBJECT,"24400320");
        jsonObject.put(JWTPrincipal.AUDIENCE,"s6BhdRkqt3");
        jsonObject.put("nonce","n-0S6_WzA2Mj");
        jsonObject.put("exp",1311281970l);
        jsonObject.put("iat",1311280970l);
        jsonObject.put("auth_time",1311280970l);
        jsonObject.put("given_name","Bob");
        jsonObject.put("family_name","Admin");
        jsonObject.put("email","bob.admin@company.com");
        jsonObject.put("realm_access", singletonMap("roles", asList("role1","role2")));

        JWTPrincipal jwtPrincipal = new JWTPrincipal(jsonObject, null);
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
        JWTPrincipal JWTPrincipal = new JWTPrincipal(new JSONObject(), null);
        assertThat(JWTPrincipal.isUserInRole("role3")).isFalse();
    }
}