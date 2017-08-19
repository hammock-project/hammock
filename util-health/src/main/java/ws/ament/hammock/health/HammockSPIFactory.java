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
import org.eclipse.microprofile.health.spi.SPIFactory;

import javax.enterprise.inject.Vetoed;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Vetoed
public class HammockSPIFactory implements SPIFactory {
    @Override
    public ResponseBuilder createResponseBuilder() {
        return new HammockResponseBuilder();
    }

    private static class HammockResponseBuilder extends ResponseBuilder {
        private String name;
        private Map<String, Object> attributes = new HashMap<>();

        @Override
        public ResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        @Override
        public ResponseBuilder withAttribute(String s, String s1) {
            attributes.put(s,s1);
            return this;
        }

        @Override
        public ResponseBuilder withAttribute(String s, long l) {
            attributes.put(s,l);
            return this;
        }

        @Override
        public ResponseBuilder withAttribute(String s, boolean b) {
            attributes.put(s,b);
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
            if(b) {
                return up();
            }
            else {
                return down();
            }
        }
    }

    private static final class HammockResponse extends Response {
        private final String name;
        private final State state;
        private final Map<String, Object> attributes;

        HammockResponse(String name, State state, Map<String, Object> attributes) {
            this.name = name;
            this.state = state;
            this.attributes = attributes;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public State getState() {
            return state;
        }

        @Override
        public Optional<Map<String, Object>> getAttributes() {
            return Optional.ofNullable(attributes);
        }
    }
}
