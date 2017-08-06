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

package ws.ament.hammock.web.undertow;

import io.undertow.servlet.api.InstanceFactory;
import io.undertow.servlet.api.InstanceHandle;

import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Unmanaged;

class HammockInstanceFactory<T> implements InstanceFactory<T> {
    private final Class<T> clazz;
    private final BeanManager beanManager;

    HammockInstanceFactory(Class<T> clazz, BeanManager beanManager) {
        this.clazz = clazz;
        this.beanManager = beanManager;
    }

    @Override
    public InstanceHandle<T> createInstance() {
        try {
            return new HammockInstanceHandle<>(new Unmanaged<>(beanManager, clazz).newInstance());
        }
        catch (Exception e) {
            try {
                return new BasicInstanceFactory<T>(clazz.newInstance());
            } catch (Exception ex) {
                throw new RuntimeException("Unable to instantiate "+clazz, ex);
            }
        }
    }
}
