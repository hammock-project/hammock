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

abstract public class AbstractRandomPortConfigSource implements ConfigSource {
    private static final int DEFAULT_PORT_START = 4000;
    private static final int DEFAULT_PORT_RANGE = 1000;
    private static final int DEFAULT_PORT_MAX_FAIL = 100;
    private static final String DEFAULT_SOURCE_NAME_POSTFIX = ".test.random";
    private final Map<String, String> properties;
    private final String configSourceName;

    public AbstractRandomPortConfigSource(String configProperty) {
        this(configProperty, configProperty + DEFAULT_SOURCE_NAME_POSTFIX);
    }
    
    public AbstractRandomPortConfigSource(String configProperty, String configSourceName) {
        this(configProperty, configSourceName, DEFAULT_PORT_START, DEFAULT_PORT_RANGE, DEFAULT_PORT_MAX_FAIL);
    }

    public AbstractRandomPortConfigSource(String configProperty, String configSourceName, int portStart, int portRange, int portMaxFail) {
        this.configSourceName = configSourceName;
        this.properties = singletonMap(configProperty, findServerPort(portStart, portRange, portMaxFail));
    }

    private String findServerPort(int portStart, int portRange, int portMaxFail) {
        int openPortTry = 0;
        Optional<ServerSocket> serverPort = Optional.empty();
        while (!serverPort.isPresent()) {
            if (openPortTry++ > portMaxFail) {
                throw new IllegalStateException("No openable server ports found, reached try limit; " + portMaxFail);
            }
            serverPort = openServerSocket(nextRandomServerPort(portStart, portRange));
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

    private int nextRandomServerPort(int portStart, int portRange) {
        int offset = new Random().nextInt(portRange);
        return portStart + offset;
    }

    private Optional<ServerSocket> openServerSocket(int port) {
        try {
            return Optional.of(new ServerSocket(port));
        } catch (IOException ex) {
            return Optional.empty();
        }
    }

    @Override
    public final int getOrdinal() {
        return Short.MAX_VALUE;
    }

    @Override
    public final Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public final String getValue(String key) {
        return properties.get(key);
    }

    @Override
    public final String getName() {
        return configSourceName;
    }
}
