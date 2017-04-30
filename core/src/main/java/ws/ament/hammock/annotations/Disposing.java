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

package ws.ament.hammock.annotations;

import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.TYPE, ElementType.PARAMETER, ElementType.FIELD,
        ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR,
        ElementType.LOCAL_VARIABLE, ElementType.PACKAGE, ElementType.TYPE_PARAMETER,
        ElementType.TYPE_USE})
public @interface Disposing {
    Disposing INSTANCE = new Literal();
    class Literal extends AnnotationLiteral<Disposing> implements Disposing {}
}
