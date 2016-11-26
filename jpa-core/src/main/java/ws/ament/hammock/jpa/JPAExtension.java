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

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;
import javax.persistence.Entity;
import javax.persistence.spi.PersistenceUnitInfo;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableSet;

public class JPAExtension implements Extension {
   private final Set<String> entityClasses = new LinkedHashSet<>();
   private final Set<Bean<PersistenceUnitInfo>> persistenceUnitInfoBeans = new LinkedHashSet<>();
   private Map<String, PersistenceUnitInfo> persistenceUnitInfos;
   public void findEntities(@Observes @WithAnnotations(Entity.class)ProcessAnnotatedType<?> pat) {
      entityClasses.add(pat.getAnnotatedType().getJavaClass().getName());
   }
   public void locatePersistenceUnits(@Observes ProcessBean<PersistenceUnitInfo> processBean) {
      persistenceUnitInfoBeans.add(processBean.getBean());
   }
   public void load(@Observes final AfterDeploymentValidation event, final BeanManager beanManager) {
      persistenceUnitInfos = persistenceUnitInfoBeans.stream().map(bean -> (PersistenceUnitInfo)beanManager.getReference(
              bean, bean.getBeanClass(), beanManager.createCreationalContext(bean)
      )).collect(Collectors.toMap(PersistenceUnitInfo::getPersistenceUnitName, Function.identity()));
   }

   public Set<String> getEntityClasses() {
      return unmodifiableSet(entityClasses);
   }

   public PersistenceUnitInfo getPersistnceUnitInfo(String name) {
      return persistenceUnitInfos.get(name);
   }
}
