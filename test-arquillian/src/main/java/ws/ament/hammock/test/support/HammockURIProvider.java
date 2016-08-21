/*
 * Copyright 2016 John D. Ament
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

package ws.ament.hammock.test.support;

import org.jboss.arquillian.container.test.impl.enricher.resource.OperatesOnDeploymentAwareProvider;
import org.jboss.arquillian.test.api.ArquillianResource;
import ws.ament.hammock.web.spi.WebServerConfiguration;

import javax.enterprise.inject.spi.CDI;
import java.lang.annotation.Annotation;
import java.net.URI;

public class HammockURIProvider extends OperatesOnDeploymentAwareProvider {
    @Override
    public Object doLookup(ArquillianResource arquillianResource, Annotation... annotations) {
        WebServerConfiguration webServerConfiguration = CDI.current().select(WebServerConfiguration.class).get();
        return URI.create("http://localhost:"+webServerConfiguration.getWebserverPort());
    }

    @Override
    public boolean canProvide(Class<?> type)
    {
        return type.isAssignableFrom(URI.class);
    }
}
