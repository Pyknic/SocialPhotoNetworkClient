package com.speedment.examples.polaroid;

import java.util.stream.Stream;

/**
 *
 * @author Emil Forslund
 */
public class JSONImage {
	private String title, description, src;
	
	public JSONImage(String msg) {
		msg = msg.substring(1, msg.length() - 2);
		Stream.of(msg.split(",")).map(s -> s.split(":")).filter(s -> s.length == 2).forEach(
			s -> {
				switch (s[0]) {
					case "title" : this.title = s[1]; break;
					case "description" : this.description = s[1]; break;
					case "src" : this.src = s[1]; break;
				}
			}
		);
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getSrc() {
		return src;
	}
}
