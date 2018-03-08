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

package ws.ament.hammock.web.base;

import ws.ament.hammock.web.api.FilterDescriptor;
import ws.ament.hammock.web.api.ServletDescriptor;
import ws.ament.hammock.web.api.WebServer;

import javax.servlet.ServletContextListener;
import java.util.*;

public abstract class AbstractWebServer implements WebServer {
    private final List<ServletDescriptor> servletDescriptors = new ArrayList<>();
    private final List<FilterDescriptor> filterDescriptors = new ArrayList<>();
    private final Map<String, Object> servletContextAttributes = new HashMap<>();
    private final Map<String, String> initParams = new HashMap<>();
    private final Set<Class<? extends ServletContextListener>> listeners = new LinkedHashSet<>();

    @Override
    public void addServlet(ServletDescriptor servletDescriptor) {
        this.servletDescriptors.add(servletDescriptor);
    }

    @Override
    public void addServletContextAttribute(String name, Object value) {
        servletContextAttributes.put(name, value);
    }

    @Override
    public void addFilter(FilterDescriptor filterDescriptor) {
        this.filterDescriptors.add(filterDescriptor);
    }

    @Override
    public void addInitParameter(String key, String value) {
        initParams.put(key, value);
    }

    @Override
    public void addListener(Class<? extends ServletContextListener> listenerClass) {
        this.listeners.add(listenerClass);
    }

    protected List<ServletDescriptor> getServletDescriptors() {
        return Collections.unmodifiableList(servletDescriptors);
    }

    protected Map<String, Object> getServletContextAttributes() {
        return Collections.unmodifiableMap(servletContextAttributes);
    }

    protected Map<String, String> getInitParams() {
        return Collections.unmodifiableMap(initParams);
    }

    protected List<FilterDescriptor> getFilterDescriptors() {
        return Collections.unmodifiableList(filterDescriptors);
    }

    protected Set<Class<? extends ServletContextListener>> getListeners() {
        return Collections.unmodifiableSet(listeners);
    }

}
