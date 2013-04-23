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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.soulgalore.web.savings.HTTPBodyFetcher;

public class HTTPClientBodyFetcher implements HTTPBodyFetcher {

	public String getBody(String url) throws IOException {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);

		try {
			HttpResponse response = httpclient.execute(httpGet);
			HttpEntity entity = response.getEntity();

			final String encoding = entity.getContentEncoding() != null ? entity
					.getContentEncoding().getValue() : "UTF-8";

			final String body = (readBody(entity, encoding));

			EntityUtils.consume(entity);
			return body;
		} finally {
			httpGet.releaseConnection();
		}

	}

	private String readBody(HttpEntity entity, String enc) throws IOException {
		final StringBuilder body = new StringBuilder();
		String buffer = "";
		if (entity != null) {
			final BufferedReader reader = new BufferedReader(
					new InputStreamReader(entity.getContent(), enc));
			while ((buffer = reader.readLine()) != null) {
				body.append(buffer);
			}
			reader.close();
		}
		return body.toString();
	}

}
