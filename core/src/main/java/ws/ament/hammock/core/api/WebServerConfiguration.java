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

package ws.ament.hammock.core.api;

import java.util.Collection;

/**
 * Represents configuration information for a webserver.
 *
 */
public interface WebServerConfiguration {
    /**
     * The port to listen on
     *
     * @return
     */
    public int getPort();

    /**
     * Context root
     * @return
     */
    public String getContextRoot();

    /**
     * The list of provider classes to register.
     * @return
     */
    public Collection<Class> getProviderClasses();

    /**
     * The list of resource classes to load.
     * @return
     */
    public Collection<Class> getResourceClasses();

    /**
     * The bind address this server.
     * @return
     */
    public String getBindAddress();
}
