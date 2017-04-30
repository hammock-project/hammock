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

package ws.ament.hammock.brave;

import ws.ament.hammock.HammockRuntime;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.client.Client;

@Path("/simple")
@RequestScoped
public class SimpleResource {
    @Inject
    private BraveCDIFeature braveCDIFeature;
    @Inject
    private HammockRuntime hammockRuntime;
    @Inject
    private Client client;
    @GET
    public String sayHello() {
        String requestURL = hammockRuntime.getMachineURL() + "/simple/second";
        client.target(requestURL).request().get();
        return "hello";
    }
    @GET
    @Path("/second")
    public String saySecond() {
        return "second";
    }
}
