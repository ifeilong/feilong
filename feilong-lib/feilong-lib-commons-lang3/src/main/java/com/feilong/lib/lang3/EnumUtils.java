/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.feilong.lib.lang3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Utility library to provide helper methods for Java enums.
 * </p>
 *
 * <p>
 * #ThreadSafe#
 * </p>
 *
 * @since 3.0
 */
public class EnumUtils{

    /**
     * <p>
     * Gets the enum for the class, returning {@code null} if not found.
     * </p>
     *
     * <p>
     * This method differs from {@link Enum#valueOf} in that it does not throw an exception
     * for an invalid enum name.
     * </p>
     *
     * @param <E>
     *            the type of the enumeration
     * @param enumClass
     *            the class of the enum to query, not null
     * @param enumName
     *            the enum name, null returns null
     * @return the enum, null if not found
     */
    public static <E extends Enum<E>> E getEnum(final Class<E> enumClass,final String enumName){
        return getEnum(enumClass, enumName, null);
    }

    /**
     * <p>
     * Gets the enum for the class, returning {@code defaultEnum} if not found.
     * </p>
     *
     * <p>
     * This method differs from {@link Enum#valueOf} in that it does not throw an exception
     * for an invalid enum name.
     * </p>
     *
     * @param <E>
     *            the type of the enumeration
     * @param enumClass
     *            the class of the enum to query, not null
     * @param enumName
     *            the enum name, null returns default enum
     * @param defaultEnum
     *            the default enum
     * @return the enum, default enum if not found
     * @since 3.10
     */
    public static <E extends Enum<E>> E getEnum(final Class<E> enumClass,final String enumName,final E defaultEnum){
        if (enumName == null){
            return defaultEnum;
        }
        try{
            return Enum.valueOf(enumClass, enumName);
        }catch (final IllegalArgumentException ex){
            return defaultEnum;
        }
    }

    /**
     * <p>
     * Gets the enum for the class, returning {@code null} if not found.
     * </p>
     *
     * <p>
     * This method differs from {@link Enum#valueOf} in that it does not throw an exception
     * for an invalid enum name and performs case insensitive matching of the name.
     * </p>
     *
     * @param <E>
     *            the type of the enumeration
     * @param enumClass
     *            the class of the enum to query, not null
     * @param enumName
     *            the enum name, null returns null
     * @return the enum, null if not found
     * @since 3.8
     */
    public static <E extends Enum<E>> E getEnumIgnoreCase(final Class<E> enumClass,final String enumName){
        return getEnumIgnoreCase(enumClass, enumName, null);
    }

    /**
     * <p>
     * Gets the enum for the class, returning {@code defaultEnum} if not found.
     * </p>
     *
     * <p>
     * This method differs from {@link Enum#valueOf} in that it does not throw an exception
     * for an invalid enum name and performs case insensitive matching of the name.
     * </p>
     *
     * @param <E>
     *            the type of the enumeration
     * @param enumClass
     *            the class of the enum to query, not null
     * @param enumName
     *            the enum name, null returns default enum
     * @param defaultEnum
     *            the default enum
     * @return the enum, default enum if not found
     * @since 3.10
     */
    public static <E extends Enum<E>> E getEnumIgnoreCase(final Class<E> enumClass,final String enumName,final E defaultEnum){
        if (enumName == null || !enumClass.isEnum()){
            return defaultEnum;
        }
        for (final E each : enumClass.getEnumConstants()){
            if (each.name().equalsIgnoreCase(enumName)){
                return each;
            }
        }
        return defaultEnum;
    }

    /**
     * <p>
     * Gets the {@code List} of enums.
     * </p>
     *
     * <p>
     * This method is useful when you need a list of enums rather than an array.
     * </p>
     *
     * @param <E>
     *            the type of the enumeration
     * @param enumClass
     *            the class of the enum to query, not null
     * @return the modifiable list of enums, never null
     */
    public static <E extends Enum<E>> List<E> getEnumList(final Class<E> enumClass){
        return new ArrayList<>(Arrays.asList(enumClass.getEnumConstants()));
    }

    /**
     * <p>
     * Gets the {@code Map} of enums by name.
     * </p>
     *
     * <p>
     * This method is useful when you need a map of enums by name.
     * </p>
     *
     * @param <E>
     *            the type of the enumeration
     * @param enumClass
     *            the class of the enum to query, not null
     * @return the modifiable map of enum names to enums, never null
     */
    public static <E extends Enum<E>> Map<String, E> getEnumMap(final Class<E> enumClass){
        final Map<String, E> map = new LinkedHashMap<>();
        for (final E e : enumClass.getEnumConstants()){
            map.put(e.name(), e);
        }
        return map;
    }

}
