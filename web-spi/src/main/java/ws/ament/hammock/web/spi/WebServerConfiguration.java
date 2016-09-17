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

package ws.ament.hammock.web.spi;

import org.apache.deltaspike.core.api.config.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.logging.Logger;

@ApplicationScoped
public class WebServerConfiguration {
    @Inject
    @ConfigProperty(name="webserver.port",defaultValue = "8080")
    private int webserverPort;

    @Inject
    @ConfigProperty(name="file.dir",defaultValue = "/tmp")
    private String fileDir;

    @PostConstruct
    public void logPort() {
        Logger.getLogger(WebServerConfiguration.class.getName()).info("Starting webserver on port '"+webserverPort+"'");
    }

    public int getWebserverPort() {
        return webserverPort;
    }

    public String getFileDir() {
        return fileDir;
    }
}
