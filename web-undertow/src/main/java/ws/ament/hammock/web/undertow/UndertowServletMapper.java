/*
 * Copyright 2015 John D. Ament
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

import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.ServletInfo;
import ws.ament.hammock.web.spi.ServletDescriptor;

import javax.enterprise.context.ApplicationScoped;
import java.util.function.Function;

@ApplicationScoped
public class UndertowServletMapper implements Function<ServletDescriptor, ServletInfo> {
    @Override
    public ServletInfo apply(ServletDescriptor servletDescriptor) {
        return Servlets.servlet(servletDescriptor.name(), servletDescriptor.servletClass())
                .setAsyncSupported(servletDescriptor.asyncSupported())
                .setLoadOnStartup(servletDescriptor.loadOnStartup())
                .addMappings(servletDescriptor.urlPatterns());
    }
}
