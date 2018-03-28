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

package ws.ament.hammock.jwt.processor;

import org.apache.commons.io.IOUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import ws.ament.hammock.jwt.JWTException;
import ws.ament.hammock.test.support.EnableRandomWebServerPort;
import ws.ament.hammock.test.support.HammockArchive;

import javax.inject.Inject;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(Arquillian.class)
@EnableRandomWebServerPort
public class DefaultValidatingJWTProcessorTest {

    @Deployment
    public static JavaArchive createDeployment() {
        return new HammockArchive().classes().jar();
    }

    @Inject
    private DefaultValidatingJWTProcessor defaultValidatingJWTProcessor;

    @Test
    public void shouldNotAcceptOutOfDateJWT() throws Exception {
        try (InputStream stream = DefaultValidatingJWTProcessorTest.class.getResourceAsStream("/samplejwt.txt")) {
            String jwt = IOUtils.toString(stream, "UTF-8");
            assertThatThrownBy(() -> defaultValidatingJWTProcessor.process(jwt))
                    .isInstanceOf(JWTException.class)
                    .hasMessage("Unable to parse jwt");
        }
    }
}