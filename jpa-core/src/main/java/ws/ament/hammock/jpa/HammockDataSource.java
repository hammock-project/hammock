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

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.annotation.sql.DataSourceDefinition;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;
import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public class HammockDataSource implements DataSource {
    private final String name;
    private final DataSource delegate;

    public HammockDataSource(DataSourceDefinition dataSourceDefinition) {
        this.name = dataSourceDefinition.name();
        this.delegate = createDelegate(dataSourceDefinition);
    }

    private DataSource createDelegate(DataSourceDefinition dataSourceDefinition) {
        HikariConfig config = new HikariConfig();
        if (dataSourceDefinition.url() != null) {
            config.setJdbcUrl(dataSourceDefinition.url());
        }
        if (dataSourceDefinition.user() != null) {
            config.setUsername(dataSourceDefinition.user());
            config.setPassword(dataSourceDefinition.password());
        }
        if (dataSourceDefinition.maxPoolSize() > 0) {
            config.setMaximumPoolSize(dataSourceDefinition.maxPoolSize());
        }
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        return wrap(new HikariDataSource(config));
    }

    private DataSource wrap(DataSource dataSource) {
        Instance<DataSourceWrapper> wrappers = CDI.current().select(DataSourceWrapper.class);
        for(DataSourceWrapper wrapper : wrappers) {
            dataSource = wrapper.wrap(name, dataSource);
        }
        return dataSource;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return delegate.getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return delegate.getConnection(username, password);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return delegate.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        delegate.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        delegate.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return delegate.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return delegate.getParentLogger();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return delegate.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return delegate.isWrapperFor(iface);
    }

    public String getName() {
        return name;
    }
}
