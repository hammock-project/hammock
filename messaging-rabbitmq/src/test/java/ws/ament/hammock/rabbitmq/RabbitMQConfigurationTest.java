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
import org.apache.deltaspike.core.impl.config.ConfigurationExtension;
import org.apache.deltaspike.core.impl.config.DefaultConfigPropertyProducer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.enterprise.inject.spi.Extension;
import javax.inject.Inject;
import java.io.File;

import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class RabbitMQConfigurationTest {
    @Deployment
    public static JavaArchive create() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(RabbitMQConfiguration.class, ConnectionFactoryProducer.class, DefaultConfigPropertyProducer.class)
                .addPackage("io.astefanutti.metrics.cdi")
                .addAsServiceProviderAndClasses(Extension.class, ConfigurationExtension.class)
                .addAsManifestResource(new FileAsset(new File("src/main/resources/META-INF/beans.xml")), "beans.xml");
    }

    @Inject
    private ConnectionFactory connectionFactory;

    @Test
    public void shouldCreateAConnectionFactory() {
        assertNotNull(connectionFactory);
    }
}
