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

package ws.ament.hammock.jwt.processor;

import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.util.Base64URL;
import ws.ament.hammock.jwt.JWTException;

import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonObject;
import java.io.StringReader;
import java.text.ParseException;

@ApplicationScoped
public class SimpleJWTProcessor implements JWTProcessor{
    @Override
    public JsonObject process(String jwt) throws JWTException {
        String[] parts = jwt.split("\\.");
        if(parts.length == 3) {
            Base64URL first = new Base64URL(parts[0]);
            Base64URL second = new Base64URL(parts[1]);
            Base64URL third = new Base64URL(parts[2]);
            try {
                String rawJwt = new JWSObject(first, second, third).getPayload().toString();
                return Json.createReader(new StringReader(rawJwt)).readObject();
            }
            catch (ParseException e) {
                throw new JWTException("Unable to parse JWT", e);
            }
        }
        else {
            return null;
        }
    }
}
