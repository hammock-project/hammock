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

import java.util.Map;

public class HealthResultModel {
    private final String id;
    private final String result;
    private final Map<String, Object> data;

    public HealthResultModel(String id, String result, Map<String, Object> data) {
        this.id = id;
        this.result = result;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public String getResult() {
        return result;
    }

    public Map<String, Object> getData() {
        return data;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HealthResultModel{");
        sb.append("id='").append(id).append('\'');
        sb.append(", result='").append(result).append('\'');
        sb.append(", data=").append(data);
        sb.append('}');
        return sb.toString();
    }
}
