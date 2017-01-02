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

package ws.ament.hammock.bootstrap.weld.jpa;

import org.assertj.core.api.Assertions;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.weld.bootstrap.api.Service;
import org.junit.Test;
import org.junit.runner.RunWith;
import ws.ament.hammock.bootstrap.weld3.jpa.BasicJpaInjectionServices;
import ws.ament.hammock.bootstrap.weld3.jpa.EntityManagerFactoryReferenceFactory;
import ws.ament.hammock.bootstrap.weld3.jpa.EntityManagerReferenceFactory;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.PersistenceException;

/**
 * Test class to check that {@link javax.persistence.PersistenceContext} annotation usage triggers call to JPA {@link javax.persistence.Persistence}
 * bootstrap class
 *
 * @author Antoine Sabot-Durand
 */
@RunWith(Arquillian.class)
public class JpaInjectionTest {

    static final String MY_PU = "myPU";

    @Inject
    Instance<BeanWithPersistence> beanWithPersistenceInstance;

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(BasicJpaInjectionServices.class,
                        EntityManagerReferenceFactory.class,
                        EntityManagerFactoryReferenceFactory.class,
                        BeanWithPersistence.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsServiceProvider(Service.class, BasicJpaInjectionServices.class);
    }

    @Test
    public void shouldTriggerPersistenceException() {
        Assertions.assertThatThrownBy(beanWithPersistenceInstance::get)
                .isInstanceOf(PersistenceException.class)
                .hasMessageContaining(MY_PU);
    }

}
