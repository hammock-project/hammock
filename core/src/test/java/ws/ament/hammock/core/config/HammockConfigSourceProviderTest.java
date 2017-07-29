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

package ws.ament.hammock.core.config;

import org.eclipse.microprofile.config.spi.ConfigSource;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HammockConfigSourceProviderTest {

    @Test
    public void shouldCreateTwoConfigSourcesWhenPropertyConfigured() {
        String[] args = {"--hammock.external.config=src/test/resources/testing.properties"};
        HammockConfigSourceProvider hammockConfigSourceProvider = new HammockConfigSourceProvider();
        assertEquals(2,hammockConfigSourceProvider.getConfigSources(getClass().getClassLoader(), args).size());
    }

    @Test
    public void shouldOnlyCreateOneConfigSource() {
        String[] args = {"--cli=true"};
        HammockConfigSourceProvider hammockConfigSourceProvider = new HammockConfigSourceProvider();
        assertEquals(1,hammockConfigSourceProvider.getConfigSources(getClass().getClassLoader(), args).size());
    }

    @Test
    public void shouldHaveCorrectPropertiesInFile() {
        String[] args = {"--hammock.external.config=src/test/resources/testing.properties"};
        HammockConfigSourceProvider hammockConfigSourceProvider = new HammockConfigSourceProvider();
        ConfigSource configSource = hammockConfigSourceProvider.getConfigSources(getClass().getClassLoader(), args).get(1);
        assertEquals("1492", configSource.getValue("something.random"));
    }
}