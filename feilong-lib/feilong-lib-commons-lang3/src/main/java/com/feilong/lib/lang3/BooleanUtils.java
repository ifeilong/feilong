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

import com.feilong.lib.lang3.math.NumberUtils;

/**
 * <p>
 * Operations on boolean primitives and Boolean objects.
 * </p>
 *
 * <p>
 * This class tries to handle {@code null} input gracefully.
 * An exception will not be thrown for a {@code null} input.
 * Each method documents its behaviour in more detail.
 * </p>
 *
 * <p>
 * #ThreadSafe#
 * </p>
 * 
 * @since 2.0
 */
public class BooleanUtils{

    /**
     * <p>
     * {@code BooleanUtils} instances should NOT be constructed in standard programming.
     * Instead, the class should be used as {@code BooleanUtils.negate(true);}.
     * </p>
     *
     * <p>
     * This constructor is public to permit tools that require a JavaBean instance
     * to operate.
     * </p>
     */
    public BooleanUtils(){
        super();
    }

    // boolean Boolean methods
    //-----------------------------------------------------------------------
    /**
     * <p>
     * Checks if a {@code Boolean} value is {@code true},
     * handling {@code null} by returning {@code false}.
     * </p>
     *
     * <pre>
     *   BooleanUtils.isTrue(Boolean.TRUE)  = true
     *   BooleanUtils.isTrue(Boolean.FALSE) = false
     *   BooleanUtils.isTrue(null)          = false
     * </pre>
     *
     * @param bool
     *            the boolean to check, {@code null} returns {@code false}
     * @return {@code true} only if the input is non-null and true
     * @since 2.1
     */
    public static boolean isTrue(final Boolean bool){
        return Boolean.TRUE.equals(bool);
    }

    /**
     * <p>
     * Checks if a {@code Boolean} value is <i>not</i> {@code true},
     * handling {@code null} by returning {@code true}.
     * </p>
     *
     * <pre>
     *   BooleanUtils.isNotTrue(Boolean.TRUE)  = false
     *   BooleanUtils.isNotTrue(Boolean.FALSE) = true
     *   BooleanUtils.isNotTrue(null)          = true
     * </pre>
     *
     * @param bool
     *            the boolean to check, null returns {@code true}
     * @return {@code true} if the input is null or false
     * @since 2.3
     */
    public static boolean isNotTrue(final Boolean bool){
        return !isTrue(bool);
    }

    /**
     * <p>
     * Checks if a {@code Boolean} value is {@code false},
     * handling {@code null} by returning {@code false}.
     * </p>
     *
     * <pre>
     *   BooleanUtils.isFalse(Boolean.TRUE)  = false
     *   BooleanUtils.isFalse(Boolean.FALSE) = true
     *   BooleanUtils.isFalse(null)          = false
     * </pre>
     *
     * @param bool
     *            the boolean to check, null returns {@code false}
     * @return {@code true} only if the input is non-{@code null} and {@code false}
     * @since 2.1
     */
    public static boolean isFalse(final Boolean bool){
        return Boolean.FALSE.equals(bool);
    }

    /**
     * <p>
     * Checks if a {@code Boolean} value is <i>not</i> {@code false},
     * handling {@code null} by returning {@code true}.
     * </p>
     *
     * <pre>
     *   BooleanUtils.isNotFalse(Boolean.TRUE)  = true
     *   BooleanUtils.isNotFalse(Boolean.FALSE) = false
     *   BooleanUtils.isNotFalse(null)          = true
     * </pre>
     *
     * @param bool
     *            the boolean to check, null returns {@code true}
     * @return {@code true} if the input is {@code null} or {@code true}
     * @since 2.3
     */
    public static boolean isNotFalse(final Boolean bool){
        return !isFalse(bool);
    }

    //-----------------------------------------------------------------------
    /**
     * <p>
     * Converts a Boolean to a boolean handling {@code null}
     * by returning {@code false}.
     * </p>
     *
     * <pre>
     *   BooleanUtils.toBoolean(Boolean.TRUE)  = true
     *   BooleanUtils.toBoolean(Boolean.FALSE) = false
     *   BooleanUtils.toBoolean(null)          = false
     * </pre>
     *
     * @param bool
     *            the boolean to convert
     * @return {@code true} or {@code false}, {@code null} returns {@code false}
     */
    public static boolean toBoolean(final Boolean bool){
        return bool != null && bool.booleanValue();
    }

