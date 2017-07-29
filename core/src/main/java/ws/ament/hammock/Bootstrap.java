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

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import ws.ament.hammock.bootstrap.Bootstrapper;

import java.util.ServiceLoader;

public class Bootstrap {

    private static final String DEFAULT_LOGGING = "true";
    static String[] ARGS;

    public static void main(String... args) {
        ARGS = args;
        Config config = ConfigProvider.getConfig();
        String log4j2Enabled = config.getOptionalValue("enable.log4j2.integration", String.class).orElse(DEFAULT_LOGGING);

        if(DEFAULT_LOGGING.equals(log4j2Enabled)) {
            System.setProperty("java.util.logging.manager","org.apache.logging.log4j.jul.LogManager");
        }
        Bootstrapper bootstrapper = ServiceLoader.load(Bootstrapper.class).iterator().next();
        bootstrapper.start();
    }
}
