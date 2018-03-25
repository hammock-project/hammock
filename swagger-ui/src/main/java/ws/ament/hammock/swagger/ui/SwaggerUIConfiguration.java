/*
 * Copyright 2018 Hammock and its contributors
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

package ws.ament.hammock.swagger.ui;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class SwaggerUIConfiguration {

    @Inject
    @ConfigProperty(name = "swagger-ui.enable", defaultValue = "true")
    private boolean swaggerUIEnable;

    @Inject
    @ConfigProperty(name = "swagger-ui.version")
    private String swaggerUIVersion;

    @Inject
    @ConfigProperty(name = "swagger-ui.redirect", defaultValue = "index.html")
    private String swaggerUIRedirect;
    
    @Inject
    @ConfigProperty(name = "swagger-ui.path", defaultValue = "/openapi.ui")
    private String swaggerUIPath;

    @Inject
    @ConfigProperty(name = "swagger-ui.api", defaultValue = "/openapi.json")
    private String swaggerUIApi;

    public boolean isSwaggerUIEnable() {
        return swaggerUIEnable;
    }

    public String getSwaggerUIVersion() {
        return swaggerUIVersion;
    }

    public String getSwaggerUIRedirect() {
        return swaggerUIRedirect;
    }

    public String getSwaggerUIPath() {
        return swaggerUIPath;
    }

    public String getSwaggerUIApi() {
        return swaggerUIApi;
    }
}
