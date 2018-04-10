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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.openwebbeans.se.OWBInitializer;
import org.apache.webbeans.config.WebBeansContext;
import org.apache.webbeans.config.WebBeansFinder;
import org.apache.webbeans.spi.SingletonService;

import javax.enterprise.inject.se.SeContainer;

public class HammockInitializer extends OWBInitializer {
    private static final Logger logger = LogManager.getLogger(HammockInitializer.class);
    @Override
    protected SeContainer newContainer(WebBeansContext context) {
        SingletonService<WebBeansContext> singletonService = WebBeansFinder.getSingletonService();
        if(singletonService instanceof HolderSingletonService) {
            ((HolderSingletonService) singletonService).register(context);
        } else {
            try {
                WebBeansFinder.setSingletonService(new HolderSingletonService(context));
            } catch (Exception e) {
                logger.info("Unable to override OWB SingletonService", e);
            }
        }
        return super.newContainer(context);
    }
}
