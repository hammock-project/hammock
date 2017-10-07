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

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Collections.emptyMap;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;

@ApplicationScoped
public class HealthCheckManager {
    private static final String REQUIRE_ANNOTATION = "hammock.health.require.annotation";
    private static final String OUTPUT_FORMAT = "hammock.health.output.format";
    @Inject
    @Any
    private Instance<HealthCheck> healthChecks;

    @Inject
    @ConfigProperty(name = REQUIRE_ANNOTATION, defaultValue = "false")
    private boolean requireAnnotation;

    @Inject
    @ConfigProperty(name = OUTPUT_FORMAT, defaultValue = "array")
    private String outputFormat;

    public HealthCheckModel performHealthChecks() {
        Instance<HealthCheck> healthChecks = this.healthChecks;
        if (requireAnnotation) {
            healthChecks = this.healthChecks.select(HealthLiteral.INSTANCE);
        }
        List<HealthCheck> healthCheckBeans = healthChecks.stream()
                .collect(toList());

        List<HealthResultModel> results = healthCheckBeans.stream()
                .map(HealthCheck::call)
                .map(r -> new HealthResultModel(r.getName(), r.getState().name(), r.getData().orElse(emptyMap())))
                .collect(toList());
        boolean anyDown = results.stream().anyMatch(r -> r.getState().equalsIgnoreCase(HealthCheckResponse.State.DOWN.name()));
        try {
            if (anyDown) {
                return toModel(HealthCheckResponse.State.DOWN, results);
            } else {
                return toModel(HealthCheckResponse.State.UP, results);
            }
        }
        finally {
            healthCheckBeans.forEach(healthChecks::destroy);
        }
    }

    private HealthCheckModel toModel(HealthCheckResponse.State state, List<HealthResultModel> healthResultModels) {
        if(outputFormat.equals("map")) {
            Map<String,HealthResultModel> data = healthResultModels.stream()
                    .collect(Collectors.toMap(HealthResultModel::getName, identity()));
            return new ObjectHealthCheckModel(state.name(),data);
        }
        else {
            return new ArrayHealthCheckModel(state.name(), healthResultModels);
        }
    }
}
