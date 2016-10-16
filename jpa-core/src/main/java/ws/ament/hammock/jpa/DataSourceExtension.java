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
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.WithAnnotations;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class DataSourceExtension implements Extension{
   private final List<DataSourceDefinition> dataSourceDefinitions = new ArrayList<>();
   public void findDataSourceDefinition(@Observes @WithAnnotations(DataSourceDefinition.class)ProcessAnnotatedType<?> pat) {
      DataSourceDefinition annotation = pat.getAnnotatedType().getJavaClass().getAnnotation(DataSourceDefinition.class);
      dataSourceDefinitions.add(annotation);
   }

   public void findDataSourceDefinitions(@Observes @WithAnnotations(DataSourceDefinitions.class)ProcessAnnotatedType<?> pat) {
      DataSourceDefinitions annotation = pat.getAnnotatedType().getJavaClass().getAnnotation(DataSourceDefinitions.class);
      dataSourceDefinitions.addAll(asList(annotation.value()));
   }

   public void registerDataSources(@Observes AfterBeanDiscovery afterBeanDiscovery) {
      dataSourceDefinitions.stream().map(DataSourceBean::new).forEach(afterBeanDiscovery::addBean);
   }
}
