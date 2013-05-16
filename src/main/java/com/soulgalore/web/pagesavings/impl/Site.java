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
package com.soulgalore.web.pagesavings.impl;

public class Site {

	private String url;
	private long pageViews;
	private long uniqueBrowsers;

	public Site(String url, long uniqueBrowsers, long pageViews) {
		this.url = url;
		this.pageViews = pageViews;
		this.uniqueBrowsers = uniqueBrowsers;
	}

	public String getUrl() {
		return url;
	}

	public long getPageViews() {
		return pageViews;
	}

	public long getUniqueBrowsers() {
		return uniqueBrowsers;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Site other = (Site) obj;
		if (url == null) {
			if (other.url != null)

				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

}
