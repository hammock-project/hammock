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

package ws.ament.hammock.web.tck;

import org.eclipse.microprofile.config.spi.ConfigSource;

import java.util.Map;

import static java.util.Collections.singletonMap;

import java.io.IOException;
import java.nio.file.Files;

public class HostFileDirTempConfigSource implements ConfigSource {

    private static final String FILE_DIR = "file.dir";
    private static String tempDir;

    public static String getTempDir() {
        if (tempDir == null) {
            try {
                tempDir = Files.createTempDirectory("junit-hammock").toString();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
        return tempDir;
    }

    @Override
    public int getOrdinal() {
        return Short.MAX_VALUE;
    }

    @Override
    public Map<String, String> getProperties() {
        return singletonMap(FILE_DIR, getTempDir());
    }

    @Override
    public String getValue(String key) {
        if (key.equals(FILE_DIR)) {
            return getTempDir();
        }
        return null;
    }

    @Override
    public String getName() {
        return "temp-file-dir";
    }
}
