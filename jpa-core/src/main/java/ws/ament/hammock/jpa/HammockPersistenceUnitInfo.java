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

import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static java.util.Collections.emptyList;

class HammockPersistenceUnitInfo implements PersistenceUnitInfo {

   private final String name;
   private final DataSource dataSource;
   private final List<String> mappingFiles;
   private final List<String> classes;
   private final Properties properties = new Properties();

   public HammockPersistenceUnitInfo(String name,
                                     DataSource dataSource,
                                     List<String> mappingFiles,
                                     List<String> classes,
                                     Map<String, String> properties) {
      this.name = name;
      this.dataSource = dataSource;
      this.mappingFiles = mappingFiles;
      this.classes = classes;
      this.properties.putAll(properties);
   }

   @Override
   public String getPersistenceUnitName() {
      return name;
   }

   @Override
   public String getPersistenceProviderClassName() {
      return null;
   }

   @Override
   public PersistenceUnitTransactionType getTransactionType() {
      return PersistenceUnitTransactionType.RESOURCE_LOCAL;
   }

   @Override
   public DataSource getJtaDataSource() {
      return dataSource;
   }

   @Override
   public DataSource getNonJtaDataSource() {
      return dataSource;
   }

   @Override
   public List<String> getMappingFileNames() {
      return mappingFiles;
   }

   @Override
   public List<URL> getJarFileUrls() {
      return emptyList();
   }

   @Override
   public URL getPersistenceUnitRootUrl() {
      return HammockPersistenceUnitInfo.class.getProtectionDomain().getCodeSource().getLocation();
   }

   @Override
   public List<String> getManagedClassNames() {
      return classes;
   }

   @Override
   public boolean excludeUnlistedClasses() {
      return false;
   }

   @Override
   public SharedCacheMode getSharedCacheMode() {
      return SharedCacheMode.NONE;
   }

   @Override
   public ValidationMode getValidationMode() {
      return ValidationMode.NONE;
   }

   @Override
   public Properties getProperties() {
      return this.properties;
   }

   @Override
   public String getPersistenceXMLSchemaVersion() {
      return "2.1";
   }

   @Override
   public ClassLoader getClassLoader() {
      return HammockPersistenceUnitInfo.class.getClassLoader();
   }

   @Override
   public void addTransformer(ClassTransformer classTransformer) {

   }

   @Override
   public ClassLoader getNewTempClassLoader() {
      return HammockPersistenceUnitInfo.class.getClassLoader();
   }
}
