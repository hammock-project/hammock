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

package org.hammock.rest.tck;

import org.eclipse.microprofile.config.spi.ConfigSource;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import ws.ament.hammock.test.support.HammockArchive;
import ws.ament.hammock.test.support.RandomWebServerPort;

import java.net.URI;

import static io.restassured.RestAssured.get;
import static org.hamcrest.CoreMatchers.is;

@RunWith(Arquillian.class)
public class PropertyURITest {
    @Deployment
    public static JavaArchive createArchive() {
        return new HammockArchive().classes(RestController.class).jar()
                .addAsServiceProviderAndClasses(ConfigSource.class, URIConfigSource.class,
                        RandomWebServerPort .class);
    }

    @ArquillianResource
    private URI uri;

    @Test
    public void shouldBeAbleToRetrieveRestEndpoint() throws Exception {
        get(uri + "/custom-uri/rest").then().assertThat().statusCode(200)
                .body(is("Hello, World!"));

        get(uri + "/rest").then().assertThat().statusCode(404);
    }
}
