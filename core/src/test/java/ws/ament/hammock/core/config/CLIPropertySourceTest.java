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

import org.apache.deltaspike.core.spi.config.ConfigSource;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CLIPropertySourceTest {

    @Test
    public void shouldReadPropertiesFromDefinedLocation() {
        System.setProperty("sun.java.command","somejar.jar --key=value -s j");
        ConfigSource cliPropertySource = CLIPropertySource.parseMainArgs();
        String key = cliPropertySource.getPropertyValue("key");
        assertEquals(key, "value");
        String s = cliPropertySource.getPropertyValue("s");
        assertEquals("j", s);
    }
}
