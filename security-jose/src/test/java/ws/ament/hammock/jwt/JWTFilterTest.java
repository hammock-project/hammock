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

package ws.ament.hammock.jwt;

import org.apache.commons.io.IOUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import ws.ament.hammock.test.support.EnableRandomWebServerPort;
import ws.ament.hammock.test.support.HammockArchive;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Arquillian.class)
@EnableRandomWebServerPort
@Ignore
public class JWTFilterTest {
    @Deployment
    public static JavaArchive createArchive() {
        return new HammockArchive().classes(DefaultServlet.class, RequiredLoggedIn.class).jar();
    }

    @ArquillianResource
    private URI uri;

    @Test
    public void shouldFindJWTInHeader() throws Exception{
        String body = given().auth().oauth2(createJWT()).get(uri)
                .then().statusCode(200).extract().body().asString().trim();
        assertThat(body).isEqualTo("admin");
    }

    @Test
    public void shouldFindJWTInQueryParam() throws Exception{
        String body = given().baseUri(uri.toString()).queryParam("access_token", createJWT()).get()
                .then().statusCode(200).extract().body().asString().trim();
        assertThat(body).isEqualTo("admin");
    }

    @Test
    public void shouldReturnNon200WhenNotLoggedIn() throws Exception {
        given().get(uri)
                .then().statusCode(401);
    }

    private String createJWT() throws IOException{
        try(InputStream stream = JWTFilterTest.class.getResourceAsStream("/samplejwt.txt")) {
            return IOUtils.toString(stream, Charset.forName("UTF-8"));
        }
    }
}
