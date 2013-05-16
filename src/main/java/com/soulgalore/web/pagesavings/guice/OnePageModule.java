package com.soulgalore.web.pagesavings.guice;

import java.util.Properties;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.soulgalore.web.pagesavings.HTTPBodyFetcher;
import com.soulgalore.web.pagesavings.Reporter;
import com.soulgalore.web.pagesavings.SavingsCollector;
import com.soulgalore.web.pagesavings.SiteReader;
import com.soulgalore.web.pagesavings.SiteResultCollector;
import com.soulgalore.web.pagesavings.googlepagespeed.GooglePageSpeedSavingsCollector;
import com.soulgalore.web.pagesavings.googlepagespeed.GooglePageSpeedSiteResultCollector;
import com.soulgalore.web.pagesavings.impl.HTTPClientBodyFetcher;
import com.soulgalore.web.pagesavings.impl.KiaIndexSiteReader;
import com.soulgalore.web.pagesavings.reporters.SystemOutReporter;

public class OnePageModule  extends AbstractModule{

	private final Properties properties = new Properties();

	/**
	 * Bind the classes.
	 */
	@Override
	protected void configure() {

		properties.putAll(System.getProperties());
		Names.bindProperties(binder(), properties);

		bind(HTTPBodyFetcher.class).to(HTTPClientBodyFetcher.class);
		bind(SiteResultCollector.class).to(
				GooglePageSpeedSiteResultCollector.class);
		bind(SavingsCollector.class).to(GooglePageSpeedSavingsCollector.class);
	}
}
