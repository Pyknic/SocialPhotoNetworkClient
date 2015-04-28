/**
 *
 * Copyright (c) 2006-2015, Speedment, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); You may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.speedment.examples.social;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Emil Forslund
 */
public class Http {
	
	public static String param(String key, String value) {
		try {
			return key + "=" + URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException ex) {
			Logger.getLogger(Http.class.getName()).log(Level.SEVERE, 
				"Failed to encode value: '" + value + "'.", ex);
			return null;
		}
	}
	
	public static String params(String... params) {
		return Stream.of(params).collect(Collectors.joining("&"));
	}

	public static Optional<String> post(String targetURL, String params, Consumer<Throwable> catcher) {
		final URL url;
		HttpURLConnection connection = null;
		
		try {
			url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty(
				"Content-Type",
				"application/x-www-form-urlencoded"
			);

			connection.setRequestProperty("Content-Length", params.getBytes().length + "");
			connection.setRequestProperty("Content-Language", "en-US");

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			try {
				connection.connect();
			} catch (IOException ex) {
				catcher.accept(ex);
				return Optional.empty();
			}

			try (final DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
				wr.writeBytes(params);
				wr.flush();
			}
			
			final InputStream is = connection.getInputStream();
			final StringBuilder response;
			
			try (BufferedReader rd = new BufferedReader(new InputStreamReader(is))) {
				String line;
				response = new StringBuilder();
				while ((line = rd.readLine()) != null) {
					response.append(line);
					response.append('\n');
				}
			}
			return Optional.ofNullable(response.toString().trim());

		} catch (IOException ex) {
			ex.printStackTrace();
			return Optional.empty();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
}