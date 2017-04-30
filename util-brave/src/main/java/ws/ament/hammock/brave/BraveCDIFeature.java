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

package ws.ament.hammock.brave;

import com.github.kristofa.brave.jaxrs2.BraveTracingFeature;
import ws.ament.hammock.annotations.Configuring;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

@ApplicationScoped
@Provider
public class BraveCDIFeature implements Feature {
    @Inject
    private BraveTracingFeature braveTracingFeature;
    @Override
    public boolean configure(FeatureContext featureContext) {
        return braveTracingFeature.configure(featureContext);
    }

    public void configureClient(@Observes @Configuring Client client) {
        client.register(this);
    }
}
