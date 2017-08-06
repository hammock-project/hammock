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

package ws.ament.hammock.web.jetty;

import org.eclipse.jetty.util.Decorator;
import ws.ament.hammock.utils.Unmanageable;

import java.util.HashMap;
import java.util.Map;

public class HammockDecorator implements Decorator {
    private final Map<Object,Unmanageable> unmanageableMap = new HashMap<>();
    @Override
    public <T> T decorate(T obj) {
        try {
            Unmanageable<T> unmanageable = new Unmanageable<T>((Class<T>) obj.getClass());
            return unmanageable.get();
        }
        catch (Exception e) {
            return obj;
        }
    }

    @Override
    public void destroy(Object o) {
        Unmanageable unmanageable = unmanageableMap.remove(o);
        if(unmanageable != null) {
            unmanageable.close();
        }
    }
}
