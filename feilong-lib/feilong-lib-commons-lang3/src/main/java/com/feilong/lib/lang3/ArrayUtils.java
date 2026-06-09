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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.feilong.lib.lang3.builder.ToStringBuilder;
import com.feilong.lib.lang3.builder.ToStringStyle;

/**
 * <p>
 * Operations on arrays, primitive arrays (like {@code int[]}) and
 * primitive wrapper arrays (like {@code Integer[]}).
 *
 * <p>
 * This class tries to handle {@code null} input gracefully.
 * An exception will not be thrown for a {@code null}
 * array input. However, an Object array that contains a {@code null}
 * element may throw an exception. Each method documents its behaviour.
 *
 * <p>
 * #ThreadSafe#
 * 
 * @since 2.0
 */
public class ArrayUtils{

    /**
     * An empty immutable {@code boolean} array.
     */
    public static final boolean[]   EMPTY_BOOLEAN_ARRAY          = new boolean[0];

    /**
     * An empty immutable {@code Boolean} array.
     */
    public static final Boolean[]   EMPTY_BOOLEAN_OBJECT_ARRAY   = new Boolean[0];

    /**
     * An empty immutable {@code byte} array.
     */
    public static final byte[]      EMPTY_BYTE_ARRAY             = new byte[0];

    /**
     * An empty immutable {@code Byte} array.
     */
    public static final Byte[]      EMPTY_BYTE_OBJECT_ARRAY      = new Byte[0];

    /**
     * An empty immutable {@code char} array.
     */
    public static final char[]      EMPTY_CHAR_ARRAY             = new char[0];

    /**
     * An empty immutable {@code Character} array.
     */
    public static final Character[] EMPTY_CHARACTER_OBJECT_ARRAY = new Character[0];

    /**
     * An empty immutable {@code Class} array.
     */
    public static final Class<?>[]  EMPTY_CLASS_ARRAY            = new Class[0];

    /**
     * An empty immutable {@code double} array.
     */
    public static final double[]    EMPTY_DOUBLE_ARRAY           = new double[0];

    /**
     * An empty immutable {@code Double} array.
     */
    public static final Double[]    EMPTY_DOUBLE_OBJECT_ARRAY    = new Double[0];

    /**
     * An empty immutable {@code Field} array.
     *
     * @since 3.10
     */
    public static final Field[]     EMPTY_FIELD_ARRAY            = new Field[0];

    /**
     * An empty immutable {@code Method} array.
     *
     * @since 3.10
     */
    public static final Method[]    EMPTY_METHOD_ARRAY           = new Method[0];

    /**
     * An empty immutable {@code float} array.
     */
    public static final float[]     EMPTY_FLOAT_ARRAY            = new float[0];

    /**
     * An empty immutable {@code Float} array.
     */
    public static final Float[]     EMPTY_FLOAT_OBJECT_ARRAY     = new Float[0];

    /**
     * An empty immutable {@code int} array.
     */
    public static final int[]       EMPTY_INT_ARRAY              = new int[0];

    /**
     * An empty immutable {@code Integer} array.
     */
    public static final Integer[]   EMPTY_INTEGER_OBJECT_ARRAY   = new Integer[0];

    /**
     * An empty immutable {@code long} array.
     */
    public static final long[]      EMPTY_LONG_ARRAY             = new long[0];

    /**
     * An empty immutable {@code Long} array.
     */
    public static final Long[]      EMPTY_LONG_OBJECT_ARRAY      = new Long[0];

    /**
     * An empty immutable {@code Object} array.
     */
    public static final Object[]    EMPTY_OBJECT_ARRAY           = new Object[0];

    /**
     * An empty immutable {@code short} array.
     */
    public static final short[]     EMPTY_SHORT_ARRAY            = new short[0];

    /**
     * An empty immutable {@code Short} array.
     */
    public static final Short[]     EMPTY_SHORT_OBJECT_ARRAY     = new Short[0];

    /**
     * An empty immutable {@code String} array.
     * 
     * @deprecated use feilong
     */
    @Deprecated
    public static final String[]    EMPTY_STRING_ARRAY           = new String[0];

    /**
     * An empty immutable {@code Type} array.
     *
     * @since 3.10
     */
    public static final Type[]      EMPTY_TYPE_ARRAY             = new Type[0];

    /**
     * The index value when an element is not found in a list or array: {@code -1}.
     * This value is returned by methods in this class and can also be used in comparisons with values returned by
     * various method from {@link java.util.List}.
     */
    public static final int         INDEX_NOT_FOUND              = -1;

    //---------------------------------------------------------------

    /**
     * <p>
     * Copies the given array and adds the given element at the end of the new array.
     *
     * <p>
     * The new array contains the same elements of the input
     * array plus the given element in the last position. The component type of
     * the new array is the same as that of the input array.
     *
     * <p>
     * If the input array is {@code null}, a new one element array is returned
     * whose component type is the same as the element, unless the element itself is null,
     * in which case the return type is Object[]
     *
     * <pre>
     * ArrayUtils.add(null, null)      = IllegalArgumentException
     * ArrayUtils.add(null, "a")       = ["a"]
     * ArrayUtils.add(["a"], null)     = ["a", null]
     * ArrayUtils.add(["a"], "b")      = ["a", "b"]
     * ArrayUtils.add(["a", "b"], "c") = ["a", "b", "c"]
     * </pre>
     *
     * @param <T>
     *            the component type of the array
     * @param array
     *            the array to "add" the element to, may be {@code null}
     * @param element
     *            the object to add, may be {@code null}
     * @return A new array containing the existing elements plus the new element
     *         The returned array type will be that of the input array (unless null),
     *         in which case it will have the same type as the element.
     *         If both are null, an IllegalArgumentException is thrown
     * @since 2.1
     * @throws IllegalArgumentException
     *             if both arguments are null
     */
    public static <T> T[] add(final T[] array,final T element){
        Class<?> type;
        if (array != null){
            type = array.getClass().getComponentType();
        }else if (element != null){
            type = element.getClass();
        }else{
            throw new IllegalArgumentException("Arguments cannot both be null");
        }
        @SuppressWarnings("unchecked") // type must be T
        final T[] newArray = (T[]) copyArrayGrow1(array, type);
        newArray[newArray.length - 1] = element;
        return newArray;
    }

    /**
     * <p>
     * Adds all the elements of the given arrays into a new array.
     * <p>
     * The new array contains all of the element of {@code array1} followed
     * by all of the elements {@code array2}. When an array is returned, it is always
     * a new array.
     *
     * <pre>
     * ArrayUtils.addAll(null, null)     = null
     * ArrayUtils.addAll(array1, null)   = cloned copy of array1
     * ArrayUtils.addAll(null, array2)   = cloned copy of array2
     * ArrayUtils.addAll([], [])         = []
     * ArrayUtils.addAll([null], [null]) = [null, null]
     * ArrayUtils.addAll(["a", "b", "c"], ["1", "2", "3"]) = ["a", "b", "c", "1", "2", "3"]
     * </pre>
     *
     * @param <T>
     *            the component type of the array
     * @param array1
     *            the first array whose elements are added to the new array, may be {@code null}
     * @param array2
     *            the second array whose elements are added to the new array, may be {@code null}
     * @return The new array, {@code null} if both arrays are {@code null}.
     *         The type of the new array is the type of the first array,
     *         unless the first array is null, in which case the type is the same as the second array.
     * @since 2.1
     * @throws IllegalArgumentException
     *             if the array types are incompatible
     */
    public static <T> T[] addAll(final T[] array1,@SuppressWarnings("unchecked") final T...array2){
        if (array1 == null){
            return clone(array2);
        }else if (array2 == null){
            return clone(array1);
        }
        final Class<?> type1 = array1.getClass().getComponentType();
        @SuppressWarnings("unchecked") // OK, because array is of type T
        final T[] joinedArray = (T[]) Array.newInstance(type1, array1.length + array2.length);
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        try{
            System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        }catch (final ArrayStoreException ase){
            // Check if problem was due to incompatible types
            /*
             * We do this here, rather than before the copy because:
             * - it would be a wasted check most of the time
             * - safer, in case check turns out to be too strict
             */
            final Class<?> type2 = array2.getClass().getComponentType();
            if (!type1.isAssignableFrom(type2)){
                throw new IllegalArgumentException("Cannot store " + type2.getName() + " in an array of " + type1.getName(), ase);
            }
            throw ase; // No, so rethrow original
        }
        return joinedArray;
    }

    /**
     * <p>
     * Clones an array returning a typecast result and handling
     * {@code null}.
     *
     * <p>
     * This method returns {@code null} for a {@code null} input array.
     *
     * @param array
     *            the array to clone, may be {@code null}
     * @return the cloned array, {@code null} if {@code null} input
     */
    public static int[] clone(final int[] array){
        if (array == null){
            return null;
        }
        return array.clone();
    }

    // Clone
    //-----------------------------------------------------------------------
    /**
     * <p>
     * Shallow clones an array returning a typecast result and handling
     * {@code null}.
     *
     * <p>
     * The objects in the array are not cloned, thus there is no special
     * handling for multi-dimensional arrays.
     *
     * <p>
     * This method returns {@code null} for a {@code null} input array.
     *
     * @param <T>
     *            the component type of the array
     * @param array
     *            the array to shallow clone, may be {@code null}
     * @return the cloned array, {@code null} if {@code null} input
     */
    public static <T> T[] clone(final T[] array){
        if (array == null){
            return null;
        }
        return array.clone();
    }

    /**
     * <p>
     * Checks if the value is in the given array.
     *
     * <p>
     * The method returns {@code false} if a {@code null} array is passed in.
     *
     * @param array
     *            the array to search through
     * @param valueToFind
     *            the value to find
     * @return {@code true} if the array contains the object
     */
    public static boolean contains(final boolean[] array,final boolean valueToFind){
        return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
    }

    /**
     * <p>
     * Checks if the value is in the given array.
     *
     * <p>
     * The method returns {@code false} if a {@code null} array is passed in.
     *
     * @param array
     *            the array to search through
     * @param valueToFind
     *            the value to find
     * @return {@code true} if the array contains the object
     */
    public static boolean contains(final byte[] array,final byte valueToFind){
        return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
    }

