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
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import ws.ament.hammock.core.config.ConfigLoader;
import ws.ament.hammock.utils.ClassUtils;
import ws.ament.hammock.web.spi.RestServerConfiguration;
import ws.ament.hammock.web.spi.ServletContextAttributeProvider;
import ws.ament.hammock.web.api.ServletDescriptor;

import static java.util.Collections.singletonMap;

@ApplicationScoped
public class JerseyContainerConfigurator implements ServletContextAttributeProvider {

    /**
     * Jersey internal contract to look for {@link ResourceConfig} instance within servlet attributes
     * named '{@value #RESOURCE_CONFIG}$ServletName$'.
     */
    private static final String RESOURCE_CONFIG = "jersey.config.servlet.internal.resourceConfig_";
    private static final String SERVLET_NAME = "JerseyServlet";

    @Inject
    private JerseyCdiExtension jerseyCdiExtension;
    @Inject
    private Instance<Application> applicationInstance;
    @Inject
    private RestServerConfiguration restServerConfiguration;

    @Produces
    public ServletDescriptor jerseyServlet() {
        String urlPattern = restServerConfiguration.getRestServletMapping();
        if (!applicationInstance.isUnsatisfied() && !applicationInstance.isAmbiguous()) {
            ApplicationPath annotation = ClassUtils.getAnnotation(applicationInstance.get().getClass(), ApplicationPath.class);
            if(annotation != null) {
                String path = annotation.value();
                urlPattern = path.endsWith("/") ? path + "*" : path + "/*";
            }
        }
        return new ServletDescriptor(SERVLET_NAME, null, new String[] { urlPattern }, 1, null, true, ServletContainer.class);
    }

    @Override
    public Map<String, Object> getAttributes() {
        final ResourceConfig resourceConfig;
        if (!applicationInstance.isUnsatisfied() && !applicationInstance.isAmbiguous()) {
            resourceConfig = ResourceConfig.forApplication(applicationInstance.get());
        } else {
            resourceConfig = new ResourceConfig();
        }

        if (resourceConfig.getClasses().isEmpty()) {
            resourceConfig
                    .registerClasses(jerseyCdiExtension.getProviders())
                    .registerClasses(jerseyCdiExtension.getResources());
        }

        resourceConfig.setProperties(ConfigLoader.loadAllProperties("jersey", false));

        final String attributeName = RESOURCE_CONFIG + SERVLET_NAME;
        return singletonMap(attributeName, resourceConfig);
    }

}
