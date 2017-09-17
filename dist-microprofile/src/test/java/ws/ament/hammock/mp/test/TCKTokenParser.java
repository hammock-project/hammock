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

package ws.ament.hammock.mp.test;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.jwt.tck.util.ITokenParser;
import ws.ament.hammock.jwt.JWTPrincipal;
import ws.ament.hammock.jwt.RoleProcessor;
import ws.ament.hammock.jwt.processor.DefaultValidatingJWTProcessor;

import javax.enterprise.inject.Vetoed;
import javax.enterprise.inject.spi.CDI;
import javax.json.JsonObject;
import java.security.PublicKey;

import static java.util.Collections.emptyList;

@Vetoed
public class TCKTokenParser implements ITokenParser{
    @Override
    public JsonWebToken parse(String bearerToken, String issuer, PublicKey signedBy) throws Exception {
        DefaultValidatingJWTProcessor processor = CDI.current().select(DefaultValidatingJWTProcessor.class).get();
        JsonObject jsonObject = processor.process(bearerToken);
        return new JWTPrincipal(jsonObject, bearerToken, new RoleProcessor(emptyList()));
    }
}