    /**
     * <p>
     * Converts a Boolean to a boolean handling {@code null}.
     * </p>
     *
     * <pre>
     *   BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false)  = true
     *   BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, true)   = true
     *   BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true)  = false
     *   BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, false) = false
     *   BooleanUtils.toBooleanDefaultIfNull(null, true)           = true
     *   BooleanUtils.toBooleanDefaultIfNull(null, false)          = false
     * </pre>
     *
     * @param bool
     *            the boolean object to convert to primitive
     * @param valueIfNull
     *            the boolean value to return if the parameter {@code bool} is {@code null}
     * @return {@code true} or {@code false}
     */
    public static boolean toBooleanDefaultIfNull(final Boolean bool,final boolean valueIfNull){
        if (bool == null){
            return valueIfNull;
        }
        return bool.booleanValue();
    }

    // Integer to Boolean methods
    //-----------------------------------------------------------------------
    /**
     * <p>
     * Converts an int to a boolean using the convention that {@code zero}
     * is {@code false}, everything else is {@code true}.
     * </p>
     *
     * <pre>
     *   BooleanUtils.toBoolean(0) = false
     *   BooleanUtils.toBoolean(1) = true
     *   BooleanUtils.toBoolean(2) = true
     * </pre>
     *
     * @param value
     *            the int to convert
     * @return {@code true} if non-zero, {@code false}
     *         if zero
     */
    public static boolean toBoolean(final int value){
        return value != 0;
    }

    /**
     * <p>
     * Converts an int to a Boolean using the convention that {@code zero}
     * is {@code false}, everything else is {@code true}.
     * </p>
     *
     * <pre>
     *   BooleanUtils.toBoolean(0) = Boolean.FALSE
     *   BooleanUtils.toBoolean(1) = Boolean.TRUE
     *   BooleanUtils.toBoolean(2) = Boolean.TRUE
     * </pre>
     *
     * @param value
     *            the int to convert
     * @return Boolean.TRUE if non-zero, Boolean.FALSE if zero,
     *         {@code null} if {@code null}
     */
    public static Boolean toBooleanObject(final int value){
        return value == 0 ? Boolean.FALSE : Boolean.TRUE;
    }

    // Boolean to Integer methods
    //-----------------------------------------------------------------------
    /**
     * <p>
     * Converts a boolean to an int using the convention that
     * {@code true} is {@code 1} and {@code false} is {@code 0}.
     * </p>
     *
     * <pre>
     *   BooleanUtils.toInteger(true)  = 1
     *   BooleanUtils.toInteger(false) = 0
     * </pre>
     *
     * @param bool
     *            the boolean to convert
     * @return one if {@code true}, zero if {@code false}
     */
    public static int toInteger(final boolean bool){
        return bool ? 1 : 0;
    }

