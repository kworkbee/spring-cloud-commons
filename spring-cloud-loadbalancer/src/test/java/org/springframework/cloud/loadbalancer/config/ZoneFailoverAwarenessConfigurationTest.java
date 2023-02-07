package org.springframework.cloud.loadbalancer.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.cloud.loadbalancer.cache.ZoneFailoverAwareCacheDataManager;

import static org.assertj.core.api.Assertions.assertThat;

class ZoneFailoverAwarenessConfigurationTest {

	@Test
	void shouldEnableCacheDataManager() {
		baseContextRunner().withPropertyValues("spring.cloud.loadbalancer.configurations=zone-failover-awareness")
				.run(context -> {
					assertThat(context.getBean("zoneFailoverAwareCacheDataManager")).isInstanceOf(
							ZoneFailoverAwareCacheDataManager.class);
				});
	}

	private ApplicationContextRunner baseContextRunner() {
		return new ApplicationContextRunner().withConfiguration(
				AutoConfigurations.of(LoadBalancerCacheAutoConfiguration.class,
						LoadBalancerLifecycleAutoConfiguration.class));
	}

}