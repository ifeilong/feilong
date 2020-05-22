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
package com.feilong.lib.collection4;

import java.io.PrintStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.SortedMap;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;

/**
 * Provides utility methods and decorators for
 * {@link Map} and {@link SortedMap} instances.
 * <p>
 * It contains various type safe methods
 * as well as other useful features like deep copying.
 * </p>
 * <p>
 * It also provides the following decorators:
 * </p>
 *
 * <ul>
 * <li>{@link #fixedSizeMap(Map)}
 * <li>{@link #fixedSizeSortedMap(SortedMap)}
 * <li>{@link #lazyMap(Map,Factory)}
 * <li>{@link #lazyMap(Map,Transformer)}
 * <li>{@link #lazySortedMap(SortedMap,Factory)}
 * <li>{@link #lazySortedMap(SortedMap,Transformer)}
 * <li>{@link #predicatedMap(Map,Predicate,Predicate)}
 * <li>{@link #predicatedSortedMap(SortedMap,Predicate,Predicate)}
 * <li>{@link #transformedMap(Map, Transformer, Transformer)}
 * <li>{@link #transformedSortedMap(SortedMap, Transformer, Transformer)}
 * <li>{@link #multiValueMap( Map )}
 * <li>{@link #multiValueMap( Map, Class )}
 * <li>{@link #multiValueMap( Map, Factory )}
 * </ul>
 *
 * @since 1.0
 */
public class MapUtils{

    /**
     * String used to indent the verbose and debug Map prints.
     */
    private static final String INDENT_STRING = "    ";

    /**
     * <code>MapUtils</code> should not normally be instantiated.
     */
    private MapUtils(){
    }

    // Type safe getters
    //-------------------------------------------------------------------------
    /**
     * Gets from a Map in a null-safe manner.
     *
     * @param <K>
     *            the key type
     * @param <V>
     *            the value type
     * @param map
     *            the map to use
     * @param key
     *            the key to look up
     * @return the value in the Map, <code>null</code> if null map input
     */
    public static <K, V> V getObject(final Map<? super K, V> map,final K key){
        if (map != null){
            return map.get(key);
        }
        return null;
    }

    /**
     * Gets a String from a Map in a null-safe manner.
     * <p>
     * The String is obtained via <code>toString</code>.
     *
     * @param <K>
     *            the key type
     * @param map
     *            the map to use
     * @param key
     *            the key to look up
     * @return the value in the Map as a String, <code>null</code> if null map input
     */
    public static <K> String getString(final Map<? super K, ?> map,final K key){
        if (map != null){
            final Object answer = map.get(key);
            if (answer != null){
                return answer.toString();
            }
        }
        return null;
    }

    /**
     * Gets a Boolean from a Map in a null-safe manner.
     * <p>
     * If the value is a <code>Boolean</code> it is returned directly.
     * If the value is a <code>String</code> and it equals 'true' ignoring case
     * then <code>true</code> is returned, otherwise <code>false</code>.
     * If the value is a <code>Number</code> an integer zero value returns
     * <code>false</code> and non-zero returns <code>true</code>.
     * Otherwise, <code>null</code> is returned.
     *
     * @param <K>
     *            the key type
     * @param map
     *            the map to use
     * @param key
     *            the key to look up
     * @return the value in the Map as a Boolean, <code>null</code> if null map input
     */
    public static <K> Boolean getBoolean(final Map<? super K, ?> map,final K key){
        if (map != null){
            final Object answer = map.get(key);
            if (answer != null){
                if (answer instanceof Boolean){
                    return (Boolean) answer;
                }
                if (answer instanceof String){
                    return Boolean.valueOf((String) answer);
                }
                if (answer instanceof Number){
                    final Number n = (Number) answer;
                    return n.intValue() != 0 ? Boolean.TRUE : Boolean.FALSE;
                }
            }
        }
        return null;
    }

    /**
     * Gets a Number from a Map in a null-safe manner.
     * <p>
     * If the value is a <code>Number</code> it is returned directly.
     * If the value is a <code>String</code> it is converted using
     * {@link NumberFormat#parse(String)} on the system default formatter
     * returning <code>null</code> if the conversion fails.
     * Otherwise, <code>null</code> is returned.
     *
     * @param <K>
     *            the key type
     * @param map
     *            the map to use
     * @param key
     *            the key to look up
     * @return the value in the Map as a Number, <code>null</code> if null map input
     */
    public static <K> Number getNumber(final Map<? super K, ?> map,final K key){
        if (map != null){
            final Object answer = map.get(key);
            if (answer != null){
                if (answer instanceof Number){
                    return (Number) answer;
                }
                if (answer instanceof String){
                    try{
                        final String text = (String) answer;
                        return NumberFormat.getInstance().parse(text);
                    }catch (final ParseException e){ // NOPMD
                        // failure means null is returned
                    }
                }
            }
        }
        return null;
    }

