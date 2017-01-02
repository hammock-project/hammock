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

package ws.ament.hammock.bootstrap.weld3;

import org.jboss.weld.environment.servlet.Container;
import org.jboss.weld.environment.servlet.ContainerContext;
import org.jboss.weld.resources.spi.ResourceLoader;

public class HammockContainer implements Container {
    @Override
    public boolean touch(ResourceLoader resourceLoader, ContainerContext context) throws Exception {
        return true;
    }

    @Override
    public void initialize(ContainerContext context) {

    }

    @Override
    public void destroy(ContainerContext context) {

    }
}
