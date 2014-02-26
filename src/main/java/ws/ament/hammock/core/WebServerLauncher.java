/*
 *
 *  * Copyright 2013 John D. Ament
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  * implied.
 *  *
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package ws.ament.hammock.core;

import io.undertow.Undertow;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ListenerInfo;
import io.undertow.servlet.api.ServletInfo;
import org.jboss.resteasy.cdi.CdiInjectorFactory;
import org.jboss.resteasy.plugins.server.servlet.HttpServlet30Dispatcher;
import org.jboss.resteasy.spi.ResteasyDeployment;
import ws.ament.hammock.core.api.WebServerConfiguration;
import ws.ament.hammock.core.impl.CDIListener;

import javax.servlet.ServletException;

/**
 * Created with IntelliJ IDEA.
 * User: johndament
 * Date: 2/25/14
 * Time: 8:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class WebServerLauncher {
    private Undertow undertow;
    public void start(WebServerConfiguration webServerConfiguration) {
        ResteasyDeployment deployment = new ResteasyDeployment();
        deployment.getActualResourceClasses().addAll(webServerConfiguration.getResourceClasses());
        deployment.getActualProviderClasses().addAll(webServerConfiguration.getProviderClasses());
        deployment.setInjectorFactoryClass(CdiInjectorFactory.class.getName());

        ListenerInfo listener = Servlets.listener(CDIListener.class);

        ServletInfo resteasyServlet = Servlets.servlet("ResteasyServlet", HttpServlet30Dispatcher.class)
                .setAsyncSupported(true)
                .setLoadOnStartup(1)
                .addMapping("/*");
        DeploymentInfo di = new DeploymentInfo()
                .addListener(listener)
                .setContextPath(webServerConfiguration.getContextRoot())
                .addServletContextAttribute(ResteasyDeployment.class.getName(), deployment)
                .addServlet(resteasyServlet).setDeploymentName("ResteasyUndertow");

        DeploymentManager deploymentManager = Servlets.defaultContainer().addDeployment(di);
        deploymentManager.deploy();
        Undertow server = null;
        try {
            server = Undertow.builder()
                    .addHttpListener(webServerConfiguration.getPort(), webServerConfiguration.getBindAddress())
                    .setHandler(deploymentManager.start())
                    .build();
        } catch (ServletException e) {
            e.printStackTrace();
        }
        server.start();
        this.undertow = server;
    }

    public void stop() {
        this.undertow.stop();
    }
}
