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

import io.undertow.servlet.api.InstanceHandle;

import javax.enterprise.inject.spi.Unmanaged;

public class HammockInstanceHandle<T> implements InstanceHandle<T> {

    private final Unmanaged.UnmanagedInstance<T> instance;

    HammockInstanceHandle(Unmanaged.UnmanagedInstance<T> instance) {
        this.instance = instance;
        instance.produce().inject().postConstruct();
    }

    @Override
    public T getInstance() {
        return instance.get();
    }

    @Override
    public void release() {
        instance.preDestroy();
        instance.dispose();
    }
}
