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

package ws.ament.hammock.web.undertow.websocket;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.WithAnnotations;
import javax.websocket.server.ServerEndpoint;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

@ApplicationScoped
public class UndertowWebSocketExtension implements Extension {
    private final Collection<Class<?>> endpointClasses = new HashSet<>();
    public void findWebSocketServers(@Observes @WithAnnotations(ServerEndpoint.class)ProcessAnnotatedType<?> pat) {
        endpointClasses.add(pat.getAnnotatedType().getJavaClass());
    }

    public Collection<Class<?>> getEndpointClasses() {
        return Collections.unmodifiableCollection(endpointClasses);
    }
}
