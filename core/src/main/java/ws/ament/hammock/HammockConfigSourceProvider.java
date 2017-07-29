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

package ws.ament.hammock;

import org.apache.geronimo.config.configsource.PropertyFileConfigSource;
import org.apache.geronimo.config.configsource.PropertyFileConfigSourceProvider;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.ConfigSourceProvider;
import ws.ament.hammock.core.config.CLIPropertySource;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class HammockConfigSourceProvider implements ConfigSourceProvider{
    private static final List<String> defaultPropertyFiles = asList("hammock.properties",
            "microprofile.properties",
            "META-INF/microprofile.properties", "META-INF/microprofile-config.properties");

    @Override
    public List<ConfigSource> getConfigSources(ClassLoader classLoader) {
        List<ConfigSource> configSources = new ArrayList<>();
        ConfigSource cli = CLIPropertySource.parseMainArgs(Bootstrap.ARGS);
        configSources.add(cli);
        String propertyValue = cli.getValue("hammock.external.config");
        if(propertyValue != null) {
            try {
                URL url = Paths.get(propertyValue).toUri().toURL();
                configSources.add(new PropertyFileConfigSource(url));
            } catch (MalformedURLException e) {
                throw new RuntimeException("Unable to load "+propertyValue,e);
            }
        }

        for(String prop : defaultPropertyFiles) {
            configSources.addAll(new PropertyFileConfigSourceProvider(prop, true, classLoader).getConfigSources(classLoader));
        }

        return configSources;
    }
}
