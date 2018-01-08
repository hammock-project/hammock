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

package ws.ament.hammock.bootstrap.weld3;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.jboss.weld.bean.builtin.BeanManagerProxy;
import org.jboss.weld.environment.servlet.Listener;
import org.jboss.weld.environment.servlet.WeldServletLifecycle;
import org.jboss.weld.manager.BeanManagerImpl;
import ws.ament.hammock.bootstrap.Bootstrapper;
import ws.ament.hammock.web.api.WebServer;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import javax.enterprise.inject.spi.CDI;

import static org.jboss.weld.environment.se.Weld.*;
import static org.jboss.weld.environment.servlet.Container.CONTEXT_PARAM_CONTAINER_CLASS;

public class Weld3Bootstrapper implements Bootstrapper{
    private SeContainer seContainer;
    private SeContainerInitializer seContainerInitializer;
    private Config config;

    public Weld3Bootstrapper() {
        seContainerInitializer = SeContainerInitializer.newInstance();
        config = ConfigProvider.getConfig();
    }

    @Override
    public void start() {
        boolean isolation = getBoolean(ARCHIVE_ISOLATION_SYSTEM_PROPERTY, false);
        boolean devMode = getBoolean(DEV_MODE_SYSTEM_PROPERTY, false);
        boolean shutdownHook = getBoolean(SHUTDOWN_HOOK_SYSTEM_PROPERTY, true);
        boolean implicit = getBoolean(JAVAX_ENTERPRISE_INJECT_SCAN_IMPLICIT, false);
        seContainer = seContainerInitializer
                .addProperty(ARCHIVE_ISOLATION_SYSTEM_PROPERTY, isolation)
                .addProperty(DEV_MODE_SYSTEM_PROPERTY, devMode)
                .addProperty(SHUTDOWN_HOOK_SYSTEM_PROPERTY, shutdownHook)
                .addProperty(JAVAX_ENTERPRISE_INJECT_SCAN_IMPLICIT, implicit)
                .initialize();
    }

    @Override
    public void stop() {
        seContainer.close();
    }

    @Override
    public void configure(WebServer webServer) {
        BeanManagerImpl beanManagerImpl = BeanManagerProxy.unwrap(CDI.current().getBeanManager());
        webServer.addServletContextAttribute(WeldServletLifecycle.BEAN_MANAGER_ATTRIBUTE_NAME, beanManagerImpl);
        webServer.addServletContextAttribute(org.jboss.weld.Container.CONTEXT_ID_KEY, beanManagerImpl.getContextId());
        webServer.addInitParameter(CONTEXT_PARAM_CONTAINER_CLASS, HammockContainer.class.getName());
        webServer.addListener(Listener.class);
    }

    private boolean getBoolean(String key, boolean defaultValue) {
        return config.getOptionalValue(key, Boolean.class).orElse(defaultValue);
    }

    public SeContainer getSeContainer() {
        return seContainer;
    }

    public SeContainerInitializer getSeContainerInitializer() {
        return seContainerInitializer;
    }
}
