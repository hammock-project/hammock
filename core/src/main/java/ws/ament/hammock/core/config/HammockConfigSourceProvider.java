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

import org.apache.deltaspike.core.impl.config.PropertiesConfigSource;
import org.apache.deltaspike.core.spi.config.ConfigSource;
import org.apache.deltaspike.core.spi.config.ConfigSourceProvider;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class HammockConfigSourceProvider implements ConfigSourceProvider{
    @Override
    public List<ConfigSource> getConfigSources() {
        List<ConfigSource> configSources = new ArrayList<>();
        ConfigSource cli = CLIPropertySource.parseMainArgs();
        configSources.add(cli);
        String propertyValue = cli.getPropertyValue("hammock.external.config");
        if(propertyValue != null) {
            try(FileInputStream fis = new FileInputStream(propertyValue)) {
                Properties props = new Properties();
                props.load(fis);
                configSources.add(new PropertiesConfigSource(props) {
                    @Override
                    public String getConfigName() {
                        return propertyValue;
                    }
                });
            }
            catch (IOException e) {
                throw new RuntimeException("Unable to load "+propertyValue,e);
            }
        }
        return configSources;
    }
}