    /**
     * Gets a Byte from a Map in a null-safe manner.
     * <p>
     * The Byte is obtained from the results of {@link #getNumber(Map,Object)}.
     *
     * @param <K>
     *            the key type
     * @param map
     *            the map to use
     * @param key
     *            the key to look up
     * @return the value in the Map as a Byte, <code>null</code> if null map input
     */
    public static <K> Byte getByte(final Map<? super K, ?> map,final K key){
        final Number answer = getNumber(map, key);
        if (answer == null){
            return null;
        }
        if (answer instanceof Byte){
            return (Byte) answer;
        }
        return Byte.valueOf(answer.byteValue());
    }

    /**
     * Gets a Short from a Map in a null-safe manner.
     * <p>
     * The Short is obtained from the results of {@link #getNumber(Map,Object)}.
     *
     * @param <K>
     *            the key type
     * @param map
     *            the map to use
     * @param key
     *            the key to look up
     * @return the value in the Map as a Short, <code>null</code> if null map input
     */
    public static <K> Short getShort(final Map<? super K, ?> map,final K key){
        final Number answer = getNumber(map, key);
        if (answer == null){
            return null;
        }
        if (answer instanceof Short){
            return (Short) answer;
        }
        return Short.valueOf(answer.shortValue());
    }

    /**
     * Gets a Integer from a Map in a null-safe manner.
     * <p>
     * The Integer is obtained from the results of {@link #getNumber(Map,Object)}.
     *
     * @param <K>
     *            the key type
     * @param map
     *            the map to use
     * @param key
     *            the key to look up
     * @return the value in the Map as a Integer, <code>null</code> if null map input
     */
    public static <K> Integer getInteger(final Map<? super K, ?> map,final K key){
        final Number answer = getNumber(map, key);
        if (answer == null){
            return null;
        }
        if (answer instanceof Integer){
            return (Integer) answer;
        }
        return Integer.valueOf(answer.intValue());
    }

    /**
     * Gets a Long from a Map in a null-safe manner.
     * <p>
     * The Long is obtained from the results of {@link #getNumber(Map,Object)}.
     *
     * @param <K>
     *            the key type
     * @param map
     *            the map to use
     * @param key
     *            the key to look up
     * @return the value in the Map as a Long, <code>null</code> if null map input
     */
    public static <K> Long getLong(final Map<? super K, ?> map,final K key){
        final Number answer = getNumber(map, key);
        if (answer == null){
            return null;
        }
        if (answer instanceof Long){
            return (Long) answer;
        }
        return Long.valueOf(answer.longValue());
    }

    /**
     * Gets a Float from a Map in a null-safe manner.
     * <p>
     * The Float is obtained from the results of {@link #getNumber(Map,Object)}.
     *
     * @param <K>
     *            the key type
     * @param map
     *            the map to use
     * @param key
     *            the key to look up
     * @return the value in the Map as a Float, <code>null</code> if null map input
     */
    public static <K> Float getFloat(final Map<? super K, ?> map,final K key){
        final Number answer = getNumber(map, key);
        if (answer == null){
            return null;
        }
        if (answer instanceof Float){
            return (Float) answer;
        }
        return Float.valueOf(answer.floatValue());
    }

    /**
     * Gets a Double from a Map in a null-safe manner.
     * <p>
     * The Double is obtained from the results of {@link #getNumber(Map,Object)}.
     *
     * @param <K>
     *            the key type
     * @param map
     *            the map to use
     * @param key
     *            the key to look up
     * @return the value in the Map as a Double, <code>null</code> if null map input
     */
    public static <K> Double getDouble(final Map<? super K, ?> map,final K key){
        final Number answer = getNumber(map, key);
        if (answer == null){
            return null;
        }
        if (answer instanceof Double){
            return (Double) answer;
        }
        return Double.valueOf(answer.doubleValue());
    }

    /**
     * Gets a Map from a Map in a null-safe manner.
     * <p>
     * If the value returned from the specified map is not a Map then
     * <code>null</code> is returned.
     *
     * @param <K>
     *            the key type
     * @param map
     *            the map to use
     * @param key
     *            the key to look up
     * @return the value in the Map as a Map, <code>null</code> if null map input
     */
    public static <K> Map<?, ?> getMap(final Map<? super K, ?> map,final K key){
        if (map != null){
            final Object answer = map.get(key);
            if (answer != null && answer instanceof Map){
                return (Map<?, ?>) answer;
            }
        }
        return null;
    }

    // Type safe getters with default values
    //-------------------------------------------------------------------------
    /**
     * Looks up the given key in the given map, converting null into the
     * given default value.
     *
     * @param <K>
     *            the key type
     * @param <V>
     *            the value type
     * @param map
     *            the map whose value to look up
     * @param key
     *            the key of the value to look up in that map
     * @param defaultValue
     *            what to return if the value is null
     * @return the value in the map, or defaultValue if the original value
     *         is null or the map is null
     */
    public static <K, V> V getObject(final Map<K, V> map,final K key,final V defaultValue){
        if (map != null){
            final V answer = map.get(key);
            if (answer != null){
                return answer;
            }
        }
        return defaultValue;
    }

