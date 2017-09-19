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

import org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet;
import org.apache.cxf.transport.sse.SseHttpTransportFactory;
import ws.ament.hammock.web.api.ServletDescriptor;
import ws.ament.hammock.web.spi.RestServerConfiguration;
import ws.ament.hammock.web.spi.WebParam;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.servlet.annotation.WebInitParam;

@ApplicationScoped
public class CXFProvider {
    @Produces
    @Dependent
    public ServletDescriptor cxfServlet(RestServerConfiguration restServerConfiguration) {
        String servletMapping = restServerConfiguration.getRestServletMapping();
        WebInitParam[] initParams = new WebInitParam[]{new WebParam(CXFNonSpringJaxrsServlet.TRANSPORT_ID, SseHttpTransportFactory.TRANSPORT_ID)};
        return new ServletDescriptor("CXF",null, new String[]{servletMapping},1, initParams,true,HammockCXFServlet.class);
    }
}
