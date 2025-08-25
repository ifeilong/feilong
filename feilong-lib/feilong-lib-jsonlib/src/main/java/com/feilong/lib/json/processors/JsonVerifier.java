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

package com.feilong.lib.json.processors;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.feilong.lib.json.JSON;
import com.feilong.lib.json.JSONNull;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Verifies if a value is a valid JSON value.
 *
 * @author <a href="mailto:aalmiray@users.sourceforge.net">Andres Almiray</a>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JsonVerifier{

    /**
     * Verifies if value is a valid JSON value.
     *
     * @param value
     *            the value to verify
     * @return true, if is valid json value
     */
    public static boolean isValidJsonValue(Object value){
        return JSONNull.getInstance().equals(value) //
                        || value instanceof JSON //

                        || value instanceof Boolean //
                        || value instanceof Byte //
                        || value instanceof Short//
                        || value instanceof Integer //
                        || value instanceof Long //
                        || value instanceof Double //
                        || value instanceof BigInteger //
                        || value instanceof BigDecimal //
                        || value instanceof String;
    }
}