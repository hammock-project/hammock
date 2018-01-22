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
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.geronimo.config.DefaultConfigProvider;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import ws.ament.hammock.jpa.DataSourceExtension;
import ws.ament.hammock.jpa.JPAExtension;

import javax.enterprise.inject.spi.Extension;
import javax.inject.Inject;
import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Arquillian.class)
public class FlexyPoolWrapperTest {
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, DefaultConfigProvider.class.getPackage())
                .addPackages(true, "ws.ament.hammock")
                .addClass(NoOpMetricsFactory.class)
                .addAsServiceProviderAndClasses(Extension.class, JPAExtension.class, DataSourceExtension.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    private FlexyPoolWrapper wrapper;

    @Test
    public void shouldHandleInvalidDataSource() {
        DataSource dataSource = null;
        assertThat(wrapper.wrap("", dataSource)).isNull();
    }

    @Test
    public void wrapsHikariDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:mem:test_mem");
        DataSource dataSource = new HikariDataSource(config);
        DataSource wrapped = wrapper.wrap("", dataSource);
        assertThat(wrapped).isInstanceOf(FlexyPoolDataSource.class);
    }
}
