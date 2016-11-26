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

package ws.ament.hammock.jpa;

import org.apache.deltaspike.core.api.config.ConfigResolver;

import static ws.ament.hammock.jpa.EntityManagerFactoryProvider.DEFAULT_EMF;

public class DefaultDataSourceBean extends DataSourceDelegateBean{

    DefaultDataSourceBean() {
        super(ConfigResolver.getPropertyValue("hammock.jpa.__default.datasource", DEFAULT_EMF),
                () -> new DataSourceDefinitionBuilder()
                .url(ConfigResolver.getPropertyValue("hammock.datasource.__default.url"))
                .user(ConfigResolver.getPropertyValue("hammock.datasource.__default.user"))
                .password(ConfigResolver.getPropertyValue("hammock.datasource.__default.password"))
                .name(ConfigResolver.getPropertyValue("hammock.jpa.__default.datasource", DEFAULT_EMF))
                .build()
                .getDataSource());
    }
}
