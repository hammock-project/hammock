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

package ws.ament.hammock.web.base;

import org.apache.tamaya.core.propertysource.BasePropertySource;

import java.util.HashMap;
import java.util.Map;

public class DefaultProperties extends BasePropertySource {
    public DefaultProperties() {
        super(-10);
    }

    @Override
    public String getName() {
        return "hammock-default";
    }

    @Override
    public Map<String, String> getProperties() {
        Map<String, String> properties = new HashMap<>();
        properties.put("webserver.port", "8080");
        properties.put("file.dir", "/tmp");
        return properties;
    }
}
