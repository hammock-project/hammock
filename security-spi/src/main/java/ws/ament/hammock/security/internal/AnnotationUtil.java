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

package ws.ament.hammock.security.internal;

import javax.interceptor.InvocationContext;
import java.lang.annotation.Annotation;

public abstract class AnnotationUtil {
    private AnnotationUtil() {}
    public static <A extends Annotation> A getAnnotation(InvocationContext invocationContext, Class<A> annotationClass) {
        A annotation = invocationContext.getMethod().getAnnotation(annotationClass);
        if(annotation != null) {
            return annotation;
        }
        else {
            return invocationContext.getMethod().getDeclaringClass().getAnnotation(annotationClass);
        }
    }
}
