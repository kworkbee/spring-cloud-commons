package org.springframework.cloud.loadbalancer.core;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import reactor.core.publisher.Flux;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.cache.ZoneFailoverAwareCacheDataManager;
import org.springframework.cloud.loadbalancer.config.LoadBalancerZoneConfig;

public class ZoneFailoverAwareServiceInstanceListSupplier extends DelegatingServiceInstanceListSupplier {


	private static final String METADATA_KEY_ZONE = "zone";

	private final ZoneFailoverAwareCacheDataManager cacheDataManager;
	private final LoadBalancerZoneConfig zoneConfig;

	public ZoneFailoverAwareServiceInstanceListSupplier(ServiceInstanceListSupplier delegate, ZoneFailoverAwareCacheDataManager cacheDataManager, LoadBalancerZoneConfig zoneConfig) {
		super(delegate);
		this.cacheDataManager = cacheDataManager;
		this.zoneConfig = zoneConfig;
	}

	@Override
	public Flux<List<ServiceInstance>> get() {
		return getDelegate().get().map(this::filteredAndOrderedByZone);
	}

	private List<ServiceInstance> filteredAndOrderedByZone(List<ServiceInstance> serviceInstances) {
		final Set<ServiceInstance> instances = cacheDataManager.getFailedInstances();
		final Stream instanceStream = serviceInstances.stream()
				.sorted(Comparator.comparing(instance -> zoneConfig.getZone()
						.indexOf(instance.getMetadata().get(METADATA_KEY_ZONE))));

		if (instances == null) {
			return instanceStream.toList();
		}

		return instanceStream.filter(instance -> !instances.contains(instance)).toList();
	}
}
