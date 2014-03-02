/*
 * Copyright 2014 John D. Ament
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.
 *
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package ws.ament.hammock.test;

import ws.ament.hammock.core.annotations.ManagementConfig;
import ws.ament.hammock.core.api.WebServerConfiguration;
import ws.ament.hammock.core.impl.ClassScannerExtension;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collection;

/**
 * A test config for management port.
 */
@ManagementConfig
@ApplicationScoped
public class ManagementConfigBean implements WebServerConfiguration{
    private int port;
    @Inject
    private ClassScannerExtension classScannerExtension;
    @PostConstruct
    public void init() {
        this.port = (int)(Math.random()* 4000 + 1005);
    }
    @Override
    public int getPort() {
        return this.port;
    }

    @Override
    public String getContextRoot() {
        return "/mgmt";
    }

    @Override
    public Collection<Class> getProviderClasses() {
        return classScannerExtension.getManagementProviders();
    }

    @Override
    public Collection<Class> getResourceClasses() {
        return classScannerExtension.getManagementResources();
    }

    @Override
    public String getBindAddress() {
        return "127.0.0.1";
    }
}