    /**
     * Looks up the given key in the given map, converting the result into
     * a string, using the default value if the conversion fails.
     *
     * @param <K>
     *            the key type
     * @param map
     *            the map whose value to look up
     * @param key
     *            the key of the value to look up in that map
     * @param defaultValue
     *            what to return if the value is null or if the
     *            conversion fails
     * @return the value in the map as a string, or defaultValue if the
     *         original value is null, the map is null or the string conversion fails
     */
    public static <K> String getString(final Map<? super K, ?> map,final K key,final String defaultValue){
        String answer = getString(map, key);
        if (answer == null){
            answer = defaultValue;
        }
        return answer;
    }

    /**
     * Looks up the given key in the given map, converting the result into
     * a boolean, using the default value if the conversion fails.
     *
     * @param <K>
     *            the key type
     * @param map
     *            the map whose value to look up
     * @param key
     *            the key of the value to look up in that map
     * @param defaultValue
     *            what to return if the value is null or if the
     *            conversion fails
     * @return the value in the map as a boolean, or defaultValue if the
     *         original value is null, the map is null or the boolean conversion fails
     */
    public static <K> Boolean getBoolean(final Map<? super K, ?> map,final K key,final Boolean defaultValue){
        Boolean answer = getBoolean(map, key);
        if (answer == null){
            answer = defaultValue;
        }
        return answer;
    }

    /**
     * Looks up the given key in the given map, converting the result into
     * a number, using the default value if the conversion fails.
     *
     * @param <K>
     *            the key type
     * @param map
     *            the map whose value to look up
     * @param key
     *            the key of the value to look up in that map
     * @param defaultValue
     *            what to return if the value is null or if the
     *            conversion fails
     * @return the value in the map as a number, or defaultValue if the
     *         original value is null, the map is null or the number conversion fails
     */
    public static <K> Number getNumber(final Map<? super K, ?> map,final K key,final Number defaultValue){
        Number answer = getNumber(map, key);
        if (answer == null){
            answer = defaultValue;
        }
        return answer;
    }

    /**
     * Looks up the given key in the given map, converting the result into
     * a byte, using the default value if the conversion fails.
     *
     * @param <K>
     *            the key type
     * @param map
     *            the map whose value to look up
     * @param key
     *            the key of the value to look up in that map
     * @param defaultValue
     *            what to return if the value is null or if the
     *            conversion fails
     * @return the value in the map as a number, or defaultValue if the
     *         original value is null, the map is null or the number conversion fails
     */
    public static <K> Byte getByte(final Map<? super K, ?> map,final K key,final Byte defaultValue){
        Byte answer = getByte(map, key);
        if (answer == null){
            answer = defaultValue;
        }
        return answer;
    }

    /**
     * Looks up the given key in the given map, converting the result into
     * a short, using the default value if the conversion fails.
     *
     * @param <K>
     *            the key type
     * @param map
     *            the map whose value to look up
     * @param key
     *            the key of the value to look up in that map
     * @param defaultValue
     *            what to return if the value is null or if the
     *            conversion fails
     * @return the value in the map as a number, or defaultValue if the
     *         original value is null, the map is null or the number conversion fails
     */
    public static <K> Short getShort(final Map<? super K, ?> map,final K key,final Short defaultValue){
        Short answer = getShort(map, key);
        if (answer == null){
            answer = defaultValue;
        }
        return answer;
    }

    /**
     * Looks up the given key in the given map, converting the result into
     * an integer, using the default value if the conversion fails.
     *
     * @param <K>
     *            the key type
     * @param map
     *            the map whose value to look up
     * @param key
     *            the key of the value to look up in that map
     * @param defaultValue
     *            what to return if the value is null or if the
     *            conversion fails
     * @return the value in the map as a number, or defaultValue if the
     *         original value is null, the map is null or the number conversion fails
     */
    public static <K> Integer getInteger(final Map<? super K, ?> map,final K key,final Integer defaultValue){
        Integer answer = getInteger(map, key);
        if (answer == null){
            answer = defaultValue;
        }
        return answer;
    }

    /**
     * Looks up the given key in the given map, converting the result into
     * a long, using the default value if the conversion fails.
     *
     * @param <K>
     *            the key type
     * @param map
     *            the map whose value to look up
     * @param key
     *            the key of the value to look up in that map
     * @param defaultValue
     *            what to return if the value is null or if the
     *            conversion fails
     * @return the value in the map as a number, or defaultValue if the
     *         original value is null, the map is null or the number conversion fails
     */
    public static <K> Long getLong(final Map<? super K, ?> map,final K key,final Long defaultValue){
        Long answer = getLong(map, key);
        if (answer == null){
            answer = defaultValue;
        }
        return answer;
    }

    /**
     * Looks up the given key in the given map, converting the result into
     * a float, using the default value if the conversion fails.
     *
     * @param <K>
     *            the key type
     * @param map
     *            the map whose value to look up
     * @param key
     *            the key of the value to look up in that map
     * @param defaultValue
     *            what to return if the value is null or if the
     *            conversion fails
     * @return the value in the map as a number, or defaultValue if the
     *         original value is null, the map is null or the number conversion fails
     */
    public static <K> Float getFloat(final Map<? super K, ?> map,final K key,final Float defaultValue){
        Float answer = getFloat(map, key);
        if (answer == null){
            answer = defaultValue;
        }
        return answer;
    }

