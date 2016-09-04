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

package ws.ament.hammock.security.impl;

import ws.ament.hammock.security.api.HasAllRoles;
import ws.ament.hammock.security.api.Identity;
import ws.ament.hammock.security.api.MissingRolesException;
import ws.ament.hammock.security.internal.AnnotationUtil;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.Arrays;
import java.util.stream.Collectors;

@Interceptor
@HasAllRoles({})
@Priority(Interceptor.Priority.APPLICATION + 100)
public class HasAllRolesInterceptor {
    @Inject
    private Identity identity;

    @AroundInvoke
    public Object hasAllRoles(InvocationContext invocationContext) throws Exception {
        HasAllRoles hasAllRoles = AnnotationUtil.getAnnotation(invocationContext, HasAllRoles.class);
        String missingRoles = Arrays.stream(hasAllRoles.value())
                .filter(this::notHasRole)
                .collect(Collectors.joining(", "));
        if(missingRoles.isEmpty()) {
            return invocationContext.proceed();
        }
        else {
            throw new MissingRolesException(missingRoles);
        }
    }

    private boolean notHasRole(String role) {
        return !identity.hasRole(role);
    }
}
