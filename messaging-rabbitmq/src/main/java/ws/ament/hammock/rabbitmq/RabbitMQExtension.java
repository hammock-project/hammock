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

package ws.ament.hammock.rabbitmq;

import com.rabbitmq.client.MetricsCollector;
import com.rabbitmq.client.NoOpMetricsCollector;
import com.rabbitmq.client.impl.StandardMetricsCollector;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;

public class RabbitMQExtension implements Extension{
    private boolean foundMetricCollectorBean = false;

    public void findMetrics(@Observes ProcessBean<? extends MetricsCollector> metricsCollectorBean) {
        this.foundMetricCollectorBean = true;
    }

    public void addMetricsBean(@Observes AfterBeanDiscovery afterBeanDiscovery, BeanManager beanManager) {
        if(!foundMetricCollectorBean) {
            try{
                Class.forName("com.codahale.metrics.MetricRegistry");
                final AnnotatedType<StandardMetricsCollector> annotatedType = beanManager.createAnnotatedType(StandardMetricsCollector.class);
                afterBeanDiscovery.addBean(new StandardMetricsCollectorBean(beanManager.createInjectionTarget(annotatedType), beanManager));
            }
            catch (Exception e) {
                final AnnotatedType<NoOpMetricsCollector> annotatedType = beanManager.createAnnotatedType(NoOpMetricsCollector.class);
                afterBeanDiscovery.addBean(new NoOpMetricCollectorBean(beanManager.createInjectionTarget(annotatedType)));
            }
        }
    }
}
