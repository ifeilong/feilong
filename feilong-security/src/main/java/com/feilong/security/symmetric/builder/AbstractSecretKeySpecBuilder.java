/*
 * Copyright (C) 2008 feilong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.feilong.security.symmetric.builder;

import java.security.Key;

import javax.crypto.spec.SecretKeySpec;

/**
 * 抽象的 KeyBuilder.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.0.0
 */
public abstract class AbstractSecretKeySpecBuilder implements KeyBuilder{

    @Override
    public Key build(String algorithm,String keyString){
        byte[] keyBytes = buildKeyBytes(algorithm, keyString);
        return new SecretKeySpec(keyBytes, algorithm);
    }

    //---------------------------------------------------------------

    /**
     * Builds the key bytes.
     *
     * @param keyString
     *            the key string
     * @return the byte[]
     */
    protected abstract byte[] buildKeyBytes(String algorithm,String keyString);
}
