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
import org.apache.cxf.cdi.CXFCdiServlet;
import org.apache.cxf.common.util.ClassUnwrapper;

import javax.inject.Inject;

public class HammockCXFServlet extends CXFCdiServlet {
    @Override
    @Inject
    public void setBus(Bus bus) {
        bus.setProperty(ClassUnwrapper.class.getName(), (ClassUnwrapper) o -> {
            Class<?> aClass = o.getClass();
            if(aClass.getName().contains("OwbNormalScopeProxy") || aClass.getName().contains("WeldClientProxy")) {
                return aClass.getSuperclass();
            }
            return aClass;
        });
        super.setBus(bus);
    }
}
