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

public class KiaIndexModule extends AbstractModule {

	private final Properties properties = new Properties();

	/**
	 * Bind the classes.
	 */
	@Override
	protected void configure() {

		properties.putAll(System.getProperties());
		Names.bindProperties(binder(), properties);

		bind(SiteReader.class).to(KiaIndexSiteReader.class);
		bind(HTTPBodyFetcher.class).to(HTTPClientBodyFetcher.class);
		bind(SiteResultCollector.class).to(
				GooglePageSpeedSiteResultCollector.class);
		bind(Reporter.class).to(SystemOutReporter.class);
		bind(SavingsCollector.class).to(GooglePageSpeedSavingsCollector.class);
	}
}
