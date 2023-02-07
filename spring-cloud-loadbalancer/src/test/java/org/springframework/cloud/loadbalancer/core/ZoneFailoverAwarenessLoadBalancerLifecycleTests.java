/*
 * Copyright 2012-2021 the original author or authors.
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

import java.net.URI;
import java.util.Map;

import org.junit.jupiter.api.Test;

import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.CompletionContext;
import org.springframework.cloud.client.loadbalancer.DefaultRequest;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.RequestData;
import org.springframework.cloud.client.loadbalancer.ResponseData;
import org.springframework.cloud.client.loadbalancer.RetryableRequestContext;
import org.springframework.cloud.loadbalancer.cache.ZoneFailoverAwareCacheDataManager;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.ClientRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link ZoneFailoverAwarenessLoadBalancerLifecycle}.
 *
 * @author Jiwon Jeon
 */

class ZoneFailoverAwarenessLoadBalancerLifecycleTests {

	private final ZoneFailoverAwareCacheDataManager cacheDataManager = mock(ZoneFailoverAwareCacheDataManager.class);
	private final ZoneFailoverAwarenessLoadBalancerLifecycle lifecycle = new ZoneFailoverAwarenessLoadBalancerLifecycle(cacheDataManager);

	@Test
	void shouldClearCacheWhenNoInstancesAvailable() {
		final CompletionContext completionContext = new CompletionContext<ResponseData, ServiceInstance, RetryableRequestContext>(
				CompletionContext.Status.DISCARD,
				getRequest(),
				new DefaultResponse(getServiceInstance())
		);

		lifecycle.onComplete(completionContext);

		verify(cacheDataManager, times(1)).clear();
	}

	@Test
	void shouldAddInstanceIntoCacheWhenTheInstanceFailed() {
		final CompletionContext completionContext = new CompletionContext<ResponseData, ServiceInstance, RetryableRequestContext>(
				CompletionContext.Status.FAILED,
				getRequest(),
				new DefaultResponse(getServiceInstance())
		);

		lifecycle.onComplete(completionContext);

		verify(cacheDataManager, times(1)).put(any(ServiceInstance.class));
	}

	private Request<RetryableRequestContext> getRequest() {
		final var clientRequest = ClientRequest.create(HttpMethod.GET, URI.create("/test")).build();
		final var requestData = new RequestData(clientRequest);
		final var requestContext = new RetryableRequestContext(null, requestData, "hint");

		return new DefaultRequest<>(requestContext);
	}

	private ServiceInstance getServiceInstance() {
		return new DefaultServiceInstance(null, "test", "127.0.0.1", 8080, false, Map.of("zone", "zone1"));
	}

}