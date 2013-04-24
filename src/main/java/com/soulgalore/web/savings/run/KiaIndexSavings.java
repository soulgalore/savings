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
package com.soulgalore.web.savings.run;

import java.io.File;
import java.io.IOException;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.soulgalore.web.savings.SavingsCollector;
import com.soulgalore.web.savings.googlepagespeed.GooglePageSpeedSavingsCollector;
import com.soulgalore.web.savings.guice.KiaIndexModule;

public class KiaIndexSavings {

	public static void main(String[] args) {

		if (args.length != 2) {
			System.out.println("Two arguments: theKiaFile googleapikey");
			return;
		}

		System.setProperty("com.soulgalore.web.savings.googlekey", args[1]);

		final Injector injector = Guice.createInjector(new KiaIndexModule());
		final SavingsCollector stats = injector
				.getInstance(SavingsCollector.class);

		File file = new File(args[0]);

		System.out.println("Start fetching savings for " + file.getAbsolutePath());
		try {
			stats.collect(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
