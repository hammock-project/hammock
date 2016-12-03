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

package ws.ament.hammock.web.tomcat;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.servlets.DefaultServlet;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import org.jboss.weld.environment.servlet.Listener;
import org.jboss.weld.environment.servlet.WeldServletLifecycle;
import ws.ament.hammock.web.base.AbstractWebServer;
import ws.ament.hammock.web.spi.FilterDescriptor;
import ws.ament.hammock.web.spi.ServletDescriptor;
import ws.ament.hammock.web.spi.WebServerConfiguration;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import java.io.File;
import java.util.List;

import static java.util.Arrays.stream;

@ApplicationScoped
public class TomcatWebServer extends AbstractWebServer{
    private BeanManager beanManager;
    private Tomcat tomcat;
    @Inject
    protected TomcatWebServer(WebServerConfiguration webServerConfiguration, BeanManager beanManager) {
        super(webServerConfiguration);
        this.beanManager = beanManager;
    }

    @Override
    public void start() {
        tomcat = new Tomcat();
        WebServerConfiguration webServerConfiguration = getWebServerConfiguration();
        tomcat.setPort(webServerConfiguration.getPort());
        //init http connector
        tomcat.getConnector();
        if (webServerConfiguration.isSecuredConfigured()){
        	Connector connector = new Connector();
        	connector.setScheme("https");
            connector.setPort(webServerConfiguration.getSecuredPort());
        	connector.setProperty("keystoreFile", webServerConfiguration.getKeystorePath());
        	connector.setProperty("keystorePass", webServerConfiguration.getKeystorePassword());
        	connector.setProperty("keystoreType", webServerConfiguration.getKeystoreType());
            connector.setProperty("clientAuth", "false");
            connector.setProperty("protocol", "HTTP/1.1");
            connector.setProperty("sslProtocol", "TLS");
            connector.setProperty("maxThreads", "200");
            connector.setProperty("protocol", "org.apache.coyote.http11.Http11AprProtocol");
            connector.setAttribute("SSLEnabled", true);
            connector.setSecure(true);
        	tomcat.getService().addConnector(connector);	
        }
        
        File base = new File(".");
        Context ctx = tomcat.addContext("",base.getAbsolutePath());
        ctx.getServletContext().setAttribute(WeldServletLifecycle.BEAN_MANAGER_ATTRIBUTE_NAME, beanManager);
        ctx.addApplicationListener(Listener.class.getName());
        List<ServletDescriptor> servletDescriptors = getServletDescriptors();
        List<FilterDescriptor> filterDescriptors = getFilterDescriptors();
        if(!filterDescriptors.isEmpty() && servletDescriptors.isEmpty()) {
            String servletName = "TomcatDefault";
            Tomcat.addServlet(ctx, servletName, DefaultServlet.class.getName());
            ctx.addServletMapping("/*", servletName);
        }
        servletDescriptors.forEach(servletDescriptor -> {
            String servletName = servletDescriptor.name();
            Tomcat.addServlet(ctx, servletName, servletDescriptor.servletClass().getName());
            stream(servletDescriptor.urlPatterns()).forEach(s -> ctx.addServletMapping(s, servletName));
        });
        filterDescriptors.forEach(filterDescriptor -> {
            String filterName = filterDescriptor.filterName();
            FilterDef filterDef = new FilterDef();
            filterDef.setFilterName(filterName);
            filterDef.setFilterClass(filterDescriptor.getClazz().getName());
            ctx.addFilterDef(filterDef);

            FilterMap mapping = new FilterMap();
            mapping.setFilterName(filterName);
            stream(filterDescriptor.urlPatterns()).forEach(mapping::addURLPattern);
            ctx.addFilterMap(mapping);
        });
        try {
            tomcat.start();
            Runnable r = () -> tomcat.getServer().await();
            new Thread(r).start();
        } catch (LifecycleException e) {
            throw new RuntimeException("Unable to launch tomcat ",e);
        }
    }

    @Override
    public void stop() {
        try {
            tomcat.stop();
        } catch (LifecycleException e) {
            throw new RuntimeException("Unable to stop tomcat ",e);
        }
    }
}