    /**
     * Looks up the given key in the given map, converting the result into
     * a double, using the default value if the conversion fails.
     *
     * @param <K>
     *            the key type
     * @param map
     *            the map whose value to look up
     * @param key
     *            the key of the value to look up in that map
     * @param defaultValue
     *            what to return if the value is null or if the
     *            conversion fails
     * @return the value in the map as a number, or defaultValue if the
     *         original value is null, the map is null or the number conversion fails
     */
    public static <K> Double getDouble(final Map<? super K, ?> map,final K key,final Double defaultValue){
        Double answer = getDouble(map, key);
        if (answer == null){
            answer = defaultValue;
        }
        return answer;
    }

    /**
     * Looks up the given key in the given map, converting the result into
     * a map, using the default value if the conversion fails.
     *
     * @param <K>
     *            the key type
     * @param map
     *            the map whose value to look up
     * @param key
     *            the key of the value to look up in that map
     * @param defaultValue
     *            what to return if the value is null or if the
     *            conversion fails
     * @return the value in the map as a number, or defaultValue if the
     *         original value is null, the map is null or the map conversion fails
     */
    public static <K> Map<?, ?> getMap(final Map<? super K, ?> map,final K key,final Map<?, ?> defaultValue){
        Map<?, ?> answer = getMap(map, key);
        if (answer == null){
            answer = defaultValue;
        }
        return answer;
    }

    // Type safe primitive getters
    //-------------------------------------------------------------------------
    /**
     * Gets a boolean from a Map in a null-safe manner.
     * <p>
     * If the value is a <code>Boolean</code> its value is returned.
     * If the value is a <code>String</code> and it equals 'true' ignoring case
     * then <code>true</code> is returned, otherwise <code>false</code>.
     * If the value is a <code>Number</code> an integer zero value returns
     * <code>false</code> and non-zero returns <code>true</code>.
     * Otherwise, <code>false</code> is returned.
     *
     * @param <K>
     *            the key type
     * @param map
     *            the map to use
     * @param key
     *            the key to look up
     * @return the value in the Map as a Boolean, <code>false</code> if null map input
     */
    public static <K> boolean getBooleanValue(final Map<? super K, ?> map,final K key){
        return Boolean.TRUE.equals(getBoolean(map, key));
    }

    /**
     * Gets a byte from a Map in a null-safe manner.
     * <p>
     * The byte is obtained from the results of {@link #getNumber(Map,Object)}.
     *
     * @param <K>
     *            the key type
     * @param map
     *            the map to use
     * @param key
     *            the key to look up
     * @return the value in the Map as a byte, <code>0</code> if null map input
     */
    public static <K> byte getByteValue(final Map<? super K, ?> map,final K key){
        final Byte byteObject = getByte(map, key);
        if (byteObject == null){
            return 0;
        }
        return byteObject.byteValue();
    }

    /**
     * Gets a short from a Map in a null-safe manner.
     * <p>
     * The short is obtained from the results of {@link #getNumber(Map,Object)}.
     *
     * @param <K>
     *            the key type
     * @param map
     *            the map to use
     * @param key
     *            the key to look up
     * @return the value in the Map as a short, <code>0</code> if null map input
     */
    public static <K> short getShortValue(final Map<? super K, ?> map,final K key){
        final Short shortObject = getShort(map, key);
        if (shortObject == null){
            return 0;
        }
        return shortObject.shortValue();
    }

    /**
     * Gets an int from a Map in a null-safe manner.
     * <p>
     * The int is obtained from the results of {@link #getNumber(Map,Object)}.
     *
     * @param <K>
     *            the key type
     * @param map
     *            the map to use
     * @param key
     *            the key to look up
     * @return the value in the Map as an int, <code>0</code> if null map input
     */
    public static <K> int getIntValue(final Map<? super K, ?> map,final K key){
        final Integer integerObject = getInteger(map, key);
        if (integerObject == null){
            return 0;
        }
        return integerObject.intValue();
    }

    /**
     * Gets a long from a Map in a null-safe manner.
     * <p>
     * The long is obtained from the results of {@link #getNumber(Map,Object)}.
     *
     * @param <K>
     *            the key type
     * @param map
     *            the map to use
     * @param key
     *            the key to look up
     * @return the value in the Map as a long, <code>0L</code> if null map input
     */
    public static <K> long getLongValue(final Map<? super K, ?> map,final K key){
        final Long longObject = getLong(map, key);
        if (longObject == null){
            return 0L;
        }
        return longObject.longValue();
    }

