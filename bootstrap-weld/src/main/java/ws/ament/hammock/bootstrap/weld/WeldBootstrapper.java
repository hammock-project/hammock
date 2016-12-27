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

package ws.ament.hammock.bootstrap.weld;

import org.jboss.weld.bootstrap.api.helpers.RegistrySingletonProvider;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.jboss.weld.environment.servlet.Listener;
import org.jboss.weld.environment.servlet.WeldServletLifecycle;
import org.jboss.weld.manager.BeanManagerImpl;
import ws.ament.hammock.bootstrap.Bootstrapper;
import ws.ament.hammock.web.api.WebServer;

import javax.enterprise.inject.spi.CDI;

public class WeldBootstrapper implements Bootstrapper{
    private Weld weld;
    private WeldContainer container;

    public WeldBootstrapper() {
        weld = new Weld(RegistrySingletonProvider.STATIC_INSTANCE);
    }

    @Override
    public void start() {
        container = weld.initialize();
    }

    @Override
    public void stop() {
        container.close();
    }

    @Override
    public void configure(WebServer webServer) {
        BeanManagerImpl beanManager = CDI.current().select(BeanManagerImpl.class).get();
        webServer.addServletContextAttribute(WeldServletLifecycle.BEAN_MANAGER_ATTRIBUTE_NAME, beanManager);
        webServer.addServletContextAttribute(org.jboss.weld.Container.CONTEXT_ID_KEY, beanManager.getContextId());
        webServer.addListener(Listener.class);
    }
}
