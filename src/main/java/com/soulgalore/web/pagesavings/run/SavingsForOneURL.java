package com.soulgalore.web.pagesavings.run;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.soulgalore.web.pagesavings.SavingsCollector;
import com.soulgalore.web.pagesavings.guice.OnePageModule;
import com.soulgalore.web.pagesavings.impl.RuleResult;
import com.soulgalore.web.pagesavings.impl.Site;
import com.soulgalore.web.pagesavings.impl.SiteResult;

public class SavingsForOneURL {

	public static void main(String[] args) throws IOException {

		if (args.length != 2) {
			System.out.println("Two arguments: theURL googleapikey");
			return;
		}

		System.setProperty("com.soulgalore.web.savings.googlekey", args[1]);

		final Injector injector = Guice.createInjector(new OnePageModule());
		final SavingsCollector savingsCollector = injector
				.getInstance(SavingsCollector.class);

		List<Site> site = Arrays.asList(new Site(args[0], 1, 1));
		Set<SiteResult> results = savingsCollector.collect(site);

		SiteResult result = (SiteResult) results.toArray()[0];
		System.out.println(result.getSite().getUrl() + " totalSaving:"
				+ result.getTotalSavings() + " kb");

		for (RuleResult ruleResult : result.getResults()) {
			System.out.println(ruleResult.getRule() + " "
					+ ruleResult.getSavings() + " kb");
		}

	}
}
