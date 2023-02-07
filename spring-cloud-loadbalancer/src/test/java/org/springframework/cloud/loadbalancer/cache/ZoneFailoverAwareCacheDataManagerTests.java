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

package org.springframework.cloud.loadbalancer.cache;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.cache.Cache;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link ZoneFailoverAwareCacheDataManager}.
 *
 * @author Jiwon Jeon
 */
class ZoneFailoverAwareCacheDataManagerTests {

	private static final String CACHE_KEY_FAILED = "FAILED";

	private LoadBalancerCacheManager cacheManager = mock(LoadBalancerCacheManager.class);
	private ZoneFailoverAwareCacheDataManager cacheDataManager = new ZoneFailoverAwareCacheDataManager(cacheManager);

	@BeforeEach
	void setUp() {
		when(cacheManager.getCache()).thenReturn(mock(Cache.class));
	}

	@Test
	void shouldReturnEmptySetWhenCacheIsEmpty() {
		when(cacheManager.getCache().get(CACHE_KEY_FAILED, Set.class)).thenReturn(Collections.EMPTY_SET);

		assertThat(cacheDataManager.getFailedInstances()).isEqualTo(Collections.EMPTY_SET);
	}

	@Test
	void shouldReturnFailedInstancesFromCache() {
		final List<ServiceInstance> failedInstances = List.of(
				new DefaultServiceInstance(),
				new DefaultServiceInstance(),
				new DefaultServiceInstance()
		);

		when(cacheManager.getCache().get(CACHE_KEY_FAILED, Set.class)).thenReturn(new HashSet(failedInstances));

		assertThat(cacheDataManager.getFailedInstances()).isEqualTo(new HashSet<>(failedInstances));
	}

	@Test
	void shouldAddFailedInstanceIntoCache() {
		final ServiceInstance instance = new DefaultServiceInstance();
		final Set<ServiceInstance> expected = new HashSet(List.of(instance));

		when(cacheManager.getCache().get(CACHE_KEY_FAILED, Set.class)).thenReturn(Collections.EMPTY_SET);
		when(cacheManager.getCache().get(CACHE_KEY_FAILED, Set.class)).thenReturn(expected);

		cacheDataManager.put(instance);

		assertThat(cacheDataManager.getFailedInstances()).isEqualTo(expected);
	}
}