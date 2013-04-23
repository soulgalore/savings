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
package com.soulgalore.web.savings.impl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

import com.google.common.io.Files;
import com.soulgalore.web.savings.SiteReader;

public class KiaIndexSiteReader implements SiteReader {

	@Override
	public List<Site> get(File file) throws IOException {

		List<String> urlsAndPw = Files
				.readLines(file, Charset.defaultCharset());
		List<Site> sites = new LinkedList<Site>();
		
		// the kia file is dirrrty
		for (String urlAndPw : urlsAndPw) {
			if (urlAndPw.contains(".")) {
				if (!urlAndPw.contains(" ")) {
					String[] u = urlAndPw.split(",");
					sites.add(new Site(u[0], Long.parseLong(u[1]), Long
							.parseLong(u[2])));
				}
			}
		}
		return sites;
	}

}
