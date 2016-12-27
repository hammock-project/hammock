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

package ws.ament.hammock.metrics;

import com.codahale.metrics.servlets.AdminServlet;
import ws.ament.hammock.web.spi.ServletContextAttributeProvider;
import ws.ament.hammock.web.api.ServletDescriptor;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.annotation.WebInitParam;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class MetricsServletProvider implements ServletContextAttributeProvider{
    @Inject
    private MetricsConfig metricsConfig;

    @Produces
    public ServletDescriptor metricsServlet() {
        String[] uris = new String[]{metricsConfig.getBaseUri()};
        WebInitParam[] params = null;
        return new ServletDescriptor("Metrics", uris, uris, 1, params, false, AdminServlet.class);
    }

    @Override
    public Map<String, Object> getAttributes() {
        Map<String,Object> attributes = new HashMap<>();
        attributes.put("com.codahale.metrics.servlets.HealthCheckServlet.registry", metricsConfig.getHealthCheckRegistry());
        attributes.put("com.codahale.metrics.servlets.MetricsServlet.registry", metricsConfig.getMetricRegistry());
        return attributes;
    }
}
