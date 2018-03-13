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
import java.util.Optional;
import java.util.Random;

import static java.util.Collections.singletonMap;

import java.io.IOException;
import java.net.ServerSocket;

public class RandomPortConfigSource implements ConfigSource {
    private static final String CONFIG_SOURCE_NAME = "random-server-port";
    private static final String WEBSERVER_PORT = "webserver.port";
    private static final int PORT_START = 4000;
    private static final int PORT_RANGE = 1000;
    private static final int PORT_MAX_FAIL = 100;
    private Map<String, String> properties = singletonMap(WEBSERVER_PORT, findServerPort());

    private String findServerPort() {
        int openPortTry = 0;
        Optional<ServerSocket> serverPort = Optional.empty();
        while (!serverPort.isPresent()) {
            if (openPortTry++ > PORT_MAX_FAIL) {
                throw new IllegalStateException("No openable server ports found, reached try limit; " + PORT_MAX_FAIL);
            }
            serverPort = openServerSocket(nextRandomServerPort());
        }
        try {
            return Integer.toString(serverPort.get().getLocalPort());
        } finally {
            try {
                serverPort.get().close();
            } catch (IOException ex) {
                // ignore
            }
            
        }
    }

    private int nextRandomServerPort() {
        int offset = new Random().nextInt(PORT_RANGE);
        return PORT_START + offset;
    }

    private Optional<ServerSocket> openServerSocket(int port) {
        try {
            return Optional.of(new ServerSocket(port));
        } catch (IOException ex) {
            return Optional.empty();
        }
    }

    @Override
    public int getOrdinal() {
        return Short.MAX_VALUE;
    }

    @Override
    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public String getValue(String key) {
        return properties.get(key);
    }

    @Override
    public String getName() {
        return CONFIG_SOURCE_NAME;
    }
}
