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

package ws.ament.hammock.core.config;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public final class ConfigLoader {

   private ConfigLoader() {

   }

   public static Map<String, String> loadAllProperties(String prefix, boolean strip) {
      Config config = ConfigProvider.getConfig();

      Iterable<String> propertyNames = config.getPropertyNames();

      return StreamSupport.stream(propertyNames.spliterator(), false)
         .filter(e -> e.startsWith(prefix))
         .collect(Collectors.toMap(
                 new PrefixStripper(strip, prefix),
                 s -> config.getValue(s, String.class)));
   }

   private static class PrefixStripper implements Function<String, String> {
      private String toRemove;
      private Function<String, String> delegate;

      private PrefixStripper(boolean strip, String prefix) {
         if(!strip) {
            delegate = s -> s;
         }
         else {
            toRemove = prefix + ".";
            this.delegate = s -> s.replaceFirst(toRemove, "");
         }
      }

      @Override
      public String apply(String s) {
         return delegate.apply(s);
      }
   }
}
