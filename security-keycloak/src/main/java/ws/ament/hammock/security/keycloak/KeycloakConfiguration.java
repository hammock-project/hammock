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

package ws.ament.hammock.security.keycloak;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.spi.HttpFacade;
import org.keycloak.representations.adapters.config.AdapterConfig;
import ws.ament.hammock.core.config.ConfigLoader;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@ApplicationScoped
public class KeycloakConfiguration implements KeycloakConfigResolver{
    private static final String UNSET = "__unset";
    private ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    @ConfigProperty(name="keycloak.config.file",defaultValue= UNSET)
    private String keycloakConfigFile;

    @Override
    public KeycloakDeployment resolve(HttpFacade.Request request) {
        return loadKeycloakDeployment();
    }

    private KeycloakDeployment loadKeycloakDeployment() {
        if(!UNSET.equals(keycloakConfigFile)) {
            return KeycloakDeploymentBuilder.build(loadConfigFile());
        }
        else {
            return KeycloakDeploymentBuilder.build(getAdapterConfig());
        }
    }

    private InputStream loadConfigFile() {
        try {
            return new FileInputStream(keycloakConfigFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Unable to load file "+keycloakConfigFile,e);
        }
    }

    @Produces
    @RequestScoped
    private AdapterConfig getAdapterConfig() {
        Map<String, String> properties = ConfigLoader.loadAllProperties("keycloak",true);
        try {
            String json = objectMapper.writeValueAsString(properties);
            return objectMapper.readValue(json, HammockAdapterConfig.class);
        } catch (IOException e) {
            throw new RuntimeException("Unable to parse JSON",e);
        }
    }
}
