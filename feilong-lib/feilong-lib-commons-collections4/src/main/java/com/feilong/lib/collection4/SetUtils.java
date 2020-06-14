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

import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;
import java.util.SortedSet;

/**
 * Provides utility methods and decorators for
 * {@link Set} and {@link SortedSet} instances.
 *
 * @since 2.1
 */
public class SetUtils{

    /**
     * Returns an immutable empty set if the argument is <code>null</code>,
     * or the argument itself otherwise.
     *
     * @param <T>
     *            the element type
     * @param set
     *            the set, possibly <code>null</code>
     * @return an empty set if the argument is <code>null</code>
     */
    public static <T> Set<T> emptyIfNull(final Set<T> set){
        return set == null ? Collections.<T> emptySet() : set;
    }

    //-----------------------------------------------------------------------

    /**
     * Get a typed empty unmodifiable Set.
     * 
     * @param <E>
     *            the element type
     * @return an empty Set
     */
    public static <E> Set<E> emptySet(){
        return Collections.<E> emptySet();
    }

    /**
     * Tests two sets for equality as per the <code>equals()</code> contract
     * in {@link java.util.Set#equals(java.lang.Object)}.
     * <p>
     * This method is useful for implementing <code>Set</code> when you cannot
     * extend AbstractSet. The method takes Collection instances to enable other
     * collection types to use the Set implementation algorithm.
     * <p>
     * The relevant text (slightly paraphrased as this is a static method) is:
     * <blockquote>
     * <p>
     * Two sets are considered equal if they have
     * the same size, and every member of the first set is contained in
     * the second. This ensures that the {@code equals} method works
     * properly across different implementations of the {@code Set}
     * interface.
     * </p>
     *
     * <p>
     * This implementation first checks if the two sets are the same object:
     * if so it returns {@code true}. Then, it checks if the two sets are
     * identical in size; if not, it returns false. If so, it returns
     * {@code a.containsAll((Collection) b)}.
     * </p>
     * </blockquote>
     *
     * @see java.util.Set
     * @param set1
     *            the first set, may be null
     * @param set2
     *            the second set, may be null
     * @return whether the sets are equal by value comparison
     */
    public static boolean isEqualSet(final Collection<?> set1,final Collection<?> set2){
        if (set1 == set2){
            return true;
        }
        if (set1 == null || set2 == null || set1.size() != set2.size()){
            return false;
        }

        return set1.containsAll(set2);
    }

    /**
     * Returns a new hash set that matches elements based on <code>==</code> not
     * <code>equals()</code>.
     * <p>
     * <strong>This set will violate the detail of various Set contracts.</strong>
     * As a general rule, don't compare this set to other sets. In particular, you can't
     * use decorators like {@link ListOrderedSet} on it, which silently assume that these
     * contracts are fulfilled.
     * <p>
     * <strong>Note that the returned set is not synchronized and is not thread-safe.</strong>
     * If you wish to use this set from multiple threads concurrently, you must use
     * appropriate synchronization. The simplest approach is to wrap this map
     * using {@link java.util.Collections#synchronizedSet(Set)}. This class may throw
     * exceptions when accessed by concurrent threads without synchronization.
     *
     * @param <E>
     *            the element type
     * @return a new identity hash set
     * @since 4.1
     */
    public static <E> Set<E> newIdentityHashSet(){
        return Collections.newSetFromMap(new IdentityHashMap<E, Boolean>());
    }

    // Set
    //-----------------------------------------------------------------------
    /**
     * Returns a synchronized set backed by the given set.
     * <p>
     * You must manually synchronize on the returned set's iterator to
     * avoid non-deterministic behavior:
     *
     * <pre>
     * Set s = SetUtils.synchronizedSet(mySet);
     * synchronized (s){
     *     Iterator i = s.iterator();
     *     while (i.hasNext()){
     *         process(i.next());
     *     }
     * }
     * </pre>
     *
     * This method is just a wrapper for {@link Collections#synchronizedSet(Set)}.
     *
     * @param <E>
     *            the element type
     * @param set
     *            the set to synchronize, must not be null
     * @return a synchronized set backed by the given set
     * @throws NullPointerException
     *             if the set is null
     */
    public static <E> Set<E> synchronizedSet(final Set<E> set){
        return Collections.synchronizedSet(set);
    }

    // SortedSet
    //-----------------------------------------------------------------------
    /**
     * Returns a synchronized sorted set backed by the given sorted set.
     * <p>
     * You must manually synchronize on the returned set's iterator to
     * avoid non-deterministic behavior:
     *
     * <pre>
     * Set s = SetUtils.synchronizedSortedSet(mySet);
     * synchronized (s){
     *     Iterator i = s.iterator();
     *     while (i.hasNext()){
     *         process(i.next());
     *     }
     * }
     * </pre>
     *
     * This method is just a wrapper for {@link Collections#synchronizedSortedSet(SortedSet)}.
     *
     * @param <E>
     *            the element type
     * @param set
     *            the sorted set to synchronize, must not be null
     * @return a synchronized set backed by the given set
     * @throws NullPointerException
     *             if the set is null
     */
    public static <E> SortedSet<E> synchronizedSortedSet(final SortedSet<E> set){
        return Collections.synchronizedSortedSet(set);
    }

    /**
     * <code>SetUtils</code> should not normally be instantiated.
     */
    private SetUtils(){
    }
}
