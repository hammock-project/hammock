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

package ws.ament.hammock.web.undertow;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.Undertow.Builder;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.*;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;
import ws.ament.hammock.HammockRuntime;
import ws.ament.hammock.web.api.ServletDescriptor;
import ws.ament.hammock.web.base.AbstractWebServer;
import ws.ament.hammock.web.spi.WebServerConfiguration;
import ws.ament.hammock.web.undertow.websocket.UndertowWebSocketExtension;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.net.ssl.*;
import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import static io.undertow.Handlers.path;
import static io.undertow.servlet.Servlets.filter;
import static io.undertow.servlet.Servlets.listener;

@ApplicationScoped
public class UndertowWebServer extends AbstractWebServer {
    @Inject
    private Function<ServletDescriptor, ServletInfo> mapper;
    @Inject
    private UndertowWebSocketExtension extension;
    @Inject
    private WebServerConfiguration webServerConfiguration;
    @Inject
    private HammockRuntime hammockRuntime;
    private Set<ServletInfo> servlets = new HashSet<>();
    private Undertow undertow;

    @Override
    public void addServlet(ServletDescriptor servletDescriptor) {
        servlets.add(mapper.apply(servletDescriptor));
    }

    @Override
    public void start() {
        DeploymentInfo di = new DeploymentInfo()
                .setContextPath("/")
                .setDeploymentName("Undertow")
                .setResourceManager(new ClassPathResourceManager(getClass().getClassLoader()))
                .setClassLoader(ClassLoader.getSystemClassLoader());

        super.getListeners().forEach(c ->di.addListener(listener(c)));

        Collection<Class<?>> endpoints = extension.getEndpointClasses();
        if(!endpoints.isEmpty()) {
            WebSocketDeploymentInfo webSocketDeploymentInfo = new WebSocketDeploymentInfo();
            endpoints.forEach(webSocketDeploymentInfo::addEndpoint);
            di.addServletContextAttribute(WebSocketDeploymentInfo.ATTRIBUTE_NAME, webSocketDeploymentInfo);
        }

        getServletContextAttributes().forEach(di::addServletContextAttribute);
        servlets.forEach(di::addServlet);
        getFilterDescriptors().forEach(filterDescriptor -> {
            FilterInfo filterInfo = filter(filterDescriptor.displayName(), filterDescriptor.getClazz()).setAsyncSupported(filterDescriptor.asyncSupported());
            if(filterDescriptor.initParams() != null) {
                for (WebInitParam param : filterDescriptor.initParams()) {
                    filterInfo.addInitParam(param.name(), param.value());
                }
            }
            di.addFilter(filterInfo);
            for(String url : filterDescriptor.urlPatterns()) {
                for(DispatcherType dispatcherType : filterDescriptor.dispatcherTypes()) {
                    di.addFilterUrlMapping(filterDescriptor.displayName(), url, dispatcherType);
                }
            }
        });

        getInitParams().forEach(di::addInitParameter);

        DeploymentManager deploymentManager = Servlets.defaultContainer().addDeployment(di);
        deploymentManager.deploy();
        try {
            HttpHandler servletHandler = deploymentManager.start();
            PathHandler path = path(Handlers.redirect("/"))
                    .addPrefixPath("/", servletHandler);
            Builder undertowBuilder = Undertow.builder()
                    .addHttpListener(webServerConfiguration.getPort(), webServerConfiguration.getAddress())
                    .setHandler(path);
            if (hammockRuntime.isSecuredConfigured()){
            	KeyManager[] keyManagers = loadKeyManager();
            	TrustManager[] trustManagers = loadTrustManager();
            	SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(keyManagers, trustManagers, null);
            	undertowBuilder = undertowBuilder.addHttpsListener(webServerConfiguration.getSecuredPort(), webServerConfiguration.getAddress(), sslContext);
            }
            this.undertow = undertowBuilder.build();
            this.undertow.start();
        } catch (ServletException | GeneralSecurityException | IOException e) {
            throw new RuntimeException("Unable to start server", e);
        }
    }
    
    private KeyManager[] loadKeyManager() throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(loadKeystore(webServerConfiguration.getKeystorePath(),
                webServerConfiguration.getKeystorePassword(),
                webServerConfiguration.getKeystoreType()),
                webServerConfiguration.getKeystorePassword().toCharArray());
        return keyManagerFactory.getKeyManagers();
    }

    private TrustManager[] loadTrustManager() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(loadKeystore(webServerConfiguration.getTruststorePath(),
                webServerConfiguration.getTruststorePassword(),
                webServerConfiguration.getTruststoreType()));
        return trustManagerFactory.getTrustManagers();
    }
    
    private KeyStore loadKeystore(String name, String password, String type) throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException {
        final InputStream stream = UndertowWebServer.class.getResourceAsStream(name);
        if (stream == null){
        	throw new RuntimeException("Unable to start server, path " + name + " doesn't exists");
        }
        try(InputStream is = stream) {
            KeyStore loadedKeystore = KeyStore.getInstance(type);
            loadedKeystore.load(is, password.toCharArray());
            return loadedKeystore;
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
