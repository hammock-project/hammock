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

import org.eclipse.microprofile.config.ConfigProvider;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;
import javax.persistence.Entity;
import javax.persistence.spi.PersistenceUnitInfo;
import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableSet;
import static ws.ament.hammock.jpa.Database.DatabaseLiteral.database;
import static ws.ament.hammock.jpa.EntityManagerFactoryProvider.DEFAULT_EMF;

public class JPAExtension implements Extension {
    private final Set<String> entityClasses = new LinkedHashSet<>();
    private final Set<Bean<PersistenceUnitInfo>> persistenceUnitInfoBeans = new LinkedHashSet<>();
    private Map<String, PersistenceUnitInfo> persistenceUnitInfos;
    private final Set<String> persistenceUnits = new LinkedHashSet<>();

    public void findEntities(@Observes @WithAnnotations(Entity.class) ProcessAnnotatedType<?> pat) {
        entityClasses.add(pat.getAnnotatedType().getJavaClass().getName());
    }
    public void locatePersistenceUnits(@Observes ProcessBean<PersistenceUnitInfo> processBean) {
        persistenceUnitInfoBeans.add(processBean.getBean());
        addDatabase(processBean.getAnnotated(), processBean.getBean().getQualifiers());
    }
    public void locatePUProducers(@Observes ProcessProducer<?, PersistenceUnitInfo> processProducer) {
        addDatabase(processProducer.getAnnotatedMember(), emptySet());
    }
    private void addDatabase(Annotated annotated, Set<Annotation> qualifiers) {
        Database annotation = annotated.getAnnotation(Database.class);
        if(annotation == null) {
            annotation = (Database)qualifiers.iterator().next();
        }
        persistenceUnits.add(annotation.value());
    }
    public void addEntityManagerBeans(@Observes AfterBeanDiscovery afterBeanDiscovery) {
        persistenceUnits.stream().map(EntityManagerBean::new).forEach(afterBeanDiscovery::addBean);
        final String defaultDataSourceName = ConfigProvider.getConfig().getOptionalValue("hammock.jpa.__default.datasource", String.class).orElse(DEFAULT_EMF);
        if(!persistenceUnits.contains(defaultDataSourceName)) {
            afterBeanDiscovery.addBean(new DefaultPersistenceUnitBean(defaultDataSourceName));
            afterBeanDiscovery.addBean(new EntityManagerBean(defaultDataSourceName));
        }
    }
    public void load(@Observes final AfterDeploymentValidation event) {
        persistenceUnitInfos = persistenceUnitInfoBeans.stream().map(bean ->
                CDI.current().select(PersistenceUnitInfo.class).select(bean.getQualifiers().toArray(new Annotation[1])).get()
        ).collect(Collectors.toMap(PersistenceUnitInfo::getPersistenceUnitName, Function.identity()));
    }

    Set<String> getEntityClasses() {
        return unmodifiableSet(entityClasses);
    }

    PersistenceUnitInfo getPersistenceUnitInfo(String name) {
        return persistenceUnitInfos.computeIfAbsent(name, s -> CDI.current().select(PersistenceUnitInfo.class).select(database(s)).get());
    }


}
