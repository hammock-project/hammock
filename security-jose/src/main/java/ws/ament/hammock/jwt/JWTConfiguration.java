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

import com.nimbusds.jose.JWSAlgorithm;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import ws.ament.hammock.jwt.processor.JWTProcessor;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class JWTConfiguration {
    private static final String UNSET_VALUE = "_____unset";
    @Inject
    @ConfigProperty(name = "jwt.header.enabled", defaultValue = "true")
    private boolean jwtHeaderEnabled;

    @Inject
    @ConfigProperty(name = "jwt.query.param.enabled", defaultValue = "true")
    private boolean jwtQueryParamEnabled;

    @Inject
    @ConfigProperty(name = "jwt.query.param.name", defaultValue = "access_token")
    private String jwtQueryParamName;

    @Inject
    @ConfigProperty(name = "jwt.processor", defaultValue = "ws.ament.hammock.jwt.processor.SimpleJWTProcessor")
    private String jwtProcessor;

    @Inject
    @ConfigProperty(name = "jwt.algorithm", defaultValue = "HS256")
    private String algorithm;

    @Inject
    @ConfigProperty(name = "jwt.jwk.resource", defaultValue = UNSET_VALUE)
    private String jwkResource;

    @Inject
    @ConfigProperty(name = "jwt.jwk.source.url", defaultValue = UNSET_VALUE)
    private String jwkSourceUrl;

    @Inject
    @ConfigProperty(name = "jwt.jwk.source.file", defaultValue = UNSET_VALUE)
    private String jwkSourceFile;

    public boolean isJwtHeaderEnabled() {
        return jwtHeaderEnabled;
    }

    public boolean isJwtQueryParamEnabled() {
        return jwtQueryParamEnabled;
    }

    public String getJwtQueryParamName() {
        return jwtQueryParamName;
    }

    public JWTProcessor getJwtProcessor() {
        return JWTProcessorConverter.INSTANCE.convert(jwtProcessor);
    }

    public JWSAlgorithm getAlgorithm() {
        return JWSAlgorithm.parse(this.algorithm);
    }

    public String getJwkSourceUrl() {
        return jwkSourceUrl.equals(UNSET_VALUE) ? "" : jwkSourceUrl;
    }

    public String getJwkResource() {
        return jwkResource.equals(UNSET_VALUE) ? "" : jwkResource;
    }

    public String getJwkSourceFile() {
        return jwkSourceFile.equals(UNSET_VALUE) ? "" : jwkSourceFile;
    }
}