    // String to Boolean methods
    //-----------------------------------------------------------------------
    /**
     * <p>
     * Converts a String to a Boolean.
     * </p>
     *
     * <p>
     * {@code 'true'}, {@code 'on'}, {@code 'y'}, {@code 't'}, {@code 'yes'}
     * or {@code '1'} (case insensitive) will return {@code true}.
     * {@code 'false'}, {@code 'off'}, {@code 'n'}, {@code 'f'}, {@code 'no'}
     * or {@code '0'} (case insensitive) will return {@code false}.
     * Otherwise, {@code null} is returned.
     * </p>
     *
     * <p>
     * NOTE: This method may return {@code null} and may throw a {@code NullPointerException}
     * if unboxed to a {@code boolean}.
     * </p>
     *
     * <pre>
     *   // N.B. case is not significant
     *   BooleanUtils.toBooleanObject(null)    = null
     *   BooleanUtils.toBooleanObject("true")  = Boolean.TRUE
     *   BooleanUtils.toBooleanObject("T")     = Boolean.TRUE // i.e. T[RUE]
     *   BooleanUtils.toBooleanObject("false") = Boolean.FALSE
     *   BooleanUtils.toBooleanObject("f")     = Boolean.FALSE // i.e. f[alse]
     *   BooleanUtils.toBooleanObject("No")    = Boolean.FALSE
     *   BooleanUtils.toBooleanObject("n")     = Boolean.FALSE // i.e. n[o]
     *   BooleanUtils.toBooleanObject("on")    = Boolean.TRUE
     *   BooleanUtils.toBooleanObject("ON")    = Boolean.TRUE
     *   BooleanUtils.toBooleanObject("off")   = Boolean.FALSE
     *   BooleanUtils.toBooleanObject("oFf")   = Boolean.FALSE
     *   BooleanUtils.toBooleanObject("yes")   = Boolean.TRUE
     *   BooleanUtils.toBooleanObject("Y")     = Boolean.TRUE // i.e. Y[ES]
     *   BooleanUtils.toBooleanObject("1")     = Boolean.TRUE
     *   BooleanUtils.toBooleanObject("0")     = Boolean.FALSE
     *   BooleanUtils.toBooleanObject("blue")  = null
     *   BooleanUtils.toBooleanObject("true ") = null // trailing space (too long)
     *   BooleanUtils.toBooleanObject("ono")   = null // does not match on or no
     * </pre>
     *
     * @param str
     *            the String to check; upper and lower case are treated as the same
     * @return the Boolean value of the string, {@code null} if no match or {@code null} input
     */
    public static Boolean toBooleanObject(final String str){
        // Previously used equalsIgnoreCase, which was fast for interned 'true'.
        // Non interned 'true' matched 15 times slower.
        //
        // Optimisation provides same performance as before for interned 'true'.
        // Similar performance for null, 'false', and other strings not length 2/3/4.
        // 'true'/'TRUE' match 4 times slower, 'tRUE'/'True' 7 times slower.
        if (str == "true"){
            return Boolean.TRUE;
        }
        if (str == null){
            return null;
        }
        switch (str.length()) {
            case 1:{
                final char ch0 = str.charAt(0);
                if (ch0 == 'y' || ch0 == 'Y' || ch0 == 't' || ch0 == 'T' || ch0 == '1'){
                    return Boolean.TRUE;
                }
                if (ch0 == 'n' || ch0 == 'N' || ch0 == 'f' || ch0 == 'F' || ch0 == '0'){
                    return Boolean.FALSE;
                }
                break;
            }
            case 2:{
                final char ch0 = str.charAt(0);
                final char ch1 = str.charAt(1);
                if ((ch0 == 'o' || ch0 == 'O') && (ch1 == 'n' || ch1 == 'N')){
                    return Boolean.TRUE;
                }
                if ((ch0 == 'n' || ch0 == 'N') && (ch1 == 'o' || ch1 == 'O')){
                    return Boolean.FALSE;
                }
                break;
            }
            case 3:{
                final char ch0 = str.charAt(0);
                final char ch1 = str.charAt(1);
                final char ch2 = str.charAt(2);
                if ((ch0 == 'y' || ch0 == 'Y') && (ch1 == 'e' || ch1 == 'E') && (ch2 == 's' || ch2 == 'S')){
                    return Boolean.TRUE;
                }
                if ((ch0 == 'o' || ch0 == 'O') && (ch1 == 'f' || ch1 == 'F') && (ch2 == 'f' || ch2 == 'F')){
                    return Boolean.FALSE;
                }
                break;
            }
            case 4:{
                final char ch0 = str.charAt(0);
                final char ch1 = str.charAt(1);
                final char ch2 = str.charAt(2);
                final char ch3 = str.charAt(3);
                if ((ch0 == 't' || ch0 == 'T') && (ch1 == 'r' || ch1 == 'R') && (ch2 == 'u' || ch2 == 'U') && (ch3 == 'e' || ch3 == 'E')){
                    return Boolean.TRUE;
                }
                break;
            }
            case 5:{
                final char ch0 = str.charAt(0);
                final char ch1 = str.charAt(1);
                final char ch2 = str.charAt(2);
                final char ch3 = str.charAt(3);
                final char ch4 = str.charAt(4);
                if ((ch0 == 'f' || ch0 == 'F') && (ch1 == 'a' || ch1 == 'A') && (ch2 == 'l' || ch2 == 'L') && (ch3 == 's' || ch3 == 'S')
                                && (ch4 == 'e' || ch4 == 'E')){
                    return Boolean.FALSE;
                }
                break;
            }
            default:
                break;
        }

        return null;
    }

