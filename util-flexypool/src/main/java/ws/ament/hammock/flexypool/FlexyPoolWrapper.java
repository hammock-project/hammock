/*
 * Copyright 2018 Hammock and its contributors
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

package ws.ament.hammock.flexypool;

import com.vladmihalcea.flexypool.FlexyPoolDataSource;
import com.vladmihalcea.flexypool.adaptor.HikariCPPoolAdapter;
import com.vladmihalcea.flexypool.config.Configuration;
import com.vladmihalcea.flexypool.metric.MetricsFactory;
import com.vladmihalcea.flexypool.strategy.IncrementPoolOnTimeoutConnectionAcquiringStrategy;
import com.vladmihalcea.flexypool.strategy.RetryConnectionAcquiringStrategy;
import com.zaxxer.hikari.HikariDataSource;
import org.eclipse.microprofile.config.Config;
import ws.ament.hammock.jpa.DataSourceWrapper;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.sql.DataSource;

import static java.lang.String.format;

@ApplicationScoped
public class FlexyPoolWrapper implements DataSourceWrapper {
    private static final String UNIQUE_ID_PROPERTY_FORMAT = "hammock.jpa.%s.datasource.flexypool.uniqueId";
    private static final String MAX_OVERFLOW_PROPERTY_FORMAT = "hammock.jpa.%s.datasource.flexypool.maxOverflow";
    private static final String RETRY_PROPERTY_FORMAT = "hammock.jpa.%s.datasource.flexypool.retry";

    @Inject
    private Config config;

    @Inject
    private Instance<MetricsFactory> metricsFactoryInstance;

    @Override
    public DataSource wrap(String name, DataSource dataSource) {
        if (!(dataSource instanceof HikariDataSource)) {
            return dataSource;
        }
        return wrapInternal(name, (HikariDataSource)dataSource);
    }

    private DataSource wrapInternal(String name, HikariDataSource dataSource) {
        Configuration<HikariDataSource> configuration = createConfiguration(name, config, dataSource);
        int maxOverflow = config.getOptionalValue(format(MAX_OVERFLOW_PROPERTY_FORMAT, name), Integer.class).orElse(5);
        int retry = config.getOptionalValue(format(RETRY_PROPERTY_FORMAT, name), Integer.class).orElse(2);
        return new FlexyPoolDataSource<HikariDataSource>(configuration,
                new IncrementPoolOnTimeoutConnectionAcquiringStrategy.Factory(maxOverflow),
                new RetryConnectionAcquiringStrategy.Factory(retry)
        );
    }

    private Configuration<HikariDataSource> createConfiguration(String name, Config config, HikariDataSource dataSource) {
        String uniqueId = config.getOptionalValue(format(UNIQUE_ID_PROPERTY_FORMAT, name), String.class).orElse(name);
        Configuration.Builder<HikariDataSource> builder = new Configuration.Builder<>(uniqueId, dataSource, HikariCPPoolAdapter.FACTORY);
        if(metricsFactoryInstance.isResolvable()) {
            MetricsFactory metricsFactory = metricsFactoryInstance.get();
            builder.setMetricsFactory(metricsFactory);
        }
        return builder.build();
    }
}
