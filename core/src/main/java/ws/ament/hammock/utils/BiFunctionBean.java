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

package ws.ament.hammock.utils;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.function.BiFunction;

import static java.util.Collections.emptySet;

public class BiFunctionBean<T> implements Bean<Object> {
    private final Set<Annotation> qualifiers;
    private final Class<? extends Annotation> scope;
    private final Class<?> beanType;
    private final Set<Type> types;
    private final BiFunction<CreationalContext<Object>, BiFunctionBean<?>, Object> biFunction;

    public BiFunctionBean(Class<?> beanType, Set<Type> types, Set<Annotation> qualifiers,
                          Class<? extends Annotation> scope,
                          BiFunction<CreationalContext<Object>, BiFunctionBean<?>, Object> biFunction) {
        this.qualifiers = qualifiers;
        this.scope = scope;
        this.biFunction = biFunction;
        this.types = types;
        this.beanType = beanType;
    }

    @Override
    public Set<InjectionPoint> getInjectionPoints() {
        return emptySet();
    }

    @Override
    public Class<?> getBeanClass() {
        return this.beanType;
    }

    @Override
    public boolean isNullable() {
        return false;
    }

    @Override
    public Object create(CreationalContext<Object> context) {
        return biFunction.apply(context, this);
    }

    @Override
    public void destroy(Object instance, CreationalContext<Object> context) {

    }

    @Override
    public Set<Type> getTypes() {
        return types;
    }

    @Override
    public Set<Annotation> getQualifiers() {
        return qualifiers;
    }

    @Override
    public Class<? extends Annotation> getScope() {
        return scope;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Set<Class<? extends Annotation>> getStereotypes() {
        return emptySet();
    }

    @Override
    public boolean isAlternative() {
        return false;
    }
}
