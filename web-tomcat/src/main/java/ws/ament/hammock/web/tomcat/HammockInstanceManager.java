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

package ws.ament.hammock.web.tomcat;

import org.apache.tomcat.InstanceManager;

import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.inject.spi.Unmanaged;
import javax.naming.NamingException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class HammockInstanceManager implements InstanceManager {
    private final BeanManager beanManager = CDI.current().getBeanManager();
    private final Map<Object, Unmanaged.UnmanagedInstance> instanceMap = new HashMap<>();

    @Override
    public Object newInstance(Class<?> clazz) throws IllegalAccessException, InvocationTargetException, NamingException, InstantiationException {
        try {
            Unmanaged.UnmanagedInstance<?> instance = new Unmanaged<>(beanManager, clazz).newInstance();
            instance.produce().inject().postConstruct();
            Object o = instance.get();
            instanceMap.put(o, instance);
            return o;
        } catch (Exception e) {
            return clazz.newInstance();
        }
    }

    @Override
    public Object newInstance(String className) throws IllegalAccessException, InvocationTargetException, NamingException, InstantiationException, ClassNotFoundException {
        return newInstance(Class.forName(className));
    }

    @Override
    public Object newInstance(String fqcn, ClassLoader classLoader) throws IllegalAccessException, InvocationTargetException, NamingException, InstantiationException, ClassNotFoundException {
        return newInstance((Class.forName(fqcn, true, classLoader)));
    }

    @Override
    public void newInstance(Object o) throws IllegalAccessException, InvocationTargetException, NamingException {
    }

    @Override
    public void destroyInstance(Object o) throws IllegalAccessException, InvocationTargetException {
        Unmanaged.UnmanagedInstance remove = instanceMap.remove(o);
        if (remove != null) {
            remove.preDestroy();
            remove.dispose();
        }
    }
}
