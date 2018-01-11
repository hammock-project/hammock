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

import io.opentracing.ActiveSpan;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.eclipse.microprofile.opentracing.Traced;

import javax.annotation.Priority;
import javax.enterprise.context.Dependent;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
@Traced
@Dependent
@Priority(Interceptor.Priority.LIBRARY_BEFORE + 1)
public class TracedInterceptor {
    @AroundInvoke
    public Object trace(InvocationContext invocationContext) throws Exception {
        TracerDefinition tracerDefinition = new TracerDefinition(invocationContext);
        if(tracerDefinition.isTraced()) {
            Tracer.SpanBuilder spanBuilder = GlobalTracer.get().buildSpan(tracerDefinition.getName());
            try (ActiveSpan activeSpan = spanBuilder.withStartTimestamp(System.currentTimeMillis()).startActive()) {
                return invocationContext.proceed();
            }
        }
        return invocationContext.proceed();
    }

}
