package com.speedment.examples.polaroid;

import static com.speedment.examples.polaroid.Http.*;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Emil Forslund
 */
public class Client implements ClientAPI {
	private final String host;
	private String sessionKey;
	private LocalDateTime lastBrowse;
	
	public Client(String host) {
		this.host = host;
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
			param("description", description),
			param("imgdata", imgData),
			param("sessionkey", sessionKey)
		))
		.filter(s -> !s.equals("false"))
		.filter(s -> !s.isEmpty())
		.map(s -> true).orElse(false);
	}

	@Override
	public List<JSONUser> find(String freeText) {
		return post(host + "/find", params(
			param("freetext", freeText),
			param("sessionkey", sessionKey)
		))
		.filter(s -> !s.equals("false"))
		.filter(s -> !s.isEmpty())
		.map(s -> JSONUser.parse(s)).orElseThrow(() -> 
			new IllegalArgumentException("'find' did not give any results. Logged in?")
		);
	}

	@Override
	public boolean follow(long userId) {
		return post(host + "/follow", params(
			param("userid", Long.toString(userId)),
			param("sessionkey", sessionKey)
		))
		.filter(s -> !s.equals("false"))
		.filter(s -> !s.isEmpty())
		.map(s -> true).orElse(false);
	}

	@Override
	public List<JSONImage> browse(Optional<LocalDateTime> after, Optional<LocalDateTime> before) {
		return post(host + "/browse", params(
			param("sessionkey", sessionKey),
			param("before", before.map(b -> b.toString()).orElse("")),
			param("after", after.map(b -> b.toString()).orElse(""))
		))
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
		))
		.filter(s -> !s.equals("false"))
		.filter(s -> !s.isEmpty())
		.map(s -> sessionKey = s)
		.map(s -> true).orElse(false);
	}
	
	public boolean isLoggedIn() {
		return sessionKey != null;
	}
}
