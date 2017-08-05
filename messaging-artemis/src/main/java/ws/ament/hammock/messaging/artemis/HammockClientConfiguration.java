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

package ws.ament.hammock.messaging.artemis;

import org.apache.artemis.client.cdi.configuration.ArtemisClientConfiguration;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class HammockClientConfiguration implements ArtemisClientConfiguration {
    @Inject
    @ConfigProperty(name = "hammock.messaging.username",defaultValue = "")
    private String username;
    @Inject
    @ConfigProperty(name = "hammock.messaging.password", defaultValue = "")
    private String password;
    @Inject
    @ConfigProperty(name = "hammock.messaging.url", defaultValue = "")
    private String url;
    @Inject
    @ConfigProperty(name = "hammock.messaging.host", defaultValue = "")
    private String host;
    @Inject
    @ConfigProperty(name = "hammock.messaging.port", defaultValue = "")
    private int port;
    @Inject
    @ConfigProperty(name = "hammock.messaging.connectorFactory",
            defaultValue = "org.apache.activemq.artemis.core.remoting.impl.netty.NettyConnectorFactory")
    private String connectorFactory;
    @Inject
    @ConfigProperty(name = "hammock.messaging.embeddedBroker",
            defaultValue = "false")
    private boolean embeddedBroker;
    @Inject
    @ConfigProperty(name = "hammock.messaging.ha",
            defaultValue = "false")
    private boolean ha;

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public Integer getPort() {
        return port;
    }

    @Override
    public String getConnectorFactory() {
        return connectorFactory;
    }

    @Override
    public boolean startEmbeddedBroker() {
        return embeddedBroker;
    }

    @Override
    public boolean isHa() {
        return ha;
    }

    @Override
    public boolean hasAuthentication() {
        return getUsername() != null && getPassword() != null;
    }
}
