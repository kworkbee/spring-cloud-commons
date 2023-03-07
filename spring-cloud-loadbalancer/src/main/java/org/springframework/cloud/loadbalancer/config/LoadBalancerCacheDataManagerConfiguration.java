/*
 * Copyright 2013-2020 the original author or authors.
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

import com.github.benmanes.caffeine.cache.Caffeine;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cloud.loadbalancer.cache.LoadBalancerCacheDataManager;
import org.springframework.cloud.loadbalancer.cache.LoadBalancerCacheManager;
import org.springframework.cloud.loadbalancer.cache.ZoneFailoverAwarenessLoadBalancerCacheDataManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 *
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(LoadBalancerCacheAutoConfiguration.class)
@AutoConfigureBefore(LoadBalancerLifecycleConfiguration.class)
public class LoadBalancerCacheDataManagerConfiguration {

	@Bean
	@DependsOn("caffeineLoadBalancerCacheManager")
	@ConditionalOnClass({ Caffeine.class, CaffeineCacheManager.class })
	@ConditionalOnProperty(value = "spring.cloud.loadbalancer.configurations", havingValue = "zone-failover-awareness")
	LoadBalancerCacheDataManager zoneFailoverAwarenessCaffeineLoadBalancerCacheManager(ApplicationContext context) {
		return new ZoneFailoverAwarenessLoadBalancerCacheDataManager((LoadBalancerCacheManager) context.getBean("caffeineLoadBalancerCacheManager"));
	}

	@Bean
	@DependsOn("defaultLoadBalancerCacheManager")
	@Conditional(LoadBalancerCacheAutoConfiguration.OnCaffeineCacheMissingCondition.class)
	@ConditionalOnProperty(value = "spring.cloud.loadbalancer.configurations", havingValue = "zone-failover-awareness")
	LoadBalancerCacheDataManager zoneFailoverAwarenessDefaultLoadBalancerCacheManager(ApplicationContext context) {
		return new ZoneFailoverAwarenessLoadBalancerCacheDataManager((LoadBalancerCacheManager) context.getBean("defaultLoadBalancerCacheManager"));
	}

}
