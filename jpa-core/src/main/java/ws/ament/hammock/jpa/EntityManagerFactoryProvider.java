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

import org.apache.deltaspike.core.api.config.ConfigProperty;
import ws.ament.hammock.core.config.ConfigLoader;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceProviderResolverHolder;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.String.format;
import static java.util.Collections.emptyMap;
import static ws.ament.hammock.jpa.Database.DatabaseLiteral.database;

@ApplicationScoped
public class EntityManagerFactoryProvider {

    static final String DEFAULT_EMF = "__default";
    private final Map<String, EntityManagerFactory> entityManagerFactoryMap = new ConcurrentHashMap<>();

    @Inject
    @ConfigProperty(name = "hammock.jpa.__default.datasource", defaultValue = DEFAULT_EMF)
    private String defaultDataSourceName;

    @Inject
    private JPAExtension jpaExtension;

    @Inject
    private PersistenceUnitBuilder builder;
    private PersistenceProvider persistenceProvider;

    @PostConstruct
    private void locatePersistenceProvider() {
        this.persistenceProvider = PersistenceProviderResolverHolder.getPersistenceProviderResolver().getPersistenceProviders().get(0);
    }

    public EntityManagerFactory lookupEntityManagerFactory(String name) {
        return entityManagerFactoryMap.computeIfAbsent(name, s -> {
            PersistenceUnitInfo persistenceUnitInfo = jpaExtension.getPersistenceUnitInfo(name);
            if (s.equals(DEFAULT_EMF) && persistenceUnitInfo == null) {
                persistenceUnitInfo = getDefaultPersistenceUnitInfo();
            }
            if (persistenceUnitInfo != null) {
                PersistenceProvider provider = this.persistenceProvider;
                return provider.createContainerEntityManagerFactory(persistenceUnitInfo, emptyMap());
            } else {
                Map<String, String> properties = ConfigLoader.loadAllProperties(createPrefix(s), true);
                return Persistence.createEntityManagerFactory(s, properties);
            }
        });
    }

    private PersistenceUnitInfo getDefaultPersistenceUnitInfo() {
        DataSource dataSource = CDI.current().select(DataSource.class).select(database(defaultDataSourceName)).get();

        return builder
                .withClasses(jpaExtension.getEntityClasses())
                .withDataSource(dataSource)
                .loadAllProperties(createPrefix(DEFAULT_EMF), true)
                .build();
    }

    @Produces
    @ApplicationScoped
    public PersistenceProvider getPersistenceProvider() {
        return persistenceProvider;
    }

    static String createPrefix(String puName) {
        return format("hammock.jpa.%s", puName);
    }
}
