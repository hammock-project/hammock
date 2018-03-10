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

package ws.ament.hammock.security.impl;

import ws.ament.hammock.security.api.*;
import ws.ament.hammock.security.internal.AnnotationUtil;

import javax.annotation.Priority;
import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

@Interceptor
@Secured
@Priority(Interceptor.Priority.APPLICATION + 100)
public class SecurityInterceptor {
    @Inject
    private Identity identity;

    @AroundInvoke
    public Object verifyLoggedIn(InvocationContext invocationContext) throws Exception {
        checkLoggedIn(invocationContext);
        checkRoles(invocationContext);
        return invocationContext.proceed();
    }

    private void checkLoggedIn(InvocationContext invocationContext) {
        LoggedIn loggedIn = AnnotationUtil.getAnnotation(invocationContext, LoggedIn.class);
        DenyAll denyAll = AnnotationUtil.getAnnotation(invocationContext, DenyAll.class);
        if (loggedIn != null || denyAll != null) {
            if(!identity.isLoggedIn()) {
                throw new NotLoggedInException(identity+" Not logged in");
            }
        }
    }

    private void checkRoles(InvocationContext invocationContext) {
        HasAllRoles hasAllRoles = AnnotationUtil.getAnnotation(invocationContext, HasAllRoles.class);
        RolesAllowed rolesAllowed = AnnotationUtil.getAnnotation(invocationContext, RolesAllowed.class);
        List<String> roles = new ArrayList<>();
        if(hasAllRoles != null) {
            roles.addAll(asList(hasAllRoles.value()));
        }
        if(rolesAllowed != null) {
            roles.addAll(asList(rolesAllowed.value()));
        }
        if(!roles.isEmpty()) {
            String missingRoles = roles.stream()
                    .filter(this::notHasRole)
                    .collect(Collectors.joining(", "));
            if (!missingRoles.isEmpty()) {
                throw new MissingRolesException("Missing Roles: "+missingRoles);
            }
        }
    }

    private boolean notHasRole(String role) {
        return !identity.hasRole(role);
    }
}
