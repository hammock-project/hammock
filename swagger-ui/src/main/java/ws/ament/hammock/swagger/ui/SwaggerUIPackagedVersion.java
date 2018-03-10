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

package ws.ament.hammock.swagger.ui;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Optional;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class SwaggerUIPackagedVersion {

    private static final String MANIFEST_RESOURCES = "META-INF/MANIFEST.MF";
    private static final String MANIFEST_SECTION = "Hammock-Swagger-UI";
    private static final String PACKAGED_VERSION = "PackagedVersion";
    private static final String UNKNOWN_VERSION = "0.0.0";

    public static String find() {
        return new SwaggerUIPackagedVersion().findPackagedVersion();
    }

    public String findPackagedVersion() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = this.getClass().getClassLoader();
        }
        try {
            return scanManifests(cl);
        } catch (IOException e) {
            return UNKNOWN_VERSION;
        }
    }

    private String scanManifests(ClassLoader cl) throws IOException {
        Enumeration<URL> infos = cl.getResources(MANIFEST_RESOURCES);
        while (infos.hasMoreElements()) {
            Optional<String> version = parseManifest(infos.nextElement());
            if (version.isPresent()) {
                return version.get();
            }
        }
        return UNKNOWN_VERSION;
    }

    private Optional<String> parseManifest(URL infoResource) {
        try {
            InputStream in = infoResource.openStream();
            if (in == null) {
                return Optional.empty();
            }
            Manifest m = new Manifest();
            try {
                m.read(in);
            } finally {
                in.close();
            }
            Attributes sectionAttributes = m.getEntries().get(MANIFEST_SECTION);
            if (sectionAttributes == null) {
                return Optional.empty();
            }
            return Optional.ofNullable(sectionAttributes.getValue(PACKAGED_VERSION));
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
