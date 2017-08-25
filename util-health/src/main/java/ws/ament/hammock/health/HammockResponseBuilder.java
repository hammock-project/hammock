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

package ws.ament.hammock.health;

import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;

import javax.enterprise.inject.Vetoed;
import java.util.HashMap;
import java.util.Map;

@Vetoed
class HammockResponseBuilder extends HealthCheckResponseBuilder {
    private String name;
    private Map<String, Object> data = new HashMap<>();
    private HealthCheckResponse.State state;

    @Override
    public HealthCheckResponseBuilder name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public HealthCheckResponseBuilder withData(String key, String value) {
        data.put(key, value);
        return this;
    }

    @Override
    public HealthCheckResponseBuilder withData(String key, long value) {
        data.put(key, value);
        return this;
    }

    @Override
    public HealthCheckResponseBuilder withData(String key, boolean value) {
        data.put(key, value);
        return this;
    }

    @Override
    public HealthCheckResponseBuilder up() {
        this.state = HealthCheckResponse.State.UP;
        return this;
    }

    @Override
    public HealthCheckResponseBuilder down() {
        this.state = HealthCheckResponse.State.DOWN;
        return this;
    }

    @Override
    public HealthCheckResponseBuilder state(boolean b) {
        if (b) {
            return up();
        } else {
            return down();
        }
    }

    @Override
    public HealthCheckResponse build() {
        return new HammockResponse(name, state, data);
    }
}
