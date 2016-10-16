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
import javax.enterprise.context.Dependent;
import javax.enterprise.util.AnnotationLiteral;
import javax.sql.DataSource;

@Dependent
public class DataSourceDefinitionBuilder {

   private String name;
   private String className;
   private String description;
   private String url;
   private String user;
   private String password;
   private String databaseName;
   private int portNumber;
   private String serverName;
   private int isolationLevel;
   private boolean transactional;
   private int initialPoolSize;
   private int maxPoolSize;
   private int minPoolSize;
   private int maxIdleTime;
   private int maxStatements;
   private String[] properties;
   private int loginTimeout;

   public DataSource build() {
      return new HammockDataSource(new DataSourceDefinitionLiteral(this));
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

   private class DataSourceDefinitionLiteral extends AnnotationLiteral<DataSourceDefinition> implements DataSourceDefinition {

      private final String name;
      private final String className;
      private final String description;
      private final String url;
      private final String user;
      private final String password;
      private final String databaseName;
      private final int portNumber;
      private final String serverName;
      private final int isolationLevel;
      private final boolean transactional;
      private final int initialPoolSize;
      private final int maxPoolSize;
      private final int minPoolSize;
      private final int maxIdleTime;
      private final int maxStatements;
      private final String[] properties;
      private final int loginTimeout;

      DataSourceDefinitionLiteral(DataSourceDefinitionBuilder builder) {
         this.name = builder.name;
         this.className = builder.className;
         this.description = builder.description;
         this.url = builder.url;
         this.user = builder.user;
         this.password = builder.password;
         this.databaseName = builder.databaseName;
         this.portNumber = builder.portNumber;
         this.serverName = builder.serverName;
         this.isolationLevel = builder.isolationLevel;
         this.transactional = builder.transactional;
         this.initialPoolSize = builder.initialPoolSize;
         this.maxPoolSize = builder.maxPoolSize;
         this.minPoolSize = builder.minPoolSize;
         this.maxIdleTime = builder.maxIdleTime;
         this.maxStatements = builder.maxStatements;
         this.properties = builder.properties;
         this.loginTimeout = builder.loginTimeout;
      }

      @Override
      public String name() {
         return name;
      }

      @Override
      public String className() {
         return className;
      }

      @Override
      public String description() {
         return description;
      }

      @Override
      public String url() {
         return url;
      }

      @Override
      public String user() {
         return user;
      }

      @Override
      public String password() {
         return password;
      }

      @Override
      public String databaseName() {
         return databaseName;
      }

      @Override
      public int portNumber() {
         return portNumber;
      }

      @Override
      public String serverName() {
         return serverName;
      }

      @Override
      public int isolationLevel() {
         return isolationLevel;
      }

      @Override
      public boolean transactional() {
         return transactional;
      }

      @Override
      public int initialPoolSize() {
         return initialPoolSize;
      }

      @Override
      public int maxPoolSize() {
         return maxPoolSize;
      }

      @Override
      public int minPoolSize() {
         return minPoolSize;
      }

      @Override
      public int maxIdleTime() {
         return maxIdleTime;
      }

      @Override
      public int maxStatements() {
         return maxStatements;
      }

      @Override
      public String[] properties() {
         return properties;
      }

      @Override
      public int loginTimeout() {
         return loginTimeout;
      }
   }

   }
