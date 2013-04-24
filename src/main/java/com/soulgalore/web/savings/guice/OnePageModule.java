package com.soulgalore.web.savings.guice;

import java.util.Properties;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.soulgalore.web.savings.HTTPBodyFetcher;
import com.soulgalore.web.savings.Reporter;
import com.soulgalore.web.savings.SavingsCollector;
import com.soulgalore.web.savings.SiteReader;
import com.soulgalore.web.savings.SiteResultCollector;
import com.soulgalore.web.savings.googlepagespeed.GooglePageSpeedSavingsCollector;
import com.soulgalore.web.savings.googlepagespeed.GooglePageSpeedSiteResultCollector;
import com.soulgalore.web.savings.impl.HTTPClientBodyFetcher;
import com.soulgalore.web.savings.impl.KiaIndexSiteReader;
import com.soulgalore.web.savings.reporters.SystemOutReporter;

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
