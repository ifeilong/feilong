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
     * <code>SetUtils</code> should not normally be instantiated.
     */
    private SetUtils(){
    }
}
