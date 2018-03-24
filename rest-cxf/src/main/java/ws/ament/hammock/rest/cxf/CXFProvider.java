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

package ws.ament.hammock.rest.cxf;

import org.apache.cxf.cdi.CXFCdiServlet;
import org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet;
import org.apache.cxf.transport.sse.SseHttpTransportFactory;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import ws.ament.hammock.web.api.ServletDescriptor;
import ws.ament.hammock.web.spi.RestServerConfiguration;
import ws.ament.hammock.web.spi.WebParam;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.annotation.WebInitParam;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class CXFProvider {
    @Inject
    @ConfigProperty(name = "cxf.enable.sse.transport", defaultValue = "true")
    private boolean enableSseTransport;
    @Produces
    @Dependent
    public ServletDescriptor cxfServlet(RestServerConfiguration restServerConfiguration) {
        String servletMapping = restServerConfiguration.getRestServletMapping();
        List<WebInitParam> params = new ArrayList<>();
        if(enableSseTransport) {
            params.add(new WebParam(CXFNonSpringJaxrsServlet.TRANSPORT_ID, SseHttpTransportFactory.TRANSPORT_ID));
        }
        WebInitParam[] initParams = params.toArray(new WebInitParam[params.size()]);
        return new ServletDescriptor("CXF",null, new String[]{servletMapping},1, initParams,true,CXFCdiServlet.class);
    }
    
}
