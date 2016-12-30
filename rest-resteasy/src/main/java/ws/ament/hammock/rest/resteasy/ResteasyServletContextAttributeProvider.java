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

package ws.ament.hammock.rest.resteasy;

import org.jboss.resteasy.cdi.ResteasyCdiExtension;
import org.jboss.resteasy.plugins.server.servlet.HttpServlet30Dispatcher;
import org.jboss.resteasy.spi.ResteasyDeployment;
import ws.ament.hammock.utils.ClassUtils;
import ws.ament.hammock.web.spi.RestServerConfiguration;
import ws.ament.hammock.web.spi.ServletContextAttributeProvider;
import ws.ament.hammock.web.api.ServletDescriptor;
import ws.ament.hammock.web.spi.WebParam;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.annotation.WebInitParam;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Map;

import static java.util.Collections.singletonMap;

@ApplicationScoped
public class ResteasyServletContextAttributeProvider implements ServletContextAttributeProvider {
    @Inject
    private ResteasyCdiExtension resteasyCdiExtension;
    @Inject
    private Instance<Application> applicationInstance;
    @Inject
    private RestServerConfiguration restServerConfiguration;

    @Override
    public Map<String, Object> getAttributes() {
        ResteasyDeployment deployment = new ResteasyDeployment();
        deployment.getActualResourceClasses().addAll(resteasyCdiExtension.getResources());
        deployment.getActualProviderClasses().addAll(resteasyCdiExtension.getProviders());
        if( !(applicationInstance.isUnsatisfied() || applicationInstance.isAmbiguous())) {
            deployment.setApplication(applicationInstance.get());
        }
        deployment.setInjectorFactoryClass(Cdi11InjectorFactory.class.getName());
        return singletonMap(ResteasyDeployment.class.getName(), deployment);
    }

    @Produces
    public ServletDescriptor resteasyServlet() {
        String path = restServerConfiguration.getRestServerUri();
        if( !(applicationInstance.isUnsatisfied() || applicationInstance.isAmbiguous())) {
            ApplicationPath appPath = ClassUtils.getAnnotation(applicationInstance.get().getClass(), ApplicationPath.class);
            if(appPath != null) {
                path = appPath.value();
            }
        }
        String pattern = path.endsWith("/") ? path + "*" : path + "/*";
        WebInitParam param = new WebParam("resteasy.servlet.mapping.prefix", path);
        return new ServletDescriptor("ResteasyServlet",new String[]{pattern}, new String[]{pattern},
                1,new WebInitParam[]{param},true,HttpServlet30Dispatcher.class);
    }
}
