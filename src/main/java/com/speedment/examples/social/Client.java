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

import static com.speedment.examples.social.Http.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 *
 * @author Emil Forslund
 */
public class Client implements ClientAPI {
	private final String host;
	private final Consumer<Throwable> catcher;
	
	private String sessionKey;
	private LocalDateTime lastBrowse;
	
	public Client(String host, Consumer<Throwable> catcher) {
		this.host = host;
		this.catcher = catcher;
	}
	
	@Override
	public boolean login(String mail, String password) {
		return send("login", mail, password);
	}
	
	@Override
	public boolean register(String mail, String password) {
		return send("register", mail, password);
	}
	
	@Override
	public boolean upload(String title, String description, String imgData) {
		return post(host + "/upload", params(
			param("title", title),
			param("description", description),
			param("imgdata", imgData),
			param("sessionkey", sessionKey)
		), catcher)
		.filter(s -> !s.equals("false"))
		.filter(s -> !s.isEmpty())
		.map(s -> true).orElse(false);
	}

	@Override
	public List<JSONUser> find(String freeText) {
		return post(host + "/find", params(
			param("freetext", freeText),
			param("sessionkey", sessionKey)
		), catcher)
		.filter(s -> !s.equals("false"))
		.filter(s -> !s.isEmpty())
		.map(s -> JSONUser.parse(s))
		.orElse(new ArrayList<>());
	}
	
	@Override
	public Optional<JSONUser> self() {
		return post(host + "/self", params(
			param("sessionkey", sessionKey)
		), catcher)
		.filter(s -> !s.equals("false"))
		.filter(s -> !s.isEmpty())
		.map(s -> JSONUser.parseOne(s));
	}

	@Override
	public boolean follow(long userId) {
		return post(host + "/follow", params(
			param("userid", Long.toString(userId)),
			param("sessionkey", sessionKey)
		), catcher)
		.filter(s -> !s.equals("false"))
		.filter(s -> !s.isEmpty())
		.map(s -> true).orElse(false);
	}
	
	@Override
	public Optional<JSONUser> update(String mail, String firstname, String lastname, String imgData) {
		return post(host + "/update", params(
			param("mail", mail),
			param("firstname", firstname),
			param("lastname", lastname),
			param("avatar", imgData),
			param("sessionkey", sessionKey)
		), catcher)
		.filter(s -> !s.equals("false"))
		.filter(s -> !s.isEmpty())
		.map(s -> JSONUser.parseOne(s));
	}

	@Override
	public List<JSONImage> browse(Optional<LocalDateTime> from, Optional<LocalDateTime> to) {
		return post(host + "/browse", params(
			param("sessionkey", sessionKey),
			param("to", to.map(b -> b.toString()).orElse("")),
			param("from", from.map(b -> b.toString()).orElse(""))
		), catcher)
		.filter(s -> !s.equals("false"))
		.filter(s -> !s.isEmpty())
		.map(s -> JSONImage.parseFrom(s))
				
		.orElseThrow(() -> 
			new IllegalArgumentException("'browse' did not give any results. Logged in?")
		);
	}
	
	@Override
	public List<JSONImage> browse() {
		final List<JSONImage> result = browse(Optional.ofNullable(lastBrowse));
		
		lastBrowse = result.stream()
			.sorted(Comparator.reverseOrder())
			.findFirst()
			.map(i -> i.getUploaded())
			.orElse(lastBrowse);
		
		return result;
	}
	
	private boolean send(String command, String mail, String password) {
		return post(host + "/" + command, params(
			param("mail", mail),
			param("password", password)
		), catcher)
		.filter(s -> !s.equals("false"))
		.filter(s -> !s.isEmpty())
		.map(s -> sessionKey = s)
		.isPresent();
	}
	
	public boolean isLoggedIn() {
		return sessionKey != null;
	}
}