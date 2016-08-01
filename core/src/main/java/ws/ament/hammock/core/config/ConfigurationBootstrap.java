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
import org.apache.tamaya.ConfigurationProvider;
import org.apache.tamaya.inject.ConfigurationInjection;
import org.apache.tamaya.spi.ConfigurationContext;
import org.apache.tamaya.spi.PropertySource;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ConfigurationBootstrap {
    @Inject
    @Any
    private Instance<PropertySource> discoveredPropertySources;

    @Produces
    @ApplicationScoped
    private Configuration configuration;

    @PostConstruct
    public void init() {
        List<PropertySource> propertySourceList = new ArrayList<>();
        discoveredPropertySources.forEach(dps -> {
            System.out.println("Adding "+dps);
            propertySourceList.add(dps);
        });
        ConfigurationContext configurationContext = ConfigurationProvider.getConfigurationContextBuilder()
                .addPropertySources(propertySourceList)
                .build();
        ConfigurationProvider.setConfigurationContext(configurationContext);
        this.configuration = ConfigurationProvider.getConfiguration();
    }

    public <T> T populate(T instance) {
        return ConfigurationInjection.getConfigurationInjector()
                .configure(instance, configuration);
    }
}
