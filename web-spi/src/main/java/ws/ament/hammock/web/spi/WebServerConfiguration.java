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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import ws.ament.hammock.HammockRuntime;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class WebServerConfiguration {
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
    
    @Inject
    @ConfigProperty(name="webserver.keystore.path")
    private Optional<String> keystorePath;
    
    @Inject
    @ConfigProperty(name="webserver.keystore.type")
    private Optional<String> keystoreType;

    @Inject
    @ConfigProperty(name="webserver.keystore.password")
    private Optional<String> keystorePassword;
    
    @Inject
    @ConfigProperty(name="webserver.truststore.path")
    private Optional<String> truststorePath;
    
    @Inject
    @ConfigProperty(name="webserver.truststore.type")
    private Optional<String> truststoreType;

    @Inject
    @ConfigProperty(name="webserver.truststore.password")
    private Optional<String> truststorePassword;

    @Inject
    @ConfigProperty(name="file.dir",defaultValue = "/tmp")
    private String fileDir;

    public int getPort() {
        return port;
    }
    
    public String getAddress() {
        return address;
    }
    
    public int getSecuredPort(){
    	return securedPort;
    }

    public String getFileDir() {
        return fileDir;
    }

	public String getKeystorePath() {
		return keystorePath.orElse(null);
	}

	public String getKeystoreType() {
		return keystoreType.orElse(null);
	}

	public String getKeystorePassword() {
		return keystorePassword.orElse(null);
	}

	public String getTruststorePath() {
		return truststorePath.orElse(null);
	}

	public String getTruststoreType() {
		return truststoreType.orElse(null);
	}

	public String getTruststorePassword() {
		return truststorePassword.orElse(null);
	}
}