    /**
     * Gets a float from a Map in a null-safe manner.
     * <p>
     * The float is obtained from the results of {@link #getNumber(Map,Object)}.
     *
     * @param <K>
     *            the key type
     * @param map
     *            the map to use
     * @param key
     *            the key to look up
     * @return the value in the Map as a float, <code>0.0F</code> if null map input
     */
    public static <K> float getFloatValue(final Map<? super K, ?> map,final K key){
        final Float floatObject = getFloat(map, key);
        if (floatObject == null){
            return 0f;
        }
        return floatObject.floatValue();
    }

    /**
     * Gets a double from a Map in a null-safe manner.
     * <p>
     * The double is obtained from the results of {@link #getNumber(Map,Object)}.
     *
     * @param <K>
     *            the key type
     * @param map
     *            the map to use
     * @param key
     *            the key to look up
     * @return the value in the Map as a double, <code>0.0</code> if null map input
     */
    public static <K> double getDoubleValue(final Map<? super K, ?> map,final K key){
        final Double doubleObject = getDouble(map, key);
        if (doubleObject == null){
            return 0d;
        }
        return doubleObject.doubleValue();
    }

    // Type safe primitive getters with default values
    //-------------------------------------------------------------------------
    /**
     * Gets a boolean from a Map in a null-safe manner,
     * using the default value if the conversion fails.
     * <p>
     * If the value is a <code>Boolean</code> its value is returned.
     * If the value is a <code>String</code> and it equals 'true' ignoring case
     * then <code>true</code> is returned, otherwise <code>false</code>.
     * If the value is a <code>Number</code> an integer zero value returns
     * <code>false</code> and non-zero returns <code>true</code>.
     * Otherwise, <code>defaultValue</code> is returned.
     *
     * @param <K>
     *            the key type
     * @param map
     *            the map to use
     * @param key
     *            the key to look up
     * @param defaultValue
     *            return if the value is null or if the conversion fails
     * @return the value in the Map as a Boolean, <code>defaultValue</code> if null map input
     */
    public static <K> boolean getBooleanValue(final Map<? super K, ?> map,final K key,final boolean defaultValue){
        final Boolean booleanObject = getBoolean(map, key);
        if (booleanObject == null){
            return defaultValue;
        }
        return booleanObject.booleanValue();
    }

    /**
     * Gets a byte from a Map in a null-safe manner,
     * using the default value if the conversion fails.
     * <p>
     * The byte is obtained from the results of {@link #getNumber(Map,Object)}.
     *
     * @param <K>
     *            the key type
     * @param map
     *            the map to use
     * @param key
     *            the key to look up
     * @param defaultValue
     *            return if the value is null or if the conversion fails
     * @return the value in the Map as a byte, <code>defaultValue</code> if null map input
     */
    public static <K> byte getByteValue(final Map<? super K, ?> map,final K key,final byte defaultValue){
        final Byte byteObject = getByte(map, key);
        if (byteObject == null){
            return defaultValue;
        }
        return byteObject.byteValue();
    }

    /**
     * Gets a short from a Map in a null-safe manner,
     * using the default value if the conversion fails.
     * <p>
     * The short is obtained from the results of {@link #getNumber(Map,Object)}.
     *
     * @param <K>
     *            the key type
     * @param map
     *            the map to use
     * @param key
     *            the key to look up
     * @param defaultValue
     *            return if the value is null or if the conversion fails
     * @return the value in the Map as a short, <code>defaultValue</code> if null map input
     */
    public static <K> short getShortValue(final Map<? super K, ?> map,final K key,final short defaultValue){
        final Short shortObject = getShort(map, key);
        if (shortObject == null){
            return defaultValue;
        }
        return shortObject.shortValue();
    }

    /**
     * Gets an int from a Map in a null-safe manner,
     * using the default value if the conversion fails.
     * <p>
     * The int is obtained from the results of {@link #getNumber(Map,Object)}.
     *
     * @param <K>
     *            the key type
     * @param map
     *            the map to use
     * @param key
     *            the key to look up
     * @param defaultValue
     *            return if the value is null or if the conversion fails
     * @return the value in the Map as an int, <code>defaultValue</code> if null map input
     */
    public static <K> int getIntValue(final Map<? super K, ?> map,final K key,final int defaultValue){
        final Integer integerObject = getInteger(map, key);
        if (integerObject == null){
            return defaultValue;
        }
        return integerObject.intValue();
    }

    /**
     * Gets a long from a Map in a null-safe manner,
     * using the default value if the conversion fails.
     * <p>
     * The long is obtained from the results of {@link #getNumber(Map,Object)}.
     *
     * @param <K>
     *            the key type
     * @param map
     *            the map to use
     * @param key
     *            the key to look up
     * @param defaultValue
     *            return if the value is null or if the conversion fails
     * @return the value in the Map as a long, <code>defaultValue</code> if null map input
     */
    public static <K> long getLongValue(final Map<? super K, ?> map,final K key,final long defaultValue){
        final Long longObject = getLong(map, key);
        if (longObject == null){
            return defaultValue;
        }
        return longObject.longValue();
    }

