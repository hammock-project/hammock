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

package ws.ament.hammock.flyway;

import javax.annotation.sql.DataSourceDefinition;
import javax.enterprise.inject.spi.Extension;
import javax.inject.Inject;
import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.assertj.core.api.Assertions;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import ws.ament.hammock.jpa.DataSourceDefinitionBuilder;
import ws.ament.hammock.jpa.DataSourceExtension;
import ws.ament.hammock.jpa.Database;
import ws.ament.hammock.jpa.EntityManagerFactoryProvider;

@RunWith(Arquillian.class)
@DataSourceDefinition(name = "__default", className = "", url = "jdbc:h2:./target/hammocktest")
public class FlywayTest {
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(DataSourceDefinitionBuilder.class)
                .addPackage(EntityManagerFactoryProvider.class.getPackage())
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsServiceProvider(Extension.class, DataSourceExtension.class, FlywayExtension.class);
    }

    @Inject
    @Database("__default")
    private DataSource testds;

    @Test
    public void shouldApplyMigrations() throws Exception {
        Connection connection = testds.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("select count(*) from \"schema_version\"");
             ResultSet rs = preparedStatement.executeQuery()) {
            rs.next();
            int anInt = rs.getInt(1);
            Assertions.assertThat(anInt).isEqualTo(1);
        }

    }
}
