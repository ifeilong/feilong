package com.feilong.lib.digester3.binder;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.util.Map;

/**
 * @since 3.2
 * @param <B>
 *            the parent builder type.
 */
public final class AddAliasBuilder<B> {

    private final B                   parentBuilder;

    private final Map<String, String> aliases;

    private final String              alias;

    AddAliasBuilder(B parentBuilder, Map<String, String> aliases, String alias){
        this.parentBuilder = parentBuilder;
        this.aliases = aliases;
        this.alias = alias;
    }

    /**
     * Allows expressing the input property name alias.
     *
     * @param propertyName
     *            The java bean property to be assigned the value
     * @return the parent builder to chain.
     */
    public B forProperty( /* @Nullable */ String propertyName){
        if (alias != null){
            aliases.put(alias, propertyName);
        }
        return parentBuilder;
    }

}