    /**
     * <p>
     * Checks if the value is in the given array.
     *
     * <p>
     * The method returns {@code false} if a {@code null} array is passed in.
     *
     * @param array
     *            the array to search through
     * @param valueToFind
     *            the value to find
     * @return {@code true} if the array contains the object
     * @since 2.1
     */
    public static boolean contains(final char[] array,final char valueToFind){
        return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
    }

    /**
     * <p>
     * Checks if the value is in the given array.
     *
     * <p>
     * The method returns {@code false} if a {@code null} array is passed in.
     *
     * @param array
     *            the array to search through
     * @param valueToFind
     *            the value to find
     * @return {@code true} if the array contains the object
     */
    public static boolean contains(final double[] array,final double valueToFind){
        return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
    }

    /**
     * <p>
     * Checks if a value falling within the given tolerance is in the
     * given array. If the array contains a value within the inclusive range
     * defined by (value - tolerance) to (value + tolerance).
     *
     * <p>
     * The method returns {@code false} if a {@code null} array
     * is passed in.
     *
     * @param array
     *            the array to search
     * @param valueToFind
     *            the value to find
     * @param tolerance
     *            the array contains the tolerance of the search
     * @return true if value falling within tolerance is in array
     */
    public static boolean contains(final double[] array,final double valueToFind,final double tolerance){
        return indexOf(array, valueToFind, 0, tolerance) != INDEX_NOT_FOUND;
    }

    /**
     * <p>
     * Checks if the value is in the given array.
     *
     * <p>
     * The method returns {@code false} if a {@code null} array is passed in.
     *
     * @param array
     *            the array to search through
     * @param valueToFind
     *            the value to find
     * @return {@code true} if the array contains the object
     */
    public static boolean contains(final float[] array,final float valueToFind){
        return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
    }

    /**
     * <p>
     * Checks if the value is in the given array.
     *
     * <p>
     * The method returns {@code false} if a {@code null} array is passed in.
     *
     * @param array
     *            the array to search through
     * @param valueToFind
     *            the value to find
     * @return {@code true} if the array contains the object
     */
    public static boolean contains(final int[] array,final int valueToFind){
        return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
    }

    /**
     * <p>
     * Checks if the value is in the given array.
     *
     * <p>
     * The method returns {@code false} if a {@code null} array is passed in.
     *
     * @param array
     *            the array to search through
     * @param valueToFind
     *            the value to find
     * @return {@code true} if the array contains the object
     */
    public static boolean contains(final long[] array,final long valueToFind){
        return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
    }

    /**
     * <p>
     * Checks if the object is in the given array.
     *
     * <p>
     * The method returns {@code false} if a {@code null} array is passed in.
     *
     * @param array
     *            the array to search through
     * @param objectToFind
     *            the object to find
     * @return {@code true} if the array contains the object
     */
    public static boolean contains(final Object[] array,final Object objectToFind){
        return indexOf(array, objectToFind) != INDEX_NOT_FOUND;
    }

    /**
     * <p>
     * Checks if the value is in the given array.
     *
     * <p>
     * The method returns {@code false} if a {@code null} array is passed in.
     *
     * @param array
     *            the array to search through
     * @param valueToFind
     *            the value to find
     * @return {@code true} if the array contains the object
     */
    public static boolean contains(final short[] array,final short valueToFind){
        return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
    }

    /**
     * Returns a copy of the given array of size 1 greater than the argument.
     * The last value of the array is left to the default value.
     *
     * @param array
     *            The array to copy, must not be {@code null}.
     * @param newArrayComponentType
     *            If {@code array} is {@code null}, create a
     *            size 1 array of this type.
     * @return A new copy of the array of size 1 greater than the input.
     */
    private static Object copyArrayGrow1(final Object array,final Class<?> newArrayComponentType){
        if (array != null){
            final int arrayLength = Array.getLength(array);
            final Object newArray = Array.newInstance(array.getClass().getComponentType(), arrayLength + 1);
            System.arraycopy(array, 0, newArray, 0, arrayLength);
            return newArray;
        }
        return Array.newInstance(newArrayComponentType, 1);
    }

    //-----------------------------------------------------------------------
    /**
     * <p>
     * Returns the length of the specified array.
     * This method can deal with {@code Object} arrays and with primitive arrays.
     *
     * <p>
     * If the input array is {@code null}, {@code 0} is returned.
     *
     * <pre>
     * ArrayUtils.getLength(null)            = 0
     * ArrayUtils.getLength([])              = 0
     * ArrayUtils.getLength([null])          = 1
     * ArrayUtils.getLength([true, false])   = 2
     * ArrayUtils.getLength([1, 2, 3])       = 3
     * ArrayUtils.getLength(["a", "b", "c"]) = 3
     * </pre>
     *
     * @param array
     *            the array to retrieve the length from, may be null
     * @return The length of the array, or {@code 0} if the array is {@code null}
     * @throws IllegalArgumentException
     *             if the object argument is not an array.
     * @since 2.1
     */
    public static int getLength(final Object array){
        if (array == null){
            return 0;
        }
        return Array.getLength(array);
    }

