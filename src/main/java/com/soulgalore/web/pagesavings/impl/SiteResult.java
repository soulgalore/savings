/******************************************************
 * Web savings, how much can your web page save in kb?
 * 
 *
 * Copyright (C) 2013 by Peter Hedenskog (http://peterhedenskog.com)
 *
 ******************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in 
 * compliance with the License. You may obtain a copy of the License at
 * 
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is 
 * distributed  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   
 * See the License for the specific language governing permissions and limitations under the License.
 *
 *******************************************************
 */
package com.soulgalore.web.pagesavings.impl;

import java.util.Set;


public class SiteResult implements Comparable<SiteResult> {

	private final Site site;
	private final Set<RuleResult> results;
	private final Integer totalSize;

	public SiteResult(Site site, Set<RuleResult> results, Integer totalSite) {
		this.site = site;
		this.results = results;
		this.totalSize = totalSite;
	}

	public Site getSite() {
		return site;
	}

	public Set<RuleResult> getResults() {
		return results;
	}
	
	public Integer getTotalSizeBytes() {
		return totalSize;
	}
	
	public Double getSavingsPercentage() {
		return (getTotalSavings() / (getTotalSizeBytes()/1000))*100;
	}

	public Double getTotalSavings() {
		Double total = 0D;
		for (RuleResult result : results) {
			total += result.getSavings();
		}
		return total;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((site == null) ? 0 : site.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SiteResult other = (SiteResult) obj;
		if (site == null) {
			if (other.site != null)
				return false;
		} else if (!site.equals(other.site))
			return false;
		return true;
	}

	@Override
	public int compareTo(SiteResult o) {
			if (getTotalSavings()<o.getTotalSavings())
				return -1;
			else if (getTotalSavings()>o.getTotalSavings())
				return 1;
		return 0;
	}

}
