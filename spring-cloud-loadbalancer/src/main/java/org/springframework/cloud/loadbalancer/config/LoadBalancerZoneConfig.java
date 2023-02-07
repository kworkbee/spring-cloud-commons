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

package org.springframework.cloud.loadbalancer.config;

import java.util.Collections;
import java.util.List;

/**
 * @author Olga Maciaszek-Sharma
 */
public class LoadBalancerZoneConfig {

	/**
	 * A {@link String} representation of the <code>zone</code> used for filtering
	 * instances by zoned load-balancing implementations.
	 */
	private String zone;
	private List<String> secondaryZones;

	public LoadBalancerZoneConfig(String zone) {
		this.zone = zone;
		this.secondaryZones = Collections.EMPTY_LIST;
	}

	public LoadBalancerZoneConfig(String zone, List<String> secondaryZones) {
		this.zone = zone;
		this.secondaryZones = secondaryZones;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public List<String> getSecondaryZones() {
		return secondaryZones;
	}

	public void setSecondaryZones(List<String> secondaryZones) {
		this.secondaryZones = secondaryZones;
	}
}
