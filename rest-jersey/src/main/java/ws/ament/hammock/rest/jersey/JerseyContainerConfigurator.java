/*
 * Copyright 2016 Hammock and its contributors
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

package ws.ament.hammock.rest.jersey;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import ws.ament.hammock.web.spi.ServletContextAttributeProvider;
import ws.ament.hammock.web.spi.ServletDescriptor;

import static java.util.Collections.singletonMap;

@ApplicationScoped
public class JerseyContainerConfigurator implements ServletContextAttributeProvider {

    /**
     * Jersey internal contract to look for {@link ResourceConfig} instance within servlet attributes
     * named '{@value #RESOURCE_CONFIG}_$ServletName$'.
     */
    private static final String RESOURCE_CONFIG = "jersey.config.servlet.internal.resourceConfig";
    private static final String SERVLET_NAME = "JerseyServlet";

    @Inject
    private JaxRsCdiExtension jaxRsCdiExtension;
    @Inject
    private Instance<Application> applicationInstance;

    @Produces
    public ServletDescriptor jerseyServlet() {
        return new ServletDescriptor(SERVLET_NAME, null, new String[] { "/*" }, 1, null, true, ServletContainer.class);
    }

    @Override
    public Map<String, Object> getAttributes() {
        final ResourceConfig resourceConfig = ResourceConfig.forApplication(applicationInstance.get());
        if (resourceConfig.getClasses().isEmpty()) {
            resourceConfig
                    .registerClasses(jaxRsCdiExtension.getProviders())
                    .registerClasses(jaxRsCdiExtension.getResources());
        }

        final String attributeName = RESOURCE_CONFIG + "_" + SERVLET_NAME;
        return singletonMap(attributeName, resourceConfig);
    }

}
