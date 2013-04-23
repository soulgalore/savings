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
package com.soulgalore.web.savings.googlepagespeed;

import java.io.File;
import java.io.IOException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.soulgalore.web.savings.SavingsCollector;
import com.soulgalore.web.savings.HTTPBodyFetcher;
import com.soulgalore.web.savings.Reporter;
import com.soulgalore.web.savings.SiteReader;
import com.soulgalore.web.savings.SiteResultCollector;
import com.soulgalore.web.savings.impl.Site;
import com.soulgalore.web.savings.impl.SiteResult;

public class GooglePageSpeedSavingsCollector implements SavingsCollector {

	private final static Map<String, DescriptiveStatistics> statistics = new HashMap<String, DescriptiveStatistics>();

	private final SiteReader reader;
	private final HTTPBodyFetcher fetcher;
	private final SiteResultCollector siteResultCollector;
	private final Reporter reporter;
	private final String key;

	@Inject
	public GooglePageSpeedSavingsCollector(SiteReader reader,
			HTTPBodyFetcher fetcher, SiteResultCollector siteResultCollector,
			Reporter reporter,
			@Named("com.soulgalore.web.savings.googlekey") String key) {
		this.reader = reader;
		this.fetcher = fetcher;
		this.siteResultCollector = siteResultCollector;
		this.reporter = reporter;
		this.key = key;

		for (String rule : GooglePageSpeedSiteResultCollector.RULES) {
			statistics.put(rule, new DescriptiveStatistics());
		}

	}

	public void collect(File file) throws IOException {
		List<Site> sites = reader.get(file);
		Set<SiteResult> results = new HashSet<SiteResult>();
		for (Site site : sites) {

			results.add(siteResultCollector.collectSiteResult(
					fetcher.getBody("https://www.googleapis.com/pagespeedonline/v1/runPagespeed?url=http://"
							+ site.getUrl() + "&key=" + key), site, statistics));
		}
		reporter.report(results, statistics);
	}

}
