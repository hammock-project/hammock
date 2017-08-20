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

import org.eclipse.microprofile.health.Response;
import org.eclipse.microprofile.health.ResponseBuilder;

import javax.enterprise.inject.Vetoed;
import java.util.HashMap;
import java.util.Map;

@Vetoed
class HammockResponseBuilder extends ResponseBuilder {
    private String name;
    private Map<String, Object> attributes = new HashMap<>();

    @Override
    public ResponseBuilder name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public ResponseBuilder withAttribute(String key, String value) {
        attributes.put(key, value);
        return this;
    }

    @Override
    public ResponseBuilder withAttribute(String key, long value) {
        attributes.put(key, value);
        return this;
    }

    @Override
    public ResponseBuilder withAttribute(String key, boolean value) {
        attributes.put(key, value);
        return this;
    }

    @Override
    public Response up() {
        return new HammockResponse(name, Response.State.UP, attributes);
    }

    @Override
    public Response down() {
        return new HammockResponse(name, Response.State.DOWN, attributes);
    }

    @Override
    public Response state(boolean b) {
        if (b) {
            return up();
        } else {
            return down();
        }
    }
}
