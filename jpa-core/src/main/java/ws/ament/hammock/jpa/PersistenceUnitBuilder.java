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

import javax.enterprise.context.Dependent;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ws.ament.hammock.core.config.ConfigLoader;

import static java.util.Arrays.asList;

@Dependent
public class PersistenceUnitBuilder {

   private String name = "__default";
   private DataSource dataSource;
   private final List<String> mappingFiles = new ArrayList<>();
   private final List<String> classes = new ArrayList<>();
   private final Map<String, String> properties = new LinkedHashMap<>();

   public PersistenceUnitBuilder withName(String name) {
      this.name = name;
      return this;
   }

   public PersistenceUnitBuilder withDataSource(DataSource dataSource) {
      this.dataSource = dataSource;
      return this;
   }

   public PersistenceUnitBuilder withMappingFile(String mappingFile) {
      this.mappingFiles.add(mappingFile);
      return this;
   }

   public PersistenceUnitBuilder withClasses(String... className) {
      this.classes.addAll(asList(className));
      return this;
   }

   public PersistenceUnitBuilder withClasses(Collection<String> classes) {
      this.classes.addAll(classes);
      return this;
   }

   public PersistenceUnitBuilder withProperties(Map<String, String> properties) {
      this.properties.putAll(properties);
      return this;
   }

   public PersistenceUnitBuilder withProperty(String key, String value) {
      this.properties.put(key, value);
      return this;
   }

   public PersistenceUnitBuilder loadAllProperties(String prefix, boolean strip) {
      Map<String, String> properties = ConfigLoader.loadAllProperties(prefix, strip);
      return withProperties(properties);
   }

   public PersistenceUnitInfo build() {
      return new HammockPersistenceUnitInfo(name, dataSource, mappingFiles, classes, properties);
   }
}
