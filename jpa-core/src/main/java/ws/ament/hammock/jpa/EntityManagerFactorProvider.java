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
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceProviderResolverHolder;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.deltaspike.core.api.config.ConfigProperty;
import org.jboss.weld.literal.NamedLiteral;
import ws.ament.hammock.core.config.ConfigLoader;

import static java.lang.String.format;
import static java.util.Collections.*;

@ApplicationScoped
public class EntityManagerFactorProvider {

   private static final String DEFAULT_EMF = "__default";
   private final Map<String, EntityManagerFactory> entityManagerFactoryMap = new ConcurrentHashMap<>();

   @Inject
   @ConfigProperty(name = "hammock.persistence.default.datasource", defaultValue = DEFAULT_EMF)
   private String defaultDataSourceName;

   @Inject
   private Instance<DataSource> dataSources;

   @Inject
   private JPAExtension jpaExtension;

   @Inject
   private PersistenceUnitBuilder builder;

   public EntityManagerFactory lookupEntityManagerFactory(String name) {
      return entityManagerFactoryMap.computeIfAbsent(name, s -> {
         if (s.equals(DEFAULT_EMF)) {
            DataSource dataSource = dataSources.select(new NamedLiteral(defaultDataSourceName)).get();

            PersistenceUnitInfo persistenceUnitInfo = builder
               .withClasses(jpaExtension.getEntityClasses())
               .withDataSource(dataSource)
               .loadAllProperties(createPrefix(DEFAULT_EMF),true)
               .build();
            PersistenceProvider provider = getPersistenceProvider();
            return provider.createContainerEntityManagerFactory(persistenceUnitInfo, emptyMap());
         }
         else {
            Map<String, String> properties = ConfigLoader.loadAllProperties(createPrefix(s), true);
            return Persistence.createEntityManagerFactory(s, properties);
         }
      });
   }

   private static String createPrefix(String puName) {
      return format("hammock.jpa.%s",puName);
   }

   private PersistenceProvider getPersistenceProvider() {
      return PersistenceProviderResolverHolder.getPersistenceProviderResolver().getPersistenceProviders().get(0);
   }
}