    // logical operations
    // ----------------------------------------------------------------------
    /**
     * <p>
     * Performs an 'and' operation on a set of booleans.
     * </p>
     *
     * <pre>
     *   BooleanUtils.and(true, true)         = true
     *   BooleanUtils.and(false, false)       = false
     *   BooleanUtils.and(true, false)        = false
     *   BooleanUtils.and(true, true, false)  = false
     *   BooleanUtils.and(true, true, true)   = true
     * </pre>
     *
     * @param array
     *            an array of {@code boolean}s
     * @return the result of the logical 'and' operation. That is {@code false}
     *         if any of the parameters is {@code false} and {@code true} otherwise.
     * @throws IllegalArgumentException
     *             if {@code array} is {@code null}
     * @throws IllegalArgumentException
     *             if {@code array} is empty.
     * @since 3.0.1
     */
    public static boolean and(final boolean...array){
        // Validates input
        if (array == null){
            throw new IllegalArgumentException("The Array must not be null");
        }
        if (array.length == 0){
            throw new IllegalArgumentException("Array is empty");
        }
        for (final boolean element : array){
            if (!element){
                return false;
            }
        }
        return true;
    }

    /**
     * <p>
     * Performs an 'and' operation on an array of Booleans.
     * </p>
     *
     * <pre>
     *   BooleanUtils.and(Boolean.TRUE, Boolean.TRUE)                 = Boolean.TRUE
     *   BooleanUtils.and(Boolean.FALSE, Boolean.FALSE)               = Boolean.FALSE
     *   BooleanUtils.and(Boolean.TRUE, Boolean.FALSE)                = Boolean.FALSE
     *   BooleanUtils.and(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE)   = Boolean.TRUE
     *   BooleanUtils.and(Boolean.FALSE, Boolean.FALSE, Boolean.TRUE) = Boolean.FALSE
     *   BooleanUtils.and(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE)  = Boolean.FALSE
     * </pre>
     *
     * @param array
     *            an array of {@code Boolean}s
     * @return the result of the logical 'and' operation. That is {@code false}
     *         if any of the parameters is {@code false} and {@code true} otherwise.
     * @throws IllegalArgumentException
     *             if {@code array} is {@code null}
     * @throws IllegalArgumentException
     *             if {@code array} is empty.
     * @throws IllegalArgumentException
     *             if {@code array} contains a {@code null}
     * @since 3.0.1
     */
    public static Boolean and(final Boolean...array){
        if (array == null){
            throw new IllegalArgumentException("The Array must not be null");
        }
        if (array.length == 0){
            throw new IllegalArgumentException("Array is empty");
        }
        try{
            final boolean[] primitive = ArrayUtils.toPrimitive(array);
            return and(primitive) ? Boolean.TRUE : Boolean.FALSE;
        }catch (final NullPointerException ex){
            throw new IllegalArgumentException("The array must not contain any null elements");
        }
    }

    /**
     * <p>
     * Performs an 'or' operation on a set of booleans.
     * </p>
     *
     * <pre>
     *   BooleanUtils.or(true, true)          = true
     *   BooleanUtils.or(false, false)        = false
     *   BooleanUtils.or(true, false)         = true
     *   BooleanUtils.or(true, true, false)   = true
     *   BooleanUtils.or(true, true, true)    = true
     *   BooleanUtils.or(false, false, false) = false
     * </pre>
     *
     * @param array
     *            an array of {@code boolean}s
     * @return {@code true} if any of the arguments is {@code true}, and it returns {@code false} otherwise.
     * @throws IllegalArgumentException
     *             if {@code array} is {@code null}
     * @throws IllegalArgumentException
     *             if {@code array} is empty.
     * @since 3.0.1
     */
    public static boolean or(final boolean...array){
        if (array == null){
            throw new IllegalArgumentException("The Array must not be null");
        }
        if (array.length == 0){
            throw new IllegalArgumentException("Array is empty");
        }
        for (final boolean element : array){
            if (element){
                return true;
            }
        }
        return false;
    }

