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
package com.soulgalore.web.pagesavings.reporters;

import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.jdom2.CDATA;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.soulgalore.web.pagesavings.Reporter;
import com.soulgalore.web.pagesavings.impl.RuleResult;
import com.soulgalore.web.pagesavings.impl.SiteResult;

public class XMLReporter implements Reporter {

	private final static String READABLE = "readable";
	private final static String KB = "kb";

	public void report(Set<SiteResult> results,
			Map<String, DescriptiveStatistics> statistics) {

		Date reportDate = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		Element root = new Element("savings");
		root.setAttribute("date", "" + reportDate);

		Element resultsXML = new Element("results");
		root.addContent(resultsXML);

		Double totalPw = 0D;
		Double totalUnique = 0D;
		for (SiteResult siteResult : results) {
			totalPw += siteResult.getTotalSavings()
					* siteResult.getSite().getPageViews();
			totalUnique += siteResult.getTotalSavings()
					* siteResult.getSite().getUniqueBrowsers();
			Element site = new Element("site");
			Element url = new Element("url");
			Element weight = new Element("total-page-weight");
			Element savingsPerPage = new Element("savings-per-page");
			Element savingsForPW = new Element("savings-for-page-view");
			Element savingsForUnique = new Element(
					"savings-for-unique-browsers");
			Element savingsPercentage = new Element("savings-percentage");

			url.addContent(new CDATA(siteResult.getSite().getUrl()));

			savingsPerPage.setAttribute(
					READABLE,
					humanReadableByteCount(
							Math.round(siteResult.getTotalSavings()), true));
			savingsPerPage.setAttribute(KB,
					"" + Math.round(siteResult.getTotalSavings()));

			savingsForPW.setAttribute(
					READABLE,
					humanReadableByteCount(
							Math.round(siteResult.getTotalSavings()
									* siteResult.getSite().getPageViews()),
							true));
			savingsForPW.setAttribute(
					KB,
					""
							+ Math.round(siteResult.getTotalSavings()
									* siteResult.getSite().getPageViews()));

			savingsForUnique
					.setAttribute(
							READABLE,
							humanReadableByteCount(
									Math.round(siteResult.getTotalSavings()
											* siteResult.getSite()
													.getUniqueBrowsers()), true));
			savingsForUnique
					.setAttribute(
							KB,
							""
									+ Math.round(siteResult.getTotalSavings()
											* siteResult.getSite()
													.getUniqueBrowsers()));

			savingsPercentage.addContent(""
					+ Math.round(siteResult.getSavingsPercentage() * 100)
					/ 100.0d);

			weight.setAttribute(
					READABLE,
					humanReadableByteCount(
							siteResult.getTotalSizeBytes() / 1000, true));
			weight.setAttribute(KB, "" + siteResult.getTotalSizeBytes() / 1000);

			Element rulesSavings = new Element("rule-savings");
			for (RuleResult ruleResult : siteResult.getResults()) {
				Element rule = new Element(ruleResult.getRule());
				rule.addContent("" + ruleResult.getSavings());
				rulesSavings.addContent(rule);
			}

			site.addContent(url);
			site.addContent(weight);
			site.addContent(savingsPerPage);
			site.addContent(savingsForPW);
			site.addContent(savingsForUnique);
			site.addContent(savingsPercentage);
			site.addContent(rulesSavings);
			resultsXML.addContent(site);
		}

		Element summary = new Element("summary");
		summary.setAttribute("nrofsites", "" + results.size());
		Element totalPW = new Element("total-pw");
		Element totalUniqueSummary = new Element("total-unique");
		totalPW.setAttribute(READABLE,
				humanReadableByteCount(Math.round(totalPw), true));
		totalPW.setAttribute(KB, "" + Math.round(totalPw));
		totalUniqueSummary.setAttribute(READABLE,
				humanReadableByteCount(Math.round(totalUnique), true));
		totalUniqueSummary.setAttribute(KB, "" + Math.round(totalUnique));
		summary.addContent(totalPW);
		summary.addContent(totalUniqueSummary);

		for (String rule : statistics.keySet()) {
			DescriptiveStatistics stats = statistics.get(rule);
			Element ruleElement = new Element(rule);
			Element max = new Element("max");
			Element median = new Element("median");
			max.addContent("" + stats.getMax());
			median.addContent("" + stats.getPercentile(50));
			ruleElement.addContent(max);
			ruleElement.addContent(median);
			summary.addContent(ruleElement);
		}

		resultsXML.addContent(summary);

		Document doc = new Document(root);
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());

		try {
			FileWriter writer = new FileWriter("report-"
					+ df.format(reportDate) + ".xml");
			outputter.output(doc, writer);
		} catch (Exception e) {
		}
	}

	private String humanReadableByteCount(long kbytes, boolean si) {
		int unit = si ? 1024 : 1000;
		long bytes = kbytes * unit;
		if (bytes < unit)
			return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1)
				+ (si ? "" : "i");
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}

}
