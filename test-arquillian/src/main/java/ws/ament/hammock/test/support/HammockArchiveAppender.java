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

package ws.ament.hammock.test.support;

import org.eclipse.microprofile.config.spi.ConfigSource;
import org.jboss.arquillian.container.test.spi.client.deployment.ApplicationArchiveProcessor;
import org.jboss.arquillian.test.spi.TestClass;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

public class HammockArchiveAppender implements ApplicationArchiveProcessor {

    protected static final StringAsset BEANS_XML = new StringAsset("<beans version=\"1.1\" bean-discovery-mode=\"all\"/>");
    protected static final StringAsset TOMCAT_BASE = new StringAsset("tomcat.catalina.base=target/tomcat-work");

    @Override
    public void process(Archive<?> archive, TestClass testClass) {
        EnableRandomWebServerPort annotation = testClass.getJavaClass().getAnnotation(EnableRandomWebServerPort.class);
        JavaArchive jar = archive.as(JavaArchive.class)
           .addPackage("io.astefanutti.metrics.cdi")
           .addAsResource(TOMCAT_BASE, "hammock.properties")
           .addAsManifestResource(BEANS_XML, "beans.xml");

        if(annotation != null) {
            if (annotation.enableSecure()) {
                jar.addAsServiceProviderAndClasses(ConfigSource.class, RandomWebServerPort.class, RandomWebServerSecuredPort.class);
            } else {
                jar.addAsServiceProviderAndClasses(ConfigSource.class, RandomWebServerPort.class);
            }
        }
    }
}
