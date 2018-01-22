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

package ws.ament.hammock.flexypool;

import com.vladmihalcea.flexypool.common.ConfigurationProperties;
import com.vladmihalcea.flexypool.metric.Histogram;
import com.vladmihalcea.flexypool.metric.Metrics;
import com.vladmihalcea.flexypool.metric.MetricsFactory;
import com.vladmihalcea.flexypool.metric.Timer;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class NoOpMetricsFactory implements MetricsFactory {
    @Override
    public Metrics newInstance(ConfigurationProperties configurationProperties) {
        return new NoOpMetrics();
    }

    private static class NoOpMetrics implements Metrics {

        @Override
        public Histogram histogram(String s) {
            return new Histogram() {
                @Override
                public void update(long l) {

                }
            };
        }

        @Override
        public Timer timer(String s) {
            return new Timer() {
                @Override
                public void update(long l, TimeUnit timeUnit) {

                }
            };
        }

        @Override
        public void start() {

        }

        @Override
        public void stop() {

        }
    }
}
