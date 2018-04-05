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

package ws.ament.hammock.weld.probe;

import org.jboss.weld.probe.ProbeFilter;
import ws.ament.hammock.web.api.FilterDescriptor;
import ws.ament.hammock.web.base.Constants;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.servlet.annotation.WebInitParam;

@ApplicationScoped
public class WeldProbeComponents {
    @Produces
    @Dependent
    private FilterDescriptor weldProbe = new FilterDescriptor("WeldProbeFilter", new String[]{"/weld-probe/*"},
            new String[]{"/weld-probe/*"},
            Constants.DISPATCHER_TYPES,
            new WebInitParam[]{}, true, null, ProbeFilter.class);
}
