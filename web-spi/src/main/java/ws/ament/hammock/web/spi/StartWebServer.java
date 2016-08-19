/*
 * Copyright 2015 John D. Ament
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

package ws.ament.hammock.web.spi;

import org.jboss.weld.environment.servlet.WeldServletLifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import java.util.Collection;
import java.util.Set;
import java.util.StringJoiner;

import static java.util.stream.Collectors.toList;

/**
 * A component that starts a standard application server component.
 *
 */
public class StartWebServer implements Extension {
    private WebServer webServer;
    private final Logger logger = LoggerFactory.getLogger(StartWebServer.class);

    void init(@Observes AfterDeploymentValidation afterDeploymentValidation, BeanManager beanManager) {
        WebServer webServer = resolveWebServer(beanManager);

        webServer.addServletContextAttribute(WeldServletLifecycle.BEAN_MANAGER_ATTRIBUTE_NAME, beanManager);
        getServletDescriptors(beanManager).forEach(webServer::addServlet);
        getServletContextAttributeProviders(beanManager)
                .forEach(s -> s.getAttributes().forEach(webServer::addServletContextAttribute));
        webServer.start();

        this.webServer = webServer;
    }

    @PreDestroy
    public void shutdown() {
        logger.info("Shutting down Application listener");
        this.webServer.stop();
    }

    private WebServer resolveWebServer(BeanManager beanManager) {
        Set<Bean<?>> beans = beanManager.getBeans(WebServer.class);
        if(beans.size() > 1) {
            StringJoiner foundInstances = new StringJoiner(",","[","]");
            beans.iterator().forEachRemaining(ws -> foundInstances.add(ws.toString()));
            throw new RuntimeException("Multiple web server implementations found on the classpath "+foundInstances);
        }
        if(beans.isEmpty()) {
            throw new RuntimeException("No web server implementations found on the classpath");
        }
        Bean<?> bean = beanManager.resolve(beans);
        CreationalContext<?> creationalContext = beanManager.createCreationalContext(bean);
        return (WebServer) beanManager.getReference(bean, WebServer.class, creationalContext);
    }

    private Collection<ServletDescriptor> getServletDescriptors(BeanManager beanManager) {
        Set<Bean<?>> beans = beanManager.getBeans(ServletDescriptor.class);
        return beans.stream().map(bean -> {
            CreationalContext<?> creationalContext = beanManager.createCreationalContext(bean);
            return (ServletDescriptor) beanManager.getReference(bean, ServletDescriptor.class, creationalContext);
        }).collect(toList());
    }

    private Collection<ServletContextAttributeProvider> getServletContextAttributeProviders(BeanManager beanManager) {
        Set<Bean<?>> beans = beanManager.getBeans(ServletContextAttributeProvider.class);
        return beans.stream().map(bean -> {
            CreationalContext<?> creationalContext = beanManager.createCreationalContext(bean);
            return (ServletContextAttributeProvider) beanManager.getReference(bean, ServletContextAttributeProvider.class, creationalContext);
        }).collect(toList());
    }
}
