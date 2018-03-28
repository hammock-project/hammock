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
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.servlets.DefaultServlet;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import ws.ament.hammock.HammockRuntime;
import ws.ament.hammock.web.base.AbstractWebServer;
import ws.ament.hammock.web.api.FilterDescriptor;
import ws.ament.hammock.web.api.ServletDescriptor;
import ws.ament.hammock.web.spi.WebServerConfiguration;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.stream;

@ApplicationScoped
public class TomcatWebServer extends AbstractWebServer{
    @Inject
    private WebServerConfiguration webServerConfiguration;

    @Inject
    private HammockRuntime hammockRuntime;
    
    @Inject
    @ConfigProperty(name="tomcat.catalina.base", defaultValue="")
    private String basedir;

    private Tomcat tomcat;

    @Override
    public void start() {
        tomcat = new Tomcat();
        if (!basedir.isEmpty()) {
            tomcat.setBaseDir(basedir); // auto fallback in Tomcat.initBaseDir
        }
        tomcat.setPort(webServerConfiguration.getPort());
        //init http connector
        tomcat.getConnector();
        if (hammockRuntime.isSecuredConfigured()){
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
        ctx.setInstanceManager(new HammockInstanceManager());
        super.getInitParams().forEach(ctx::addParameter);
        ServletContext servletContext = ctx.getServletContext();
        getListeners().forEach(c -> ctx.addApplicationListener(c.getName()));
        for(Map.Entry<String, Object> attribute : getServletContextAttributes().entrySet()) {
            servletContext.setAttribute(attribute.getKey(), attribute.getValue());
        }
        List<ServletDescriptor> servletDescriptors = getServletDescriptors();
        List<FilterDescriptor> filterDescriptors = getFilterDescriptors();
        if(!filterDescriptors.isEmpty() && servletDescriptors.isEmpty()) {
            String servletName = "TomcatDefault";
            Wrapper wrapper = Tomcat.addServlet(ctx, servletName, DefaultServlet.class.getName());
            wrapper.setAsyncSupported(true);
            ctx.addServletMappingDecoded("/*", servletName);
        }
        servletDescriptors.forEach(servletDescriptor -> {
            String servletName = servletDescriptor.name();
            Wrapper wrapper = Tomcat.addServlet(ctx, servletName, servletDescriptor.servletClass().getName());
            if(servletDescriptor.initParams() != null) {
                Arrays.stream(servletDescriptor.initParams())
                        .forEach(p -> wrapper.addInitParameter(p.name(), p.value()));
            }
            wrapper.setAsyncSupported(true);
            stream(servletDescriptor.urlPatterns()).forEach(s -> ctx.addServletMappingDecoded(s, servletName));
        });
        filterDescriptors.forEach(filterDescriptor -> {
            String filterName = filterDescriptor.filterName();
            FilterDef filterDef = new FilterDef();
            filterDef.setFilterName(filterName);
            filterDef.setFilterClass(filterDescriptor.getClazz().getName());
            filterDef.setAsyncSupported("true");
            ctx.addFilterDef(filterDef);

            FilterMap mapping = new FilterMap();
            mapping.setFilterName(filterName);
            stream(filterDescriptor.urlPatterns()).forEach(mapping::addURLPattern);
            ctx.addFilterMap(mapping);
        });
        try {
            tomcat.start();
        } catch (LifecycleException e) {
            throw new RuntimeException("Unable to launch tomcat ",e);
        }
    }

    @Override
    public void stop() {
        try {
            tomcat.stop();
            tomcat.destroy();
        } catch (LifecycleException e) {
            throw new RuntimeException("Unable to stop tomcat ",e);
        }
    }
}
