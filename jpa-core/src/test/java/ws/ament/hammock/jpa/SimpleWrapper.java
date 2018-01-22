/*
 * Copyright 2018 Hammock and its contributors
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

package ws.ament.hammock.jpa;

import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;

@ApplicationScoped
public class SimpleWrapper implements DataSourceWrapper{
    private boolean invoked = false;

    @Override
    public DataSource wrap(String name, DataSource dataSource) {
        invoked = true;
        return dataSource;
    }

    public boolean isInvoked() {
        return invoked;
    }
}
