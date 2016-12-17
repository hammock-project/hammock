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

package ws.ament.hammock.web.jetty;

import org.apache.commons.io.IOUtils;
import org.apache.deltaspike.core.impl.config.DefaultConfigPropertyProducer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import ws.ament.hammock.web.spi.StartWebServer;
import ws.ament.hammock.web.spi.WebServerConfiguration;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Arquillian.class)
public class JettyBootTest {
    @Deployment
    public static JavaArchive createArchive() {
        return ShrinkWrap.create(JavaArchive.class).addClasses(JettyWebServer.class, DefaultServlet.class, MessageProvider.class,
                WebServerConfiguration.class, DefaultConfigPropertyProducer.class, StartWebServer.class)
                .addAsManifestResource(new FileAsset(new File("src/main/resources/META-INF/beans.xml")), "beans.xml");
    }

    @Test
    public void shouldBootWebServer() throws Exception {
        SSLBypass.disableSSLChecks();
        try (InputStream stream = new URL("http://localhost:8080/").openStream()) {
            String data = IOUtils.toString(stream).trim();
            assertThat(data).isEqualTo(MessageProvider.DATA);
        }

        try (InputStream stream = new URL("https://localhost:8443/").openStream()) {
            String data = IOUtils.toString(stream).trim();
            assertThat(data).isEqualTo(MessageProvider.DATA);
        }
    }

    @Test
    public void shouldBootWebServerWithOnlyFilter() throws Exception {
        DefaultFilter.doFilter = true;
        try (InputStream stream = new URL("http://localhost:8080/").openStream()) {
            String data = IOUtils.toString(stream).trim();
            assertThat(data).isEqualTo("Hello, world!");
        }

        try (InputStream stream = new URL("http://localhost:8080/rest").openStream()) {
            String data = IOUtils.toString(stream).trim();
            assertThat(data).isEqualTo("Hello, world!");
        }
    }
}
