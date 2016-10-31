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

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.deltaspike.core.api.config.ConfigResolver;

public final class ConfigLoader {

   private ConfigLoader() {

   }

   public static Map<String, String> loadAllProperties(String prefix, boolean strip) {
      return ConfigResolver.getAllProperties()
         .entrySet().stream()
         .filter(e -> e.getKey().startsWith(prefix))
         .collect(Collectors.toMap(new PrefixStripper(strip, prefix), Map.Entry::getValue));
   }

   private static class PrefixStripper implements Function<Map.Entry<String, String>, String> {
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
      public String apply(Map.Entry<String, String> s) {
         return delegate.apply(s.getKey());
      }
   }
}
