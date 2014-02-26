/*
 *
 *  * Copyright 2013 John D. Ament
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  * implied.
 *  *
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package ws.ament.hammock.core.impl;

import org.jboss.resteasy.cdi.CdiInjectorFactory;

import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;

/**
 * Created with IntelliJ IDEA.
 * User: johndament
 * Date: 2/25/14
 * Time: 8:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class Cdi11InjectorFactory extends CdiInjectorFactory{
    @Override
    protected BeanManager lookupBeanManager() {
        return CDI.current().getBeanManager();
    }
}
