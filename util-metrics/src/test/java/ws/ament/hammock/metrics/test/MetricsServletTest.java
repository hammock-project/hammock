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

package ws.ament.hammock.metrics.test;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import ws.ament.hammock.metrics.MetricsConfig;
import ws.ament.hammock.metrics.MetricsServletProvider;
import ws.ament.hammock.test.support.EnableRandomWebServerPort;
import ws.ament.hammock.test.support.HammockArchive;

import java.net.URI;

import static io.restassured.RestAssured.get;

@RunWith(Arquillian.class)
@EnableRandomWebServerPort
public class MetricsServletTest {
    @Deployment
    public static JavaArchive createArchive() {
        return new HammockArchive().classes(MetricsConfig.class, MetricsServletProvider.class).jar();
    }

    @ArquillianResource
    private URI uri;

    @Test
    public void shouldHaveContentInMetrics() throws Exception {
        get(uri + "/metrics").then().assertThat().statusCode(200);
    }

    @Test
    public void shouldHaveContentInMetricsSubEndpoint() throws Exception {
        get(uri + "/metrics/metrics").then().assertThat().statusCode(200);
    }
}
