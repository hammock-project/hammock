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
    private int port;
    
    @Inject
    @ConfigProperty(name="webserver.address",defaultValue = "0.0.0.0")
    private String address;
    
    @Inject
    @ConfigProperty(name="webserver.secured.port", defaultValue="0")
    private int securedPort;
    
    @Inject
    @ConfigProperty(name="webserver.keystore.path")
    private String keystorePath;
    
    @Inject
    @ConfigProperty(name="webserver.keystore.type")
    private String keystoreType;

    @Inject
    @ConfigProperty(name="webserver.keystore.password")
    private String keystorePassword;
    
    @Inject
    @ConfigProperty(name="webserver.truststore.path")
    private String truststorePath;
    
    @Inject
    @ConfigProperty(name="webserver.truststore.type")
    private String truststoreType;

    @Inject
    @ConfigProperty(name="webserver.truststore.password")
    private String truststorePassword;

    @Inject
    @ConfigProperty(name="file.dir",defaultValue = "/tmp")
    private String fileDir;

    @PostConstruct
    public void logPort() {
        Logger.getLogger(WebServerConfiguration.class.getName()).info("Starting webserver on http://" + address + ":" + port + (securedPort != 0 ? " https://" +address  + ":" + securedPort + " " :""));
    }

    public int getPort() {
        return port;
    }
    
    public String getAddress() {
        return address;
    }
    
    public boolean isSecuredConfigured(){
    	return securedPort != 0;
    }
    
    public int getSecuredPort(){
    	return securedPort;
    }

    public String getFileDir() {
        return fileDir;
    }

	public String getKeystorePath() {
		return keystorePath;
	}

	public String getKeystoreType() {
		return keystoreType;
	}

	public String getKeystorePassword() {
		return keystorePassword;
	}

	public String getTruststorePath() {
		return truststorePath;
	}

	public String getTruststoreType() {
		return truststoreType;
	}

	public String getTruststorePassword() {
		return truststorePassword;
	}
}
