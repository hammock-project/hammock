/*
 * Copyright 2018 Hammock and its contributors
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

package org.hammock.test.jersey;

import static org.eclipse.microprofile.config.ConfigProvider.getConfig;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;

import javax.inject.Inject;

import org.glassfish.jersey.server.ResourceConfig;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import ws.ament.hammock.rest.jersey.JerseyContainerConfigurator;
import ws.ament.hammock.test.support.EnableRandomWebServerPort;
import ws.ament.hammock.test.support.HammockArchive;

@RunWith(Arquillian.class)
@EnableRandomWebServerPort
public class JerseyPropertiesTest {

    @Inject
    JerseyContainerConfigurator configurator;

    @Deployment
    public static JavaArchive createArchive() {
        return new HammockArchive().classes(JerseyContainerConfigurator.class).jar();
    }

    @Test
    public void applicationNameIsSetViaConfigs() throws ReflectiveOperationException {
        ResourceConfig config = (ResourceConfig) configurator.getAttributes().values().iterator().next();

        Method m = ResourceConfig.class.getDeclaredMethod("setupApplicationName");
        m.setAccessible(true);
        m.invoke(config);

        assertEquals(
            getConfig().getValue("jersey.config.server.application.name", String.class),
            config.getApplicationName()
        );
    }

}
