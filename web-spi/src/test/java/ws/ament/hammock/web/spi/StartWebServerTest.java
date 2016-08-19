/*
 * Copyright 2015 John D. Ament
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

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.exceptions.WeldException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import javax.enterprise.inject.spi.DeploymentException;
import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StartWebServerTest {
    private static final String NO_WEBSERVERS = "No web server implementations found on the classpath";
    private static final String WEBSERVERS = "Multiple web server implementations found on the classpath";
    @Rule
    public TestName testName = new TestName();
    @Test
    public void shouldNotFindImpls() {
        assertThatThrownBy(() -> {
            new Weld(testName.getMethodName()).addExtension(new StartWebServer()).initialize();
        }).isInstanceOf(DeploymentException.class)
                .hasMessageContaining(NO_WEBSERVERS);
    }

    @Test
    public void shouldFindTooMany() {
        assertThatThrownBy(() -> {
            new Weld(testName.getMethodName()).addExtension(new StartWebServer()).beanClasses(WebServerA.class, WebServerB.class).initialize();
        }).isInstanceOf(DeploymentException.class)
                .hasMessageContaining(WEBSERVERS);
    }

    public static class WebServerA implements WebServer {

        @Override
        public void addServlet(ServletDescriptor servletDescriptor) {

        }

        @Override
        public void addServletContextAttribute(String name, Object value) {

        }

        @Override
        public void start() {

        }

        @Override
        public void stop() {

        }

        @Override
        public String toString() {
            return "WebServerA";
        }
    }

    public static class WebServerB implements WebServer {

        @Override
        public void addServlet(ServletDescriptor servletDescriptor) {

        }

        @Override
        public void addServletContextAttribute(String name, Object value) {

        }

        @Override
        public void start() {

        }

        @Override
        public void stop() {

        }

        @Override
        public String toString() {
            return "WebServerB";
        }
    }
}