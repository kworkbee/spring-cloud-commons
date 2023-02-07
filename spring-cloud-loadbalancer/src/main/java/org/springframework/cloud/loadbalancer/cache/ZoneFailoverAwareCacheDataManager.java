package org.springframework.cloud.loadbalancer.cache;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.cache.Cache;
import org.springframework.cloud.client.ServiceInstance;

public class ZoneFailoverAwareCacheDataManager {

	private static final String CACHE_KEY_FAILED = "FAILED";

	private final LoadBalancerCacheManager cacheManager;

	public ZoneFailoverAwareCacheDataManager(LoadBalancerCacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public Set<ServiceInstance> getFailedInstances() {
		Set<ServiceInstance> failedInstances = getCache().get(CACHE_KEY_FAILED, Set.class);
		if (failedInstances == null) {
			return Collections.EMPTY_SET;
		}

		return failedInstances;
	}

	public void put(ServiceInstance instance) {
		Set<ServiceInstance> failedInstances = getCache().get(CACHE_KEY_FAILED, HashSet::new);

		if (failedInstances == null) {
			failedInstances = new HashSet<>();
		}

		failedInstances.add(instance);
		getCache().put(CACHE_KEY_FAILED, failedInstances);
	}

	public void clear() {
		getCache().clear();
	}

	private Cache getCache() {
		return cacheManager.getCache();
	}
}
