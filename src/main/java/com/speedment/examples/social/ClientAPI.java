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

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.concurrent.CompletableFuture;

/**
 *
 * @author Emil Forslund
 */
public interface ClientAPI {

	CompletableFuture<Boolean> register(String username, String password);
	CompletableFuture<Boolean> login(String username, String password);
	CompletableFuture<Boolean> upload(String title, String description, String data);
	CompletableFuture<List<JSONUser>> find(String freeText);
	CompletableFuture<Optional<JSONUser>> profile();
	CompletableFuture<Boolean> update(String firstname, String lastname, String data);
	CompletableFuture<Boolean> follow(String usernameToFollow);
	CompletableFuture<List<JSONImage>> browse(OptionalLong from, OptionalLong to);
	
    default CompletableFuture<List<JSONImage>> browse(OptionalLong from) {
		return browse(from, OptionalLong.empty());
	}
	
	default CompletableFuture<List<JSONImage>> browse() {
		return browse(OptionalLong.empty(), OptionalLong.empty());
	}
}