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

package ws.ament.hammock.web.resource;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.annotation.WebInitParam;
import java.io.InputStream;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import ws.ament.hammock.web.api.FilterDescriptor;

import static ws.ament.hammock.web.base.Constants.DISPATCHER_TYPES;

@ApplicationScoped
public class ResourceManager {

   public static final String SLASH = "/";
   private String cleanBaseUri;

   ResourceManager() {

   }

   @Inject
   public ResourceManager( @ConfigProperty(name = "static.resource.location", defaultValue = "META-INF/resources/")
               String baseUri) {
      this();
      String cleanBaseUri = baseUri;
      if(cleanBaseUri.startsWith(SLASH)) {
         cleanBaseUri = cleanBaseUri.replaceFirst(SLASH, "");
      }
      if(!cleanBaseUri.endsWith(SLASH)) {
         cleanBaseUri = cleanBaseUri + SLASH;
      }
      this.cleanBaseUri = cleanBaseUri;
   }

   InputStream load(String path) {
      if(path.startsWith(SLASH)) {
         path = path.replaceFirst(SLASH, "");
      }
      return ResourceManager.class.getClassLoader().getResourceAsStream( cleanBaseUri + path);
   }

   @Produces
   @Dependent
   private FilterDescriptor resourceFilter = new FilterDescriptor("ResourceFilter", new String[]{"/*"}, new String[]{"/*"},
        DISPATCHER_TYPES, new WebInitParam[]{}, true, null, ResourceRenderFilter.class);
}
