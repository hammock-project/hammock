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
import java.util.Map;
import java.util.Optional;

@Vetoed
public class HammockSPIFactory implements SPIFactory {
    @Override
    public ResponseBuilder createResponseBuilder() {
        return new HammockResponseBuilder();
    }

}