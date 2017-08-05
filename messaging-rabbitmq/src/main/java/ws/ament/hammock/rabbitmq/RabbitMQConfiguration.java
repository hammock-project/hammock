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

package ws.ament.hammock.rabbitmq;

import com.rabbitmq.client.ExceptionHandler;
import com.rabbitmq.client.MetricsCollector;
import com.rabbitmq.client.SaslConfig;
import com.rabbitmq.client.SocketConfigurator;
import com.rabbitmq.client.impl.AMQConnection;
import com.rabbitmq.client.impl.nio.NioParams;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

@ApplicationScoped
public class RabbitMQConfiguration {
    @Inject
    @ConfigProperty(name = "rabbitmq.username", defaultValue = "guest")
    private String username;
    @Inject
    @ConfigProperty(name = "rabbitmq.password", defaultValue = "guest")
    private String password;
    @Inject
    @ConfigProperty(name = "rabbitmq.virtualHost", defaultValue = "/")
    private String virtualHost;
    @Inject
    @ConfigProperty(name = "rabbitmq.host", defaultValue = "localhost")
    private String host;
    @Inject
    @ConfigProperty(name = "rabbitmq.port", defaultValue = "-1")
    private int port;
    @Inject
    @ConfigProperty(name="rabbitmq.uri")
    private Optional<String> uri;
    @Inject
    @ConfigProperty(name = "rabbitmq.requestedChannelMax", defaultValue = "0")
    private int requestedChannelMax;
    @Inject
    @ConfigProperty(name = "rabbitmq.requestedFrameMax", defaultValue = "0")
    private int requestedFrameMax;
    @Inject
    @ConfigProperty(name = "rabbitmq.requestedHeartbeat", defaultValue = "60")
    private int requestedHeartbeat;
    @Inject
    @ConfigProperty(name = "rabbitmq.connectionTimeout", defaultValue = "60")
    private int connectionTimeout;
    @Inject
    @ConfigProperty(name = "rabbitmq.handshakeTimeout", defaultValue = "10000")
    private int handshakeTimeout;
    @Inject
    @ConfigProperty(name = "rabbitmq.shutdownTimeout", defaultValue = "10000")
    private int shutdownTimeout;
    @Inject
    @ConfigProperty(name = "rabbitmq.automaticRecovery", defaultValue = "false")
    private boolean automaticRecovery;
    @Inject
    @ConfigProperty(name = "rabbitmq.topologyRecovery", defaultValue = "false")
    private boolean topologyRecovery;
    @Inject
    @ConfigProperty(name = "rabbitmq.networkRecoveryInterval", defaultValue = "0")
    private long networkRecoveryInterval;
    @Inject
    @ConfigProperty(name = "rabbitmq.nio", defaultValue = "false")
    private boolean nio;
    @Inject
    private MetricsCollector metricsCollector;

    private Map<String, Object> clientProperties = AMQConnection.defaultClientProperties();
    private SocketFactory factory = SocketFactory.getDefault();
    private SaslConfig saslConfig;
    private ExecutorService sharedExecutor;
    private ThreadFactory threadFactory;
    private ExecutorService shutdownExecutor;
    private ScheduledExecutorService heartbeatExecutor;
    private SocketConfigurator socketConf;
    private ExceptionHandler exceptionHandler;
    private NioParams nioParams;
    private SSLContext sslContext;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getVirtualHost() {
        return virtualHost;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public int getRequestedChannelMax() {
        return requestedChannelMax;
    }

    public int getRequestedFrameMax() {
        return requestedFrameMax;
    }

    public int getRequestedHeartbeat() {
        return requestedHeartbeat;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public int getHandshakeTimeout() {
        return handshakeTimeout;
    }

    public int getShutdownTimeout() {
        return shutdownTimeout;
    }

    public boolean isAutomaticRecovery() {
        return automaticRecovery;
    }

    public boolean isTopologyRecovery() {
        return topologyRecovery;
    }

    public long getNetworkRecoveryInterval() {
        return networkRecoveryInterval;
    }

    public boolean isNio() {
        return nio;
    }

    public Map<String, Object> getClientProperties() {
        return clientProperties;
    }

    public SocketFactory getFactory() {
        return factory;
    }

    public SaslConfig getSaslConfig() {
        return saslConfig;
    }

    public ExecutorService getSharedExecutor() {
        return sharedExecutor;
    }

    public ThreadFactory getThreadFactory() {
        return threadFactory;
    }

    public ExecutorService getShutdownExecutor() {
        return shutdownExecutor;
    }

    public ScheduledExecutorService getHeartbeatExecutor() {
        return heartbeatExecutor;
    }

    public SocketConfigurator getSocketConf() {
        return socketConf;
    }

    public ExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    public MetricsCollector getMetricsCollector() {
        return metricsCollector;
    }

    public NioParams getNioParams() {
        return nioParams;
    }

    public SSLContext getSslContext() {
        return sslContext;
    }

    public URI getUri() {
        return URI.create(uri.orElse(null));
    }
}