    /**
     * Gets a float from a Map in a null-safe manner,
     * using the default value if the conversion fails.
     * <p>
     * The float is obtained from the results of {@link #getNumber(Map,Object)}.
     *
     * @param <K>
     *            the key type
     * @param map
     *            the map to use
     * @param key
     *            the key to look up
     * @param defaultValue
     *            return if the value is null or if the conversion fails
     * @return the value in the Map as a float, <code>defaultValue</code> if null map input
     */
    public static <K> float getFloatValue(final Map<? super K, ?> map,final K key,final float defaultValue){
        final Float floatObject = getFloat(map, key);
        if (floatObject == null){
            return defaultValue;
        }
        return floatObject.floatValue();
    }

    /**
     * Gets a double from a Map in a null-safe manner,
     * using the default value if the conversion fails.
     * <p>
     * The double is obtained from the results of {@link #getNumber(Map,Object)}.
     *
     * @param <K>
     *            the key type
     * @param map
     *            the map to use
     * @param key
     *            the key to look up
     * @param defaultValue
     *            return if the value is null or if the conversion fails
     * @return the value in the Map as a double, <code>defaultValue</code> if null map input
     */
    public static <K> double getDoubleValue(final Map<? super K, ?> map,final K key,final double defaultValue){
        final Double doubleObject = getDouble(map, key);
        if (doubleObject == null){
            return defaultValue;
        }
        return doubleObject.doubleValue();
    }

    // Conversion methods
    //-------------------------------------------------------------------------
    /**
     * Gets a new Properties object initialised with the values from a Map.
     * A null input will return an empty properties object.
     * <p>
     * A Properties object may only store non-null keys and values, thus if
     * the provided map contains either a key or value which is {@code null},
     * a {@link NullPointerException} will be thrown.
     *
     * @param <K>
     *            the key type
     * @param <V>
     *            the value type
     * @param map
     *            the map to convert to a Properties object
     * @return the properties object
     * @throws NullPointerException
     *             if a key or value in the provided map is {@code null}
     */
    public static <K, V> Properties toProperties(final Map<K, V> map){
        final Properties answer = new Properties();
        if (map != null){
            for (final Entry<K, V> entry2 : map.entrySet()){
                final Map.Entry<?, ?> entry = entry2;
                final Object key = entry.getKey();
                final Object value = entry.getValue();
                answer.put(key, value);
            }
        }
        return answer;
    }

    /**
     * Creates a new HashMap using data copied from a ResourceBundle.
     *
     * @param resourceBundle
     *            the resource bundle to convert, may not be null
     * @return the hashmap containing the data
     * @throws NullPointerException
     *             if the bundle is null
     */
    public static Map<String, Object> toMap(final ResourceBundle resourceBundle){
        final Enumeration<String> enumeration = resourceBundle.getKeys();
        final Map<String, Object> map = new HashMap<>();

        while (enumeration.hasMoreElements()){
            final String key = enumeration.nextElement();
            final Object value = resourceBundle.getObject(key);
            map.put(key, value);
        }

        return map;
    }

    // Printing methods
    //-------------------------------------------------------------------------
    /**
     * Prints the given map with nice line breaks.
     * <p>
     * This method prints a nicely formatted String describing the Map.
     * Each map entry will be printed with key and value.
     * When the value is a Map, recursive behaviour occurs.
     * <p>
     * This method is NOT thread-safe in any special way. You must manually
     * synchronize on either this class or the stream as required.
     *
     * @param out
     *            the stream to print to, must not be null
     * @param label
     *            The label to be used, may be <code>null</code>.
     *            If <code>null</code>, the label is not output.
     *            It typically represents the name of the property in a bean or similar.
     * @param map
     *            The map to print, may be <code>null</code>.
     *            If <code>null</code>, the text 'null' is output.
     * @throws NullPointerException
     *             if the stream is <code>null</code>
     */
    public static void verbosePrint(final PrintStream out,final Object label,final Map<?, ?> map){
        verbosePrintInternal(out, label, map, new ArrayDeque<Map<?, ?>>(), false);
    }

    /**
     * Prints the given map with nice line breaks.
     * <p>
     * This method prints a nicely formatted String describing the Map.
     * Each map entry will be printed with key, value and value classname.
     * When the value is a Map, recursive behaviour occurs.
     * <p>
     * This method is NOT thread-safe in any special way. You must manually
     * synchronize on either this class or the stream as required.
     *
     * @param out
     *            the stream to print to, must not be null
     * @param label
     *            The label to be used, may be <code>null</code>.
     *            If <code>null</code>, the label is not output.
     *            It typically represents the name of the property in a bean or similar.
     * @param map
     *            The map to print, may be <code>null</code>.
     *            If <code>null</code>, the text 'null' is output.
     * @throws NullPointerException
     *             if the stream is <code>null</code>
     */
    public static void debugPrint(final PrintStream out,final Object label,final Map<?, ?> map){
        verbosePrintInternal(out, label, map, new ArrayDeque<Map<?, ?>>(), true);
    }

