/*
 * Copyright 2017 Hammock and its contributors
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

package ws.ament.hammock.bootstrap.weld3.jpa;

import org.jboss.logging.Logger;
import org.jboss.weld.injection.spi.JpaInjectionServices;
import org.jboss.weld.injection.spi.ResourceReferenceFactory;

import javax.enterprise.inject.spi.InjectionPoint;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

/**
 * This implementation of {@link JpaInjectionServices} allows direct injection of persistence unit and persistence context
 * with JPA annotations in Weld container
 *
 * @author Antoine Sabot-Durand
 */
public class BasicJpaInjectionServices implements JpaInjectionServices {

    private static Logger LOG = Logger.getLogger(BasicJpaInjectionServices.class);

    @Override
    public ResourceReferenceFactory<EntityManager> registerPersistenceContextInjectionPoint(InjectionPoint ip) {
        PersistenceContext pc = ip.getAnnotated().getAnnotation(PersistenceContext.class);
        if (pc == null) {
            throw new IllegalArgumentException("No @PersistenceContext annotation found on EntityManager");
        }
        String name = pc.unitName();
        LOG.info("Creating EntityManagerReferenceFactory for unit " + name);
        return new EntityManagerReferenceFactory(name);
    }

    @Override
    public ResourceReferenceFactory<EntityManagerFactory> registerPersistenceUnitInjectionPoint(InjectionPoint ip) {
        PersistenceUnit pc = ip.getAnnotated().getAnnotation(PersistenceUnit.class);
        if (pc == null) {
            throw new IllegalArgumentException("No @PersistenceUnit annotation found on EntityManagerFactory");
        }
        String name = pc.unitName();
        LOG.info("Creating EntityManagerFactoryReferenceFactory for unit " + name);
        return new EntityManagerFactoryReferenceFactory(name);
    }

    @Override
    public EntityManager resolvePersistenceContext(InjectionPoint ip) {
        return registerPersistenceContextInjectionPoint(ip).createResource().getInstance();
    }

    @Override
    public EntityManagerFactory resolvePersistenceUnit(InjectionPoint ip) {
        return registerPersistenceUnitInjectionPoint(ip).createResource().getInstance();
    }

    @Override
    public void cleanup() {

    }
}
