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

package ws.ament.hammock.web.tomcat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.jboss.arquillian.core.spi.LoadableExtension;

public class SecureTomcatArquillianExtension implements LoadableExtension {
    @Override
    public void register(ExtensionBuilder extensionBuilder) {
        File baseDir = new File("target/tomcat-work");
        if (!baseDir.exists()) {
            baseDir.mkdir(); // only auto create work folder.
        }
        File keystore = new File(baseDir, "keystore.jks");
        if (keystore.exists()) {
            return; // already copyed
        }
        
        File keystoreSource = new File("src/test/keystore.jks");
        try {
            Files.copy(keystoreSource.toPath(), keystore.toPath());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
