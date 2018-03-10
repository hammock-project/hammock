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

package ws.ament.hammock.web.cors;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CORSFilterTest {

   @Mock
   private HttpServletResponse response;

   @Mock
   private CORSConfiguration corsConfiguration;

   @InjectMocks
   private CORSFilter corsFilter;

   @Test
   public void shouldNotAddHeadersWhenNotEnabled() {
      when(corsConfiguration.isEnabled()).thenReturn(false);

      corsFilter.doFilter(null, response);

      verify(response, never()).addHeader(anyString(), anyString());
   }

   @Test
   public void shouldAddCORSHeadersWhenEnabled() {
      when(corsConfiguration.isEnabled()).thenReturn(true);
      when(corsConfiguration.getOrigin()).thenReturn("origin");
      when(corsConfiguration.getHeaders()).thenReturn("headers");
      when(corsConfiguration.getCredentials()).thenReturn("credentials");
      when(corsConfiguration.getMethods()).thenReturn("methods");
      when(corsConfiguration.getMaxAge()).thenReturn("maxAge");

      corsFilter.doFilter(null, response);

      verify(response).addHeader("Access-Control-Allow-Origin", corsConfiguration.getOrigin());
      verify(response).addHeader("Access-Control-Allow-Headers", corsConfiguration.getHeaders());
      verify(response).addHeader("Access-Control-Allow-Credentials", corsConfiguration.getCredentials());
      verify(response).addHeader("Access-Control-Allow-Methods", corsConfiguration.getMethods());
      verify(response).addHeader("Access-Control-Max-Age", corsConfiguration.getMaxAge());
   }
}