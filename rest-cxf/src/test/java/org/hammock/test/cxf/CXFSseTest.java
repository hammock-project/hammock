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

package org.hammock.test.cxf;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import ws.ament.hammock.test.support.EnableRandomWebServerPort;
import ws.ament.hammock.test.support.HammockArchive;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.sse.SseEventSource;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Arquillian.class)
@EnableRandomWebServerPort
public class CXFSseTest {
    @Deployment
    public static JavaArchive createArchive() {
        return new HammockArchive().classes(SseEventHandler.class, SseEventEndpoint.class).jar();
    }

    @ArquillianResource
    private URI uri;

    @Test
    public void testFireSseEventsAsync() throws InterruptedException {
        final WebTarget target = createWebTarget("/sse/" + UUID.randomUUID());
        final Collection<String> messages = new ArrayList<>();
        try (SseEventSource eventSource = SseEventSource.target(target).build()) {
            eventSource.register(e -> {
                System.out.println("New event...");
                messages.add(e.readData());
            }, System.out::println);
            eventSource.open();
            // wait for messages to come in

            if(messages.size() <= 4) {
                Thread.sleep(1000);
            }
        }

        messages.forEach(System.out::println);
        assertThat(messages).hasSize(4);
    }

    private WebTarget createWebTarget(final String url) {
        return ClientBuilder
                .newClient()
                .property("http.receive.timeout", 8000)
                .target(uri + url);
    }
}
