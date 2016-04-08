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

import java.util.Objects;
import static java.util.Objects.requireNonNull;

/**
 *
 * @author Emil Forslund
 */
public final class Param {
    
    private final String key;
    private final String value;
    
    private Param(String key, String value) {
        this.key   = requireNonNull(key);
        this.value = requireNonNull(value);
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.key);
        hash = 53 * hash + Objects.hashCode(this.value);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        else if (obj == null) return false;
        else if (getClass() != obj.getClass()) return false;
        
        final Param other = (Param) obj;
        return Objects.equals(this.key, other.key)
            && Objects.equals(this.value, other.value);
    }
    
    public static Param param(String key, String value) {
        return new Param(key, value);
    }
}