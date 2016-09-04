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

package ws.ament.hammock.web.jetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.jboss.weld.environment.servlet.Listener;
import ws.ament.hammock.web.base.AbstractWebServer;
import ws.ament.hammock.web.spi.FilterDescriptor;
import ws.ament.hammock.web.spi.ServletDescriptor;
import ws.ament.hammock.web.spi.WebServerConfiguration;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.DispatcherType;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;

@ApplicationScoped
public class JettyWebServer extends AbstractWebServer {
    private Server jetty;
    @Inject
    public JettyWebServer(WebServerConfiguration webServerConfiguration) {
        super(webServerConfiguration);
    }

    @Override
    public void start() {
        WebAppContext context = new WebAppContext();
        context.setContextPath("/");
        context.setResourceBase(getWebServerConfiguration().getFileDir());
        context.addEventListener(new Listener());

        for(Map.Entry<String, Object> attribute : getServletContextAttributes().entrySet()) {
            context.setAttribute(attribute.getKey(), attribute.getValue());
        }

        for(ServletDescriptor servletDescriptor : getServletDescriptors()) {
            for(String pattern : servletDescriptor.urlPatterns()) {
                context.addServlet(servletDescriptor.servletClass(), pattern);
            }
        }

        for(FilterDescriptor filterDescriptor : getFilterDescriptors()) {
            DispatcherType[] dispatcherTypesArr = filterDescriptor.dispatcherTypes();
            EnumSet<DispatcherType> dispatcherTypes = EnumSet.copyOf(Arrays.asList(dispatcherTypesArr));
            for(String pattern : filterDescriptor.urlPatterns()) {
                System.out.println(filterDescriptor.getClazz() + pattern + dispatcherTypes);
                context.addFilter(filterDescriptor.getClazz(), pattern, dispatcherTypes);
            }
        }

        try {
            Server server = new Server(getWebServerConfiguration().getWebserverPort());
            server.setHandler(context);
            server.start();
            jetty = server;
        }
        catch (Exception e) {
            throw new RuntimeException("Unable to start server", e);
        }
    }

    @Override
    public void stop() {
        if(jetty != null) {
            try {
                jetty.stop();
                jetty = null;
            } catch (Exception e) {
                throw new RuntimeException("Unable to stop server", e);
            }
        }
    }
}
