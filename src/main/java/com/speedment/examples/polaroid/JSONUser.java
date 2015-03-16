package com.speedment.examples.polaroid;

import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author Emil Forslund
 */
public class JSONUser {
	
	private long id;
	private String mail;
	private String firstname;
	private String lastname;
	
	private JSONUser() {}
	
	public static List<JSONUser> parse(String json) {
		final JSONObject container   = (JSONObject) JSONValue.parse(json);
		final JSONArray array		 = (JSONArray) container.get("users");
		final List<JSONUser> users = new ArrayList<>();
		
		array.stream().forEach(u -> {
			users.add(parse((JSONObject) u));
		});
		
		return users;
	}
	
	public static JSONUser parse(JSONObject user) {
		final JSONUser usr = new JSONUser();
		usr.id        = Long.parseLong(user.get("id").toString());
		usr.mail      = user.get("mail").toString();
		usr.firstname = user.get("firstname").toString();
		usr.lastname  = user.get("lastname").toString();
		return usr;
	}
}