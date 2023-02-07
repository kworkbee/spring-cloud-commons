/*
 * Copyright 2012-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.loadbalancer.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.LoadBalancerLifecycle;
import org.springframework.cloud.loadbalancer.cache.LoadBalancerCacheManager;
import org.springframework.cloud.loadbalancer.cache.ZoneFailoverAwareCacheDataManager;
import org.springframework.cloud.loadbalancer.core.ZoneFailoverAwarenessLoadBalancerLifecycle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * An AutoConfiguration that enables {@link ZoneFailoverAwarenessLoadBalancerLifecycle} Bean
 * when Zone Failover Awareness Property is enabled.
 * @author Jiwon Jeon
 * @since ?
 */
@Configuration(proxyBeanMethods = false)
public class LoadBalancerLifecycleAutoConfiguration {

	@Bean
	@ConditionalOnBean(LoadBalancerCacheManager.class)
	@ConditionalOnProperty(value = "spring.cloud.loadbalancer.configurations", havingValue = "zone-failover-awareness")
	public LoadBalancerLifecycle zoneFailoverAwarenessLoadBalancerLifecycle(ZoneFailoverAwareCacheDataManager cacheDataManager) {
		return new ZoneFailoverAwarenessLoadBalancerLifecycle(cacheDataManager);
	}
}
