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

import org.glassfish.jersey.servlet.ServletContainer;
import ws.ament.hammock.web.spi.ServletContextAttributeProvider;
import ws.ament.hammock.web.spi.ServletDescriptor;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.ws.rs.core.Application;
import java.util.Map;

import static java.util.Collections.singletonMap;

@ApplicationScoped
public class JerseyContainerConfigurator implements ServletContextAttributeProvider {

    @Inject
    private Instance<Application> applicationInstance;
    @Produces
    public ServletDescriptor resteasyServlet() {
        return new ServletDescriptor("JerseyServlet",null, new String[]{"/*"},1,null,true,ServletContainer.class);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return singletonMap(Application.class.getName(), applicationInstance.get());
    }
}
