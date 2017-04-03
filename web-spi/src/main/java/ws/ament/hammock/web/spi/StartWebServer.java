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

package ws.ament.hammock.web.spi;

import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.ament.hammock.bootstrap.Bootstrapper;
import ws.ament.hammock.utils.ClassUtils;
import ws.ament.hammock.web.api.FilterDescriptor;
import ws.ament.hammock.web.api.ServletDescriptor;
import ws.ament.hammock.web.api.WebServer;
import ws.ament.hammock.web.extension.WebServerExtension;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * A component that starts a standard application server component.
 *
 */
@ApplicationScoped
public class StartWebServer {
    public static final String PREFIX = "${";
    public static final String PREFIX_REGEX = "\\$\\{";
    public static final String SUFFIX = "}";
    @Inject
    @Any
    private Instance<HttpServlet> servlets;

    @Inject
    @Any
    private Instance<Filter> filters;
    @Inject
    private BeanManager beanManager;

    @Inject
    private WebServerExtension extension;

    private WebServer webServer;
    private final Logger logger = LoggerFactory.getLogger(StartWebServer.class);

    @PostConstruct
    public void init() {
        WebServer webServer = resolveWebServer(beanManager);
        Bootstrapper bootstrapper = ServiceLoader.load(Bootstrapper.class).iterator().next();
        bootstrapper.configure(webServer);
        extension.processListeners(webServer::addListener);
        processInstances(beanManager, ServletDescriptor.class, webServer::addServlet);
        processInstances(beanManager, FilterDescriptor.class, webServer::addFilter);
        this.webServer = webServer;
        this.processFilters();
        this.procesServlets();
        processInstances(beanManager, ServletContextAttributeProvider.class,
                s -> s.getAttributes().forEach(webServer::addServletContextAttribute));
        webServer.start();
    }

    public void start(@Observes @Initialized(ApplicationScoped.class) Object obj) {

    }

    @PreDestroy
    public void shutdown() {
        logger.info("Shutting down Application listener");
        this.webServer.stop();
    }

    private WebServer resolveWebServer(BeanManager beanManager) {
        Set<Bean<?>> beans = beanManager.getBeans(WebServer.class);
        if (beans.size() > 1) {
            StringJoiner foundInstances = new StringJoiner(",", "[", "]");
            beans.iterator().forEachRemaining(ws -> foundInstances.add(ws.toString()));
            throw new RuntimeException("Multiple web server implementations found on the classpath " + foundInstances);
        }
        if (beans.isEmpty()) {
            throw new RuntimeException("No web server implementations found on the classpath");
        }
        Bean<?> bean = beanManager.resolve(beans);
        CreationalContext<?> creationalContext = beanManager.createCreationalContext(bean);
        return (WebServer) beanManager.getReference(bean, WebServer.class, creationalContext);
    }

    private <T> void processInstances(BeanManager beanManager, Class<T> clazz, Consumer<T> consumer) {
        Set<Bean<?>> beans = beanManager.getBeans(clazz);
        beans.stream().map(bean -> (T) beanManager.getReference(bean, clazz, beanManager.createCreationalContext(bean))).forEach(consumer);
    }

    private void processFilters() {
        filters.forEach(filter -> {
            WebFilter webFilter = ClassUtils.getAnnotation(filter.getClass(), WebFilter.class);
            if(webFilter != null) {
                FilterDescriptor filterDescriptor = new FilterDescriptor(webFilter.filterName(),
                        webFilter.value(), mapUrls(webFilter.urlPatterns()), webFilter.dispatcherTypes(),
                        webFilter.initParams(), webFilter.asyncSupported(), webFilter.servletNames(),
                        filter.getClass());
                webServer.addFilter(filterDescriptor);
            }
        });
    }

    private void procesServlets() {
        servlets.forEach(servlet -> {
            WebServlet webServlet = ClassUtils.getAnnotation(servlet.getClass(), WebServlet.class);
            if(webServlet != null) {
                ServletDescriptor servletDescriptor = new ServletDescriptor(webServlet.name(),
                        webServlet.value(), mapUrls(webServlet.urlPatterns()), webServlet.loadOnStartup(),
                        webServlet.initParams(),webServlet.asyncSupported(),servlet.getClass());
                webServer.addServlet(servletDescriptor);
            }
        });
    }

    private static String[] mapUrls(String[] urls) {
        List<String> patterns = Arrays.stream(urls)
                .map(s -> {
                    if(s.startsWith(PREFIX) && s.endsWith(SUFFIX)) {
                        String key = s.replaceFirst(PREFIX_REGEX,"").replace(SUFFIX,"");
                        return ConfigResolver.getPropertyValue(key, s);
                    }
                    else {
                        return s;
                    }
                })
                .collect(toList());
                return patterns.toArray(new String[urls.length]);
    }
}