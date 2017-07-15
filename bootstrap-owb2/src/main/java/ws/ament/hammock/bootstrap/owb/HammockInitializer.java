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

package ws.ament.hammock.bootstrap.owb;

import org.apache.openwebbeans.se.OWBInitializer;

import javax.enterprise.inject.Vetoed;
import javax.enterprise.inject.se.SeContainerInitializer;

@Vetoed
public class HammockInitializer extends OWBInitializer {
    private final HammockScannerService scannerService = new HammockScannerService();

    HammockInitializer() {
        super();
        scannerService.loader(loader);
    }

    @Override
    protected HammockScannerService getScannerService() {
        return scannerService;
    }

    @Override
    public SeContainerInitializer addBeanClasses(Class<?>... classes) {
        getScannerService().classes(classes);
        return this;
    }

    @Override
    public SeContainerInitializer addPackages(boolean scanRecursively, Package... packages) {
        getScannerService().packages(scanRecursively, packages);
        return this;
    }

    @Override
    public SeContainerInitializer addPackages(boolean scanRecursively, Class<?>... packageClasses) {
        getScannerService().packages(scanRecursively, packageClasses);
        return this;
    }

    @Override
    public SeContainerInitializer disableDiscovery() {
        getScannerService().disableAutoScanning();
        return this;
    }

    @Override
    public SeContainerInitializer setClassLoader(ClassLoader classLoader) {
        super.loader = classLoader;
        getScannerService().loader(super.loader);
        return this;
    }
}
