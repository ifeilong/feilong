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

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.SortedMap;

import org.apache.commons.collections4.Transformer;

/**
 * Provides utility methods and decorators for
 * {@link Map} and {@link SortedMap} instances.
 * <p>
 * It contains various type safe methods
 * as well as other useful features like deep copying.
 * </p>
 *
 * @since 1.0
 */
public class MapUtils{

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

    // Type safe primitive getters with default values

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

}
