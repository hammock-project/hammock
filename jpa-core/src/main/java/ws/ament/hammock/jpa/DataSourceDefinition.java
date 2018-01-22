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

import javax.enterprise.util.AnnotationLiteral;

public class DataSourceDefinition {
    private final DataSourceDefinitionBuilder builder;
    private final String name;

    DataSourceDefinition(DataSourceDefinitionBuilder builder) {
        this.name = builder.getName();
        this.builder = builder;
    }

    public HammockDataSource getDataSource() {
        DataSourceDefinitionLiteral dataSourceDefinition = new DataSourceDefinitionLiteral(builder);
        return new HammockDataSource(dataSourceDefinition);
    }

    public String getName() {
        return name;
    }

    private class DataSourceDefinitionLiteral extends AnnotationLiteral<javax.annotation.sql.DataSourceDefinition> implements
            javax.annotation.sql.DataSourceDefinition {

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

        private DataSourceDefinitionLiteral(DataSourceDefinitionBuilder builder) {
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
