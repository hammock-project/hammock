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

import javax.enterprise.context.Dependent;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.eclipse.microprofile.config.ConfigProvider;
import org.flywaydb.core.Flyway;
import org.jboss.logging.Logger;
import ws.ament.hammock.core.config.ConfigLoader;

import static java.util.Collections.emptySet;

public class FlywayBean implements Bean<Flyway> {
   private static Logger LOG = Logger.getLogger(FlywayBean.class);
   private final Flyway flyway;

   FlywayBean() {
      Map<String, String> properties = ConfigLoader.loadAllProperties("flyway", false);
      this.flyway = new Flyway();
      Properties props = new Properties();
      props.putAll(properties);
      flyway.configure(props);
      this.postCreate();
   }

   private void postCreate() {
      String executions = ConfigProvider.getConfig().getOptionalValue("flyway.execute",String.class)
              .orElse("migrate");
      String[] methods = executions.split(",");
      for(String method :methods) {
         switch (method.toLowerCase()) {
            case "repair":
               flyway.repair();
               break;
            case "migrate":
               flyway.migrate();
               break;
            case "clean":
               flyway.clean();
               break;
            case "validate":
               flyway.validate();
               break;
            case "baseline":
               flyway.baseline();
               break;
            default:
               LOG.warn("Invalid callback method "+method);
               break;
         }
      }
   }

   @Override
   public Class<?> getBeanClass() {
      return Flyway.class;
   }

   @Override
   public Set<InjectionPoint> getInjectionPoints() {
      return emptySet();
   }

   @Override
   public boolean isNullable() {
      return false;
   }

   @Override
   public Flyway create(CreationalContext<Flyway> creationalContext) {
      return flyway;
   }

   @Override
   public void destroy(Flyway flyway, CreationalContext<Flyway> creationalContext) {

   }

   @Override
   public Set<Type> getTypes() {
      Set<Type> types = new HashSet<>();
      types.add(Flyway.class);
      types.add(Object.class);
      return types;
   }

   @Override
   public Set<Annotation> getQualifiers() {
      return emptySet();
   }

   @Override
   public Class<? extends Annotation> getScope() {
      return Dependent.class;
   }

   @Override
   public String getName() {
      return "flyway";
   }

   @Override
   public Set<Class<? extends Annotation>> getStereotypes() {
      return emptySet();
   }

   @Override
   public boolean isAlternative() {
      return false;
   }
}
