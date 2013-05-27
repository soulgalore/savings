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
package com.soulgalore.web.pagesavings.googlepagespeed;

import java.io.IOException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.soulgalore.web.pagesavings.HTTPBodyFetcher;
import com.soulgalore.web.pagesavings.SavingsCollector;
import com.soulgalore.web.pagesavings.SiteResultCollector;
import com.soulgalore.web.pagesavings.impl.Site;
import com.soulgalore.web.pagesavings.impl.SiteResult;

public class GooglePageSpeedSavingsCollector implements SavingsCollector {

	private final static Map<String, DescriptiveStatistics> statistics = new HashMap<String, DescriptiveStatistics>();

	private final HTTPBodyFetcher fetcher;
	private final SiteResultCollector siteResultCollector;
	private final String key;

	@Inject
	public GooglePageSpeedSavingsCollector(
			HTTPBodyFetcher fetcher, SiteResultCollector siteResultCollector,
			@Named("com.soulgalore.web.savings.googlekey") String key) {
		this.fetcher = fetcher;
		this.siteResultCollector = siteResultCollector;
		this.key = key;

		for (String rule : GooglePageSpeedSiteResultCollector.RULES) {
			statistics.put(rule, new DescriptiveStatistics());
		}

	}

	public  Set<SiteResult> collect(List<Site> sites) throws IOException {

		Set<SiteResult> results = new HashSet<SiteResult>();
		for (Site site : sites) {

			SiteResult result = siteResultCollector.collectSiteResult(
					fetcher.getBody("https://www.googleapis.com/pagespeedonline/v1/runPagespeed?url=http://"
							+ site.getUrl() + "&key=" + key), site, statistics);
			if (result!=null)
				results.add(result);
		}
		return results;
	}

	@Override
	public Map<String, DescriptiveStatistics> getStatistics() {
	
		return statistics;
	}

}