    // Implementation methods
    //-------------------------------------------------------------------------
    /**
     * Implementation providing functionality for {@link #debugPrint} and for
     * {@link #verbosePrint}. This prints the given map with nice line breaks.
     * If the debug flag is true, it additionally prints the type of the object
     * value. If the contents of a map include the map itself, then the text
     * <em>(this Map)</em> is printed out. If the contents include a
     * parent container of the map, the text <em>(ancestor[i] Map)</em> is
     * printed, where i actually indicates the number of levels which must be
     * traversed in the sequential list of ancestors (e.g. father, grandfather,
     * great-grandfather, etc).
     *
     * @param out
     *            the stream to print to
     * @param label
     *            the label to be used, may be <code>null</code>.
     *            If <code>null</code>, the label is not output.
     *            It typically represents the name of the property in a bean or similar.
     * @param map
     *            the map to print, may be <code>null</code>.
     *            If <code>null</code>, the text 'null' is output
     * @param lineage
     *            a stack consisting of any maps in which the previous
     *            argument is contained. This is checked to avoid infinite recursion when
     *            printing the output
     * @param debug
     *            flag indicating whether type names should be output.
     * @throws NullPointerException
     *             if the stream is <code>null</code>
     */
    private static void verbosePrintInternal(
                    final PrintStream out,
                    final Object label,
                    final Map<?, ?> map,
                    final Deque<Map<?, ?>> lineage,
                    final boolean debug){
        printIndent(out, lineage.size());

        if (map == null){
            if (label != null){
                out.print(label);
                out.print(" = ");
            }
            out.println("null");
            return;
        }
        if (label != null){
            out.print(label);
            out.println(" = ");
        }

        printIndent(out, lineage.size());
        out.println("{");

        lineage.addLast(map);

        for (final Map.Entry<?, ?> entry : map.entrySet()){
            final Object childKey = entry.getKey();
            final Object childValue = entry.getValue();
            if (childValue instanceof Map && !lineage.contains(childValue)){
                verbosePrintInternal(out, childKey == null ? "null" : childKey, (Map<?, ?>) childValue, lineage, debug);
            }else{
                printIndent(out, lineage.size());
                out.print(childKey);
                out.print(" = ");

                final int lineageIndex = IterableUtils.indexOf(lineage, PredicateUtils.equalPredicate(childValue));
                if (lineageIndex == -1){
                    out.print(childValue);
                }else if (lineage.size() - 1 == lineageIndex){
                    out.print("(this Map)");
                }else{
                    out.print("(ancestor[" + (lineage.size() - 1 - lineageIndex - 1) + "] Map)");
                }

                if (debug && childValue != null){
                    out.print(' ');
                    out.println(childValue.getClass().getName());
                }else{
                    out.println();
                }
            }
        }

        lineage.removeLast();

        printIndent(out, lineage.size());
        out.println(debug ? "} " + map.getClass().getName() : "}");
    }

    /**
     * Writes indentation to the given stream.
     *
     * @param out
     *            the stream to indent
     */
    private static void printIndent(final PrintStream out,final int indent){
        for (int i = 0; i < indent; i++){
            out.print(INDENT_STRING);
        }
    }

    // Misc
    //-----------------------------------------------------------------------
    /**
     * Inverts the supplied map returning a new HashMap such that the keys of
     * the input are swapped with the values.
     * <p>
     * This operation assumes that the inverse mapping is well defined.
     * If the input map had multiple entries with the same value mapped to
     * different keys, the returned map will map one of those keys to the
     * value, but the exact key which will be mapped is undefined.
     *
     * @param <K>
     *            the key type
     * @param <V>
     *            the value type
     * @param map
     *            the map to invert, may not be null
     * @return a new HashMap containing the inverted data
     * @throws NullPointerException
     *             if the map is null
     */
    public static <K, V> Map<V, K> invertMap(final Map<K, V> map){
        final Map<V, K> out = new HashMap<>(map.size());
        for (final Entry<K, V> entry : map.entrySet()){
            out.put(entry.getValue(), entry.getKey());
        }
        return out;
    }

    //-----------------------------------------------------------------------
    /**
     * Protects against adding null values to a map.
     * <p>
     * This method checks the value being added to the map, and if it is null
     * it is replaced by an empty string.
     * <p>
     * This could be useful if the map does not accept null values, or for
     * receiving data from a source that may provide null or empty string
     * which should be held in the same way in the map.
     * <p>
     * Keys are not validated.
     * Note that this method can be used to circumvent the map's
     * value type at runtime.
     *
     * @param <K>
     *            the key type
     * @param map
     *            the map to add to, may not be null
     * @param key
     *            the key
     * @param value
     *            the value, null converted to ""
     * @throws NullPointerException
     *             if the map is null
     */
    public static <K> void safeAddToMap(final Map<? super K, Object> map,final K key,final Object value) throws NullPointerException{
        map.put(key, value == null ? "" : value);
    }

    //-----------------------------------------------------------------------

    /**
     * Returns an immutable empty map if the argument is <code>null</code>,
     * or the argument itself otherwise.
     *
     * @param <K>
     *            the key type
     * @param <V>
     *            the value type
     * @param map
     *            the map, possibly <code>null</code>
     * @return an empty map if the argument is <code>null</code>
     */
    public static <K, V> Map<K, V> emptyIfNull(final Map<K, V> map){
        return map == null ? Collections.<K, V> emptyMap() : map;
    }

