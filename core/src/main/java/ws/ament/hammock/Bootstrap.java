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

package ws.ament.hammock;

import org.apache.geronimo.config.ConfigImpl;
import org.apache.geronimo.config.DefaultConfigBuilder;
import org.apache.geronimo.config.configsource.PropertyFileConfigSourceProvider;
import org.apache.geronimo.config.converters.*;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;
import org.eclipse.microprofile.config.spi.ConfigSource;
import ws.ament.hammock.bootstrap.Bootstrapper;
import ws.ament.hammock.core.config.CLIPropertySource;

import java.util.List;
import java.util.ServiceLoader;

import static java.util.Arrays.asList;

public class Bootstrap {

    private static final String DEFAULT_LOGGING = "true";

    private static final List<String> defaultPropertyFiles = asList("hammock.properties", "microprofile.properties",
            "META-INF/microprofile.properties", "META-INF/microprofile-config.properties");

    public static void main(String... args) {
        Config config = setupConfig(args);
        ConfigProviderResolver.instance().registerConfig(config, null);
        String log4j2Enabled = config.getOptionalValue("enable.log4j2.integration", String.class).orElse(DEFAULT_LOGGING);

        if(DEFAULT_LOGGING.equals(log4j2Enabled)) {
            System.setProperty("java.util.logging.manager","org.apache.logging.log4j.jul.LogManager");
        }
        Bootstrapper bootstrapper = ServiceLoader.load(Bootstrapper.class).iterator().next();
        bootstrapper.start();
    }

    public static Config setupConfig(String[] args) {
        DefaultConfigBuilder defaultConfigBuilder = new DefaultConfigBuilder();
        defaultConfigBuilder.addDefaultSources()
                .addDiscoveredConverters()
                .addDiscoveredSources();
        ConfigSource cli = CLIPropertySource.parseMainArgs(args);
        defaultConfigBuilder.withSources(cli);
        for(String prop : defaultPropertyFiles) {
            List<ConfigSource> configSources = new PropertyFileConfigSourceProvider(prop, true, ClassLoader.getSystemClassLoader())
                    .getConfigSources(ClassLoader.getSystemClassLoader());
            defaultConfigBuilder.withSources(configSources.toArray(new ConfigSource[configSources.size()]));
        }
        ConfigImpl config = (ConfigImpl)defaultConfigBuilder.build();
        config.getConverters().put(int.class, IntegerConverter.INSTANCE);
        config.getConverters().put(long.class, LongConverter.INSTANCE);
        config.getConverters().put(float.class, FloatConverter.INSTANCE);
        config.getConverters().put(double.class, DoubleConverter.INSTANCE);
        config.getConverters().put(boolean.class, BooleanConverter.INSTANCE);

        return config;
    }
}
