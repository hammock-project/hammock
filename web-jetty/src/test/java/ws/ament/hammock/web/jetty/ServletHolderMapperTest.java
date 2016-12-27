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

package ws.ament.hammock.web.jetty;

import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlet.Source;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ws.ament.hammock.web.api.ServletDescriptor;
import ws.ament.hammock.web.spi.WebParam;
import ws.ament.hammock.web.tck.DefaultServlet;

import javax.servlet.annotation.WebInitParam;

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ServletHolderMapperTest {
    @Mock
    private ServletHandler servletHandler;

    @InjectMocks
    private ServletHolderMapper mapper;

    @Test
    public void shouldCreateServletHolder() {
        when(servletHandler.newServletHolder(Source.EMBEDDED)).thenReturn(new ServletHolder(Source.EMBEDDED));
        ServletDescriptor servletDescriptor = new ServletDescriptor("name", new String[]{"uri"}, new String[]{"uri"},
                1,null,true, DefaultServlet.class);
        ServletHolder holder = mapper.apply(servletDescriptor);
        assertThat(holder.getName()).isEqualTo("name");
        assertThat(holder.getClassName()).isEqualTo(DefaultServlet.class.getName());
        verify(servletHandler).addServletWithMapping(holder, "uri");
    }

    @Test
    public void shouldCreateServletHolderWithParams() {
        when(servletHandler.newServletHolder(Source.EMBEDDED)).thenReturn(new ServletHolder(Source.EMBEDDED));
        ServletDescriptor servletDescriptor = new ServletDescriptor("name", new String[]{"uri"}, new String[]{"uri"},
                1,new WebInitParam[]{new WebParam("key","value")},true, DefaultServlet.class);
        ServletHolder holder = mapper.apply(servletDescriptor);
        assertThat(holder.getInitParameters()).isEqualTo(singletonMap("key","value"));
    }
}