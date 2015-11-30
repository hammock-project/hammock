/*
 * Copyright 2015 John D. Ament
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

import io.undertow.servlet.api.ClassIntrospecter;
import io.undertow.servlet.api.InstanceFactory;
import io.undertow.servlet.api.InstanceHandle;
import io.undertow.servlet.util.DefaultClassIntrospector;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;

public class CDIClassIntrospecter implements ClassIntrospecter {

    public static final ClassIntrospecter INSTANCE = new CDIClassIntrospecter();

    private CDIClassIntrospecter() {

    }

    @Override
    public <T> InstanceFactory<T> createInstanceFactory(Class<T> aClass) throws NoSuchMethodException {
        Instance<T> inst = CDI.current().select(aClass);
        if(inst.isUnsatisfied() || inst.isAmbiguous()) {
            return DefaultClassIntrospector.INSTANCE.createInstanceFactory(aClass);
        }
        else {
            return new CDIInstanceFactory<>(inst);
        }
    }

    private static class CDIInstanceFactory<T> implements InstanceFactory<T> {
        private final Instance<T> inst;

        public CDIInstanceFactory(Instance<T> inst) {
            this.inst = inst;
        }

        @Override
        public InstanceHandle<T> createInstance() throws InstantiationException {
            return new CDIInstanceHandler<>(inst);
        }
    }

    private static class CDIInstanceHandler<T> implements InstanceHandle<T> {

        private final Instance<T> inst;
        private T found = null;

        public CDIInstanceHandler(Instance<T> inst) {
            this.inst = inst;
        }

        @Override
        public T getInstance() {
            if(this.found == null) {
                this.found = inst.get();
            }
            return this.found;
        }

        @Override
        public void release() {
            inst.destroy(this.found);
        }
    }
}
