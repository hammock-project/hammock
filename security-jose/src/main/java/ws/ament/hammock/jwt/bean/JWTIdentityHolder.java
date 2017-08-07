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

package ws.ament.hammock.jwt.bean;

import ws.ament.hammock.jwt.JWTIdentity;
import ws.ament.hammock.jwt.JWTPrincipal;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import java.util.Collections;

@RequestScoped
public class JWTIdentityHolder {

    private JWTIdentity jwtIdentity = new JWTIdentity(new JWTPrincipal(Collections.emptyMap(), null));

    @Produces
    @RequestScoped
    public JWTIdentity getJwtIdentity() {
        return jwtIdentity;
    }

    public void setJwtIdentity(JWTIdentity jwtIdentity) {
        this.jwtIdentity = jwtIdentity;
    }
}
