/*
 * Copyright 2018 Hammock and its contributors
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

package ws.ament.hammock.rest.cxf.opentracing;

import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.apache.cxf.tracing.opentracing.jaxrs.OpenTracingFeature;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

@ApplicationScoped
public class TracerFeature extends OpenTracingFeature {
    private static Tracer instance = null;

    public TracerFeature() {
        super(getInstance());
    }

    @Produces
    @Singleton
    public Tracer createTracer() {
        return instance;
    }

    private static Tracer getInstance() {
        if(instance == null) {
            instance = GlobalTracer.get();
        }
        return instance;
    }

    public static void setInstance(Tracer instance) {
        TracerFeature.instance = instance;
    }
}
