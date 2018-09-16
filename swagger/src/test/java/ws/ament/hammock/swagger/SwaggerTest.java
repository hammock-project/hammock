/*
 * Copyright 2018 Hammock and its contributors
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

package ws.ament.hammock.swagger;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import ws.ament.hammock.test.support.EnableRandomWebServerPort;
import ws.ament.hammock.test.support.HammockArchive;

import java.net.URI;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@RunWith(Arquillian.class)
@EnableRandomWebServerPort
public class SwaggerTest {
    @Deployment
    public static JavaArchive createArchive() {
        return new HammockArchive().jar()
                .addPackages(true, "io.swagger.v3.jaxrs2");
    }

    @ArquillianResource
    private URI uri;

    @Test
    public void shouldBeAbleToRetrieveOpenApiDefinitionsHeaderJson() throws InterruptedException {
        given().accept("application/json").
        when().get(uri + "/openapi").
        then().contentType("application/json")
                .assertThat().statusCode(200)
                .body("openapi", is("3.0.1"));
    }

    @Test
    public void shouldBeAbleToRetrieveOpenApiDefinitionsHeaderYaml() throws InterruptedException {
        given().accept("application/yaml").
        when().get(uri + "/openapi").
        then().contentType("application/yaml")
            .assertThat().statusCode(200);
    }

    @Test
    public void shouldBeAbleToRetrieveOpenApiDefinitionsPathJson() throws InterruptedException {
        given().
                when().get(uri + "/openapi.json").
                then().contentType("application/json")
                .assertThat().statusCode(200)
                .body("openapi", is("3.0.1"));
    }

    @Test
    public void shouldBeAbleToRetrieveOpenApiDefinitionsPathYaml() throws InterruptedException {
        given().
                when().get(uri + "/openapi.yaml").
                then().contentType("application/yaml")
                .assertThat().statusCode(200);
    }
}
