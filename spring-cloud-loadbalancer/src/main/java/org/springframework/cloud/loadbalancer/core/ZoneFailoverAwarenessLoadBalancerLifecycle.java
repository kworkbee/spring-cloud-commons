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

package org.springframework.cloud.loadbalancer.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.CompletionContext;
import org.springframework.cloud.client.loadbalancer.LoadBalancerLifecycle;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.client.loadbalancer.ResponseData;
import org.springframework.cloud.client.loadbalancer.RetryableRequestContext;
import org.springframework.cloud.loadbalancer.cache.ZoneFailoverAwareCacheDataManager;
import org.springframework.cloud.loadbalancer.config.LoadBalancerLifecycleAutoConfiguration;

/**
 * A lifecycle that
 *
 * @author Jiwon Jeon
 * @since ?
 * @see LoadBalancerLifecycleAutoConfiguration
 */
public class ZoneFailoverAwarenessLoadBalancerLifecycle implements LoadBalancerLifecycle<RetryableRequestContext, ResponseData, ServiceInstance> {

	private static final String INSTANCE_KEY_ZONE = "zone";

	private static final Log LOG = LogFactory.getLog(ZoneFailoverAwarenessLoadBalancerLifecycle.class);

	private final ZoneFailoverAwareCacheDataManager cacheDataManager;

	public ZoneFailoverAwarenessLoadBalancerLifecycle(ZoneFailoverAwareCacheDataManager cacheDataManager) {
		this.cacheDataManager = cacheDataManager;
	}

	@Override
	public boolean supports(Class requestContextClass, Class responseClass, Class serverTypeClass) {
		return RetryableRequestContext.class.isAssignableFrom(requestContextClass)
				&& ResponseData.class.isAssignableFrom(responseClass)
				&& ServiceInstance.class.isAssignableFrom(serverTypeClass);
	}

	@Override
	public void onStart(Request request) {}

	@Override
	public void onStartRequest(Request<RetryableRequestContext> request, Response<ServiceInstance> lbResponse) {}

	@Override
	public void onComplete(CompletionContext<ResponseData, ServiceInstance, RetryableRequestContext> completionContext) {
		final CompletionContext.Status status = completionContext.status();
		final ServiceInstance instance = completionContext.getLoadBalancerResponse().getServer();

		if (status.equals(CompletionContext.Status.DISCARD)) {
			if (LOG.isWarnEnabled()) {
				LOG.warn("Any instance selected. Will evict cache...");
			}
			cacheDataManager.clear();
		} else if (status.equals(CompletionContext.Status.FAILED)) {
			final String zone = instance.getMetadata().getOrDefault(INSTANCE_KEY_ZONE, null);
			final int port = instance.getPort();

			if (LOG.isErrorEnabled()) {
				LOG.error("Request to zone [%s] (Port [%d]) failed".formatted(zone, port));
			}
			cacheDataManager.put(instance);
		}
	}
}
