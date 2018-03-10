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

package ws.ament.hammock.bootstrap.owb;

import ws.ament.hammock.bootstrap.Bootstrapper;
import ws.ament.hammock.web.api.WebServer;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;

public class OWBBootstrapper implements Bootstrapper {
    private SeContainer seContainer;
    private SeContainerInitializer seContainerInitializer;

    public OWBBootstrapper() {
        seContainerInitializer = SeContainerInitializer.newInstance();
    }

    @Override
    public void start() {
        seContainer = seContainerInitializer.initialize();
        initThread();
    }

    @Override
    public void stop() {
        seContainer.close();
    }

    private void initThread() {
        Thread t = new Thread(() -> {
            while(true) {
                try {
                    Thread.sleep(Long.MAX_VALUE-1);
                } catch (InterruptedException e) {

                }
            }
        });
        t.setDaemon(false);
        t.start();
    }

    @Override
    public void configure(WebServer webServer) {
        webServer.addListener(OWBListener.class);
    }
}
