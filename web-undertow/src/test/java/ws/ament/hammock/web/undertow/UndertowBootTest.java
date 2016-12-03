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

package ws.ament.hammock.web.undertow;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.deltaspike.core.impl.config.ConfigurationExtension;
import org.apache.deltaspike.core.impl.config.DefaultConfigPropertyProducer;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.jboss.weld.environment.servlet.WeldServletLifecycle;
import org.junit.Test;

import ws.ament.hammock.web.spi.ServletDescriptor;
import ws.ament.hammock.web.spi.WebServerConfiguration;
import ws.ament.hammock.web.undertow.websocket.UndertowWebSocketExtension;

public class UndertowBootTest {
    @Test
    public void shouldBootWebServer() throws Exception {
        SSLBypass.disableSSLChecks();
        try(WeldContainer weldContainer = new Weld().disableDiscovery()
                .extensions(new UndertowWebSocketExtension(), new ConfigurationExtension())
                .beanClasses(UndertowServletMapper.class, UndertowWebServer.class, DefaultServlet.class, MessageProvider.class,
                        WebServerConfiguration.class, DefaultConfigPropertyProducer.class)
                .initialize()) {
            UndertowWebServer undertowWebServer = weldContainer.select(UndertowWebServer.class).get();
            undertowWebServer.addServletContextAttribute(WeldServletLifecycle.BEAN_MANAGER_ATTRIBUTE_NAME, weldContainer.getBeanManager());
            undertowWebServer.addServlet(new ServletDescriptor("Default",null,new String[]{"/"},1,null,true,DefaultServlet.class));
            undertowWebServer.start();

            try(InputStream stream = new URL("http://localhost:8080/").openStream()) {
                String data = IOUtils.toString(stream).trim();
                assertThat(data).isEqualTo(MessageProvider.DATA);
            }

            try(InputStream stream = new URL("https://localhost:8443/").openStream()) {
                String data = IOUtils.toString(stream).trim();
                assertThat(data).isEqualTo(MessageProvider.DATA);
            }
        }
    }

}
