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

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.WithAnnotations;
import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: johndament
 * Date: 2/26/14
 * Time: 7:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClassScannerExtension implements Extension{
    private Set<Class> resources = new HashSet<Class>();
    private Set<Class> providers = new HashSet<Class>();
    public void watchPaths(@Observes @WithAnnotations(Path.class)ProcessAnnotatedType pat) {
        this.resources.add(pat.getAnnotatedType().getJavaClass());
    }
    public void watchProviders(@Observes @WithAnnotations(Provider.class) ProcessAnnotatedType pat) {
        this.providers.add(pat.getAnnotatedType().getJavaClass());
    }

    public Set<Class> getResources() {
        return resources;
    }

    public Set<Class> getProviders() {
        return providers;
    }
}
