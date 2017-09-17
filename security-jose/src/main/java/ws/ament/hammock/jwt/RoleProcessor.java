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

package ws.ament.hammock.jwt;

import ws.ament.hammock.security.api.GroupRolesMapper;

import javax.enterprise.inject.Vetoed;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@Vetoed
public class RoleProcessor {
    private final Collection<GroupRolesMapper> mappers;

    public RoleProcessor(Collection<GroupRolesMapper> mappers) {
        this.mappers = mappers;
    }

    public Set<String> parseRoles(Collection<String> existingRoles, Collection<String> groups) {
        Set<String> roles = new LinkedHashSet<>(existingRoles);
        if(mappers != null && groups != null && !mappers.isEmpty() && !groups.isEmpty()) {
            for(GroupRolesMapper mapper : mappers) {
                groups.stream().map(mapper::convertGroupsToRoles).forEach(roles::addAll);
            }
        }
        return roles;
    }
}
