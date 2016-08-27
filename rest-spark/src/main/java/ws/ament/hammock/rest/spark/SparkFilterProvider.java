/*
 * Copyright 2016 John D. Ament
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

package ws.ament.hammock.rest.spark;

import ws.ament.hammock.web.spi.FilterDescriptor;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.servlet.DispatcherType;
import javax.servlet.annotation.WebInitParam;

@ApplicationScoped
public class SparkFilterProvider {
    private static final String[] PATTERN = new String[]{"/*"};
    private static final DispatcherType[] DISPATCHER_TYPES = new DispatcherType[]{DispatcherType.FORWARD,
            DispatcherType.ASYNC,DispatcherType.REQUEST};

    @Produces
    private FilterDescriptor filterDescriptor = new FilterDescriptor("Spark",PATTERN, PATTERN,DISPATCHER_TYPES
            ,new WebInitParam[]{},true,new String[]{}, CDISparkFilter.class);

}
