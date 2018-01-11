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

import org.eclipse.microprofile.opentracing.Traced;

import javax.interceptor.InvocationContext;
import javax.ws.rs.HttpMethod;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class TracerDefinition {
    private final InvocationContext invocationContext;
    private final boolean traced;
    private final String name;

    TracerDefinition(InvocationContext invocationContext) {
        this.invocationContext = invocationContext;
        Method method = this.invocationContext.getMethod();
        Traced traced = method.getAnnotation(Traced.class);
        if(traced == null) {
            traced = method.getDeclaringClass().getAnnotation(Traced.class);
        }
        if(traced == null) {
            this.traced = false;
            name = null;
        } else {
            this.traced = traced.value();
            this.name = traced.operationName().isEmpty() ? defaultName(method) : traced.operationName();
        }

    }

    private String defaultName(Method method) {
        String httpMethodName = null;
        for(Annotation annotation : method.getAnnotations()) {
            HttpMethod httpMethod = annotation.getClass().getAnnotation(HttpMethod.class);
            if(httpMethod != null) {
                httpMethodName = httpMethod.value();
                break;
            }
        }
        return httpMethodName + ":" + method.getDeclaringClass().getName() + "." + method.getName();
    }

    public boolean isTraced() {
        return traced;
    }

    public String getName() {
        return name;
    }
}
