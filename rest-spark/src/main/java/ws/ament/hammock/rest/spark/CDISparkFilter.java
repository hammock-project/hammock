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

package ws.ament.hammock.rest.spark;

import spark.servlet.SparkApplication;
import spark.servlet.SparkFilter;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import java.util.ArrayList;
import java.util.List;

@Singleton
@WebFilter(urlPatterns = {"${spark.filter}"},filterName = "Spark")
public class CDISparkFilter extends SparkFilter{
    @Inject
    @Any
    private Instance<SparkApplication> sparkApplications;

    @Override
    protected SparkApplication[] getApplications(FilterConfig filterConfig) throws ServletException {
        List<SparkApplication> applicationList = new ArrayList<>();
        for(SparkApplication sparkApplication : sparkApplications) {
            applicationList.add(sparkApplication);
        }
        return applicationList.toArray(new SparkApplication[applicationList.size()]);
    }
}
