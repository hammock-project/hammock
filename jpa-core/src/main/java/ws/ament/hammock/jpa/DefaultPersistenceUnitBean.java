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
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.persistence.spi.PersistenceUnitInfo;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static ws.ament.hammock.jpa.Database.DatabaseLiteral.database;

public class DefaultPersistenceUnitBean implements Bean<PersistenceUnitInfo> {
    private final String defaultDataSourceName;

    public DefaultPersistenceUnitBean(String defaultDataSourceName) {
        this.defaultDataSourceName = defaultDataSourceName;
    }
    @Override
    public Class<?> getBeanClass() {
        return PersistenceUnitInfo.class;
    }

    @Override
    public Set<InjectionPoint> getInjectionPoints() {
        return emptySet();
    }

    @Override
    public boolean isNullable() {
        return false;
    }

    @Override
    public PersistenceUnitInfo create(CreationalContext<PersistenceUnitInfo> creationalContext) {
        EntityManagerFactoryProvider entityManagerFactoryProvider = CDI.current().select(EntityManagerFactoryProvider.class).get();
        return entityManagerFactoryProvider.getDefaultPersistenceUnitInfo();
    }

    @Override
    public void destroy(PersistenceUnitInfo persistenceUnitInfo, CreationalContext<PersistenceUnitInfo> creationalContext) {

    }

    @Override
    public Set<Type> getTypes() {
        return singleton(PersistenceUnitInfo.class);
    }

    @Override
    public Set<Annotation> getQualifiers() {
        return singleton(database(defaultDataSourceName));
    }

    @Override
    public Class<? extends Annotation> getScope() {
        return ApplicationScoped.class;
    }

    @Override
    public String getName() {
        return "PersistenceUnitInfo."+defaultDataSourceName;
    }

    @Override
    public Set<Class<? extends Annotation>> getStereotypes() {
        return emptySet();
    }

    @Override
    public boolean isAlternative() {
        return false;
    }
}
