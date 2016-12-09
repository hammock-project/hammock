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

import com.rabbitmq.client.ConnectionFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class ConnectionFactoryProducer {
    @Produces
    @ApplicationScoped
    public ConnectionFactory createConnectionFactory(RabbitMQConfiguration rabbitMQConfiguration) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setAutomaticRecoveryEnabled(rabbitMQConfiguration.isAutomaticRecovery());
        connectionFactory.setClientProperties(rabbitMQConfiguration.getClientProperties());
        connectionFactory.setConnectionTimeout(rabbitMQConfiguration.getConnectionTimeout());
        connectionFactory.setExceptionHandler(rabbitMQConfiguration.getExceptionHandler());
        connectionFactory.setHandshakeTimeout(rabbitMQConfiguration.getHandshakeTimeout());
        connectionFactory.setHeartbeatExecutor(rabbitMQConfiguration.getHeartbeatExecutor());
        connectionFactory.setMetricsCollector(rabbitMQConfiguration.getMetricsCollector());
        connectionFactory.setHost(rabbitMQConfiguration.getHost());
        connectionFactory.setNetworkRecoveryInterval(rabbitMQConfiguration.getNetworkRecoveryInterval());
        if(rabbitMQConfiguration.isNio()) {
            connectionFactory.useNio();
            connectionFactory.setNioParams(rabbitMQConfiguration.getNioParams());
        }
        connectionFactory.setPassword(rabbitMQConfiguration.getPassword());
        connectionFactory.setPort(rabbitMQConfiguration.getPort());
        connectionFactory.setRequestedChannelMax(rabbitMQConfiguration.getRequestedChannelMax());
        connectionFactory.setRequestedFrameMax(rabbitMQConfiguration.getRequestedFrameMax());
        connectionFactory.setRequestedHeartbeat(rabbitMQConfiguration.getRequestedHeartbeat());
        connectionFactory.setSaslConfig(rabbitMQConfiguration.getSaslConfig());
        connectionFactory.setSharedExecutor(rabbitMQConfiguration.getSharedExecutor());
        connectionFactory.setShutdownExecutor(rabbitMQConfiguration.getShutdownExecutor());
        connectionFactory.setShutdownTimeout(rabbitMQConfiguration.getShutdownTimeout());
        connectionFactory.setSocketConfigurator(rabbitMQConfiguration.getSocketConf());
        connectionFactory.setSocketFactory(rabbitMQConfiguration.getFactory());
        connectionFactory.setThreadFactory(rabbitMQConfiguration.getThreadFactory());
        connectionFactory.setTopologyRecoveryEnabled(rabbitMQConfiguration.isTopologyRecovery());
        try {
            connectionFactory.setUri(rabbitMQConfiguration.getUri());
        }
        catch (Exception e) {
            throw new RuntimeException("Unable to populate URI ",e);
        }
        connectionFactory.setUsername(rabbitMQConfiguration.getUsername());
        connectionFactory.setVirtualHost(rabbitMQConfiguration.getVirtualHost());
        if(rabbitMQConfiguration.getSslContext() != null) {
            connectionFactory.useSslProtocol(rabbitMQConfiguration.getSslContext());
        }
        return connectionFactory;
    }
}
