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

package ws.ament.hammock.camel.test;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import ws.ament.hammock.camel.servlet.HammockCamelServlet;
import ws.ament.hammock.test.support.EnableRandomWebServerPort;
import ws.ament.hammock.test.support.HammockArchive;

import javax.inject.Inject;

import static io.restassured.RestAssured.get;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;

@RunWith(Arquillian.class)
@EnableRandomWebServerPort
public class ServletRoutesTest {
    @Deployment
    public static JavaArchive createDeployment() {
        return new HammockArchive()
                .classes(HammockCamelServlet.class, ServletRoutesTest.class, TestCamelRoutes.class, BasicProcessor.class)
                .beansXmlEmpty()
                .jar();
    }


    @ArquillianResource
    private URI uri;

    @Inject
    private BasicProcessor basicProcessor;

    @Test
    public void doSomething() throws Exception{
        get(uri + "/camel/hello/").then().statusCode(200);
        assertThat(basicProcessor.getHits()).isEqualTo(1);
    }
}
