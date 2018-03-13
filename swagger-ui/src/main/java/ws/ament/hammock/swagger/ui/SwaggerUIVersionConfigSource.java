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

import static java.util.Collections.singletonMap;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.Optional;
import java.util.jar.Manifest;

import org.eclipse.microprofile.config.spi.ConfigSource;

public class SwaggerUIVersionConfigSource implements ConfigSource {
    private static final String CONFIG_SOURCE_NAME = "packaged-swagger-ui-version";
    private static final String MANIFEST_RESOURCES = "META-INF/MANIFEST.MF";
    private static final String BUNDLE_NAME_VALUE = "Swagger UI";
    private static final String BUNDLE_NAME = "Bundle-Name";
    private static final String BUNDLE_VERSION = "Bundle-Version";
    private static final String UNKNOWN_VERSION = "0.0.0";
    private static final String SWAGGER_UI_VERSION = "swagger-ui.version";
    private final Map<String, String> properties = singletonMap(SWAGGER_UI_VERSION, findPackagedVersion());

    @Override
    public int getOrdinal() {
        return 1;
    }

    @Override
    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public String getValue(String key) {
        return properties.get(key);
    }

    @Override
    public String getName() {
        return CONFIG_SOURCE_NAME;
    }

    private String findPackagedVersion() {
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
            String bundleName = m.getMainAttributes().getValue(BUNDLE_NAME);
            if (!BUNDLE_NAME_VALUE.equals(bundleName)) {
                return Optional.empty();
            }
            return Optional.ofNullable(m.getMainAttributes().getValue(BUNDLE_VERSION));
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
