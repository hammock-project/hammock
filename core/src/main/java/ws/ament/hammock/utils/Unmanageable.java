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

import javax.enterprise.inject.spi.Unmanaged;

public class Unmanageable<T> implements AutoCloseable{

    private final Unmanaged.UnmanagedInstance<T> instance;

    public Unmanageable(Class<T> clazz) {
        this.instance = new Unmanaged<T>(clazz).newInstance();
        this.instance.produce().inject().postConstruct();
    }

    public T get() {
        return this.instance.get();
    }

    @Override
    public void close() {
        this.instance.preDestroy().dispose();
    }
}
