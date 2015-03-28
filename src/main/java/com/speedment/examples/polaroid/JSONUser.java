package com.speedment.examples.polaroid;

import static com.speedment.examples.polaroid.util.Base64Util.fromBase64;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
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
	private Image avatar;
	
	private JSONUser() {}

	public long getId() {
		return id;
	}

	public String getMail() {
		return mail;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}
	
	public Image getAvatar() {
		return avatar;
	}
	
	public static List<JSONUser> parse(String json) {
		final JSONObject container = (JSONObject) JSONValue.parse(json);
		final JSONArray array	   = (JSONArray) container.get("users");
		final List<JSONUser> users = new ArrayList<>();
		
		array.stream().forEach(u -> {
			users.add(parse((JSONObject) u));
		});
		
		return users;
	}
	
	public static JSONUser parseOne(String json) {
		return parse((JSONObject) JSONValue.parse(json));
	}
	
	public static JSONUser parse(JSONObject user) {
		final JSONUser usr = new JSONUser();
		usr.id        = Long.parseLong(user.get("id").toString());
		usr.mail      = user.get("mail").toString();
		usr.firstname = user.get("firstname").toString();
		usr.lastname  = user.get("lastname").toString();
		usr.avatar    = fromBase64(user.get("avatar").toString());
		return usr;
	}
}