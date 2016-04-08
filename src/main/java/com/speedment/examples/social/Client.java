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

import com.speedment.examples.social.rest.Param;
import static com.speedment.examples.social.rest.Param.param;
import com.speedment.examples.social.rest.Response;
import com.speedment.examples.social.rest.Rest;
import static com.speedment.examples.social.rest.Rest.encode;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import static java.util.stream.Collectors.toList;
import java.util.stream.Stream;

/**
 *
 * @author Emil Forslund
 */
public class Client implements ClientAPI {
    
	private final String host;
    private final int port;
	private final Consumer<Throwable> catcher;
	
	private long lastPicture = Long.MIN_VALUE;
    private String username  = null;
    private String password  = null;
	
	public Client(String host, int port, Consumer<Throwable> catcher) {
		this.host = host;
		this.port = port;
		this.catcher = catcher;
	}
    
    private Rest rest() {
        if (username != null && password != null) {
            return Rest.auth(host, port, username, password);
        } else {
            return Rest.get(host, port);
        }
    }
    
    private Response login(String username, String password, Response response) {
        if (response.success()) {
            this.username = username;
            this.password = password;
        }
        
        return response;
    }
	
    @Override
    public CompletableFuture<Boolean> register(String username, String password) {
        return rest().post(
            "user/" + encode(username), 
            param("password", password)
        ).thenApply(res -> login(username, password, res))
         .thenApply(Response::success);
    }

    @Override
    public CompletableFuture<Boolean> login(String username, String password) {
        return Rest.auth(host, port, username, password).get(
            "user/" + encode(username)
        ).thenApply(res -> login(username, password, res))
         .thenApply(Response::success);
    }

    @Override
    public CompletableFuture<Boolean> upload(String title, String description, String data) {
        return rest().post("picture", 
            param("title", title),
            param("description", description),
            param("data", data)
        ).thenApply(Response::success);
    }

    @Override
    public CompletableFuture<List<JSONUser>> find(String freeText) {
        return rest().get("picture", 
            param("search", freeText)
        ).thenApply(res -> res.decodeJsonArray(JSONUser[].class).collect(toList()));
    }

    @Override
    public CompletableFuture<Optional<JSONUser>> profile() {
        return rest().get("user/" + encode(username)
        ).thenApply(res -> res.decodeJson(JSONUser.class));
    }

    @Override
    public CompletableFuture<Boolean> update(String firstname, String lastname, String data) {
        return rest().put("user/" + encode(username), 
            param("firstname", firstname),
            param("lastname", lastname),
            param("data", data)
        ).thenApply(Response::success);
    }

    @Override
    public CompletableFuture<Boolean> follow(String usernameToFollow) {
        return rest().post("follow/" + encode(usernameToFollow)
        ).thenApply(Response::success);
    }

    @Override
    public CompletableFuture<List<JSONImage>> browse(OptionalLong from, OptionalLong to) {
        final Stream.Builder<Param> params = Stream.builder();
        from.ifPresent(f -> params.accept(param("from", "" + f)));
        to.ifPresent(t -> params.accept(param("to", "" + t)));
        
        return rest().post("picture", params.build().toArray(Param[]::new)
        ).thenApply(res -> res.decodeJsonArray(JSONImage[].class).collect(toList()));
    }
}