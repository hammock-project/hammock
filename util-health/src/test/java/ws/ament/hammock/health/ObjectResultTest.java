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

package ws.ament.hammock.health;

import org.apache.geronimo.config.ConfigImpl;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.junit.Test;
import org.junit.runner.RunWith;

import ws.ament.hammock.test.support.HammockArchive;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Arquillian.class)
public class ObjectResultTest {
    @Deployment
    public static Archive<?> jar() {
        return new HammockArchive()
                .classes(InjectedResponseBuilderTest.TestWebServer.class, InjectedCheck.class)
                .beansXmlEmpty()
                .jar()
                .addPackages(true, ConfigImpl.class.getPackage())
                .addAsManifestResource(new StringAsset("hammock.health.output.format=map"), "microprofile-config.properties");
    }

    @Inject
    private HealthCheckManager healthCheckManager;

    @Test
    public void shouldReturnObjectBasedCheck() {
        HealthCheckModel healthCheckModel = healthCheckManager.performHealthChecks();
        assertThat(healthCheckModel).isInstanceOf(ObjectHealthCheckModel.class);
        ObjectHealthCheckModel objectHealthCheckModel = (ObjectHealthCheckModel)healthCheckModel;
        assertThat(objectHealthCheckModel.getChecks()).containsKeys("InjectedCheck");
        HealthResultModel result = objectHealthCheckModel.getChecks().get("InjectedCheck");
        assertThat(result.getState()).isEqualTo(HealthCheckResponse.State.UP.name());
    }

}
