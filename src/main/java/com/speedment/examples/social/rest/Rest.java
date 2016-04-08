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
package com.speedment.examples.social.rest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.CompletableFuture;

/**
 * This is the main interface of the REST system.
 * <p>
 * Usage:
 * <pre>
 *     Rest rest = Rest.get("127.0.0.1", 8080);
 * 
 *     CompletableFuture&lt;Response&gt; future = rest.get("/user", 
 *         Param.of("firstname", "Adam"), 
 *         Param.of("lastname", "Adamsson")
 *     );
 * 
 *     future.get();
 * </pre>
 * 
 * @author Emil Forslund
 */
public interface Rest {
    
    final String ENCODING = "UTF-8";
    
    enum Method {
        POST, GET, DELETE, PUT, OPTIONS
    }
    
    enum Protocol {
        HTTP, HTTPS
    }
    
    CompletableFuture<Response> get(String path, Param... param);
    CompletableFuture<Response> post(String path, Param... param);
    CompletableFuture<Response> delete(String path, Param... param);
    CompletableFuture<Response> put(String path, Param... param);
    CompletableFuture<Response> options(String path, Param... param);
    
    static Rest get(String host, int port) {
        return new RestImpl(Protocol.HTTP, host, port, null, null);
    }
    
    static Rest auth(String host, int port, String username, String password) {
        return new RestImpl(Protocol.HTTP, host, port, username, password);
    }
    
    static String encode(String value) {
        try {
            return URLEncoder.encode(value, ENCODING);
        } catch (final UnsupportedEncodingException ex) {
            throw new RuntimeException("Error encoding value '" + value + "'.");
        }
    }
}
