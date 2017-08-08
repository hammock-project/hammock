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

package ws.ament.hammock.security.internal;

import ws.ament.hammock.security.api.HasAllRoles;
import ws.ament.hammock.security.api.LoggedIn;
import ws.ament.hammock.security.api.Secured;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;

public class SecurityExtension implements Extension {

    void registerLoggedIn(@Observes @WithAnnotations(LoggedIn.class) ProcessAnnotatedType<?> pat) {
        if (pat.getAnnotatedType().isAnnotationPresent(LoggedIn.class)) {
            pat.configureAnnotatedType().methods().forEach(amc -> {
                amc.add(LoggedIn.INSTANCE);
                amc.add(Secured.INSTANCE);
            });
        } else {
            pat.configureAnnotatedType()
                    .filterMethods(m -> m.isAnnotationPresent(LoggedIn.class))
                    .forEach(amc -> {
                        amc.add(Secured.INSTANCE);
                    });
        }
    }

    void registerHasAllRoles(@Observes @WithAnnotations(HasAllRoles.class) ProcessAnnotatedType<?> pat) {
        if (pat.getAnnotatedType().isAnnotationPresent(HasAllRoles.class)) {
            HasAllRoles hasAllRoles = pat.getAnnotatedType().getAnnotation(HasAllRoles.class);
            pat.configureAnnotatedType().methods().forEach(amc -> {
                amc.add(hasAllRoles);
                amc.add(Secured.INSTANCE);
            });
        } else {
            pat.configureAnnotatedType()
                    .filterMethods(m -> m.isAnnotationPresent(HasAllRoles.class))
                    .forEach(amc -> {
                        amc.add(Secured.INSTANCE);
                    });
        }
    }

    void registerRolesAllowed(@Observes @WithAnnotations(RolesAllowed.class) ProcessAnnotatedType<?> pat) {
        if (pat.getAnnotatedType().isAnnotationPresent(RolesAllowed.class)) {
            RolesAllowed roles = pat.getAnnotatedType().getAnnotation(RolesAllowed.class);
            pat.configureAnnotatedType().methods().forEach(amc -> {
                amc.add(roles);
                amc.add(Secured.INSTANCE);
            });
        } else {
            pat.configureAnnotatedType()
                    .filterMethods(m -> m.isAnnotationPresent(RolesAllowed.class))
                    .forEach(amc -> {
                        amc.add(Secured.INSTANCE);
                    });
        }
    }
}
