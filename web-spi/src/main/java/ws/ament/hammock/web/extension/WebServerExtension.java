/*
 * Copyright 2017 Hammock and its contributors
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

package ws.ament.hammock.web.extension;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.WithAnnotations;
import javax.servlet.Filter;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class WebServerExtension implements Extension{
    private final List<Class<? extends ServletContextListener>> listeners = new ArrayList<>();
    private final List<Class<? extends HttpServlet>> servlets = new ArrayList<>();
    private final List<Class<? extends Filter>> filters = new ArrayList<>();
    public void findListeners(@Observes @WithAnnotations({WebListener.class})
                              ProcessAnnotatedType<? extends ServletContextListener> pat) {
        listeners.add(pat.getAnnotatedType().getJavaClass());
    }
    public void findServlets(@Observes @WithAnnotations({WebServlet.class})
                                      ProcessAnnotatedType<? extends HttpServlet> pat) {
        servlets.add(pat.getAnnotatedType().getJavaClass());
    }
    public void findFilters(@Observes @WithAnnotations({WebFilter.class})
                                      ProcessAnnotatedType<? extends Filter> pat) {
        filters.add(pat.getAnnotatedType().getJavaClass());
    }

    public void processListeners(Consumer<Class<? extends ServletContextListener>> consumer) {
        listeners.forEach(consumer);
    }
    public void processFilters(Consumer<Class<? extends Filter>> consumer) {
        filters.forEach(consumer);
    }
    public void processServlets(Consumer<Class<? extends HttpServlet>> consumer) {
        servlets.forEach(consumer);
    }
}
