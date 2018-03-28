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

package ws.ament.hammock.health;

import org.apache.geronimo.config.ConfigImpl;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

import ws.ament.hammock.test.support.HammockArchive;
import ws.ament.hammock.web.api.FilterDescriptor;
import ws.ament.hammock.web.api.ServletDescriptor;
import ws.ament.hammock.web.api.WebServer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletContextListener;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Arquillian.class)
public class InjectedResponseBuilderTest {

    @Deployment
    public static Archive<?> jar() {
        return new HammockArchive()
                .classes(TestWebServer.class, InjectedCheck.class)
                .beansXmlEmpty()
                .jar()
                .addPackages(true, ConfigImpl.class.getPackage());
    }

    @Inject
    private InjectedCheck injectedCheck;

    @Test
    public void shouldHaveProperName() {
        assertThat(injectedCheck.call().getName()).isEqualTo("InjectedCheck");
    }

    @ApplicationScoped
    public static class TestWebServer implements WebServer {

        @Override
        public void addServlet(ServletDescriptor servletDescriptor) {

        }

        @Override
        public void addFilter(FilterDescriptor filterDescriptor) {

        }

        @Override
        public void addServletContextAttribute(String name, Object value) {

        }

        @Override
        public void addInitParameter(String key, String value) {

        }

        @Override
        public void addListener(Class<? extends ServletContextListener> listenerClass) {

        }

        @Override
        public void start() {

        }

        @Override
        public void stop() {

        }
    }
}