    /**
     * Null-safe check if the specified map is empty.
     * <p>
     * Null returns true.
     *
     * @param map
     *            the map to check, may be null
     * @return true if empty or null
     * @since 3.2
     */
    public static boolean isEmpty(final Map<?, ?> map){
        return map == null || map.isEmpty();
    }

    /**
     * Null-safe check if the specified map is not empty.
     * <p>
     * Null returns false.
     *
     * @param map
     *            the map to check, may be null
     * @return true if non-null and non-empty
     * @since 3.2
     */
    public static boolean isNotEmpty(final Map<?, ?> map){
        return !MapUtils.isEmpty(map);
    }

    // Map decorators
    //-----------------------------------------------------------------------
    /**
     * Returns a synchronized map backed by the given map.
     * <p>
     * You must manually synchronize on the returned buffer's iterator to
     * avoid non-deterministic behavior:
     *
     * <pre>
     * Map m = MapUtils.synchronizedMap(myMap);
     * Set s = m.keySet(); // outside synchronized block
     * synchronized (m){ // synchronized on MAP!
     *     Iterator i = s.iterator();
     *     while (i.hasNext()){
     *         process(i.next());
     *     }
     * }
     * </pre>
     *
     * This method uses the implementation in {@link java.util.Collections Collections}.
     *
     * @param <K>
     *            the key type
     * @param <V>
     *            the value type
     * @param map
     *            the map to synchronize, must not be null
     * @return a synchronized map backed by the given map
     */
    public static <K, V> Map<K, V> synchronizedMap(final Map<K, V> map){
        return Collections.synchronizedMap(map);
    }

    // SortedMap decorators
    //-----------------------------------------------------------------------
    /**
     * Returns a synchronized sorted map backed by the given sorted map.
     * <p>
     * You must manually synchronize on the returned buffer's iterator to
     * avoid non-deterministic behavior:
     *
     * <pre>
     * Map m = MapUtils.synchronizedSortedMap(myMap);
     * Set s = m.keySet(); // outside synchronized block
     * synchronized (m){ // synchronized on MAP!
     *     Iterator i = s.iterator();
     *     while (i.hasNext()){
     *         process(i.next());
     *     }
     * }
     * </pre>
     *
     * This method uses the implementation in {@link java.util.Collections Collections}.
     *
     * @param <K>
     *            the key type
     * @param <V>
     *            the value type
     * @param map
     *            the map to synchronize, must not be null
     * @return a synchronized map backed by the given map
     * @throws NullPointerException
     *             if the map is null
     */
    public static <K, V> SortedMap<K, V> synchronizedSortedMap(final SortedMap<K, V> map){
        return Collections.synchronizedSortedMap(map);
    }

    /**
     * Populates a Map using the supplied <code>Transformer</code> to transform the elements
     * into keys, using the unaltered element as the value in the <code>Map</code>.
     *
     * @param <K>
     *            the key type
     * @param <V>
     *            the value type
     * @param map
     *            the <code>Map</code> to populate.
     * @param elements
     *            the <code>Iterable</code> containing the input values for the map.
     * @param keyTransformer
     *            the <code>Transformer</code> used to transform the element into a key value
     * @throws NullPointerException
     *             if the map, elements or transformer are null
     */
    public static <K, V> void populateMap(final Map<K, V> map,final Iterable<? extends V> elements,final Transformer<V, K> keyTransformer){
        populateMap(map, elements, keyTransformer, TransformerUtils.<V> nopTransformer());
    }

    /**
     * Populates a Map using the supplied <code>Transformer</code>s to transform the elements
     * into keys and values.
     *
     * @param <K>
     *            the key type
     * @param <V>
     *            the value type
     * @param <E>
     *            the type of object contained in the {@link Iterable}
     * @param map
     *            the <code>Map</code> to populate.
     * @param elements
     *            the <code>Iterable</code> containing the input values for the map.
     * @param keyTransformer
     *            the <code>Transformer</code> used to transform the element into a key value
     * @param valueTransformer
     *            the <code>Transformer</code> used to transform the element into a value
     * @throws NullPointerException
     *             if the map, elements or transformers are null
     */
    public static <K, V, E> void populateMap(
                    final Map<K, V> map,
                    final Iterable<? extends E> elements,
                    final Transformer<E, K> keyTransformer,
                    final Transformer<E, V> valueTransformer){
        final Iterator<? extends E> iter = elements.iterator();
        while (iter.hasNext()){
            final E temp = iter.next();
            map.put(keyTransformer.transform(temp), valueTransformer.transform(temp));
        }
    }

    /**
     * Gets the given map size or 0 if the map is null
     * 
     * @param map
     *            a Map or null
     * @return the given map size or 0 if the map is null
     */
    public static int size(final Map<?, ?> map){
        return map == null ? 0 : map.size();
    }

}
