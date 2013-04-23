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
package com.soulgalore.web.savings.reporters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.soulgalore.web.savings.Reporter;
import com.soulgalore.web.savings.impl.RuleResult;
import com.soulgalore.web.savings.impl.SavingsPercentageComparator;
import com.soulgalore.web.savings.impl.SiteResult;

public class SystemOutReporter implements Reporter {

	public static final int TOPLIST_ITEMS = 10;

	public void report(Set<SiteResult> results,
			Map<String, DescriptiveStatistics> statistics) {

		Double totalPw = 0D;
		Double totalUnique = 0D;
		for (SiteResult siteResult : results) {
			totalPw += siteResult.getTotalSavings()
					* siteResult.getSite().getPageViews();
			totalUnique += siteResult.getTotalSavings()
					* siteResult.getSite().getUniqueBrowsers();
			System.out.println("-------------------------------------");
			System.out
					.println(siteResult.getSite().getUrl()
							+ " saved per page: "
							+ humanReadableByteCount(
									Math.round(siteResult.getTotalSavings()),
									true)
							+ " pw:"
							+ humanReadableByteCount(
									Math.round(siteResult.getTotalSavings()
											* siteResult.getSite()
													.getPageViews()), true)
							+ " unique:"
							+ humanReadableByteCount(
									Math.round(siteResult.getTotalSavings()
											* siteResult.getSite()
													.getUniqueBrowsers()), true));
			for (RuleResult ruleResult : siteResult.getResults()) {
				System.out.println(ruleResult.getRule() + " "
						+ ruleResult.getSavings() + " kb");
			}

		}

		System.out.println("-------------------------------------");
		System.out.println("Tested " + results.size() + " sites");
		System.out.println("total pw:"
				+ humanReadableByteCount(Math.round(totalPw), true));
		System.out.println("total unique:"
				+ humanReadableByteCount(Math.round(totalUnique), true));

		for (String rule : statistics.keySet()) {
			DescriptiveStatistics stats = statistics.get(rule);
			System.out.println(rule + " median save:" + stats.getPercentile(50)
					+ "  max save:" + stats.getMax());
		}

		List<SiteResult> toplist = new ArrayList<SiteResult>();
		toplist.addAll(results);
		Collections.sort(toplist);
		
		System.out.println("-------------------------------------");
		System.out.println("TopList (kb):");

		for (int i = 0; i < TOPLIST_ITEMS; i++)
			System.out.println(toplist.get(i).getSite().getUrl() + " "
					+  Math.round(toplist.get(i).getTotalSavings()*100)/100.0d);

		System.out.println("-------------------------------------");
		System.out.println("BottomList (kb):");

		for (int i = 1; i < TOPLIST_ITEMS + 1; i++)
			System.out.println(toplist.get(toplist.size() - i).getSite()
					.getUrl()
					+ " " +  Math.round(toplist.get(toplist.size() - i).getTotalSavings()*100)/100.0d);

		Collections.sort(toplist, new SavingsPercentageComparator());

		System.out.println("-------------------------------------");
		System.out.println("TopList %:");
		for (int i = 0; i < TOPLIST_ITEMS; i++)
			System.out.println(toplist.get(i).getSite().getUrl() + " "
					+  Math.round(toplist.get(i).getSavingsPercentage()*100)/100.0d);

		System.out.println("-------------------------------------");
		System.out.println("BottomList %:");

		for (int i = 1; i < TOPLIST_ITEMS + 1; i++)
			System.out.println(toplist.get(toplist.size() - i).getSite()
					.getUrl()
					+ " "
					+ Math.round(toplist.get(toplist.size() - i).getSavingsPercentage()*100)/100.0d);

	}

	private String humanReadableByteCount(long kbytes, boolean si) {
		int unit = si ? 1000 : 1024;
		long bytes = kbytes * unit;
		if (bytes < unit)
			return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1)
				+ (si ? "" : "i");
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}

}
