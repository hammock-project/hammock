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
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.sql.DataSource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

import org.jboss.weld.literal.NamedLiteral;

import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;

public class DataSourceBean implements Bean<DataSource>{
   private final DataSourceDefinition dataSourceDefinition;

   public DataSourceBean(DataSourceDefinition dataSourceDefinition) {
      this.dataSourceDefinition = dataSourceDefinition;
   }

   @Override
   public Class<?> getBeanClass() {
      return HammockDataSource.class;
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
   public DataSource create(CreationalContext<DataSource> creationalContext) {
      return new HammockDataSource(dataSourceDefinition);
   }

   @Override
   public void destroy(DataSource dataSource, CreationalContext<DataSource> creationalContext) {

   }

   @Override
   public Set<Type> getTypes() {
      return singleton(DataSource.class);
   }

   @Override
   public Set<Annotation> getQualifiers() {
      return singleton(new NamedLiteral(dataSourceDefinition.name()));
   }

   @Override
   public Class<? extends Annotation> getScope() {
      return ApplicationScoped.class;
   }

   @Override
   public String getName() {
      return dataSourceDefinition.name();
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
