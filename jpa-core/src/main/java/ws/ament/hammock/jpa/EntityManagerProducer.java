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

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.apache.deltaspike.core.api.config.ConfigProperty;

@ApplicationScoped
public class EntityManagerProducer {
   @Inject
   private EntityManagerFactorProvider entityManagerFactorProvider;

   @Inject
   @ConfigProperty(name="hammock.datasource.__default.user")
   private String user;
   @Inject
   @ConfigProperty(name="hammock.datasource.__default.password")
   private String password;
   @Inject
   @ConfigProperty(name="hammock.datasource.__default.url")
   private String url;

   @Produces
   @Dependent
   public EntityManager createEM() {
      return entityManagerFactorProvider.lookupEntityManagerFactory("__default").createEntityManager();
   }

   public void closeEM(@Disposes EntityManager entityManager) {
      if(entityManager.isOpen()) {
         entityManager.close();
      }
   }

   @Produces
   @Named("__default")
   public DataSource createDefaultDataSource(DataSourceDefinitionBuilder builder) {
      return builder.url(url)
         .user(user)
         .password(password)
         .build();
   }
}
