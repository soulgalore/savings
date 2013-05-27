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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.soulgalore.web.pagesavings.SiteResultCollector;
import com.soulgalore.web.pagesavings.impl.RuleResult;
import com.soulgalore.web.pagesavings.impl.Site;
import com.soulgalore.web.pagesavings.impl.SiteResult;

public class GooglePageSpeedSiteResultCollector implements SiteResultCollector {

	/**
	 * The rules that will be checked.
	 */
	static final String[] RULES = { "MinifyCss", "MinifyHTML",
			"EnableGzipCompression", "MinifyJavaScript", "OptimizeImages",
			"ServeScaledImages" };

	/**
	 * The names of the response types that will be used to create total size.
	 */
	static final String[] RESPONSE_SIZE_NAMES = { "htmlResponseBytes",
			"cssResponseBytes", "imageResponseBytes",
			"javascriptResponseBytes", "otherResponseBytes" };

	@Override
	public SiteResult collectSiteResult(String body, Site site,
			Map<String, DescriptiveStatistics> statistics) {
		JsonElement jElement = new JsonParser().parse(body);

		if (jElement == null) {
			System.out.println("Skipping " + site.getUrl()
					+ " the json element was null");
			return null;
		}
		
		if (jElement.getAsJsonObject().get("responseCode")==null) {
			System.out.println("Skipping " + site.getUrl()
					+ " missing response code");
			return null;
		}
			

		if (jElement.getAsJsonObject().get("responseCode").getAsInt() != 200) {
			System.out.println("Skipping "
					+ site.getUrl()
					+ " response code "
					+ jElement.getAsJsonObject().get("responseCode")
							.getAsString());
			return null;
		}

		Set<RuleResult> results = new HashSet<RuleResult>(RULES.length);
		System.out.println("Testing " + site.getUrl());

		Integer totalPageSize = 0;

		for (String name : RESPONSE_SIZE_NAMES) {
			if (jElement.getAsJsonObject().get("pageStats").getAsJsonObject()
					.get(name) != null) {
				totalPageSize += Integer.parseInt(jElement.getAsJsonObject()
						.get("pageStats").getAsJsonObject().get(name)
						.getAsString());
			}
		}

		for (String rule : RULES) {

			if (jElement.getAsJsonObject().get("formattedResults")
					.getAsJsonObject().get("ruleResults").getAsJsonObject()
					.get(rule).getAsJsonObject().has("urlBlocks")) {
				JsonArray array = (jElement.getAsJsonObject()
						.get("formattedResults").getAsJsonObject()
						.get("ruleResults").getAsJsonObject().get(rule)
						.getAsJsonObject().get("urlBlocks").getAsJsonArray());
				for (JsonElement jsonElement : array) {
					String sizeWithKiB = jsonElement.getAsJsonObject()
							.get("header").getAsJsonObject().get("args")
							.getAsJsonArray().get(0).getAsJsonObject()
							.get("value").getAsString();
					if (sizeWithKiB.contains("KiB")) {

						if (sizeWithKiB.contains(","))
							sizeWithKiB = sizeWithKiB.replace(",", "");

						Double value = Double.parseDouble(sizeWithKiB.replace(
								"KiB", ""));
						results.add(new RuleResult(rule, value));
						statistics.get(rule).addValue(value);

					}
				}
			}

		}
		return new SiteResult(site, results, totalPageSize);
	}

}
