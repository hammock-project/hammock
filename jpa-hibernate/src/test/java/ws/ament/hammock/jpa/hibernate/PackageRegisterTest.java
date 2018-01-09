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

package ws.ament.hammock.jpa.hibernate;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import ws.ament.hammock.jpa.DataSourceExtension;
import ws.ament.hammock.jpa.EntityManagerProducer;
import ws.ament.hammock.jpa.JPAExtension;

import javax.enterprise.inject.spi.Extension;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Arquillian.class)
public class PackageRegisterTest {
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(Employee.class.getPackage())
                .addAsServiceProviderAndClasses(Extension.class, DataSourceExtension.class, JPAExtension.class)
                .addPackage(EntityManagerProducer.class.getPackage())
                .addAsManifestResource("META-INF/beans.xml", "beans.xml")
                .addAsManifestResource("META-INF/load.sql")
                .addAsManifestResource("META-INF/persistence.xml");
    }

    @Inject
    private EntityManager entityManager;

    @Test
    public void insertsUUIDColumnRegistered() {
        entityManager.getTransaction().begin();
        entityManager.persist(new UUIDEntity());
        entityManager.getTransaction().commit();

        List<UUIDEntity> entities = entityManager.createNamedQuery("UUIDPKG.find", UUIDEntity.class).getResultList();
        assertThat(entities).hasSize(1);
        assertThat(entities.get(0).getUuid()).isNotNull();
    }
}
