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

package ws.ament.hammock.flyway;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessBean;

import org.flywaydb.core.Flyway;
import org.jboss.logging.Logger;

public class FlywayExtension implements Extension{
   private static Logger LOG = Logger.getLogger(FlywayExtension.class);
   private boolean foundFlywayBean = false;
   public void findFlywayBean(@Observes ProcessBean<Flyway> flywayProcessBean) {
      this.foundFlywayBean = true;
   }

   public void addFlywayBean(@Observes AfterBeanDiscovery afterBeanDiscovery) {
      if(!foundFlywayBean) {
         LOG.info("Installing default Flyway bean");
         afterBeanDiscovery.addBean(new FlywayBean());
      }
   }

}
