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

import static com.speedment.examples.social.rest.Rest.encode;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;

/**
 *
 * @author Emil Forslund
 */
class RestImpl implements Rest {
   
    private final Protocol protocol;
    private final String host;
    private final int port;
    private final String username;
    private final String password;

    RestImpl(Protocol protocol, String host, int port, String username, String password) {
        this.protocol = requireNonNull(protocol);
        this.host     = requireNonNull(host);
        this.port     = port;
        this.username = username; // Can be null
        this.password = password; // Can be null
    }
    
    @Override
    public CompletableFuture<Response> get(String path, Param... param) {
        return send(Method.GET, path, param);
    }

    @Override
    public CompletableFuture<Response> post(String path, Param... param) {
        return send(Method.POST, path, param);
    }

    @Override
    public CompletableFuture<Response> delete(String path, Param... param) {
        return send(Method.DELETE, path, param);
    }

    @Override
    public CompletableFuture<Response> put(String path, Param... param) {
        return send(Method.PUT, path, param);
    }

    @Override
    public CompletableFuture<Response> options(String path, Param... param) {
        return send(Method.OPTIONS, path, param);
    }
    
    protected String getProtocol() {
        switch (protocol) {
            case HTTP : return "http";
            case HTTPS : return "https";
            default : throw new UnsupportedOperationException(
                "Unknown enum constant '" + protocol + "'."
            );
        }
    }

    protected String getHost() {
        return host;
    }

    protected int getPort() {
        return port;
    }
    
    protected final URL getUrl(String path, Param... param) {
        try {
            final StringBuilder url = new StringBuilder()
                .append(getProtocol())
                .append("://")
                .append(host);
            
            if (port > 0) {
                url.append(":").append(port);
            }
            
            url.append("/").append(path).append(
                Stream.of(param)
                    .map(p -> 
                        encode(p.getKey()) + 
                        "=" + 
                        encode(p.getValue())
                    )
                    .collect(joining("&", "?", ""))
            );
            
            return new URL(url.toString());
        } catch (final MalformedURLException ex) {
            throw new RuntimeException("Error building URL.", ex);
        }
    }
    
    private CompletableFuture<Response> send(Method method, String path, Param... params) {
        return CompletableFuture.supplyAsync(() -> {
            final URL url = getUrl(path, params);
            
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) url.openConnection();
                
                switch (method) {
                    case POST    : conn.setRequestMethod("POST"); break;
                    case GET     : conn.setRequestMethod("GET"); break;
                    case DELETE  : conn.setRequestMethod("DELETE"); break;
                    case OPTIONS : conn.setRequestMethod("OPTIONS"); break;
                    case PUT     : conn.setRequestMethod("PUT"); break;
                    default : throw new UnsupportedOperationException(
                        "Unknown enum constant '" + method + "'."
                    );
                }
                
                if (username != null && password != null) {
                    final byte[] authentication = (username + ":" + password).getBytes();
                    final String encoding = Base64.getEncoder().encodeToString(authentication);
                    conn.setRequestProperty("Authorization", "Basic " + encoding);
                }
                
                conn.setUseCaches(false);
                conn.setAllowUserInteraction(false);

                final int status = conn.getResponseCode();
                final String text;
                
                try (final BufferedReader rd = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {
                    
                    final StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = rd.readLine()) != null) {
                        sb.append(line);
                    }
                    
                    text = sb.toString();
                }
                
                return new Response(status, text);
            } catch (final IOException ex) {
                throw new RuntimeException("Could not send get-command.", ex);
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        });
    }
}