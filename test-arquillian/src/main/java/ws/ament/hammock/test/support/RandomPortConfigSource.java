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

package ws.ament.hammock.test.support;

import org.eclipse.microprofile.config.spi.ConfigSource;

import java.util.Map;
import java.util.Random;

import static java.util.Collections.singletonMap;

public class RandomPortConfigSource implements ConfigSource {
    private static final String WEBSERVER_PORT = "webserver.port";

    private final String port;

    public RandomPortConfigSource() {
        Random random = new Random();
        this.port = Integer.toString(4000 + random.nextInt(500));
    }

    @Override
    public int getOrdinal() {
        return 10000;
    }

    @Override
    public Map<String, String> getProperties() {
        return singletonMap(WEBSERVER_PORT, port);
    }

    @Override
    public String getValue(String key) {
        if (key.equals(WEBSERVER_PORT)) {
            return port;
        }
        return null;
    }

    @Override
    public String getName() {
        return "random-port";
    }

}
