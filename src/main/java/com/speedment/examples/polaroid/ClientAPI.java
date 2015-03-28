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
	JSONUser self();
	JSONUser update(String mail, String firstname, String lastname, String imgData);
	boolean follow(long userId);
	List<JSONImage> browse(Optional<LocalDateTime> from, Optional<LocalDateTime> to);
	
	default List<JSONImage> browse(Optional<LocalDateTime> after) {
		return browse(Optional.empty(), after);
	}
	
	default List<JSONImage> browse() {
		return browse(Optional.empty(), Optional.empty());
	}
	
}
