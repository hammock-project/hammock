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

package ws.ament.hammock.rest.cxf;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.extension.ExtensionManagerBus;
import org.apache.cxf.common.util.ClassUnwrapper;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Named;
import javax.ws.rs.Produces;

@ApplicationScoped
public class CXFCdiBusBean {
    @Produces
    @Alternative
    @Named("cxf")
    @ApplicationScoped
    public Bus createBus() {
        ExtensionManagerBus bus = new ExtensionManagerBus();
        bus.setProperty(ClassUnwrapper.class.getName(), (ClassUnwrapper) o -> {
            Class<?> aClass = o.getClass();
            if(aClass.getName().contains("OwbNormalScopeProxy") || aClass.getName().contains("ClientProxyd")) {
                return aClass.getSuperclass();
            }
            return aClass;
        });
        return bus;
    }
}
