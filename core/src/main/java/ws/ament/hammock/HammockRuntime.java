/*
 * Copyright 2017 Hammock and its contributors
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

package ws.ament.hammock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.net.InetAddress;
import java.net.UnknownHostException;

@ApplicationScoped
public class HammockRuntime {
    private final Logger LOGGER = LogManager.getLogger(HammockRuntime.class.getName());
    @Inject
    @ConfigProperty(name="webserver.port",defaultValue = "8080")
    private int port;

    @Inject
    @ConfigProperty(name="webserver.address",defaultValue = "0.0.0.0")
    private String address;

    @Inject
    @ConfigProperty(name="webserver.secured.port", defaultValue="0")
    private int securedPort;

    void init(@Observes @Initialized(ApplicationScoped.class) Object obj) {
        LOGGER.info("Starting webserver on "+getMachineURL());
        if(isSecuredConfigured()) {
            LOGGER.info("Running securely on " + getSecureURL());
        }
    }

    public String getMachineURL() {
        String serverName = getServerName();
        return String.format("http://%s:%d", serverName, port);
    }

    public String getSecureURL() {
        return String.format("https://%s:%d",getServerName(), securedPort);
    }

    public boolean isSecuredConfigured(){
        return securedPort != 0;
    }

    private String getServerName() {
        if(address.equals("0.0.0.0")) {
            try {
                return InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException e) {
                LOGGER.warn("Unable to read hostname",e);
            }
        }
        return address;
    }

}
