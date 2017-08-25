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

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import ws.ament.hammock.web.api.FilterDescriptor;
import ws.ament.hammock.web.api.ServletDescriptor;
import ws.ament.hammock.web.api.WebServer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.servlet.ServletContextListener;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Arquillian.class)
public class InjectedResponseBuilderTest {

    @Deployment
    public static Archive<?> jar() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(TestWebServer.class, InjectedCheck.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    private InjectedCheck injectedCheck;

    @Test
    public void shouldHaveProperName() {
        assertThat(injectedCheck.call().getName()).isEqualTo("InjectedCheck");
    }

    @Dependent
    public static class InjectedCheck implements HealthCheck {
        @Inject
        private HealthCheckResponseBuilder responseBuilder;
        @Override
        public HealthCheckResponse call() {
            return responseBuilder.up().build();
        }
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
