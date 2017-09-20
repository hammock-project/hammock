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

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.OutboundSseEvent;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.function.BiConsumer;

@ApplicationScoped
public class SseEventHandler {
    public void sendEvents(@Observes SseEvent sseEvent) {
        String threadName = Thread.currentThread().getName();
        try {
            for (int i = 0; i < 4; i++) {

                final OutboundSseEvent.Builder builder = sseEvent.sse.newEventBuilder();
                String id = UUID.randomUUID().toString();
                String message = String.format("%s-%d-%d-%s", threadName, i, System.currentTimeMillis(), sseEvent.id);
                CompletionStage<?> stage = sseEvent.sink.send(createEvent(builder.name("message"), id, message));
                stage.whenComplete(new BiConsumer<Object, Throwable>() {
                    @Override
                    public void accept(Object o, Throwable t) {
                        System.out.println("Completed... "+o+" exception "+t);
                    }
                });
                try {
                    Thread.sleep(200);
                    System.out.println("Sleeping.... " + message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static OutboundSseEvent createEvent(final OutboundSseEvent.Builder builder, final String id, final String body) {
        return builder.id(id).data(body).mediaType(MediaType.APPLICATION_JSON_TYPE).build();
    }

}
