package org.hammock.test.jersey.sse;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;

import static javax.ws.rs.core.MediaType.SERVER_SENT_EVENTS;

@RequestScoped
@Path("/sse/")
public class SseEndpoint {
    @GET
    @Path("/{uuid}")
    @Produces(SERVER_SENT_EVENTS)
    public void doSseCall(@PathParam("uuid") String uuid, @Context SseEventSink sink, @Context Sse sse) {
        final OutboundSseEvent.Builder builder = sse.newEventBuilder();
        OutboundSseEvent event = builder.id(uuid)
                .data(SseModel.class, new SseModel("some model "+uuid))
                .build();
        sink.send(event);
        sink.close();
    }
}
