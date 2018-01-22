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

@Dependent
public class DataSourceDefinitionBuilder {

   String name;
   String className;
   String description;
   String url;
   String user;
   String password;
   String databaseName;
   int portNumber;
   String serverName;
   int isolationLevel;
   boolean transactional;
   int initialPoolSize;
   int maxPoolSize;
   int minPoolSize;
   int maxIdleTime;
   int maxStatements;
   String[] properties;
   int loginTimeout;

   public DataSourceDefinition build() {
      return new DataSourceDefinition(this);
   }

   public DataSourceDefinitionBuilder name(String name) {
      this.name = name;
      return this;
   }

   public DataSourceDefinitionBuilder className(String className) {
      this.className = className;
      return this;
   }

   public DataSourceDefinitionBuilder description(String description) {
      this.description = description;
      return this;
   }

   public DataSourceDefinitionBuilder url(String url) {
      this.url = url;
      return this;
   }

   public DataSourceDefinitionBuilder user(String user) {
      this.user = user;
      return this;
   }

   public DataSourceDefinitionBuilder password(String password) {
      this.password = password;
      return this;
   }

   public DataSourceDefinitionBuilder databaseName(String databaseName) {
      this.databaseName = databaseName;
      return this;
   }

   public DataSourceDefinitionBuilder portNumber(int portNumber) {
      this.portNumber = portNumber;
      return this;
   }

   public DataSourceDefinitionBuilder serverName(String serverName) {
      this.serverName = serverName;
      return this;
   }

   public DataSourceDefinitionBuilder isolationLevel(int isolationLevel) {
      this.isolationLevel = isolationLevel;
      return this;
   }

   public DataSourceDefinitionBuilder transactional(boolean transactional) {
      this.transactional = transactional;
      return this;
   }

   public DataSourceDefinitionBuilder initialPoolSize(int initialPoolSize) {
      this.initialPoolSize = initialPoolSize;
      return this;
   }

   public DataSourceDefinitionBuilder maxPoolSize(int maxPoolSize) {
      this.maxPoolSize = maxPoolSize;
      return this;
   }

   public DataSourceDefinitionBuilder minPoolSize(int minPoolSize) {
      this.minPoolSize = minPoolSize;
      return this;
   }

   public DataSourceDefinitionBuilder maxIdleTime(int maxIdleTime) {
      this.maxIdleTime = maxIdleTime;
      return this;
   }

   public DataSourceDefinitionBuilder maxStatements(int maxStatements) {
      this.maxStatements = maxStatements;
      return this;
   }

   public DataSourceDefinitionBuilder properties(String[] properties) {
      this.properties = properties;
      return this;
   }

   public DataSourceDefinitionBuilder loginTimeout(int loginTimeout) {
      this.loginTimeout = loginTimeout;
      return this;
   }

   public String getName() {
      return name;
   }
}
