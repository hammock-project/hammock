/*
 * Copyright 2016 John D. Ament
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

package ws.ament.hammock.web.tomcat;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.ApplicationParameter;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.servlet.Listener;
import org.jboss.weld.environment.servlet.WeldServletLifecycle;
import ws.ament.hammock.web.base.AbstractWebServer;
import ws.ament.hammock.web.spi.WebServerConfiguration;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import java.io.File;
import java.util.Arrays;
import java.util.Set;

@ApplicationScoped
public class TomcatWebServer extends AbstractWebServer{
    private Tomcat tomcat;
    @Inject
    protected TomcatWebServer(WebServerConfiguration webServerConfiguration) {
        super(webServerConfiguration);
    }

    @Override
    public void start() {
        tomcat = new Tomcat();
        tomcat.setPort(getWebServerConfiguration().getWebserverPort());
        File base = new File("./target");
        Context ctx = tomcat.addContext("/",base.getAbsolutePath());

        ((StandardJarScanner) ctx.getJarScanner()).setScanAllDirectories(true);
        StandardContext standardContext = (StandardContext)ctx;
//        standardContext.setatt(WeldServletLifecycle.BEAN_MANAGER_ATTRIBUTE_NAME, container.getBeanManager());
        standardContext.addApplicationListener(Listener.class.getName());
        getServletDescriptors().forEach(servletDescriptor -> {
            Tomcat.addServlet(ctx, servletDescriptor.name(), servletDescriptor.servletClass().getName());
            Arrays.stream(servletDescriptor.urlPatterns()).forEach(s -> ctx.addServletMapping(s, servletDescriptor.name()));
        });
        try {
            tomcat.start();
            Runnable r = () -> tomcat.getServer().await();
            new Thread(r).start();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        try {
            tomcat.stop();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }
}
