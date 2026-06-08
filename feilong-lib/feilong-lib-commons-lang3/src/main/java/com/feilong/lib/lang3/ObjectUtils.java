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

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Supplier;

/**
 * <p>
 * Operations on {@code Object}.
 * </p>
 *
 * <p>
 * This class tries to handle {@code null} input gracefully.
 * An exception will generally not be thrown for a {@code null} input.
 * Each method documents its behaviour in more detail.
 * </p>
 *
 * <p>
 * #ThreadSafe#
 * </p>
 * 
 * @since 1.0
 */
//@Immutable
// because it is part of the signature of deprecated methods
public class ObjectUtils{

    /**
     * Checks if all values in the array are not {@code nulls}.
     *
     * <p>
     * If any value is {@code null} or the array is {@code null} then
     * {@code false} is returned. If all elements in array are not
     * {@code null} or the array is empty (contains no elements) {@code true}
     * is returned.
     * </p>
     *
     * <pre>
     * ObjectUtils.allNotNull(*)             = true
     * ObjectUtils.allNotNull(*, *)          = true
     * ObjectUtils.allNotNull(null)          = false
     * ObjectUtils.allNotNull(null, null)    = false
     * ObjectUtils.allNotNull(null, *)       = false
     * ObjectUtils.allNotNull(*, null)       = false
     * ObjectUtils.allNotNull(*, *, null, *) = false
     * </pre>
     *
     * @param values
     *            the values to test, may be {@code null} or empty
     * @return {@code false} if there is at least one {@code null} value in the array or the array is {@code null},
     *         {@code true} if all values in the array are not {@code null}s or array contains no elements.
     * @since 3.5
     */
    public static boolean allNotNull(final Object...values){
        if (values == null){
            return false;
        }

        for (final Object val : values){
            if (val == null){
                return false;
            }
        }

        return true;
    }

    /**
     * <p>
     * Null safe comparison of Comparables.
     * {@code null} is assumed to be less than a non-{@code null} value.
     * </p>
     *
     * @param <T>
     *            type of the values processed by this method
     * @param c1
     *            the first comparable, may be null
     * @param c2
     *            the second comparable, may be null
     * @return a negative value if c1 &lt; c2, zero if c1 = c2
     *         and a positive value if c1 &gt; c2
     */
    public static <T extends Comparable<? super T>> int compare(final T c1,final T c2){
        return compare(c1, c2, false);
    }

    /**
     * <p>
     * Null safe comparison of Comparables.
     * </p>
     *
     * @param <T>
     *            type of the values processed by this method
     * @param c1
     *            the first comparable, may be null
     * @param c2
     *            the second comparable, may be null
     * @param nullGreater
     *            if true {@code null} is considered greater
     *            than a non-{@code null} value or if false {@code null} is
     *            considered less than a Non-{@code null} value
     * @return a negative value if c1 &lt; c2, zero if c1 = c2
     *         and a positive value if c1 &gt; c2
     * @see java.util.Comparator#compare(Object, Object)
     */
    public static <T extends Comparable<? super T>> int compare(final T c1,final T c2,final boolean nullGreater){
        if (c1 == c2){
            return 0;
        }else if (c1 == null){
            return nullGreater ? 1 : -1;
        }else if (c2 == null){
            return nullGreater ? -1 : 1;
        }
        return c1.compareTo(c2);
    }

}
