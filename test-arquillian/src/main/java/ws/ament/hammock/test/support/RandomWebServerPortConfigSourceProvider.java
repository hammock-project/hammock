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

package ws.ament.hammock.test.support;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.deltaspike.core.spi.config.ConfigSource;
import org.apache.deltaspike.core.spi.config.ConfigSourceProvider;

import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;

public class RandomWebServerPortConfigSourceProvider implements ConfigSourceProvider {

   private static final String WEBSERVER_PORT = "webserver.port";

   @Override
   public List<ConfigSource> getConfigSources() {
      return singletonList(new RandomPortConfigSource());
   }

   private final class RandomPortConfigSource implements ConfigSource {
      private final String port;

      RandomPortConfigSource() {
         Random random = new Random();
         this.port = Integer.toString(4000 + random.nextInt(500));
      }
      @Override
      public int getOrdinal() {
         return 10000;
      }

      @Override
      public Map<String, String> getProperties() {
         return singletonMap(WEBSERVER_PORT, port);
      }

      @Override
      public String getPropertyValue(String key) {
         if(key.equals(WEBSERVER_PORT)) {
            return port;
         }
         return null;
      }

      @Override
      public String getConfigName() {
         return "random-port";
      }

      @Override
      public boolean isScannable() {
         return true;
      }
   }
}
