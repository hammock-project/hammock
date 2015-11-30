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

import org.jboss.weld.environment.se.events.ContainerInitialized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import java.util.StringJoiner;

/**
 * A component that starts a standard application server component.
 *
 * Looks for a {@see WebServerConfiguration} that is annotated {@see ApplicationConfig}
 *
 */
@ApplicationScoped
public class StartWebServer {
    private WebServer webServer;
    private final Logger logger = LoggerFactory.getLogger(StartWebServer.class);

    @Inject
    private Instance<WebServer> webServerInstance;

    @Inject
    private Instance<ServletDescriptor> servletDescriptors;

    @Inject
    private Instance<ServletContextAttributeProvider> servletContextAttributeProviders;

    @PostConstruct
    public void init() {
        this.webServer = resolveWebServer();

        servletDescriptors.forEach(webServer::addServlet);

        servletContextAttributeProviders.forEach(s -> s.getAttributes().forEach(webServer::addServletContextAttribute));

        this.webServer.start();
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

    void watch(@Observes ContainerInitialized containerInitialized) {

    }

    @PreDestroy
    public void shutdown() {
        logger.info("Shutting down Application listener");
        this.webServer.stop();
    }
}
