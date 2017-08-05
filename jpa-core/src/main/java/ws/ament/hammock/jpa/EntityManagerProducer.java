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

package ws.ament.hammock.jpa;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static ws.ament.hammock.jpa.EntityManagerFactoryProvider.DEFAULT_EMF;

@ApplicationScoped
public class EntityManagerProducer {
   @Inject
   private EntityManagerFactoryProvider entityManagerFactoryProvider;
   @Inject
   @ConfigProperty(name="hammock.default.persistence.unit.name",defaultValue = DEFAULT_EMF)
   private String defaultPersistenceUnitName;

   @Produces
   @Dependent
   public EntityManager createEM() {
      String name = defaultPersistenceUnitName;
      return getEntityManagerFactoryByName(name).createEntityManager();
   }

   public void closeEM(@Disposes @Any EntityManager entityManager) {
      if(entityManager.isOpen()) {
         entityManager.close();
      }
   }

   EntityManagerFactory getEntityManagerFactoryByName(String name) {
      return entityManagerFactoryProvider.lookupEntityManagerFactory(name);
   }
}
