/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.speedment.examples.polaroid;

import static com.speedment.examples.polaroid.Http.*;
import java.util.Optional;

/**
 *
 * @author Emil Forslund
 */
public class Client {
	private final String host;
	private String sessionKey;
	
	public Client(String host) {
		this.host = host;
	}
	
	public boolean ping() {
		final Optional<String> result = post(host, param("c", "ping"));

		if (result.filter(s -> "true".equals(s)).isPresent()) {
			return true;
		} else {
			throw new UnsupportedOperationException("Could not connect to " + host + ".");
		}
	}
	
	public boolean login(String mail, String password) {
		return send("login", mail, password);
	}
	
	public boolean register(String mail, String password) {
		return send("register", mail, password);
	}
	
	public JSONImage getImage() {
		return new JSONImage(
			post(host, param("c", "ping")).orElseThrow(() ->
				new UnsupportedOperationException(
					"Failed to retreive image."
				)
			)
		);
	}
	
	private boolean send(String command, String mail, String password) {
		return post(host, params(
			param("c", command),
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
