/*
 * Copyright 2016 John D. Ament
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

package ws.ament.hammock.web.tomcat;

import org.apache.commons.io.IOUtils;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.Test;
import ws.ament.hammock.core.config.ConfigurationBootstrap;
import ws.ament.hammock.web.spi.ConfigurationProvider;
import ws.ament.hammock.web.spi.ServletDescriptor;

import javax.servlet.annotation.WebInitParam;
import java.io.InputStream;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by johnament on 7/2/16.
 */
public class TomcatWebServerTest {
    @Test
    public void shouldBootWebServer() throws Exception {
        try(WeldContainer weldContainer = new Weld().disableDiscovery()
                .beanClasses(TomcatWebServer.class, DefaultServlet.class, MessageProvider.class,
                        ConfigurationProvider.class, ConfigurationBootstrap.class, TestServletProducer.class)
                .initialize()) {
            TomcatWebServer tomcat = weldContainer.select(TomcatWebServer.class).get();
            tomcat.addServlet(new ServletDescriptor("Default",new String[]{"/*"},new String[]{"/*"},1,new WebInitParam[]{}, true, DefaultServlet.class));
            tomcat.start();
            Thread.sleep(10000);
            try(InputStream stream = new URL("http://localhost:8080/").openStream()) {
                String data = IOUtils.toString(stream).trim();
                assertThat(data).isEqualTo(MessageProvider.DATA);
            }

            try(InputStream stream = new URL("http://localhost:8080/").openStream()) {
                String data = IOUtils.toString(stream).trim();
                assertThat(data).isEqualTo(MessageProvider.DATA);
            }
        }
    }
}
