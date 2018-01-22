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

import javax.annotation.sql.DataSourceDefinition;
import javax.annotation.sql.DataSourceDefinitions;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Arrays.asList;
import static ws.ament.hammock.jpa.Database.DatabaseLiteral.database;

public class DataSourceExtension implements Extension {
    private final List<DataSourceDefinition> dataSourceDefinitions = new ArrayList<>();
    private final List<Bean<ws.ament.hammock.jpa.DataSourceDefinition>> beanDelegates = new ArrayList<>();
    private final List<String> databaseProducers = new ArrayList<>();

    public void findDataSourceDefinition(@Observes @WithAnnotations(DataSourceDefinition.class) ProcessAnnotatedType<?> pat) {
        DataSourceDefinition annotation = pat.getAnnotatedType().getJavaClass().getAnnotation(DataSourceDefinition.class);
        dataSourceDefinitions.add(annotation);
    }

    public void findDataSourceDefinitions(@Observes @WithAnnotations(DataSourceDefinitions.class) ProcessAnnotatedType<?> pat) {
        DataSourceDefinitions annotation = pat.getAnnotatedType().getJavaClass().getAnnotation(DataSourceDefinitions.class);
        dataSourceDefinitions.addAll(asList(annotation.value()));
    }

    public void findDataSourceDefinitionBeans(@Observes ProcessBean<ws.ament.hammock.jpa.DataSourceDefinition> processBean) {
        beanDelegates.add(processBean.getBean());
    }

    public void findDataSourceProducers(@Observes ProcessProducer<?, ws.ament.hammock.jpa.DataSourceDefinition> processProducer) {
        Database database = processProducer.getAnnotatedMember().getAnnotation(Database.class);
        databaseProducers.add(database.value());
    }

    public void registerDataSources(@Observes AfterBeanDiscovery afterBeanDiscovery) {
        DataSourceDefinition defaultDataSource = dataSourceDefinitions.stream()
                .filter(dsd -> dsd.name().equals(EntityManagerFactoryProvider.DEFAULT_EMF))
                .findFirst()
                .orElse(null);
        Bean<ws.ament.hammock.jpa.DataSourceDefinition> defaultBean = beanDelegates
                .stream()
                .filter(b -> b.getName().equals(EntityManagerFactoryProvider.DEFAULT_EMF))
                .findFirst()
                .orElse(null);
        dataSourceDefinitions.stream().map(dataSourceDefinitionDataSourceDefinitionFunction)
                .map(dataSourceDefinition ->  new DataSourceDelegateBean(dataSourceDefinition.getName(),
                        () -> dataSourceDefinition))
                .forEach(afterBeanDiscovery::addBean);
        beanDelegates.stream().map(bean -> new DataSourceDelegateBean(bean.getName(), () -> bean.create(null)))
                .forEach(afterBeanDiscovery::addBean);
        databaseProducers.stream().map(producer -> new DataSourceDelegateBean(producer, create(producer)))
                .forEach(afterBeanDiscovery::addBean);
        if (defaultBean == null && defaultDataSource == null) {
            afterBeanDiscovery.addBean(new DefaultDataSourceBean());
        }
    }

    private Supplier<ws.ament.hammock.jpa.DataSourceDefinition> create(String name) {
        return () -> CDI.current().select(ws.ament.hammock.jpa.DataSourceDefinition.class)
                .select(database(name))
                .get();
    }

    private Function<DataSourceDefinition, ws.ament.hammock.jpa.DataSourceDefinition>
            dataSourceDefinitionDataSourceDefinitionFunction = dataSourceDefinition -> new DataSourceDefinitionBuilder()
            .name(dataSourceDefinition.name())
            .password(dataSourceDefinition.password())
            .user(dataSourceDefinition.user())
            .className(dataSourceDefinition.className())
            .databaseName(dataSourceDefinition.databaseName())
            .description(dataSourceDefinition.description())
            .initialPoolSize(dataSourceDefinition.initialPoolSize())
            .isolationLevel(dataSourceDefinition.isolationLevel())
            .loginTimeout(dataSourceDefinition.loginTimeout())
            .maxIdleTime(dataSourceDefinition.maxIdleTime())
            .maxStatements(dataSourceDefinition.maxStatements())
            .portNumber(dataSourceDefinition.portNumber())
            .serverName(dataSourceDefinition.serverName())
            .transactional(dataSourceDefinition.transactional())
            .minPoolSize(dataSourceDefinition.minPoolSize())
            .maxPoolSize(dataSourceDefinition.maxPoolSize())
            .properties(dataSourceDefinition.properties())
            .url(dataSourceDefinition.url())
            .build();
}
