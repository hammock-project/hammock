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

package ws.ament.hammock.mp.cochise.test;

import org.jboss.arquillian.container.test.spi.client.deployment.ApplicationArchiveProcessor;
import org.jboss.arquillian.test.spi.TestClass;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public class HammockArchiveAppender implements ApplicationArchiveProcessor {
    private static final StringAsset BEANS_XML = new StringAsset("<beans version=\"1.1\" bean-discovery-mode=\"all\"/>");
    @Override
    public void process(Archive<?> archive, TestClass testClass) {
        if (archive instanceof WebArchive) {
            JavaArchive jar = ShrinkWrap.create(JavaArchive.class, "hammock.jar")
                    .addPackages(true, "ws.ament.hammock")
                    .addPackage("io.astefanutti.metrics.cdi")
                    .addAsManifestResource(BEANS_XML, "beans.xml");
            archive.as(WebArchive.class).addAsLibrary(jar);
        } else {
            archive.as(JavaArchive.class).addPackages(true, "ws.ament.hammock");
        }
    }
}
