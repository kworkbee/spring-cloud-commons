package org.springframework.cloud.loadbalancer.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClientConfiguration;
import org.springframework.cloud.loadbalancer.cache.LoadBalancerCacheManager;
import org.springframework.cloud.loadbalancer.cache.ZoneFailoverAwareCacheDataManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter({LoadBalancerCacheAutoConfiguration.class, LoadBalancerLifecycleAutoConfiguration.class})
@AutoConfigureBefore(LoadBalancerClientConfiguration.class)
public class ZoneFailoverAwarenessConfiguration {

	@Bean
	@ConditionalOnBean(LoadBalancerCacheManager.class)
	@ConditionalOnProperty(value = "spring.cloud.loadbalancer.configurations", havingValue = "zone-failover-awareness")
	public ZoneFailoverAwareCacheDataManager zoneFailoverAwareCacheDataManager(LoadBalancerCacheManager cacheManager) {
		return new ZoneFailoverAwareCacheDataManager(cacheManager);
	}
}
