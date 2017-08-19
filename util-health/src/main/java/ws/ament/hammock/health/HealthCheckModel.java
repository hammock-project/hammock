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

import java.util.Collections;
import java.util.List;

public class HealthCheckModel {
    private final String outcome;
    private final List<HealthResultModel> checks;

    public HealthCheckModel(String outcome, List<HealthResultModel> checks) {
        this.outcome = outcome;
        this.checks = checks;
    }

    public String getOutcome() {
        return outcome;
    }

    public List<HealthResultModel> getChecks() {
        return Collections.unmodifiableList(checks);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HealthCheckModel{");
        sb.append("outcome='").append(outcome).append('\'');
        sb.append(", checks=").append(checks);
        sb.append('}');
        return sb.toString();
    }
}
