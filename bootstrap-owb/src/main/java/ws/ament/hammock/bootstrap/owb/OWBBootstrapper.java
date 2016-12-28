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

package ws.ament.hammock.bootstrap.owb;

import org.apache.webbeans.config.WebBeansContext;
import org.apache.webbeans.spi.ContainerLifecycle;
import ws.ament.hammock.bootstrap.Bootstrapper;
import ws.ament.hammock.web.api.WebServer;

public class OWBBootstrapper implements Bootstrapper {
    private ContainerLifecycle lifecycle;

    @Override
    public void start() {
        lifecycle = WebBeansContext.currentInstance().getService(ContainerLifecycle.class);
        lifecycle.startApplication(null);
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
    public void stop() {
        lifecycle.stopApplication(null);
    }

    @Override
    public void configure(WebServer webServer) {
        webServer.addListener(OWBListener.class);
    }
}
