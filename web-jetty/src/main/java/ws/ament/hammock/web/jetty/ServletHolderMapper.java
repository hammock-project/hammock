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

package ws.ament.hammock.web.jetty;

import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlet.Source;
import ws.ament.hammock.web.api.ServletDescriptor;

import java.util.Arrays;
import java.util.function.Function;

public class ServletHolderMapper implements Function<ServletDescriptor, ServletHolder> {
    private final ServletHandler servletHandler;

    public ServletHolderMapper(ServletHandler servletHandler) {
        this.servletHandler = servletHandler;
    }

    @Override
    public ServletHolder apply(ServletDescriptor servletDescriptor) {
        ServletHolder servletHolder = servletHandler.newServletHolder(Source.EMBEDDED);
        servletHolder.setHeldClass(servletDescriptor.servletClass());
        servletHolder.setName(servletDescriptor.name());
        if(servletDescriptor.initParams() != null) {
            Arrays.stream(servletDescriptor.initParams())
                    .forEach(p -> servletHolder.setInitParameter(p.name(), p.value()));
        }
        for(String pattern : servletDescriptor.urlPatterns()) {
            servletHandler.addServletWithMapping(servletHolder, pattern);
        }
        return servletHolder;
    }
}
