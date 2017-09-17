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

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import ws.ament.hammock.jwt.JWTConfiguration;
import ws.ament.hammock.jwt.JWTException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;

@ApplicationScoped
public class DefaultValidatingJWTProcessor implements JWTProcessor{
    private ConfigurableJWTProcessor<SecurityContext> delegate = new DefaultJWTProcessor<>();

    @Inject
    private JWTConfiguration jwtConfiguration;

    @PostConstruct
    public void init() {
        try {
            JWKSource<SecurityContext> keySource = lookupJWKSource();
            JWSAlgorithm expectedJWSAlg = jwtConfiguration.getAlgorithm();
            JWSKeySelector<SecurityContext> keySelector = new JWSVerificationKeySelector<>(expectedJWSAlg, keySource);
            delegate.setJWSKeySelector(keySelector);
        } catch (IOException | ParseException e) {
            throw new JWTException("Unable to read JWT Configuration",e);
        }
    }

    @Override
    public JsonObject process(String jwt) throws JWTException {
        try {
            String rawJwt = delegate.process(jwt, null).toString();
            return Json.createReader(new StringReader(rawJwt)).readObject();
        } catch (ParseException | BadJOSEException | JOSEException e) {
            throw new JWTException("Unable to parse jwt", e);
        }
    }

    private JWKSource<SecurityContext> lookupJWKSource() throws IOException, ParseException {
        if(jwtConfiguration.getJwkResource() != null &&
                !"".equals(jwtConfiguration.getJwkResource())) {
            URL resource = DefaultValidatingJWTProcessor.class.getResource(jwtConfiguration.getJwkResource());
            try(InputStream stream = resource.openStream()) {
                String key = com.nimbusds.jose.util.IOUtils.readInputStreamToString(stream, Charset.defaultCharset());
                return new ImmutableJWKSet<>(JWKSet.parse(key));
            }
        }
        else if(jwtConfiguration.getJwkSourceUrl() != null &&
                !"".equals(jwtConfiguration.getJwkSourceUrl())) {
            return new RemoteJWKSet<>(new URL(jwtConfiguration.getJwkSourceUrl()));
        }
        else {
            JWKSet jwkSet = JWKSet.load(new File(jwtConfiguration.getJwkSourceFile()));

            return new ImmutableJWKSet<>(jwkSet);
        }
    }
}
