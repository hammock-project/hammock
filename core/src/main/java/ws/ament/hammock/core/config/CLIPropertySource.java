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

import org.apache.geronimo.config.configsource.BaseConfigSource;
import org.eclipse.microprofile.config.spi.ConfigSource;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * PropertySource that allows to add the programs main arguments as configuration entries. Unix syntax using '--' and
 * '-' params is supported.
 */
public class CLIPropertySource extends BaseConfigSource {

    private final Map<String, String> cli;

    /**
     * Creates a new instance.
     */
    private CLIPropertySource(Map<String, String> cliProps) {
        this.cli = cliProps;
    }

    /**
     * Configure the main arguments, herby parsing and mapping the main arguments into
     * configuration properties.
     *
     * @return the parsed main arguments as key/value pairs.
     */
    public static ConfigSource parseMainArgs(String...args) {
        Map<String, String> result;
        if (args == null) {
            result = Collections.emptyMap();
        } else {
            result = new HashMap<>();
            String prefix = System.getProperty("main.args.prefix","");
            String key = null;
            for (String arg : args) {
                if (arg.startsWith("--")) {
                    arg = arg.substring(2);
                    int index = arg.indexOf('=');
                    if (index > 0) {
                        key = arg.substring(0, index).trim();
                        result.put(prefix + key, arg.substring(index + 1).trim());
                        key = null;
                    } else {
                        result.put(prefix + arg, arg);
                    }
                } else if (arg.charAt(0) == '-') {
                    key = arg.substring(1);
                } else {
                    if (key != null) {
                        result.put(prefix + key, arg);
                        key = null;
                    } else {
                        result.put(prefix + arg, arg);
                    }
                }
            }
        }
        return new CLIPropertySource(result);
    }

    @Override
    public Map<String, String> getProperties() {
        return Collections.unmodifiableMap(cli);
    }

    @Override
    public String getValue(String s) {
        return cli.get(s);
    }

    @Override
    public String getName() {
        return "cli";
    }
}