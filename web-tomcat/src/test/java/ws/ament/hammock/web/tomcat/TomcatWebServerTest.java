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

import org.apache.commons.io.IOUtils;
import org.apache.deltaspike.core.impl.config.ConfigurationExtension;
import org.apache.deltaspike.core.impl.config.DefaultConfigPropertyProducer;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.Test;
import ws.ament.hammock.web.spi.FilterDescriptor;
import ws.ament.hammock.web.spi.ServletDescriptor;
import ws.ament.hammock.web.spi.WebServerConfiguration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.servlet.DispatcherType;
import javax.servlet.annotation.WebInitParam;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

public class TomcatWebServerTest {
    @Test
    public void shouldBootWebServer() throws Exception {
        try(WeldContainer weldContainer = new Weld().disableDiscovery()
                .extensions(new ConfigurationExtension())
                .beanClasses(TomcatWebServer.class, DefaultServlet.class, MessageProvider.class,
                        WebServerConfiguration.class, DefaultConfigPropertyProducer.class)
                .initialize()) {
            TomcatWebServer tomcat = weldContainer.select(TomcatWebServer.class).get();
            tomcat.addServlet(new ServletDescriptor("Default",new String[]{"/*"},new String[]{"/*"},1,new WebInitParam[]{}, true, DefaultServlet.class));
            tomcat.addFilter(new FilterDescriptor("Default", null, new String[]{"/rest"},new DispatcherType[]{DispatcherType.REQUEST},null,true,null,DefaultFilter.class));
            tomcat.start();
            try(InputStream stream = new URL("http://localhost:8080/").openStream()) {
                String data = IOUtils.toString(stream).trim();
                assertThat(data).isEqualTo(MessageProvider.DATA);
            }

            try(InputStream stream = new URL("http://localhost:8080/rest").openStream()) {
                String data = IOUtils.toString(stream).trim();
                assertThat(data).isEqualTo("Hello, world!");
            }
            tomcat.stop();
        }
    }
    
    @Test
    public void shouldBootWebServerSecured() throws Exception {
    	HammockTestPropertyFileConfig.testProperty="hammock-test.properties";
        try(WeldContainer weldContainer = new Weld().disableDiscovery()
                .extensions(new ConfigurationExtension())
                .beanClasses(TomcatWebServer.class, DefaultServlet.class, MessageProvider.class,
                        WebServerConfiguration.class, HammockTestPropertyFileConfig.class, DefaultConfigPropertyProducer.class)
                .initialize()) {
            TomcatWebServer tomcat = weldContainer.select(TomcatWebServer.class).get();
            tomcat.addServlet(new ServletDescriptor("Default",new String[]{"/*"},new String[]{"/*"},1,new WebInitParam[]{}, true, DefaultServlet.class));
            tomcat.addFilter(new FilterDescriptor("Default", null, new String[]{"/rest"},new DispatcherType[]{DispatcherType.REQUEST},null,true,null,DefaultFilter.class));
            tomcat.start();
            try(InputStream stream = new URL("http://localhost:8081/").openStream()) {
                String data = IOUtils.toString(stream).trim();
                assertThat(data).isEqualTo(MessageProvider.DATA);
            }

            try(InputStream stream = new URL("http://localhost:8081/rest").openStream()) {
                String data = IOUtils.toString(stream).trim();
                assertThat(data).isEqualTo("Hello, world!");
            }
            
         	SSLContext ctx = SSLContext.getInstance("TLS");
        	ctx.init(null, getTestTrustManagers(), null);
        	SSLSocketFactory socketFactory = ctx.getSocketFactory();
        	Socket socket = socketFactory.createSocket("localhost", 8443);
        	
            try(InputStream stream = socket.getInputStream();
            		OutputStream output = socket.getOutputStream()) {
            	output.write("GET / HTTP/1.1\nHost: localhost:8443\nConnection: close\n\n".getBytes());
                String data = IOUtils.toString(stream).trim();
                assertThat(data).endsWith(MessageProvider.DATA);
            }
            
            tomcat.stop();
        }
        HammockTestPropertyFileConfig.testProperty=null;
    }
    
    private TrustManager[] getTestTrustManagers(){
    	try (InputStream is  = TomcatWebServerTest.class.getResourceAsStream("/keystore.jks")){
			KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
			ks.load(is, "123456".toCharArray());
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmf.init(ks);
			return tmf.getTrustManagers();
		} catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException e) {
			e.printStackTrace();
			fail("The test certificate could not be loaded" + e.getMessage());
		}
    	return null;
    }
}
