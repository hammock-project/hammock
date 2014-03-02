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

package ws.ament.hammock.core.impl;

import ws.ament.hammock.core.annotations.ManagementResource;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.WithAnnotations;
import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;
import java.util.HashSet;
import java.util.Set;

/**
 * You can leverage the ClassScannerExtension to do lookup of paths, providers in your JAR Files that are bean archives.
 *
 * This makes the lookup of resources much easier, and done during deployment.
 */
public class ClassScannerExtension implements Extension{
    private Set<Class> resources = new HashSet<>();
    private Set<Class> providers = new HashSet<>();
    private Set<Class> managementResources = new HashSet<>();
    private Set<Class> managementProviders = new HashSet<>();
    public void watchPaths(@Observes @WithAnnotations(Path.class)ProcessAnnotatedType pat) {
        if(pat.getAnnotatedType().getJavaClass().isAnnotationPresent(ManagementResource.class)) {
            this.managementResources.add(pat.getAnnotatedType().getJavaClass());
        }
        else {
            this.resources.add(pat.getAnnotatedType().getJavaClass());
        }
    }
    public void watchProviders(@Observes @WithAnnotations(Provider.class) ProcessAnnotatedType pat) {
        if(pat.getAnnotatedType().getJavaClass().isAnnotationPresent(ManagementResource.class)) {
            this.managementProviders.add(pat.getAnnotatedType().getJavaClass());
        }
        else {
            this.providers.add(pat.getAnnotatedType().getJavaClass());
        }
    }

    public Set<Class> getResources() {
        return resources;
    }

    public Set<Class> getProviders() {
        return providers;
    }

    public Set<Class> getManagementResources() {
        return managementResources;
    }

    public Set<Class> getManagementProviders() {
        return managementProviders;
    }
}