    /**
     * <p>
     * Performs an 'or' operation on an array of Booleans.
     * </p>
     *
     * <pre>
     *   BooleanUtils.or(Boolean.TRUE, Boolean.TRUE)                  = Boolean.TRUE
     *   BooleanUtils.or(Boolean.FALSE, Boolean.FALSE)                = Boolean.FALSE
     *   BooleanUtils.or(Boolean.TRUE, Boolean.FALSE)                 = Boolean.TRUE
     *   BooleanUtils.or(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE)    = Boolean.TRUE
     *   BooleanUtils.or(Boolean.FALSE, Boolean.FALSE, Boolean.TRUE)  = Boolean.TRUE
     *   BooleanUtils.or(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE)   = Boolean.TRUE
     *   BooleanUtils.or(Boolean.FALSE, Boolean.FALSE, Boolean.FALSE) = Boolean.FALSE
     * </pre>
     *
     * @param array
     *            an array of {@code Boolean}s
     * @return {@code true} if any of the arguments is {@code true}, and it returns {@code false} otherwise.
     * @throws IllegalArgumentException
     *             if {@code array} is {@code null}
     * @throws IllegalArgumentException
     *             if {@code array} is empty.
     * @throws IllegalArgumentException
     *             if {@code array} contains a {@code null}
     * @since 3.0.1
     */
    public static Boolean or(final Boolean...array){
        if (array == null){
            throw new IllegalArgumentException("The Array must not be null");
        }
        if (array.length == 0){
            throw new IllegalArgumentException("Array is empty");
        }
        try{
            final boolean[] primitive = ArrayUtils.toPrimitive(array);
            return or(primitive) ? Boolean.TRUE : Boolean.FALSE;
        }catch (final NullPointerException ex){
            throw new IllegalArgumentException("The array must not contain any null elements");
        }
    }

    /**
     * <p>
     * Performs an xor on a set of booleans.
     * </p>
     *
     * <pre>
     *   BooleanUtils.xor(true, true)   = false
     *   BooleanUtils.xor(false, false) = false
     *   BooleanUtils.xor(true, false)  = true
     * </pre>
     *
     * @param array
     *            an array of {@code boolean}s
     * @return the result of the xor operations
     * @throws IllegalArgumentException
     *             if {@code array} is {@code null}
     * @throws IllegalArgumentException
     *             if {@code array} is empty.
     */
    public static boolean xor(final boolean...array){
        if (array == null){
            throw new IllegalArgumentException("The Array must not be null");
        }
        if (array.length == 0){
            throw new IllegalArgumentException("Array is empty");
        }

        // false if the neutral element of the xor operator
        boolean result = false;
        for (final boolean element : array){
            result ^= element;
        }

        return result;
    }

    /**
     * <p>
     * Performs an xor on an array of Booleans.
     * </p>
     *
     * <pre>
     *   BooleanUtils.xor(new Boolean[] { Boolean.TRUE, Boolean.TRUE })   = Boolean.FALSE
     *   BooleanUtils.xor(new Boolean[] { Boolean.FALSE, Boolean.FALSE }) = Boolean.FALSE
     *   BooleanUtils.xor(new Boolean[] { Boolean.TRUE, Boolean.FALSE })  = Boolean.TRUE
     *   BooleanUtils.xor(Boolean.TRUE, Boolean.FALSE, Boolean.FALSE)     = Boolean.TRUE
     * </pre>
     *
     * @param array
     *            an array of {@code Boolean}s
     * @return the result of the xor operations
     * @throws IllegalArgumentException
     *             if {@code array} is {@code null}
     * @throws IllegalArgumentException
     *             if {@code array} is empty.
     * @throws IllegalArgumentException
     *             if {@code array} contains a {@code null}
     */
    public static Boolean xor(final Boolean...array){
        if (array == null){
            throw new IllegalArgumentException("The Array must not be null");
        }
        if (array.length == 0){
            throw new IllegalArgumentException("Array is empty");
        }
        try{
            final boolean[] primitive = ArrayUtils.toPrimitive(array);
            return xor(primitive) ? Boolean.TRUE : Boolean.FALSE;
        }catch (final NullPointerException ex){
            throw new IllegalArgumentException("The array must not contain any null elements");
        }
    }

    /**
     * <p>
     * Compares two {@code boolean} values. This is the same functionality as provided in Java 7.
     * </p>
     *
     * @param x
     *            the first {@code boolean} to compare
     * @param y
     *            the second {@code boolean} to compare
     * @return the value {@code 0} if {@code x == y};
     *         a value less than {@code 0} if {@code !x && y}; and
     *         a value greater than {@code 0} if {@code x && !y}
     * @since 3.4
     */
    public static int compare(final boolean x,final boolean y){
        if (x == y){
            return 0;
        }
        return x ? 1 : -1;
    }

}
