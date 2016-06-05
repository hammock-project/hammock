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

package ws.ament.hammock.core.config;

import org.apache.tamaya.Configuration;
import org.apache.tamaya.core.propertysource.SystemPropertySource;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.Test;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigurationBootstrapTest {
    @Test
    public void shouldLoadConfiguration() {
        Weld weld = new Weld().disableDiscovery().beanClasses(ConfigurationBootstrap.class, CLIPropertySource.class);
        try(WeldContainer container = weld.initialize()) {
            Configuration configuration = container.select(Configuration.class).get();
            assertThat(configuration).isNotNull();
        }
    }

    @Test
    public void shouldHaveProperties() {
        String value = "I am populated";
        System.setProperty("populated.value", value);
        Weld weld = new Weld().disableDiscovery().beanClasses(ConfigurationBootstrap.class,
                BeanClass.class);
        try(WeldContainer container = weld.initialize()) {
            ConfigurationBootstrap configuration = container.select(ConfigurationBootstrap.class).get();
            TypeSafeConfig ty = configuration.populate(new TypeSafeConfig());
            assertThat(ty.getPopulatedValue()).isEqualTo(value);
        }
    }
    public static class BeanClass {
        @Produces
        @ApplicationScoped
        private SystemPropertySource systemPropertySource = new SystemPropertySource();
    }
}
