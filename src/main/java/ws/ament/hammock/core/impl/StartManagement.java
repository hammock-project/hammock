/*
 * Copyright 2014 John D. Ament
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.
 *
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package ws.ament.hammock.core.impl;

import org.jboss.weld.environment.se.events.ContainerInitialized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.ament.hammock.core.WebServerLauncher;
import ws.ament.hammock.core.annotations.ManagementConfigLiteral;
import ws.ament.hammock.core.api.WebServerConfiguration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;

/**
 * A component that starts up a management interface, if defined.
 *
 */
@ApplicationScoped
public class StartManagement {
    private WebServerLauncher webServerLauncher = null;
    private final Logger logger = LoggerFactory.getLogger(StartApplication.class);
    @PostConstruct
    public void init() {
        Instance<WebServerConfiguration> webServerConfigurationInstance = CDI.current().select(WebServerConfiguration.class,
                ManagementConfigLiteral.INSTANCE);
        if(webServerConfigurationInstance.isAmbiguous() || webServerConfigurationInstance.isUnsatisfied()) {
            logger.warn("Not starting up management interface.");
        }
        else {
            this.webServerLauncher = new WebServerLauncher();
            WebServerConfiguration mgmtConfig = webServerConfigurationInstance.get();
            logger.info("Starting up managementConfig: [%s]",mgmtConfig);
            this.webServerLauncher.start(mgmtConfig);
        }

    }

    public void watch(@Observes ContainerInitialized containerInitialized) {

    }

    @PreDestroy
    public void shutdown() {
        if(this.webServerLauncher != null) {
            logger.info("Shutting down management listener");
            this.webServerLauncher.stop();
        }
    }
}