    // boolean IndexOf
    //-----------------------------------------------------------------------
    /**
     * <p>
     * Finds the index of the given value in the array.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array
     *            the array to search through for the object, may be {@code null}
     * @param valueToFind
     *            the value to find
     * @return the index of the value within the array,
     *         {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final boolean[] array,final boolean valueToFind){
        return indexOf(array, valueToFind, 0);
    }

    /**
     * <p>
     * Finds the index of the given value in the array starting at the given index.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>
     * A negative startIndex is treated as zero. A startIndex larger than the array
     * length will return {@link #INDEX_NOT_FOUND} ({@code -1}).
     *
     * @param array
     *            the array to search through for the object, may be {@code null}
     * @param valueToFind
     *            the value to find
     * @param startIndex
     *            the index to start searching at
     * @return the index of the value within the array,
     *         {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null}
     *         array input
     */
    public static int indexOf(final boolean[] array,final boolean valueToFind,int startIndex){
        if (isEmpty(array)){
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0){
            startIndex = 0;
        }
        for (int i = startIndex; i < array.length; i++){
            if (valueToFind == array[i]){
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    // byte IndexOf
    //-----------------------------------------------------------------------
    /**
     * <p>
     * Finds the index of the given value in the array.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array
     *            the array to search through for the object, may be {@code null}
     * @param valueToFind
     *            the value to find
     * @return the index of the value within the array,
     *         {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final byte[] array,final byte valueToFind){
        return indexOf(array, valueToFind, 0);
    }

    /**
     * <p>
     * Finds the index of the given value in the array starting at the given index.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>
     * A negative startIndex is treated as zero. A startIndex larger than the array
     * length will return {@link #INDEX_NOT_FOUND} ({@code -1}).
     *
     * @param array
     *            the array to search through for the object, may be {@code null}
     * @param valueToFind
     *            the value to find
     * @param startIndex
     *            the index to start searching at
     * @return the index of the value within the array,
     *         {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final byte[] array,final byte valueToFind,int startIndex){
        if (array == null){
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0){
            startIndex = 0;
        }
        for (int i = startIndex; i < array.length; i++){
            if (valueToFind == array[i]){
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    // char IndexOf
    //-----------------------------------------------------------------------
    /**
     * <p>
     * Finds the index of the given value in the array.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array
     *            the array to search through for the object, may be {@code null}
     * @param valueToFind
     *            the value to find
     * @return the index of the value within the array,
     *         {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     * @since 2.1
     */
    public static int indexOf(final char[] array,final char valueToFind){
        return indexOf(array, valueToFind, 0);
    }

    /**
     * <p>
     * Finds the index of the given value in the array starting at the given index.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>
     * A negative startIndex is treated as zero. A startIndex larger than the array
     * length will return {@link #INDEX_NOT_FOUND} ({@code -1}).
     *
     * @param array
     *            the array to search through for the object, may be {@code null}
     * @param valueToFind
     *            the value to find
     * @param startIndex
     *            the index to start searching at
     * @return the index of the value within the array,
     *         {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     * @since 2.1
     */
    public static int indexOf(final char[] array,final char valueToFind,int startIndex){
        if (array == null){
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0){
            startIndex = 0;
        }
        for (int i = startIndex; i < array.length; i++){
            if (valueToFind == array[i]){
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    // double IndexOf
    //-----------------------------------------------------------------------
    /**
     * <p>
     * Finds the index of the given value in the array.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array
     *            the array to search through for the object, may be {@code null}
     * @param valueToFind
     *            the value to find
     * @return the index of the value within the array,
     *         {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final double[] array,final double valueToFind){
        return indexOf(array, valueToFind, 0);
    }

    /**
     * <p>
     * Finds the index of the given value within a given tolerance in the array.
     * This method will return the index of the first value which falls between the region
     * defined by valueToFind - tolerance and valueToFind + tolerance.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array
     *            the array to search through for the object, may be {@code null}
     * @param valueToFind
     *            the value to find
     * @param tolerance
     *            tolerance of the search
     * @return the index of the value within the array,
     *         {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final double[] array,final double valueToFind,final double tolerance){
        return indexOf(array, valueToFind, 0, tolerance);
    }

    /**
     * <p>
     * Finds the index of the given value in the array starting at the given index.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>
     * A negative startIndex is treated as zero. A startIndex larger than the array
     * length will return {@link #INDEX_NOT_FOUND} ({@code -1}).
     *
     * @param array
     *            the array to search through for the object, may be {@code null}
     * @param valueToFind
     *            the value to find
     * @param startIndex
     *            the index to start searching at
     * @return the index of the value within the array,
     *         {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final double[] array,final double valueToFind,int startIndex){
        if (isEmpty(array)){
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0){
            startIndex = 0;
        }
        for (int i = startIndex; i < array.length; i++){
            if (valueToFind == array[i]){
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * <p>
     * Finds the index of the given value in the array starting at the given index.
     * This method will return the index of the first value which falls between the region
     * defined by valueToFind - tolerance and valueToFind + tolerance.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>
     * A negative startIndex is treated as zero. A startIndex larger than the array
     * length will return {@link #INDEX_NOT_FOUND} ({@code -1}).
     *
     * @param array
     *            the array to search through for the object, may be {@code null}
     * @param valueToFind
     *            the value to find
     * @param startIndex
     *            the index to start searching at
     * @param tolerance
     *            tolerance of the search
     * @return the index of the value within the array,
     *         {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final double[] array,final double valueToFind,int startIndex,final double tolerance){
        if (isEmpty(array)){
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0){
            startIndex = 0;
        }
        final double min = valueToFind - tolerance;
        final double max = valueToFind + tolerance;
        for (int i = startIndex; i < array.length; i++){
            if (array[i] >= min && array[i] <= max){
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    // float IndexOf
    //-----------------------------------------------------------------------
    /**
     * <p>
     * Finds the index of the given value in the array.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array
     *            the array to search through for the object, may be {@code null}
     * @param valueToFind
     *            the value to find
     * @return the index of the value within the array,
     *         {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final float[] array,final float valueToFind){
        return indexOf(array, valueToFind, 0);
    }

    /**
     * <p>
     * Finds the index of the given value in the array starting at the given index.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>
     * A negative startIndex is treated as zero. A startIndex larger than the array
     * length will return {@link #INDEX_NOT_FOUND} ({@code -1}).
     *
     * @param array
     *            the array to search through for the object, may be {@code null}
     * @param valueToFind
     *            the value to find
     * @param startIndex
     *            the index to start searching at
     * @return the index of the value within the array,
     *         {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final float[] array,final float valueToFind,int startIndex){
        if (isEmpty(array)){
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0){
            startIndex = 0;
        }
        for (int i = startIndex; i < array.length; i++){
            if (valueToFind == array[i]){
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    // int IndexOf
    //-----------------------------------------------------------------------
    /**
     * <p>
     * Finds the index of the given value in the array.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array
     *            the array to search through for the object, may be {@code null}
     * @param valueToFind
     *            the value to find
     * @return the index of the value within the array,
     *         {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final int[] array,final int valueToFind){
        return indexOf(array, valueToFind, 0);
    }

    /**
     * <p>
     * Finds the index of the given value in the array starting at the given index.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>
     * A negative startIndex is treated as zero. A startIndex larger than the array
     * length will return {@link #INDEX_NOT_FOUND} ({@code -1}).
     *
     * @param array
     *            the array to search through for the object, may be {@code null}
     * @param valueToFind
     *            the value to find
     * @param startIndex
     *            the index to start searching at
     * @return the index of the value within the array,
     *         {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final int[] array,final int valueToFind,int startIndex){
        if (array == null){
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0){
            startIndex = 0;
        }
        for (int i = startIndex; i < array.length; i++){
            if (valueToFind == array[i]){
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    // long IndexOf
    //-----------------------------------------------------------------------
    /**
     * <p>
     * Finds the index of the given value in the array.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array
     *            the array to search through for the object, may be {@code null}
     * @param valueToFind
     *            the value to find
     * @return the index of the value within the array,
     *         {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final long[] array,final long valueToFind){
        return indexOf(array, valueToFind, 0);
    }

    /**
     * <p>
     * Finds the index of the given value in the array starting at the given index.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>
     * A negative startIndex is treated as zero. A startIndex larger than the array
     * length will return {@link #INDEX_NOT_FOUND} ({@code -1}).
     *
     * @param array
     *            the array to search through for the object, may be {@code null}
     * @param valueToFind
     *            the value to find
     * @param startIndex
     *            the index to start searching at
     * @return the index of the value within the array,
     *         {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final long[] array,final long valueToFind,int startIndex){
        if (array == null){
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0){
            startIndex = 0;
        }
        for (int i = startIndex; i < array.length; i++){
            if (valueToFind == array[i]){
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    // Object IndexOf
    //-----------------------------------------------------------------------
    /**
     * <p>
     * Finds the index of the given object in the array.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array
     *            the array to search through for the object, may be {@code null}
     * @param objectToFind
     *            the object to find, may be {@code null}
     * @return the index of the object within the array,
     *         {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final Object[] array,final Object objectToFind){
        return indexOf(array, objectToFind, 0);
    }

    /**
     * <p>
     * Finds the index of the given object in the array starting at the given index.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>
     * A negative startIndex is treated as zero. A startIndex larger than the array
     * length will return {@link #INDEX_NOT_FOUND} ({@code -1}).
     *
     * @param array
     *            the array to search through for the object, may be {@code null}
     * @param objectToFind
     *            the object to find, may be {@code null}
     * @param startIndex
     *            the index to start searching at
     * @return the index of the object within the array starting at the index,
     *         {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final Object[] array,final Object objectToFind,int startIndex){
        if (array == null){
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0){
            startIndex = 0;
        }
        if (objectToFind == null){
            for (int i = startIndex; i < array.length; i++){
                if (array[i] == null){
                    return i;
                }
            }
        }else{
            for (int i = startIndex; i < array.length; i++){
                if (objectToFind.equals(array[i])){
                    return i;
                }
            }
        }
        return INDEX_NOT_FOUND;
    }

    // short IndexOf
    //-----------------------------------------------------------------------
    /**
     * <p>
     * Finds the index of the given value in the array.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array
     *            the array to search through for the object, may be {@code null}
     * @param valueToFind
     *            the value to find
     * @return the index of the value within the array,
     *         {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final short[] array,final short valueToFind){
        return indexOf(array, valueToFind, 0);
    }

    /**
     * <p>
     * Finds the index of the given value in the array starting at the given index.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>
     * A negative startIndex is treated as zero. A startIndex larger than the array
     * length will return {@link #INDEX_NOT_FOUND} ({@code -1}).
     *
     * @param array
     *            the array to search through for the object, may be {@code null}
     * @param valueToFind
     *            the value to find
     * @param startIndex
     *            the index to start searching at
     * @return the index of the value within the array,
     *         {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final short[] array,final short valueToFind,int startIndex){
        if (array == null){
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0){
            startIndex = 0;
        }
        for (int i = startIndex; i < array.length; i++){
            if (valueToFind == array[i]){
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * <p>
     * Inserts elements into an array at the given index (starting from zero).
     * </p>
     *
     * <p>
     * When an array is returned, it is always a new array.
     * </p>
     *
     * <pre>
     * ArrayUtils.insert(index, null, null)      = null
     * ArrayUtils.insert(index, array, null)     = cloned copy of 'array'
     * ArrayUtils.insert(index, null, values)    = null
     * </pre>
     *
     * @param <T>
     *            The type of elements in {@code array} and {@code values}
     * @param index
     *            the position within {@code array} to insert the new values
     * @param array
     *            the array to insert the values into, may be {@code null}
     * @param values
     *            the new values to insert, may be {@code null}
     * @return The new array.
     * @throws IndexOutOfBoundsException
     *             if {@code array} is provided
     *             and either {@code index < 0} or {@code index > array.length}
     * @since 3.6
     */
    @SafeVarargs
    public static <T> T[] insert(final int index,final T[] array,final T...values){
        /*
         * Note on use of @SafeVarargs:
         *
         * By returning null when 'array' is null, we avoid returning the vararg
         * array to the caller. We also avoid relying on the type of the vararg
         * array, by inspecting the component type of 'array'.
         */

        if (array == null){
            return null;
        }
        if (ArrayUtils.isEmpty(values)){
            return clone(array);
        }
        if (index < 0 || index > array.length){
            throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + array.length);
        }

        final Class<?> type = array.getClass().getComponentType();
        @SuppressWarnings("unchecked") // OK, because array and values are of type T
        final T[] result = (T[]) Array.newInstance(type, array.length + values.length);

        System.arraycopy(values, 0, result, index, values.length);
        if (index > 0){
            System.arraycopy(array, 0, result, 0, index);
        }
        if (index < array.length){
            System.arraycopy(array, index, result, index + values.length, array.length - index);
        }
        return result;
    }

    /**
     * <p>
     * Checks if an array of primitive booleans is empty or {@code null}.
     *
     * @param array
     *            the array to test
     * @return {@code true} if the array is empty or {@code null}
     * @since 2.1
     */
    public static boolean isEmpty(final boolean[] array){
        return getLength(array) == 0;
    }

    // IndexOf search
    // ----------------------------------------------------------------------

    /**
     * <p>
     * Checks if an array of primitive bytes is empty or {@code null}.
     *
     * @param array
     *            the array to test
     * @return {@code true} if the array is empty or {@code null}
     * @since 2.1
     */
    public static boolean isEmpty(final byte[] array){
        return getLength(array) == 0;
    }

    /**
     * <p>
     * Checks if an array of primitive chars is empty or {@code null}.
     *
     * @param array
     *            the array to test
     * @return {@code true} if the array is empty or {@code null}
     * @since 2.1
     */
    public static boolean isEmpty(final char[] array){
        return getLength(array) == 0;
    }

    /**
     * <p>
     * Checks if an array of primitive doubles is empty or {@code null}.
     *
     * @param array
     *            the array to test
     * @return {@code true} if the array is empty or {@code null}
     * @since 2.1
     */
    public static boolean isEmpty(final double[] array){
        return getLength(array) == 0;
    }

    /**
     * <p>
     * Checks if an array of primitive floats is empty or {@code null}.
     *
     * @param array
     *            the array to test
     * @return {@code true} if the array is empty or {@code null}
     * @since 2.1
     */
    public static boolean isEmpty(final float[] array){
        return getLength(array) == 0;
    }

    /**
     * <p>
     * Checks if an array of primitive ints is empty or {@code null}.
     *
     * @param array
     *            the array to test
     * @return {@code true} if the array is empty or {@code null}
     * @since 2.1
     */
    public static boolean isEmpty(final int[] array){
        return getLength(array) == 0;
    }

    /**
     * <p>
     * Checks if an array of primitive longs is empty or {@code null}.
     *
     * @param array
     *            the array to test
     * @return {@code true} if the array is empty or {@code null}
     * @since 2.1
     */
    public static boolean isEmpty(final long[] array){
        return getLength(array) == 0;
    }

    // ----------------------------------------------------------------------
    /**
     * <p>
     * Checks if an array of Objects is empty or {@code null}.
     *
     * @param array
     *            the array to test
     * @return {@code true} if the array is empty or {@code null}
     * @since 2.1
     */
    public static boolean isEmpty(final Object[] array){
        return getLength(array) == 0;
    }

    /**
     * <p>
     * Checks if an array of primitive shorts is empty or {@code null}.
     *
     * @param array
     *            the array to test
     * @return {@code true} if the array is empty or {@code null}
     * @since 2.1
     */
    public static boolean isEmpty(final short[] array){
        return getLength(array) == 0;
    }

    /**
     * <p>
     * Checks if an array of primitive doubles is not empty and not {@code null}.
     *
     * @param array
     *            the array to test
     * @return {@code true} if the array is not empty and not {@code null}
     * @since 2.5
     */
    public static boolean isNotEmpty(final double[] array){
        return !isEmpty(array);
    }

    /**
     * <p>
     * Checks if an array of primitive ints is not empty and not {@code null}.
     *
     * @param array
     *            the array to test
     * @return {@code true} if the array is not empty and not {@code null}
     * @since 2.5
     */
    public static boolean isNotEmpty(final int[] array){
        return !isEmpty(array);
    }

    /**
     * <p>
     * Checks if an array of primitive longs is not empty and not {@code null}.
     *
     * @param array
     *            the array to test
     * @return {@code true} if the array is not empty and not {@code null}
     * @since 2.5
     */
    public static boolean isNotEmpty(final long[] array){
        return !isEmpty(array);
    }

    // ----------------------------------------------------------------------
    /**
     * <p>
     * Checks if an array of Objects is not empty and not {@code null}.
     *
     * @param <T>
     *            the component type of the array
     * @param array
     *            the array to test
     * @return {@code true} if the array is not empty and not {@code null}
     * @since 2.5
     */
    public static <T> boolean isNotEmpty(final T[] array){
        return !isEmpty(array);
    }

    // Is same length
    //-----------------------------------------------------------------------
    /**
     * <p>
     * Checks whether two arrays are the same length, treating
     * {@code null} arrays as length {@code 0}.
     *
     * <p>
     * Any multi-dimensional aspects of the arrays are ignored.
     *
     * @param array1
     *            the first array, may be {@code null}
     * @param array2
     *            the second array, may be {@code null}
     * @return {@code true} if length of arrays matches, treating
     *         {@code null} as an empty array
     */
    public static boolean isSameLength(final Object[] array1,final Object[] array2){
        return getLength(array1) == getLength(array2);
    }

    /**
     * <p>
     * Finds the last index of the given value within the array.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) if
     * {@code null} array input.
     *
     * @param array
     *            the array to traverse backwards looking for the object, may be {@code null}
     * @param valueToFind
     *            the object to find
     * @return the last index of the value within the array,
     *         {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int lastIndexOf(final boolean[] array,final boolean valueToFind){
        return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
    }

    /**
     * <p>
     * Finds the last index of the given value in the array starting at the given index.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>
     * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A startIndex larger than
     * the array length will search from the end of the array.
     *
     * @param array
     *            the array to traverse for looking for the object, may be {@code null}
     * @param valueToFind
     *            the value to find
     * @param startIndex
     *            the start index to traverse backwards from
     * @return the last index of the value within the array,
     *         {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int lastIndexOf(final boolean[] array,final boolean valueToFind,int startIndex){
        if (isEmpty(array)){
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0){
            return INDEX_NOT_FOUND;
        }else if (startIndex >= array.length){
            startIndex = array.length - 1;
        }
        for (int i = startIndex; i >= 0; i--){
            if (valueToFind == array[i]){
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * <p>
     * Finds the last index of the given value within the array.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array
     *            the array to traverse backwards looking for the object, may be {@code null}
     * @param valueToFind
     *            the object to find
     * @return the last index of the value within the array,
     *         {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int lastIndexOf(final byte[] array,final byte valueToFind){
        return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
    }

    /**
     * <p>
     * Finds the last index of the given value in the array starting at the given index.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>
     * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A startIndex larger than the
     * array length will search from the end of the array.
     *
     * @param array
     *            the array to traverse for looking for the object, may be {@code null}
     * @param valueToFind
     *            the value to find
     * @param startIndex
     *            the start index to traverse backwards from
     * @return the last index of the value within the array,
     *         {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int lastIndexOf(final byte[] array,final byte valueToFind,int startIndex){
        if (array == null){
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0){
            return INDEX_NOT_FOUND;
        }else if (startIndex >= array.length){
            startIndex = array.length - 1;
        }
        for (int i = startIndex; i >= 0; i--){
            if (valueToFind == array[i]){
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * <p>
     * Finds the last index of the given value within the array.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array
     *            the array to traverse backwards looking for the object, may be {@code null}
     * @param valueToFind
     *            the object to find
     * @return the last index of the value within the array,
     *         {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     * @since 2.1
     */
    public static int lastIndexOf(final char[] array,final char valueToFind){
        return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
    }

    /**
     * <p>
     * Finds the last index of the given value in the array starting at the given index.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>
     * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A startIndex larger than the
     * array length will search from the end of the array.
     *
     * @param array
     *            the array to traverse for looking for the object, may be {@code null}
     * @param valueToFind
     *            the value to find
     * @param startIndex
     *            the start index to traverse backwards from
     * @return the last index of the value within the array,
     *         {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     * @since 2.1
     */
    public static int lastIndexOf(final char[] array,final char valueToFind,int startIndex){
        if (array == null){
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0){
            return INDEX_NOT_FOUND;
        }else if (startIndex >= array.length){
            startIndex = array.length - 1;
        }
        for (int i = startIndex; i >= 0; i--){
            if (valueToFind == array[i]){
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * <p>
     * Finds the last index of the given value within the array.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array
     *            the array to traverse backwards looking for the object, may be {@code null}
     * @param valueToFind
     *            the object to find
     * @return the last index of the value within the array,
     *         {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int lastIndexOf(final double[] array,final double valueToFind){
        return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
    }

    /**
     * <p>
     * Finds the last index of the given value within a given tolerance in the array.
     * This method will return the index of the last value which falls between the region
     * defined by valueToFind - tolerance and valueToFind + tolerance.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array
     *            the array to search through for the object, may be {@code null}
     * @param valueToFind
     *            the value to find
     * @param tolerance
     *            tolerance of the search
     * @return the index of the value within the array,
     *         {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int lastIndexOf(final double[] array,final double valueToFind,final double tolerance){
        return lastIndexOf(array, valueToFind, Integer.MAX_VALUE, tolerance);
    }

    /**
     * <p>
     * Finds the last index of the given value in the array starting at the given index.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>
     * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A startIndex larger than the
     * array length will search from the end of the array.
     *
     * @param array
     *            the array to traverse for looking for the object, may be {@code null}
     * @param valueToFind
     *            the value to find
     * @param startIndex
     *            the start index to traverse backwards from
     * @return the last index of the value within the array,
     *         {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int lastIndexOf(final double[] array,final double valueToFind,int startIndex){
        if (isEmpty(array)){
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0){
            return INDEX_NOT_FOUND;
        }else if (startIndex >= array.length){
            startIndex = array.length - 1;
        }
        for (int i = startIndex; i >= 0; i--){
            if (valueToFind == array[i]){
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * <p>
     * Finds the last index of the given value in the array starting at the given index.
     * This method will return the index of the last value which falls between the region
     * defined by valueToFind - tolerance and valueToFind + tolerance.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>
     * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A startIndex larger than the
     * array length will search from the end of the array.
     *
     * @param array
     *            the array to traverse for looking for the object, may be {@code null}
     * @param valueToFind
     *            the value to find
     * @param startIndex
     *            the start index to traverse backwards from
     * @param tolerance
     *            search for value within plus/minus this amount
     * @return the last index of the value within the array,
     *         {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int lastIndexOf(final double[] array,final double valueToFind,int startIndex,final double tolerance){
        if (isEmpty(array)){
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0){
            return INDEX_NOT_FOUND;
        }else if (startIndex >= array.length){
            startIndex = array.length - 1;
        }
        final double min = valueToFind - tolerance;
        final double max = valueToFind + tolerance;
        for (int i = startIndex; i >= 0; i--){
            if (array[i] >= min && array[i] <= max){
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * <p>
     * Finds the last index of the given value within the array.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array
     *            the array to traverse backwards looking for the object, may be {@code null}
     * @param valueToFind
     *            the object to find
     * @return the last index of the value within the array,
     *         {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int lastIndexOf(final float[] array,final float valueToFind){
        return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
    }

    /**
     * <p>
     * Finds the last index of the given value in the array starting at the given index.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>
     * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A startIndex larger than the
     * array length will search from the end of the array.
     *
     * @param array
     *            the array to traverse for looking for the object, may be {@code null}
     * @param valueToFind
     *            the value to find
     * @param startIndex
     *            the start index to traverse backwards from
     * @return the last index of the value within the array,
     *         {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int lastIndexOf(final float[] array,final float valueToFind,int startIndex){
        if (isEmpty(array)){
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0){
            return INDEX_NOT_FOUND;
        }else if (startIndex >= array.length){
            startIndex = array.length - 1;
        }
        for (int i = startIndex; i >= 0; i--){
            if (valueToFind == array[i]){
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * <p>
     * Finds the last index of the given value within the array.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array
     *            the array to traverse backwards looking for the object, may be {@code null}
     * @param valueToFind
     *            the object to find
     * @return the last index of the value within the array,
     *         {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int lastIndexOf(final int[] array,final int valueToFind){
        return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
    }

    /**
     * <p>
     * Finds the last index of the given value in the array starting at the given index.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>
     * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A startIndex larger than the
     * array length will search from the end of the array.
     *
     * @param array
     *            the array to traverse for looking for the object, may be {@code null}
     * @param valueToFind
     *            the value to find
     * @param startIndex
     *            the start index to traverse backwards from
     * @return the last index of the value within the array,
     *         {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int lastIndexOf(final int[] array,final int valueToFind,int startIndex){
        if (array == null){
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0){
            return INDEX_NOT_FOUND;
        }else if (startIndex >= array.length){
            startIndex = array.length - 1;
        }
        for (int i = startIndex; i >= 0; i--){
            if (valueToFind == array[i]){
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * <p>
     * Finds the last index of the given value within the array.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array
     *            the array to traverse backwards looking for the object, may be {@code null}
     * @param valueToFind
     *            the object to find
     * @return the last index of the value within the array,
     *         {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int lastIndexOf(final long[] array,final long valueToFind){
        return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
    }

    /**
     * <p>
     * Finds the last index of the given value in the array starting at the given index.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>
     * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A startIndex larger than the
     * array length will search from the end of the array.
     *
     * @param array
     *            the array to traverse for looking for the object, may be {@code null}
     * @param valueToFind
     *            the value to find
     * @param startIndex
     *            the start index to traverse backwards from
     * @return the last index of the value within the array,
     *         {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int lastIndexOf(final long[] array,final long valueToFind,int startIndex){
        if (array == null){
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0){
            return INDEX_NOT_FOUND;
        }else if (startIndex >= array.length){
            startIndex = array.length - 1;
        }
        for (int i = startIndex; i >= 0; i--){
            if (valueToFind == array[i]){
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * <p>
     * Finds the last index of the given object within the array.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array
     *            the array to traverse backwards looking for the object, may be {@code null}
     * @param objectToFind
     *            the object to find, may be {@code null}
     * @return the last index of the object within the array,
     *         {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int lastIndexOf(final Object[] array,final Object objectToFind){
        return lastIndexOf(array, objectToFind, Integer.MAX_VALUE);
    }

    /**
     * <p>
     * Finds the last index of the given object in the array starting at the given index.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>
     * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A startIndex larger than
     * the array length will search from the end of the array.
     *
     * @param array
     *            the array to traverse for looking for the object, may be {@code null}
     * @param objectToFind
     *            the object to find, may be {@code null}
     * @param startIndex
     *            the start index to traverse backwards from
     * @return the last index of the object within the array,
     *         {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int lastIndexOf(final Object[] array,final Object objectToFind,int startIndex){
        if (array == null){
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0){
            return INDEX_NOT_FOUND;
        }else if (startIndex >= array.length){
            startIndex = array.length - 1;
        }
        if (objectToFind == null){
            for (int i = startIndex; i >= 0; i--){
                if (array[i] == null){
                    return i;
                }
            }
        }else if (array.getClass().getComponentType().isInstance(objectToFind)){
            for (int i = startIndex; i >= 0; i--){
                if (objectToFind.equals(array[i])){
                    return i;
                }
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * <p>
     * Finds the last index of the given value within the array.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array
     *            the array to traverse backwards looking for the object, may be {@code null}
     * @param valueToFind
     *            the object to find
     * @return the last index of the value within the array,
     *         {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int lastIndexOf(final short[] array,final short valueToFind){
        return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
    }

    /**
     * <p>
     * Finds the last index of the given value in the array starting at the given index.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>
     * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A startIndex larger than the
     * array length will search from the end of the array.
     *
     * @param array
     *            the array to traverse for looking for the object, may be {@code null}
     * @param valueToFind
     *            the value to find
     * @param startIndex
     *            the start index to traverse backwards from
     * @return the last index of the value within the array,
     *         {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int lastIndexOf(final short[] array,final short valueToFind,int startIndex){
        if (array == null){
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0){
            return INDEX_NOT_FOUND;
        }else if (startIndex >= array.length){
            startIndex = array.length - 1;
        }
        for (int i = startIndex; i >= 0; i--){
            if (valueToFind == array[i]){
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * <p>
     * Defensive programming technique to change a {@code null}
     * reference to an empty one.
     *
     * <p>
     * This method returns an empty array for a {@code null} input array.
     *
     * <p>
     * As a memory optimizing technique an empty array passed in will be overridden with
     * the empty {@code public static} references in this class.
     *
     * @param array
     *            the array to check for {@code null} or empty
     * @return the same array, {@code public static} empty array if {@code null} or empty input
     * @since 3.2
     */
    public static Class<?>[] nullToEmpty(final Class<?>[] array){
        if (isEmpty(array)){
            return EMPTY_CLASS_ARRAY;
        }
        return array;
    }

    // Primitive/Object array converters
    // ----------------------------------------------------------------------

    /**
     * <p>
     * Defensive programming technique to change a {@code null}
     * reference to an empty one.
     *
     * <p>
     * This method returns an empty array for a {@code null} input array.
     *
     * <p>
     * As a memory optimizing technique an empty array passed in will be overridden with
     * the empty {@code public static} references in this class.
     *
     * @param array
     *            the array to check for {@code null} or empty
     * @return the same array, {@code public static} empty array if {@code null} or empty input
     * @since 2.5
     */
    public static Object[] nullToEmpty(final Object[] array){
        if (isEmpty(array)){
            return EMPTY_OBJECT_ARRAY;
        }
        return array;
    }

    // nullToEmpty

    /**
     * <p>
     * Removes the element at the specified position from the specified array.
     * All subsequent elements are shifted to the left (subtracts one from
     * their indices).
     *
     * <p>
     * This method returns a new array with the same elements of the input
     * array except the element on the specified position. The component
     * type of the returned array is always the same as that of the input
     * array.
     *
     * <p>
     * If the input array is {@code null}, an IndexOutOfBoundsException
     * will be thrown, because in that case no valid index can be specified.
     *
     * <pre>
     * ArrayUtils.remove([true], 0)              = []
     * ArrayUtils.remove([true, false], 0)       = [false]
     * ArrayUtils.remove([true, false], 1)       = [true]
     * ArrayUtils.remove([true, true, false], 1) = [true, false]
     * </pre>
     *
     * @param array
     *            the array to remove the element from, may not be {@code null}
     * @param index
     *            the position of the element to be removed
     * @return A new array containing the existing elements except the element
     *         at the specified position.
     * @throws IndexOutOfBoundsException
     *             if the index is out of range
     *             (index &lt; 0 || index &gt;= array.length), or if the array is {@code null}.
     * @since 2.1
     */
    public static boolean[] remove(final boolean[] array,final int index){
        return (boolean[]) remove((Object) array, index);
    }

    /**
     * <p>
     * Removes the element at the specified position from the specified array.
     * All subsequent elements are shifted to the left (subtracts one from
     * their indices).
     *
     * <p>
     * This method returns a new array with the same elements of the input
     * array except the element on the specified position. The component
     * type of the returned array is always the same as that of the input
     * array.
     *
     * <p>
     * If the input array is {@code null}, an IndexOutOfBoundsException
     * will be thrown, because in that case no valid index can be specified.
     *
     * <pre>
     * ArrayUtils.remove([1], 0)          = []
     * ArrayUtils.remove([1, 0], 0)       = [0]
     * ArrayUtils.remove([1, 0], 1)       = [1]
     * ArrayUtils.remove([1, 0, 1], 1)    = [1, 1]
     * </pre>
     *
     * @param array
     *            the array to remove the element from, may not be {@code null}
     * @param index
     *            the position of the element to be removed
     * @return A new array containing the existing elements except the element
     *         at the specified position.
     * @throws IndexOutOfBoundsException
     *             if the index is out of range
     *             (index &lt; 0 || index &gt;= array.length), or if the array is {@code null}.
     * @since 2.1
     */
    public static byte[] remove(final byte[] array,final int index){
        return (byte[]) remove((Object) array, index);
    }

    /**
     * <p>
     * Removes the element at the specified position from the specified array.
     * All subsequent elements are shifted to the left (subtracts one from
     * their indices).
     *
     * <p>
     * This method returns a new array with the same elements of the input
     * array except the element on the specified position. The component
     * type of the returned array is always the same as that of the input
     * array.
     *
     * <p>
     * If the input array is {@code null}, an IndexOutOfBoundsException
     * will be thrown, because in that case no valid index can be specified.
     *
     * <pre>
     * ArrayUtils.remove(['a'], 0)           = []
     * ArrayUtils.remove(['a', 'b'], 0)      = ['b']
     * ArrayUtils.remove(['a', 'b'], 1)      = ['a']
     * ArrayUtils.remove(['a', 'b', 'c'], 1) = ['a', 'c']
     * </pre>
     *
     * @param array
     *            the array to remove the element from, may not be {@code null}
     * @param index
     *            the position of the element to be removed
     * @return A new array containing the existing elements except the element
     *         at the specified position.
     * @throws IndexOutOfBoundsException
     *             if the index is out of range
     *             (index &lt; 0 || index &gt;= array.length), or if the array is {@code null}.
     * @since 2.1
     */
    public static char[] remove(final char[] array,final int index){
        return (char[]) remove((Object) array, index);
    }

    /**
     * <p>
     * Removes the element at the specified position from the specified array.
     * All subsequent elements are shifted to the left (subtracts one from
     * their indices).
     *
     * <p>
     * This method returns a new array with the same elements of the input
     * array except the element on the specified position. The component
     * type of the returned array is always the same as that of the input
     * array.
     *
     * <p>
     * If the input array is {@code null}, an IndexOutOfBoundsException
     * will be thrown, because in that case no valid index can be specified.
     *
     * <pre>
     * ArrayUtils.remove([1.1], 0)           = []
     * ArrayUtils.remove([2.5, 6.0], 0)      = [6.0]
     * ArrayUtils.remove([2.5, 6.0], 1)      = [2.5]
     * ArrayUtils.remove([2.5, 6.0, 3.8], 1) = [2.5, 3.8]
     * </pre>
     *
     * @param array
     *            the array to remove the element from, may not be {@code null}
     * @param index
     *            the position of the element to be removed
     * @return A new array containing the existing elements except the element
     *         at the specified position.
     * @throws IndexOutOfBoundsException
     *             if the index is out of range
     *             (index &lt; 0 || index &gt;= array.length), or if the array is {@code null}.
     * @since 2.1
     */
    public static double[] remove(final double[] array,final int index){
        return (double[]) remove((Object) array, index);
    }

    /**
     * <p>
     * Removes the element at the specified position from the specified array.
     * All subsequent elements are shifted to the left (subtracts one from
     * their indices).
     *
     * <p>
     * This method returns a new array with the same elements of the input
     * array except the element on the specified position. The component
     * type of the returned array is always the same as that of the input
     * array.
     *
     * <p>
     * If the input array is {@code null}, an IndexOutOfBoundsException
     * will be thrown, because in that case no valid index can be specified.
     *
     * <pre>
     * ArrayUtils.remove([1.1], 0)           = []
     * ArrayUtils.remove([2.5, 6.0], 0)      = [6.0]
     * ArrayUtils.remove([2.5, 6.0], 1)      = [2.5]
     * ArrayUtils.remove([2.5, 6.0, 3.8], 1) = [2.5, 3.8]
     * </pre>
     *
     * @param array
     *            the array to remove the element from, may not be {@code null}
     * @param index
     *            the position of the element to be removed
     * @return A new array containing the existing elements except the element
     *         at the specified position.
     * @throws IndexOutOfBoundsException
     *             if the index is out of range
     *             (index &lt; 0 || index &gt;= array.length), or if the array is {@code null}.
     * @since 2.1
     */
    public static float[] remove(final float[] array,final int index){
        return (float[]) remove((Object) array, index);
    }

    /**
     * <p>
     * Removes the element at the specified position from the specified array.
     * All subsequent elements are shifted to the left (subtracts one from
     * their indices).
     *
     * <p>
     * This method returns a new array with the same elements of the input
     * array except the element on the specified position. The component
     * type of the returned array is always the same as that of the input
     * array.
     *
     * <p>
     * If the input array is {@code null}, an IndexOutOfBoundsException
     * will be thrown, because in that case no valid index can be specified.
     *
     * <pre>
     * ArrayUtils.remove([1], 0)         = []
     * ArrayUtils.remove([2, 6], 0)      = [6]
     * ArrayUtils.remove([2, 6], 1)      = [2]
     * ArrayUtils.remove([2, 6, 3], 1)   = [2, 3]
     * </pre>
     *
     * @param array
     *            the array to remove the element from, may not be {@code null}
     * @param index
     *            the position of the element to be removed
     * @return A new array containing the existing elements except the element
     *         at the specified position.
     * @throws IndexOutOfBoundsException
     *             if the index is out of range
     *             (index &lt; 0 || index &gt;= array.length), or if the array is {@code null}.
     * @since 2.1
     */
    public static int[] remove(final int[] array,final int index){
        return (int[]) remove((Object) array, index);
    }

    /**
     * <p>
     * Removes the element at the specified position from the specified array.
     * All subsequent elements are shifted to the left (subtracts one from
     * their indices).
     *
     * <p>
     * This method returns a new array with the same elements of the input
     * array except the element on the specified position. The component
     * type of the returned array is always the same as that of the input
     * array.
     *
     * <p>
     * If the input array is {@code null}, an IndexOutOfBoundsException
     * will be thrown, because in that case no valid index can be specified.
     *
     * <pre>
     * ArrayUtils.remove([1], 0)         = []
     * ArrayUtils.remove([2, 6], 0)      = [6]
     * ArrayUtils.remove([2, 6], 1)      = [2]
     * ArrayUtils.remove([2, 6, 3], 1)   = [2, 3]
     * </pre>
     *
     * @param array
     *            the array to remove the element from, may not be {@code null}
     * @param index
     *            the position of the element to be removed
     * @return A new array containing the existing elements except the element
     *         at the specified position.
     * @throws IndexOutOfBoundsException
     *             if the index is out of range
     *             (index &lt; 0 || index &gt;= array.length), or if the array is {@code null}.
     * @since 2.1
     */
    public static long[] remove(final long[] array,final int index){
        return (long[]) remove((Object) array, index);
    }

    /**
     * <p>
     * Removes the element at the specified position from the specified array.
     * All subsequent elements are shifted to the left (subtracts one from
     * their indices).
     *
     * <p>
     * This method returns a new array with the same elements of the input
     * array except the element on the specified position. The component
     * type of the returned array is always the same as that of the input
     * array.
     *
     * <p>
     * If the input array is {@code null}, an IndexOutOfBoundsException
     * will be thrown, because in that case no valid index can be specified.
     *
     * @param array
     *            the array to remove the element from, may not be {@code null}
     * @param index
     *            the position of the element to be removed
     * @return A new array containing the existing elements except the element
     *         at the specified position.
     * @throws IndexOutOfBoundsException
     *             if the index is out of range
     *             (index &lt; 0 || index &gt;= array.length), or if the array is {@code null}.
     * @since 2.1
     */
    private static Object remove(final Object array,final int index){
        final int length = getLength(array);
        if (index < 0 || index >= length){
            throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
        }

        final Object result = Array.newInstance(array.getClass().getComponentType(), length - 1);
        System.arraycopy(array, 0, result, 0, index);
        if (index < length - 1){
            System.arraycopy(array, index + 1, result, index, length - index - 1);
        }

        return result;
    }

    /**
     * <p>
     * Removes the element at the specified position from the specified array.
     * All subsequent elements are shifted to the left (subtracts one from
     * their indices).
     *
     * <p>
     * This method returns a new array with the same elements of the input
     * array except the element on the specified position. The component
     * type of the returned array is always the same as that of the input
     * array.
     *
     * <p>
     * If the input array is {@code null}, an IndexOutOfBoundsException
     * will be thrown, because in that case no valid index can be specified.
     *
     * <pre>
     * ArrayUtils.remove([1], 0)         = []
     * ArrayUtils.remove([2, 6], 0)      = [6]
     * ArrayUtils.remove([2, 6], 1)      = [2]
     * ArrayUtils.remove([2, 6, 3], 1)   = [2, 3]
     * </pre>
     *
     * @param array
     *            the array to remove the element from, may not be {@code null}
     * @param index
     *            the position of the element to be removed
     * @return A new array containing the existing elements except the element
     *         at the specified position.
     * @throws IndexOutOfBoundsException
     *             if the index is out of range
     *             (index &lt; 0 || index &gt;= array.length), or if the array is {@code null}.
     * @since 2.1
     */
    public static short[] remove(final short[] array,final int index){
        return (short[]) remove((Object) array, index);
    }

    /**
     * <p>
     * Removes the element at the specified position from the specified array.
     * All subsequent elements are shifted to the left (subtracts one from
     * their indices).
     *
     * <p>
     * This method returns a new array with the same elements of the input
     * array except the element on the specified position. The component
     * type of the returned array is always the same as that of the input
     * array.
     *
     * <p>
     * If the input array is {@code null}, an IndexOutOfBoundsException
     * will be thrown, because in that case no valid index can be specified.
     *
     * <pre>
     * ArrayUtils.remove(["a"], 0)           = []
     * ArrayUtils.remove(["a", "b"], 0)      = ["b"]
     * ArrayUtils.remove(["a", "b"], 1)      = ["a"]
     * ArrayUtils.remove(["a", "b", "c"], 1) = ["a", "c"]
     * </pre>
     *
     * @param <T>
     *            the component type of the array
     * @param array
     *            the array to remove the element from, may not be {@code null}
     * @param index
     *            the position of the element to be removed
     * @return A new array containing the existing elements except the element
     *         at the specified position.
     * @throws IndexOutOfBoundsException
     *             if the index is out of range
     *             (index &lt; 0 || index &gt;= array.length), or if the array is {@code null}.
     * @since 2.1
     */
    // remove() always creates an array of the same type as its input
    public static <T> T[] remove(final T[] array,final int index){
        return remove(array, index);
    }

    /**
     * Removes multiple array elements specified by index.
     * 
     * @param array
     *            source
     * @param indices
     *            to remove
     * @return new array of same type minus elements specified by unique values of {@code indices}
     * @since 3.0.1
     */
    // package protected for access by unit tests
    static Object removeAll(final Object array,final int...indices){
        final int length = getLength(array);
        int diff = 0; // number of distinct indexes, i.e. number of entries that will be removed
        final int[] clonedIndices = clone(indices);
        Arrays.sort(clonedIndices);

        // identify length of result array
        if (isNotEmpty(clonedIndices)){
            int i = clonedIndices.length;
            int prevIndex = length;
            while (--i >= 0){
                final int index = clonedIndices[i];
                if (index < 0 || index >= length){
                    throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
                }
                if (index >= prevIndex){
                    continue;
                }
                diff++;
                prevIndex = index;
            }
        }

        // create result array
        final Object result = Array.newInstance(array.getClass().getComponentType(), length - diff);
        if (diff < length){
            int end = length; // index just after last copy
            int dest = length - diff; // number of entries so far not copied
            for (int i = clonedIndices.length - 1; i >= 0; i--){
                final int index = clonedIndices[i];
                if (end - index > 1){ // same as (cp > 0)
                    final int cp = end - index - 1;
                    dest -= cp;
                    System.arraycopy(array, index + 1, result, dest, cp);
                    // Afer this copy, we still have room for dest items.
                }
                end = index;
            }
            if (end > 0){
                System.arraycopy(array, 0, result, 0, end);
            }
        }
        return result;
    }

    /**
     * <p>
     * Removes the elements at the specified positions from the specified array.
     * All remaining elements are shifted to the left.
     *
     * <p>
     * This method returns a new array with the same elements of the input
     * array except those at the specified positions. The component
     * type of the returned array is always the same as that of the input
     * array.
     *
     * <p>
     * If the input array is {@code null}, an IndexOutOfBoundsException
     * will be thrown, because in that case no valid index can be specified.
     *
     * <pre>
     * ArrayUtils.removeAll(["a", "b", "c"], 0, 2) = ["b"]
     * ArrayUtils.removeAll(["a", "b", "c"], 1, 2) = ["a"]
     * </pre>
     *
     * @param <T>
     *            the component type of the array
     * @param array
     *            the array to remove the element from, may not be {@code null}
     * @param indices
     *            the positions of the elements to be removed
     * @return A new array containing the existing elements except those
     *         at the specified positions.
     * @throws IndexOutOfBoundsException
     *             if any index is out of range
     *             (index &lt; 0 || index &gt;= array.length), or if the array is {@code null}.
     * @since 3.0.1
     */
    // removeAll() always creates an array of the same type as its input
    public static <T> T[] removeAll(final T[] array,final int...indices){
        return removeAll(array, indices);
    }

    /**
     * <p>
     * Removes the first occurrence of the specified element from the
     * specified array. All subsequent elements are shifted to the left
     * (subtracts one from their indices). If the array doesn't contains
     * such an element, no elements are removed from the array.
     *
     * <p>
     * This method returns a new array with the same elements of the input
     * array except the first occurrence of the specified element. The component
     * type of the returned array is always the same as that of the input
     * array.
     *
     * <pre>
     * ArrayUtils.removeElement(null, "a")            = null
     * ArrayUtils.removeElement([], "a")              = []
     * ArrayUtils.removeElement(["a"], "b")           = ["a"]
     * ArrayUtils.removeElement(["a", "b"], "a")      = ["b"]
     * ArrayUtils.removeElement(["a", "b", "a"], "a") = ["b", "a"]
     * </pre>
     *
     * @param <T>
     *            the component type of the array
     * @param array
     *            the array to remove the element from, may be {@code null}
     * @param element
     *            the element to be removed
     * @return A new array containing the existing elements except the first
     *         occurrence of the specified element.
     * @since 2.1
     */
    public static <T> T[] removeElement(final T[] array,final Object element){
        final int index = indexOf(array, element);
        if (index == INDEX_NOT_FOUND){
            return clone(array);
        }
        return remove(array, index);
    }

    /**
     * Shifts the order of a series of elements in the given double array.
     *
     * <p>
     * There is no special handling for multi-dimensional arrays. This method
     * does nothing for {@code null} or empty input arrays.
     * </p>
     *
     * @param array
     *            the array to shift, may be {@code null}
     * @param startIndexInclusive
     *            the starting index. Undervalue (&lt;0) is promoted to 0, overvalue (&gt;array.length) results in no
     *            change.
     * @param endIndexExclusive
     *            elements up to endIndex-1 are shifted in the array. Undervalue (&lt; start index) results in no
     *            change. Overvalue (&gt;array.length) is demoted to array length.
     * @param offset
     *            The number of positions to rotate the elements. If the offset is larger than the number of elements to
     *            rotate, than the effective offset is modulo the number of elements to rotate.
     * @since 3.5
     */
    public static void shift(final double[] array,int startIndexInclusive,int endIndexExclusive,int offset){
        if (array == null){
            return;
        }
        if (startIndexInclusive >= array.length - 1 || endIndexExclusive <= 0){
            return;
        }
        if (startIndexInclusive < 0){
            startIndexInclusive = 0;
        }
        if (endIndexExclusive >= array.length){
            endIndexExclusive = array.length;
        }
        int n = endIndexExclusive - startIndexInclusive;
        if (n <= 1){
            return;
        }
        offset %= n;
        if (offset < 0){
            offset += n;
        }
        // For algorithm explanations and proof of O(n) time complexity and O(1) space complexity
        // see https://beradrian.wordpress.com/2015/04/07/shift-an-array-in-on-in-place/
        while (n > 1 && offset > 0){
            final int n_offset = n - offset;

            if (offset > n_offset){
                swap(array, startIndexInclusive, startIndexInclusive + n - n_offset, n_offset);
                n = offset;
                offset -= n_offset;
            }else if (offset < n_offset){
                swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
                startIndexInclusive += offset;
                n = n_offset;
            }else{
                swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
                break;
            }
        }
    }

    /**
     * Shifts the order of the given int array.
     *
     * <p>
     * There is no special handling for multi-dimensional arrays. This method
     * does nothing for {@code null} or empty input arrays.
     * </p>
     *
     * @param array
     *            the array to shift, may be {@code null}
     * @param offset
     *            The number of positions to rotate the elements. If the offset is larger than the number of elements to
     *            rotate, than the effective offset is modulo the number of elements to rotate.
     * @since 3.5
     */
    public static void shift(final int[] array,final int offset){
        if (array == null){
            return;
        }
        shift(array, 0, array.length, offset);
    }

    /**
     * Shifts the order of a series of elements in the given int array.
     *
     * <p>
     * There is no special handling for multi-dimensional arrays. This method
     * does nothing for {@code null} or empty input arrays.
     * </p>
     *
     * @param array
     *            the array to shift, may be {@code null}
     * @param startIndexInclusive
     *            the starting index. Undervalue (&lt;0) is promoted to 0, overvalue (&gt;array.length) results in no
     *            change.
     * @param endIndexExclusive
     *            elements up to endIndex-1 are shifted in the array. Undervalue (&lt; start index) results in no
     *            change. Overvalue (&gt;array.length) is demoted to array length.
     * @param offset
     *            The number of positions to rotate the elements. If the offset is larger than the number of elements to
     *            rotate, than the effective offset is modulo the number of elements to rotate.
     * @since 3.5
     */
    public static void shift(final int[] array,int startIndexInclusive,int endIndexExclusive,int offset){
        if (array == null){
            return;
        }
        if (startIndexInclusive >= array.length - 1 || endIndexExclusive <= 0){
            return;
        }
        if (startIndexInclusive < 0){
            startIndexInclusive = 0;
        }
        if (endIndexExclusive >= array.length){
            endIndexExclusive = array.length;
        }
        int n = endIndexExclusive - startIndexInclusive;
        if (n <= 1){
            return;
        }
        offset %= n;
        if (offset < 0){
            offset += n;
        }
        // For algorithm explanations and proof of O(n) time complexity and O(1) space complexity
        // see https://beradrian.wordpress.com/2015/04/07/shift-an-array-in-on-in-place/
        while (n > 1 && offset > 0){
            final int n_offset = n - offset;

            if (offset > n_offset){
                swap(array, startIndexInclusive, startIndexInclusive + n - n_offset, n_offset);
                n = offset;
                offset -= n_offset;
            }else if (offset < n_offset){
                swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
                startIndexInclusive += offset;
                n = n_offset;
            }else{
                swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
                break;
            }
        }
    }

    // Subarrays
    //-----------------------------------------------------------------------
    /**
     * <p>
     * Produces a new array containing the elements between
     * the start and end indices.
     *
     * <p>
     * The start index is inclusive, the end index exclusive.
     * Null array input produces null output.
     *
     * <p>
     * The component type of the subarray is always the same as
     * that of the input array. Thus, if the input is an array of type
     * {@code Date}, the following usage is envisaged:
     *
     * <pre>
     * 
     * Date[] someDates = (Date[]) ArrayUtils.subarray(allDates, 2, 5);
     * </pre>
     *
     * @param <T>
     *            the component type of the array
     * @param array
     *            the array
     * @param startIndexInclusive
     *            the starting index. Undervalue (&lt;0)
     *            is promoted to 0, overvalue (&gt;array.length) results
     *            in an empty array.
     * @param endIndexExclusive
     *            elements up to endIndex-1 are present in the
     *            returned subarray. Undervalue (&lt; startIndex) produces
     *            empty array, overvalue (&gt;array.length) is demoted to
     *            array length.
     * @return a new array containing the elements between
     *         the start and end indices.
     * @since 2.1
     * @see Arrays#copyOfRange(Object[], int, int)
     */
    public static <T> T[] subarray(final T[] array,int startIndexInclusive,int endIndexExclusive){
        if (array == null){
            return null;
        }
        if (startIndexInclusive < 0){
            startIndexInclusive = 0;
        }
        if (endIndexExclusive > array.length){
            endIndexExclusive = array.length;
        }
        final int newSize = endIndexExclusive - startIndexInclusive;
        final Class<?> type = array.getClass().getComponentType();
        if (newSize <= 0){
            @SuppressWarnings("unchecked") // OK, because array is of type T
            final T[] emptyArray = (T[]) Array.newInstance(type, 0);
            return emptyArray;
        }
        @SuppressWarnings("unchecked") // OK, because array is of type T
        final T[] subarray = (T[]) Array.newInstance(type, newSize);
        System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
        return subarray;
    }

    /**
     * Swaps a series of elements in the given double array.
     *
     * <p>
     * This method does nothing for a {@code null} or empty input array or
     * for overflow indices. Negative indices are promoted to 0(zero). If any
     * of the sub-arrays to swap falls outside of the given array, then the
     * swap is stopped at the end of the array and as many as possible elements
     * are swapped.
     * </p>
     *
     * Examples:
     * <ul>
     * <li>ArrayUtils.swap([1, 2, 3, 4], 0, 2, 1) -&gt; [3, 2, 1, 4]</li>
     * <li>ArrayUtils.swap([1, 2, 3, 4], 0, 0, 1) -&gt; [1, 2, 3, 4]</li>
     * <li>ArrayUtils.swap([1, 2, 3, 4], 2, 0, 2) -&gt; [3, 4, 1, 2]</li>
     * <li>ArrayUtils.swap([1, 2, 3, 4], -3, 2, 2) -&gt; [3, 4, 1, 2]</li>
     * <li>ArrayUtils.swap([1, 2, 3, 4], 0, 3, 3) -&gt; [4, 2, 3, 1]</li>
     * </ul>
     *
     * @param array
     *            the array to swap, may be {@code null}
     * @param offset1
     *            the index of the first element in the series to swap
     * @param offset2
     *            the index of the second element in the series to swap
     * @param len
     *            the number of elements to swap starting with the given indices
     * @since 3.5
     */
    public static void swap(final double[] array,int offset1,int offset2,int len){
        if (isEmpty(array) || offset1 >= array.length || offset2 >= array.length){
            return;
        }
        if (offset1 < 0){
            offset1 = 0;
        }
        if (offset2 < 0){
            offset2 = 0;
        }
        len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
        for (int i = 0; i < len; i++, offset1++, offset2++){
            final double aux = array[offset1];
            array[offset1] = array[offset2];
            array[offset2] = aux;
        }
    }

    /**
     * Swaps a series of elements in the given int array.
     *
     * <p>
     * This method does nothing for a {@code null} or empty input array or
     * for overflow indices. Negative indices are promoted to 0(zero). If any
     * of the sub-arrays to swap falls outside of the given array, then the
     * swap is stopped at the end of the array and as many as possible elements
     * are swapped.
     * </p>
     *
     * Examples:
     * <ul>
     * <li>ArrayUtils.swap([1, 2, 3, 4], 0, 2, 1) -&gt; [3, 2, 1, 4]</li>
     * <li>ArrayUtils.swap([1, 2, 3, 4], 0, 0, 1) -&gt; [1, 2, 3, 4]</li>
     * <li>ArrayUtils.swap([1, 2, 3, 4], 2, 0, 2) -&gt; [3, 4, 1, 2]</li>
     * <li>ArrayUtils.swap([1, 2, 3, 4], -3, 2, 2) -&gt; [3, 4, 1, 2]</li>
     * <li>ArrayUtils.swap([1, 2, 3, 4], 0, 3, 3) -&gt; [4, 2, 3, 1]</li>
     * </ul>
     *
     * @param array
     *            the array to swap, may be {@code null}
     * @param offset1
     *            the index of the first element in the series to swap
     * @param offset2
     *            the index of the second element in the series to swap
     * @param len
     *            the number of elements to swap starting with the given indices
     * @since 3.5
     */
    public static void swap(final int[] array,int offset1,int offset2,int len){
        if (isEmpty(array) || offset1 >= array.length || offset2 >= array.length){
            return;
        }
        if (offset1 < 0){
            offset1 = 0;
        }
        if (offset2 < 0){
            offset2 = 0;
        }
        len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
        for (int i = 0; i < len; i++, offset1++, offset2++){
            final int aux = array[offset1];
            array[offset1] = array[offset2];
            array[offset2] = aux;
        }
    }

    // Generic array
    //-----------------------------------------------------------------------
    /**
     * <p>
     * Create a type-safe generic array.
     *
     * <p>
     * The Java language does not allow an array to be created from a generic type:
     *
     * <pre>
     * 
     * public static &lt;T&gt; T[] createAnArray(int size){
     *     return new T[size]; // compiler error here
     * }
     * 
     * public static &lt;T&gt; T[] createAnArray(int size){
     *     return (T[]) new Object[size]; // ClassCastException at runtime
     * }
     * </pre>
     *
     * <p>
     * Therefore new arrays of generic types can be created with this method.
     * For example, an array of Strings can be created:
     *
     * <pre>
     * 
     * String[] array = ArrayUtils.toArray("1", "2");
     * 
     * String[] emptyArray = ArrayUtils.&lt;String&gt; toArray();
     * </pre>
     *
     * <p>
     * The method is typically used in scenarios, where the caller itself uses generic types
     * that have to be combined into an array.
     *
     * <p>
     * Note, this method makes only sense to provide arguments of the same type so that the
     * compiler can deduce the type of the array itself. While it is possible to select the
     * type explicitly like in
     * {@code Number[] array = ArrayUtils.&lt;Number&gt;toArray(Integer.valueOf(42), Double.valueOf(Math.PI))},
     * there is no real advantage when compared to
     * {@code new Number[] {Integer.valueOf(42), Double.valueOf(Math.PI)}}.
     *
     * @param <T>
     *            the array's element type
     * @param items
     *            the varargs array items, null allowed
     * @return the array, not null unless a null array is passed in
     * @since 3.0
     */
    public static <T> T[] toArray(@SuppressWarnings("unchecked") final T...items){
        return items;
    }

    // To map
    //-----------------------------------------------------------------------
    /**
     * <p>
     * Converts the given array into a {@link java.util.Map}. Each element of the array
     * must be either a {@link java.util.Map.Entry} or an Array, containing at least two
     * elements, where the first element is used as key and the second as
     * value.
     *
     * <p>
     * This method can be used to initialize:
     * 
     * <pre>
     * 
     * // Create a Map mapping colors.
     * Map colorMap = ArrayUtils.toMap(new String[][] { { "RED", "#FF0000" }, { "GREEN", "#00FF00" }, { "BLUE", "#0000FF" } });
     * </pre>
     *
     * <p>
     * This method returns {@code null} for a {@code null} input array.
     *
     * @param array
     *            an array whose elements are either a {@link java.util.Map.Entry} or
     *            an Array containing at least two elements, may be {@code null}
     * @return a {@code Map} that was created from the array
     * @throws IllegalArgumentException
     *             if one element of this Array is
     *             itself an Array containing less then two elements
     * @throws IllegalArgumentException
     *             if the array contains elements other
     *             than {@link java.util.Map.Entry} and an Array
     */
    public static Map<Object, Object> toMap(final Object[] array){
        if (array == null){
            return null;
        }
        final Map<Object, Object> map = new HashMap<>((int) (array.length * 1.5));
        for (int i = 0; i < array.length; i++){
            final Object object = array[i];
            if (object instanceof Map.Entry<?, ?>){
                final Map.Entry<?, ?> entry = (Map.Entry<?, ?>) object;
                map.put(entry.getKey(), entry.getValue());
            }else if (object instanceof Object[]){
                final Object[] entry = (Object[]) object;
                if (entry.length < 2){
                    throw new IllegalArgumentException("Array element " + i + ", '" + object + "', has a length less than 2");
                }
                map.put(entry[0], entry[1]);
            }else{
                throw new IllegalArgumentException("Array element " + i + ", '" + object + "', is neither of type Map.Entry nor an Array");
            }
        }
        return map;
    }

    /**
     * <p>
     * Converts an array of primitive ints to objects.
     *
     * <p>
     * This method returns {@code null} for a {@code null} input array.
     *
     * @param array
     *            an {@code int} array
     * @return an {@code Integer} array, {@code null} if null array input
     */
    public static Integer[] toObject(final int[] array){
        if (array == null){
            return null;
        }else if (array.length == 0){
            return EMPTY_INTEGER_OBJECT_ARRAY;
        }
        final Integer[] result = new Integer[array.length];
        for (int i = 0; i < array.length; i++){
            result[i] = Integer.valueOf(array[i]);
        }
        return result;
    }

    // Boolean array converters

    // Double array converters
    // ----------------------------------------------------------------------
    /**
     * <p>
     * Converts an array of object Doubles to primitives.
     *
     * <p>
     * This method returns {@code null} for a {@code null} input array.
     *
     * @param array
     *            a {@code Double} array, may be {@code null}
     * @return a {@code double} array, {@code null} if null array input
     * @throws NullPointerException
     *             if array content is {@code null}
     */
    public static double[] toPrimitive(final Double[] array){
        if (array == null){
            return null;
        }else if (array.length == 0){
            return EMPTY_DOUBLE_ARRAY;
        }
        final double[] result = new double[array.length];
        for (int i = 0; i < array.length; i++){
            result[i] = array[i].doubleValue();
        }
        return result;
    }

    //   Float array converters
    // ----------------------------------------------------------------------
    /**
     * <p>
     * Converts an array of object Floats to primitives.
     *
     * <p>
     * This method returns {@code null} for a {@code null} input array.
     *
     * @param array
     *            a {@code Float} array, may be {@code null}
     * @return a {@code float} array, {@code null} if null array input
     * @throws NullPointerException
     *             if array content is {@code null}
     */
    public static float[] toPrimitive(final Float[] array){
        if (array == null){
            return null;
        }else if (array.length == 0){
            return EMPTY_FLOAT_ARRAY;
        }
        final float[] result = new float[array.length];
        for (int i = 0; i < array.length; i++){
            result[i] = array[i].floatValue();
        }
        return result;
    }

    // Int array converters
    // ----------------------------------------------------------------------
    /**
     * <p>
     * Converts an array of object Integers to primitives.
     *
     * <p>
     * This method returns {@code null} for a {@code null} input array.
     *
     * @param array
     *            a {@code Integer} array, may be {@code null}
     * @return an {@code int} array, {@code null} if null array input
     * @throws NullPointerException
     *             if array content is {@code null}
     */
    public static int[] toPrimitive(final Integer[] array){
        if (array == null){
            return null;
        }else if (array.length == 0){
            return EMPTY_INT_ARRAY;
        }
        final int[] result = new int[array.length];
        for (int i = 0; i < array.length; i++){
            result[i] = array[i].intValue();
        }
        return result;
    }

    // Long array converters
    // ----------------------------------------------------------------------
    /**
     * <p>
     * Converts an array of object Longs to primitives.
     *
     * <p>
     * This method returns {@code null} for a {@code null} input array.
     *
     * @param array
     *            a {@code Long} array, may be {@code null}
     * @return a {@code long} array, {@code null} if null array input
     * @throws NullPointerException
     *             if array content is {@code null}
     */
    public static long[] toPrimitive(final Long[] array){
        if (array == null){
            return null;
        }else if (array.length == 0){
            return EMPTY_LONG_ARRAY;
        }
        final long[] result = new long[array.length];
        for (int i = 0; i < array.length; i++){
            result[i] = array[i].longValue();
        }
        return result;
    }

    /**
     * <p>
     * Create an array of primitive type from an array of wrapper types.
     *
     * <p>
     * This method returns {@code null} for a {@code null} input array.
     *
     * @param array
     *            an array of wrapper object
     * @return an array of the corresponding primitive type, or the original array
     * @since 3.5
     */
    public static Object toPrimitive(final Object array){
        if (array == null){
            return null;
        }
        final Class<?> ct = array.getClass().getComponentType();
        final Class<?> pt = ClassUtils.wrapperToPrimitive(ct);
        if (Integer.TYPE.equals(pt)){
            return toPrimitive((Integer[]) array);
        }
        if (Long.TYPE.equals(pt)){
            return toPrimitive((Long[]) array);
        }
        if (Short.TYPE.equals(pt)){
            return toPrimitive((Short[]) array);
        }
        if (Double.TYPE.equals(pt)){
            return toPrimitive((Double[]) array);
        }
        if (Float.TYPE.equals(pt)){
            return toPrimitive((Float[]) array);
        }
        return array;
    }

    // Short array converters
    // ----------------------------------------------------------------------
    /**
     * <p>
     * Converts an array of object Shorts to primitives.
     *
     * <p>
     * This method returns {@code null} for a {@code null} input array.
     *
     * @param array
     *            a {@code Short} array, may be {@code null}
     * @return a {@code byte} array, {@code null} if null array input
     * @throws NullPointerException
     *             if array content is {@code null}
     */
    public static short[] toPrimitive(final Short[] array){
        if (array == null){
            return null;
        }else if (array.length == 0){
            return EMPTY_SHORT_ARRAY;
        }
        final short[] result = new short[array.length];
        for (int i = 0; i < array.length; i++){
            result[i] = array[i].shortValue();
        }
        return result;
    }

    // Basic methods handling multi-dimensional arrays
    //-----------------------------------------------------------------------
    /**
     * <p>
     * Outputs an array as a String, treating {@code null} as an empty array.
     *
     * <p>
     * Multi-dimensional arrays are handled correctly, including
     * multi-dimensional primitive arrays.
     *
     * <p>
     * The format is that of Java source code, for example {@code {a,b}}.
     *
     * @param array
     *            the array to get a toString for, may be {@code null}
     * @return a String representation of the array, '{}' if null array input
     */
    public static String toString(final Object array){
        return toString(array, "{}");
    }

    /**
     * <p>
     * Outputs an array as a String handling {@code null}s.
     *
     * <p>
     * Multi-dimensional arrays are handled correctly, including
     * multi-dimensional primitive arrays.
     *
     * <p>
     * The format is that of Java source code, for example {@code {a,b}}.
     *
     * @param array
     *            the array to get a toString for, may be {@code null}
     * @param stringIfNull
     *            the String to return if the array is {@code null}
     * @return a String representation of the array
     */
    public static String toString(final Object array,final String stringIfNull){
        if (array == null){
            return stringIfNull;
        }
        return new ToStringBuilder(array, ToStringStyle.SIMPLE_STYLE).append(array).toString();
    }

}
