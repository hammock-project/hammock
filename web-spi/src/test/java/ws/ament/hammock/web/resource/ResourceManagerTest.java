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

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceManagerTest {
   private ResourceManager resourceManager = new ResourceManager("/META-INF/resources");
   @Test
   public void shouldLoadFromURI() throws Exception{
      assertThat(resourceManager.load("boop.txt")).isNotNull();
   }

   @Test
   public void shouldReturnNullWhenNotFound() throws Exception {
      assertThat(resourceManager.load("idontexist.xls")).isNull();
   }
}