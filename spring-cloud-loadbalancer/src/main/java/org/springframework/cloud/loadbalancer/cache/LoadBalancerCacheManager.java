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

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import static org.springframework.cloud.loadbalancer.core.CachingServiceInstanceListSupplier.SERVICE_INSTANCE_CACHE_NAME;

/**
 * A marker interface for Spring Cloud LoadBalancer-specific {@link CacheManager} beans.
 *
 * @author Olga Maciaszek-Sharma
 * @since 2.2.0
 */
public interface LoadBalancerCacheManager extends CacheManager {

	// TODO Configurable Cache Name (in the future)
	default Cache getCache() {
		return getCache(SERVICE_INSTANCE_CACHE_NAME);
	}
}
