/*
 * Copyright 2018 Hammock and its contributors
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

package ws.ament.hammock.bootstrap.owb;

import org.apache.webbeans.config.WebBeansContext;
import org.apache.webbeans.corespi.DefaultSingletonService;

public class HolderSingletonService extends DefaultSingletonService {
    private WebBeansContext webBeansContext;

    public HolderSingletonService(WebBeansContext webBeansContext) {
        this.webBeansContext = webBeansContext;
    }

    @Override
    public WebBeansContext get(Object key) {
        return webBeansContext;
    }

    @Override
    public void register(ClassLoader key, WebBeansContext context) {
        if(webBeansContext == null) {
            this.webBeansContext = context;
        }
    }

    @Override
    public void clearInstances(ClassLoader classLoader) {
        webBeansContext = null;
    }

    public void register(WebBeansContext context) {
        this.webBeansContext = context;
    }
}
