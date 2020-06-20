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
     * <p>
     * Returns the first value in the array which is not {@code null}.
     * If all the values are {@code null} or the array is {@code null}
     * or empty then {@code null} is returned.
     * </p>
     *
     * <pre>
     * ObjectUtils.firstNonNull(null, null)      = null
     * ObjectUtils.firstNonNull(null, "")        = ""
     * ObjectUtils.firstNonNull(null, null, "")  = ""
     * ObjectUtils.firstNonNull(null, "zz")      = "zz"
     * ObjectUtils.firstNonNull("abc", *)        = "abc"
     * ObjectUtils.firstNonNull(null, "xyz", *)  = "xyz"
     * ObjectUtils.firstNonNull(Boolean.TRUE, *) = Boolean.TRUE
     * ObjectUtils.firstNonNull()                = null
     * </pre>
     *
     * @param <T>
     *            the component type of the array
     * @param values
     *            the values to test, may be {@code null} or empty
     * @return the first value from {@code values} which is not {@code null},
     *         or {@code null} if there are no non-null values
     * @since 3.0
     */
    @SafeVarargs
    public static <T> T firstNonNull(final T...values){
        if (values != null){
            for (final T val : values){
                if (val != null){
                    return val;
                }
            }
        }
        return null;
    }

    /**
     * <p>
     * Executes the given suppliers in order and returns the first return
     * value where a value other than {@code null} is returned.
     * Once a non-{@code null} value is obtained, all following suppliers are
     * not executed anymore.
     * If all the return values are {@code null} or no suppliers are provided
     * then {@code null} is returned.
     * </p>
     *
     * <pre>
     * ObjectUtils.firstNonNullLazy(null, () -&gt; null) = null
     * ObjectUtils.firstNonNullLazy(() -&gt; null, () -&gt; "") = ""
     * ObjectUtils.firstNonNullLazy(() -&gt; "", () -&gt; throw new IllegalStateException()) = ""
     * ObjectUtils.firstNonNullLazy(() -&gt; null, () -&gt; "zz) = "zz"
     * ObjectUtils.firstNonNullLazy() = null
     * </pre>
     *
     * @param <T>
     *            the type of the return values
     * @param suppliers
     *            the suppliers returning the values to test.
     *            {@code null} values are ignored.
     *            Suppliers may return {@code null} or a value of type @{code T}
     * @return the first return value from {@code suppliers} which is not {@code null},
     *         or {@code null} if there are no non-null values
     * @since 3.10
     */
    @SafeVarargs
    public static <T> T getFirstNonNull(final Supplier<T>...suppliers){
        if (suppliers != null){
            for (final Supplier<T> supplier : suppliers){
                if (supplier != null){
                    final T value = supplier.get();
                    if (value != null){
                        return value;
                    }
                }
            }
        }
        return null;
    }

    /**
     * <p>
     * Returns the given {@code object} is it is non-null, otherwise returns the Supplier's {@link Supplier#get()}
     * value.
     * </p>
     *
     * <p>
     * The caller responsible for thread-safety and exception handling of default value supplier.
     * </p>
     *
     * <pre>
     * ObjectUtils.getIfNull(null, () -&gt; null)     = null
     * ObjectUtils.getIfNull(null, null)              = null
     * ObjectUtils.getIfNull(null, () -&gt; "")       = ""
     * ObjectUtils.getIfNull(null, () -&gt; "zz")     = "zz"
     * ObjectUtils.getIfNull("abc", *)                = "abc"
     * ObjectUtils.getIfNull(Boolean.TRUE, *)         = Boolean.TRUE
     * </pre>
     *
     * @param <T>
     *            the type of the object
     * @param object
     *            the {@code Object} to test, may be {@code null}
     * @param defaultSupplier
     *            the default value to return, may be {@code null}
     * @return {@code object} if it is not {@code null}, {@code defaultValueSupplier.get()} otherwise
     * @since 3.10
     */
    public static <T> T getIfNull(final T object,final Supplier<T> defaultSupplier){
        return object != null ? object : defaultSupplier == null ? null : defaultSupplier.get();
    }

    /**
     * Checks if any value in the given array is not {@code null}.
     *
     * <p>
     * If all the values are {@code null} or the array is {@code null}
     * or empty then {@code false} is returned. Otherwise {@code true} is returned.
     * </p>
     *
     * <pre>
     * ObjectUtils.anyNotNull(*)                = true
     * ObjectUtils.anyNotNull(*, null)          = true
     * ObjectUtils.anyNotNull(null, *)          = true
     * ObjectUtils.anyNotNull(null, null, *, *) = true
     * ObjectUtils.anyNotNull(null)             = false
     * ObjectUtils.anyNotNull(null, null)       = false
     * </pre>
     *
     * @param values
     *            the values to test, may be {@code null} or empty
     * @return {@code true} if there is at least one non-null value in the array,
     *         {@code false} if all values in the array are {@code null}s.
     *         If the array is {@code null} or empty {@code false} is also returned.
     * @since 3.5
     */
    public static boolean anyNotNull(final Object...values){
        return firstNonNull(values) != null;
    }

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

    // Mode
    //-----------------------------------------------------------------------
    /**
     * 
     * 
     * // cloning
     * //-----------------------------------------------------------------------
     * /**
     * <p>
     * Clone an object.
     * </p>
     *
     * @param <T>
     *            the type of the object
     * @param obj
     *            the object to clone, null returns null
     * @return the clone if the object implements {@link Cloneable} otherwise {@code null}
     * @since 3.0
     */
    public static <T> T clone(final T obj){
        if (obj instanceof Cloneable){
            final Object result;
            if (obj.getClass().isArray()){
                final Class<?> componentType = obj.getClass().getComponentType();
                if (componentType.isPrimitive()){
                    int length = Array.getLength(obj);
                    result = Array.newInstance(componentType, length);
                    while (length-- > 0){
                        Array.set(result, length, Array.get(obj, length));
                    }
                }else{
                    result = ((Object[]) obj).clone();
                }
            }else{
                try{
                    final Method clone = obj.getClass().getMethod("clone");
                    result = clone.invoke(obj);
                }catch (final NoSuchMethodException e){
                    throw new IllegalArgumentException("Cloneable type " + obj.getClass().getName() + " has no clone method", e);
                }catch (final IllegalAccessException e){
                    throw new IllegalArgumentException("Cannot clone Cloneable type " + obj.getClass().getName(), e);
                }catch (final InvocationTargetException e){
                    throw new IllegalArgumentException("Exception cloning Cloneable type " + obj.getClass().getName(), e.getCause());
                }
            }
            @SuppressWarnings("unchecked") // OK because input is of type T
            final T checked = (T) result;
            return checked;
        }

        return null;
    }

    /**
     * <p>
     * Clone an object if possible.
     * </p>
     *
     * <p>
     * This method is similar to {@link #clone(Object)}, but will return the provided
     * instance as the return value instead of {@code null} if the instance
     * is not cloneable. This is more convenient if the caller uses different
     * implementations (e.g. of a service) and some of the implementations do not allow concurrent
     * processing or have state. In such cases the implementation can simply provide a proper
     * clone implementation and the caller's code does not have to change.
     * </p>
     *
     * @param <T>
     *            the type of the object
     * @param obj
     *            the object to clone, null returns null
     * @return the clone if the object implements {@link Cloneable} otherwise the object itself
     * @throws CloneFailedException
     *             if the object is cloneable and the clone operation fails
     * @since 3.0
     */
    public static <T> T cloneIfPossible(final T obj){
        final T clone = clone(obj);
        return clone == null ? obj : clone;
    }

}
