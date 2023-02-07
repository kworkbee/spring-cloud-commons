package org.springframework.cloud.loadbalancer.config;

import org.junit.jupiter.api.Test;

import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.cloud.client.loadbalancer.LoadBalancerLifecycle;
import org.springframework.cloud.loadbalancer.core.ZoneFailoverAwarenessLoadBalancerLifecycle;

import static org.assertj.core.api.Assertions.assertThat;


class LoadBalancerLifecycleAutoConfigurationTests {

	@Test
	void shouldInstantiateZoneFailoverAwarenessLoadBalancerLifecycle() {
		baseApplicationRunner()
				.withPropertyValues("spring.cloud.loadbalancer.configurations=zone-failover-awareness")
				.run(context -> {
					assertThat(context.getBeansOfType(LoadBalancerLifecycle.class)).hasSize(1);
					assertThat(context.getBean("zoneFailoverAwarenessLoadBalancerLifecycle"))
							.isInstanceOf(ZoneFailoverAwarenessLoadBalancerLifecycle.class);
				});
	}

	private ApplicationContextRunner baseApplicationRunner() {
		return new ApplicationContextRunner().withConfiguration(
				AutoConfigurations.of(LoadBalancerAutoConfiguration.class, LoadBalancerLifecycleAutoConfiguration.class)
		);
	}

}