package com.speedment.examples.polaroid;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Emil Forslund
 */
public interface ClientAPI {

	boolean register(String mail, String password);
	boolean login(String mail, String password);
	boolean upload(String title, String description, String imgData);
	List<JSONUser> find(String freeText);
	boolean follow(long userId);
	List<JSONImage> browse(Optional<LocalDateTime> after, Optional<LocalDateTime> before);
	
	default List<JSONImage> browse(Optional<LocalDateTime> after) {
		return browse(Optional.empty(), after);
	}
	
	default List<JSONImage> browse() {
		return browse(Optional.empty(), Optional.empty());
	}
	
}
