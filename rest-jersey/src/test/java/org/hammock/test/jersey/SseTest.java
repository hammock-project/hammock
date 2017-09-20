package org.hammock.test.jersey;

import org.hammock.test.jersey.sse.SseEndpoint;
import org.hammock.test.jersey.sse.SseModel;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import ws.ament.hammock.johnzon.JohnzonExtension;
import ws.ament.hammock.test.support.EnableRandomWebServerPort;
import ws.ament.hammock.test.support.HammockArchive;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.SseEventSource;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertFalse;

@RunWith(Arquillian.class)
@EnableRandomWebServerPort
public class SseTest {

    @Deployment
    public static JavaArchive createArchive() {
        return new HammockArchive().classes(SseEndpoint.class, JohnzonExtension.class).jar();
    }

    @ArquillianResource
    private URI uri;

    @Test
    public void shouldBeAbleToRetrieveRestEndpoint() throws Exception {
        WebTarget target = ClientBuilder.newClient().register(JohnzonExtension.class).target(uri+"/sse/{uuid}").resolveTemplate("uuid", UUID.randomUUID().toString());
        List<SseModel> receivedModels = new ArrayList<>();
        try (SseEventSource eventSource = SseEventSource.target(target).build()) {
            eventSource.register(event -> {
                SseModel body = event.readData(SseModel.class, MediaType.APPLICATION_JSON_TYPE);
                System.out.println("Received "+body.getName());
                receivedModels.add(body);
            }, System.out::println);
            eventSource.open();
            // Give the SSE stream some time to collect all events
            Thread.sleep(1000);
        }
        assertFalse(receivedModels.isEmpty());
    }

}
