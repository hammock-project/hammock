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

package ws.ament.hammock.jpa.openjpa;

import ws.ament.hammock.jpa.DataSourceDefinition;
import ws.ament.hammock.jpa.DataSourceDefinitionBuilder;
import ws.ament.hammock.jpa.Database;
import ws.ament.hammock.jpa.PersistenceUnitBuilder;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.spi.PersistenceUnitInfo;

@ApplicationScoped
public class PUConfiguration {
    @Produces
    @Database("another")
    public PersistenceUnitInfo createPU(PersistenceUnitBuilder builder, DataSourceDefinitionBuilder dataSourceDefinitionBuilder) {
        DataSourceDefinition dataSourceDefinition = dataSourceDefinitionBuilder.url("jdbc:h2:./target/default").build();
        return builder.withDataSource(dataSourceDefinition.getDataSource())
                .withName("another")
                .withClasses(Employee.class.getName())
                .build();
    }
}
