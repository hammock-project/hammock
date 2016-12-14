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

import com.codahale.metrics.MetricRegistry;
import com.rabbitmq.client.MetricsCollector;
import com.rabbitmq.client.impl.StandardMetricsCollector;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionTarget;
import java.util.Set;

final class StandardMetricsCollectorBean extends MetricCollectorBean {
    private final BeanManager beanManager;
    StandardMetricsCollectorBean(InjectionTarget<? extends MetricsCollector> injectionTarget, BeanManager beanManager) {
        super(injectionTarget);
        this.beanManager = beanManager;
    }

    @Override
    public Class<?> getBeanClass() {
        return StandardMetricsCollector.class;
    }

    @Override
    public MetricsCollector create(CreationalContext<MetricsCollector> creationalContext) {
        Set<Bean<?>> beans = beanManager.getBeans(MetricRegistry.class);
        Bean<?> bean = beanManager.resolve(beans);
        CreationalContext<?> metricsContext = beanManager.createCreationalContext(bean);
        MetricRegistry metricRegistry = (MetricRegistry)beanManager
                .getReference(bean, MetricRegistry.class, metricsContext);
        return new StandardMetricsCollector(metricRegistry);
    }
}
