/*
 * Copyright 2015 John D. Ament
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

package ws.ament.hammock.web.undertow;

import io.undertow.Undertow;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ServletInfo;
import ws.ament.hammock.web.spi.CDIListener;
import ws.ament.hammock.web.spi.ServletDescriptor;
import ws.ament.hammock.web.spi.WebServer;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static io.undertow.servlet.Servlets.listener;

@ApplicationScoped
public class UndertowWebServer implements WebServer {
    private Set<ServletInfo> servlets = new HashSet<>();
    private Map<String, Object> servletContextAttributes = new HashMap<>();
    @Inject
    private Function<ServletDescriptor, ServletInfo> mapper;
    private Undertow undertow;
    private static int count = 0;

    @Override
    public void addServlet(ServletDescriptor servletDescriptor) {
        servlets.add(mapper.apply(servletDescriptor));
    }

    @Override
    public void addServletContextAttribute(String name, Object value) {
        servletContextAttributes.put(name, value);
    }

    @Override
    public void start() {
        DeploymentInfo di = new DeploymentInfo()
                .setContextPath("/")
                .setDeploymentName("Undertow")
                .setClassIntrospecter(CDIClassIntrospecter.INSTANCE)
                .setClassLoader(ClassLoader.getSystemClassLoader());

        di.addListener(listener(CDIListener.class));

        servletContextAttributes.forEach(di::addServletContextAttribute);

        servlets.forEach(s -> {
            try {
                s.setInstanceFactory(CDIClassIntrospecter.INSTANCE.createInstanceFactory(s.getServletClass()));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            di.addServlet(s);
        });

        DeploymentManager deploymentManager = Servlets.defaultContainer().addDeployment(di);
        deploymentManager.deploy();
        try {
            this.undertow = Undertow.builder()
                    // TODO yikes
                    .addHttpListener(8080, "0.0.0.0")
                    .setHandler(deploymentManager.start())
                    .build();
            this.undertow.start();
        } catch (ServletException e) {
            throw new RuntimeException("Unable to start server", e);
        }
    }

    @Override
    @PreDestroy
    public void stop() {
        if(this.undertow != null) {
            this.undertow.stop();
            this.undertow = null;
        }
    }
}
