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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import java.util.StringJoiner;

/**
 * A component that starts a standard application server component.
 *
 */
@ApplicationScoped
public class StartWebServer {
    private WebServer webServer;
    private final Logger logger = LoggerFactory.getLogger(StartWebServer.class);
    @Inject
    private BeanManager beanManager;

    @Inject
    private Instance<WebServer> webServerInstance;

    @Inject
    private Instance<ServletDescriptor> servletDescriptors;

    @Inject
    private Instance<ServletContextAttributeProvider> servletContextAttributeProviders;

    @PostConstruct
    public void init() {
        WebServer webServer = resolveWebServer();

        webServer.addServletContextAttribute(WeldServletLifecycle.BEAN_MANAGER_ATTRIBUTE_NAME, beanManager);
        servletDescriptors.forEach(webServer::addServlet);

        servletContextAttributeProviders.forEach(s -> s.getAttributes().forEach(webServer::addServletContextAttribute));

        webServer.start();

        this.webServer = webServer;
    }

    private WebServer resolveWebServer() {
        if(webServerInstance.isAmbiguous()) {
            StringJoiner foundInstances = new StringJoiner(",","[","]");
            webServerInstance.iterator().forEachRemaining(ws -> foundInstances.add(ws.toString()));
            throw new RuntimeException("Multiple web server implementations found on the classpath "+foundInstances);
        }
        if(webServerInstance.isUnsatisfied()) {
            throw new RuntimeException("No web server implementations found on the classpath");
        }
        return webServerInstance.get();
    }

    void watch(@Observes @Initialized(ApplicationScoped.class) Object initialized) {

    }

    @PreDestroy
    public void shutdown() {
        logger.info("Shutting down Application listener");
        this.webServer.stop();
    }
}
