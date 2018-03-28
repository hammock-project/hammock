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

package ws.ament.hammock.web.tck;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import ws.ament.hammock.HammockRuntime;
import ws.ament.hammock.bootstrap.Bootstrapper;
import ws.ament.hammock.test.support.EnableRandomWebServerPort;
import ws.ament.hammock.web.spi.StartWebServer;
import ws.ament.hammock.web.spi.WebServerConfiguration;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Arquillian.class)
@EnableRandomWebServerPort
public abstract class ListenerTest {

    public static JavaArchive createArchive(Class<?>... classes) {
        String property = System.getProperty(Bootstrapper.class.getName());
        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(DefaultServlet.class, HammockRuntime.class, MessageProvider.class, DefaultListener.class,
                        WebServerConfiguration.class, StartWebServer.class)
                .addClasses(classes)
                .addAsServiceProvider(Bootstrapper.class.getName(), property)
                .addAsManifestResource(new FileAsset(new File("src/main/resources/META-INF/beans.xml")), "beans.xml");
    }

    @Test
    public void shouldInvokeListener() throws Exception {
        assertThat(DefaultListener.INITIALIZED).isTrue();
    }

}