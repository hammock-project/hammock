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

package ws.ament.hammock.jaxrs;

import ws.ament.hammock.annotations.Configuring;
import ws.ament.hammock.annotations.Disposing;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

@ApplicationScoped
public class JAXRSClientProvider {
    @Inject
    private Event<Client> clientEvent;
    @Produces
    @Dependent
    public Client createClient() {
        Client client = ClientBuilder.newClient();
        clientEvent.select(Configuring.INSTANCE).fire(client);
        return client;
    }

    public void closeClient(@Disposes Client client) {
        clientEvent.select(Disposing.INSTANCE).fire(client);
        client.close();
    }
}
