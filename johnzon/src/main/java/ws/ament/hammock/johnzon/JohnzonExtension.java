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

package ws.ament.hammock.johnzon;

import org.apache.johnzon.jaxrs.*;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

@ApplicationScoped
@Provider
public class JohnzonExtension implements Feature {

    @Override
    public boolean configure(FeatureContext featureContext) {
        featureContext.register(ConfigurableJohnzonProvider.class);
        featureContext.register(JsrProvider.class);
        featureContext.register(JohnzonProvider.class);
        featureContext.register(WildcardJsrProvider.class);
        featureContext.register(WildcardJohnzonProvider.class);
        return true;
    }
}
