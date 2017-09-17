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

package ws.ament.hammock.mp.test;

import ws.ament.hammock.security.api.GroupRolesMapper;

import javax.enterprise.context.ApplicationScoped;
import java.util.Collection;

import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;

@ApplicationScoped
public class Group1MappedRoleMapper implements GroupRolesMapper {
    @Override
    public Collection<String> convertGroupsToRoles(String s) {
        if(s.equals("group1")) {
            return singleton("Group1MappedRole");
        }
        return emptySet();
    }
}
