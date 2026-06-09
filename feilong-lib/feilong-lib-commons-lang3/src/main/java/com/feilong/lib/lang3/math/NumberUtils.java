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
package com.feilong.lib.lang3.math;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.feilong.lib.lang3.StringUtils;
import com.feilong.lib.lang3.Validate;

/**
 * <p>
 * Provides extra functionality for Java Number classes.
 * </p>
 *
 * @since 2.0
 */
public class NumberUtils{

    /** Reusable Long constant for zero. */
    public static final Long    LONG_ZERO         = Long.valueOf(0L);

    /** Reusable Long constant for one. */
    public static final Long    LONG_ONE          = Long.valueOf(1L);

    /** Reusable Long constant for minus one. */
    public static final Long    LONG_MINUS_ONE    = Long.valueOf(-1L);

    /** Reusable Integer constant for zero. */
    public static final Integer INTEGER_ZERO      = Integer.valueOf(0);

    /** Reusable Integer constant for one. */
    public static final Integer INTEGER_ONE       = Integer.valueOf(1);

    /** Reusable Integer constant for two */
    public static final Integer INTEGER_TWO       = Integer.valueOf(2);

    /** Reusable Integer constant for minus one. */
    public static final Integer INTEGER_MINUS_ONE = Integer.valueOf(-1);

    //-----------------------------------------------------------------------

    /**
     * <p>
     * Turns a string value into a java.lang.Number.
     * </p>
     *
     * <p>
     * If the string starts with {@code 0x} or {@code -0x} (lower or upper case) or {@code #} or {@code -#}, it
     * will be interpreted as a hexadecimal Integer - or Long, if the number of digits after the
     * prefix is more than 8 - or BigInteger if there are more than 16 digits.
     * </p>
     * <p>
     * Then, the value is examined for a type qualifier on the end, i.e. one of
     * {@code 'f', 'F', 'd', 'D', 'l', 'L'}. If it is found, it starts
     * trying to create successively larger types from the type specified
     * until one is found that can represent the value.
     * </p>
     *
     * <p>
     * If a type specifier is not found, it will check for a decimal point
     * and then try successively larger types from {@code Integer} to
     * {@code BigInteger} and from {@code Float} to
     * {@code BigDecimal}.
     * </p>
     *
     * <p>
     * Integral values with a leading {@code 0} will be interpreted as octal; the returned number will
     * be Integer, Long or BigDecimal as appropriate.
     * </p>
     *
     * <p>
     * Returns {@code null} if the string is {@code null}.
     * </p>
     *
     * <p>
     * This method does not trim the input string, i.e., strings with leading
     * or trailing spaces will generate NumberFormatExceptions.
     * </p>
     *
     * @param str
     *            String containing a number, may be null
     * @return Number created from the string (or null if the input is null)
     * @throws NumberFormatException
     *             if the value cannot be converted
     */
    public static Number createNumber(final String str){
        if (str == null){
            return null;
        }
        if (StringUtils.isBlank(str)){
            throw new NumberFormatException("A blank string is not a valid number");
        }
        // Need to deal with all possible hex prefixes here
        final String[] hex_prefixes = { "0x", "0X", "-0x", "-0X", "#", "-#" };
        int pfxLen = 0;
        for (final String pfx : hex_prefixes){
            if (str.startsWith(pfx)){
                pfxLen += pfx.length();
                break;
            }
        }
        if (pfxLen > 0){ // we have a hex number
            char firstSigDigit = 0; // strip leading zeroes
            for (int i = pfxLen; i < str.length(); i++){
                firstSigDigit = str.charAt(i);
                if (firstSigDigit == '0'){ // count leading zeroes
                    pfxLen++;
                }else{
                    break;
                }
            }
            final int hexDigits = str.length() - pfxLen;
            if (hexDigits > 16 || hexDigits == 16 && firstSigDigit > '7'){ // too many for Long
                return createBigInteger(str);
            }
            if (hexDigits > 8 || hexDigits == 8 && firstSigDigit > '7'){ // too many for an int
                return createLong(str);
            }
            return createInteger(str);
        }
        final char lastChar = str.charAt(str.length() - 1);
        String mant;
        String dec;
        String exp;
        final int decPos = str.indexOf('.');
        final int expPos = str.indexOf('e') + str.indexOf('E') + 1; // assumes both not present
        // if both e and E are present, this is caught by the checks on expPos (which prevent IOOBE)
        // and the parsing which will detect if e or E appear in a number due to using the wrong offset

        if (decPos > -1){ // there is a decimal point
            if (expPos > -1){ // there is an exponent
                if (expPos < decPos || expPos > str.length()){ // prevents double exponent causing IOOBE
                    throw new NumberFormatException(str + " is not a valid number.");
                }
                dec = str.substring(decPos + 1, expPos);
            }else{
                dec = str.substring(decPos + 1);
            }
            mant = getMantissa(str, decPos);
        }else{
            if (expPos > -1){
                if (expPos > str.length()){ // prevents double exponent causing IOOBE
                    throw new NumberFormatException(str + " is not a valid number.");
                }
                mant = getMantissa(str, expPos);
            }else{
                mant = getMantissa(str);
            }
            dec = null;
        }
        if (!Character.isDigit(lastChar) && lastChar != '.'){
            if (expPos > -1 && expPos < str.length() - 1){
                exp = str.substring(expPos + 1, str.length() - 1);
            }else{
                exp = null;
            }
            //Requesting a specific type..
            final String numeric = str.substring(0, str.length() - 1);
            final boolean allZeros = isAllZeros(mant) && isAllZeros(exp);
            switch (lastChar) {
                case 'l':
                case 'L':
                    if (dec == null && exp == null && (!numeric.isEmpty() && numeric.charAt(0) == '-' && isDigits(numeric.substring(1))
                                    || isDigits(numeric))){
                        try{
                            return createLong(numeric);
                        }catch (final NumberFormatException nfe){ // NOPMD
                            // Too big for a long
                        }
                        return createBigInteger(numeric);

                    }
                    throw new NumberFormatException(str + " is not a valid number.");
                case 'f':
                case 'F':
                    try{
                        final Float f = createFloat(str);
                        if (!(f.isInfinite() || f.floatValue() == 0.0F && !allZeros)){
                            //If it's too big for a float or the float value = 0 and the string
                            //has non-zeros in it, then float does not have the precision we want
                            return f;
                        }

                    }catch (final NumberFormatException nfe){ // NOPMD
                        // ignore the bad number
                    }
                    //$FALL-THROUGH$
                case 'd':
                case 'D':
                    try{
                        final Double d = createDouble(str);
                        if (!(d.isInfinite() || d.floatValue() == 0.0D && !allZeros)){
                            return d;
                        }
                    }catch (final NumberFormatException nfe){ // NOPMD
                        // ignore the bad number
                    }
                    try{
                        return createBigDecimal(numeric);
                    }catch (final NumberFormatException e){ // NOPMD
                        // ignore the bad number
                    }
                    //$FALL-THROUGH$
                default:
                    throw new NumberFormatException(str + " is not a valid number.");

            }
        }
        //User doesn't have a preference on the return type, so let's start
        //small and go from there...
        if (expPos > -1 && expPos < str.length() - 1){
            exp = str.substring(expPos + 1, str.length());
        }else{
            exp = null;
        }
        if (dec == null && exp == null){ // no decimal point and no exponent
            //Must be an Integer, Long, Biginteger
            try{
                return createInteger(str);
            }catch (final NumberFormatException nfe){ // NOPMD
                // ignore the bad number
            }
            try{
                return createLong(str);
            }catch (final NumberFormatException nfe){ // NOPMD
                // ignore the bad number
            }
            return createBigInteger(str);
        }

        //Must be a Float, Double, BigDecimal
        final boolean allZeros = isAllZeros(mant) && isAllZeros(exp);
        try{
            final Float f = createFloat(str);
            final Double d = createDouble(str);
            if (!f.isInfinite() && !(f.floatValue() == 0.0F && !allZeros) && f.toString().equals(d.toString())){
                return f;
            }
            if (!d.isInfinite() && !(d.doubleValue() == 0.0D && !allZeros)){
                final BigDecimal b = createBigDecimal(str);
                if (b.compareTo(BigDecimal.valueOf(d.doubleValue())) == 0){
                    return d;
                }
                return b;
            }
        }catch (final NumberFormatException nfe){ // NOPMD
            // ignore the bad number
        }
        return createBigDecimal(str);
    }

    /**
     * <p>
     * Utility method for {@link #createNumber(java.lang.String)}.
     * </p>
     *
     * <p>
     * Returns mantissa of the given number.
     * </p>
     *
     * @param str
     *            the string representation of the number
     * @return mantissa of the given number
     */
    private static String getMantissa(final String str){
        return getMantissa(str, str.length());
    }

    /**
     * <p>
     * Utility method for {@link #createNumber(java.lang.String)}.
     * </p>
     *
     * <p>
     * Returns mantissa of the given number.
     * </p>
     *
     * @param str
     *            the string representation of the number
     * @param stopPos
     *            the position of the exponent or decimal point
     * @return mantissa of the given number
     */
    private static String getMantissa(final String str,final int stopPos){
        final char firstChar = str.charAt(0);
        final boolean hasSign = firstChar == '-' || firstChar == '+';

        return hasSign ? str.substring(1, stopPos) : str.substring(0, stopPos);
    }

    /**
     * <p>
     * Utility method for {@link #createNumber(java.lang.String)}.
     * </p>
     *
     * <p>
     * Returns {@code true} if s is {@code null}.
     * </p>
     *
     * @param str
     *            the String to check
     * @return if it is all zeros or {@code null}
     */
    private static boolean isAllZeros(final String str){
        if (str == null){
            return true;
        }
        for (int i = str.length() - 1; i >= 0; i--){
            if (str.charAt(i) != '0'){
                return false;
            }
        }
        return !str.isEmpty();
    }

    //-----------------------------------------------------------------------
    /**
     * <p>
     * Convert a {@code String} to a {@code Float}.
     * </p>
     *
     * <p>
     * Returns {@code null} if the string is {@code null}.
     * </p>
     *
     * @param str
     *            a {@code String} to convert, may be null
     * @return converted {@code Float} (or null if the input is null)
     * @throws NumberFormatException
     *             if the value cannot be converted
     */
    public static Float createFloat(final String str){
        if (str == null){
            return null;
        }
        return Float.valueOf(str);
    }

    /**
     * <p>
     * Convert a {@code String} to a {@code Double}.
     * </p>
     *
     * <p>
     * Returns {@code null} if the string is {@code null}.
     * </p>
     *
     * @param str
     *            a {@code String} to convert, may be null
     * @return converted {@code Double} (or null if the input is null)
     * @throws NumberFormatException
     *             if the value cannot be converted
     */
    public static Double createDouble(final String str){
        if (str == null){
            return null;
        }
        return Double.valueOf(str);
    }

    /**
     * <p>
     * Convert a {@code String} to a {@code Integer}, handling
     * hex (0xhhhh) and octal (0dddd) notations.
     * N.B. a leading zero means octal; spaces are not trimmed.
     * </p>
     *
     * <p>
     * Returns {@code null} if the string is {@code null}.
     * </p>
     *
     * @param str
     *            a {@code String} to convert, may be null
     * @return converted {@code Integer} (or null if the input is null)
     * @throws NumberFormatException
     *             if the value cannot be converted
     */
    public static Integer createInteger(final String str){
        if (str == null){
            return null;
        }
        // decode() handles 0xAABD and 0777 (hex and octal) as well.
        return Integer.decode(str);
    }

    /**
     * <p>
     * Convert a {@code String} to a {@code Long};
     * since 3.1 it handles hex (0Xhhhh) and octal (0ddd) notations.
     * N.B. a leading zero means octal; spaces are not trimmed.
     * </p>
     *
     * <p>
     * Returns {@code null} if the string is {@code null}.
     * </p>
     *
     * @param str
     *            a {@code String} to convert, may be null
     * @return converted {@code Long} (or null if the input is null)
     * @throws NumberFormatException
     *             if the value cannot be converted
     */
    public static Long createLong(final String str){
        if (str == null){
            return null;
        }
        return Long.decode(str);
    }

    /**
     * <p>
     * Convert a {@code String} to a {@code BigInteger};
     * since 3.2 it handles hex (0x or #) and octal (0) notations.
     * </p>
     *
     * <p>
     * Returns {@code null} if the string is {@code null}.
     * </p>
     *
     * @param str
     *            a {@code String} to convert, may be null
     * @return converted {@code BigInteger} (or null if the input is null)
     * @throws NumberFormatException
     *             if the value cannot be converted
     */
    public static BigInteger createBigInteger(final String str){
        if (str == null){
            return null;
        }
        int pos = 0; // offset within string
        int radix = 10;
        boolean negate = false; // need to negate later?
        if (str.startsWith("-")){
            negate = true;
            pos = 1;
        }
        if (str.startsWith("0x", pos) || str.startsWith("0X", pos)){ // hex
            radix = 16;
            pos += 2;
        }else if (str.startsWith("#", pos)){ // alternative hex (allowed by Long/Integer)
            radix = 16;
            pos++;
        }else if (str.startsWith("0", pos) && str.length() > pos + 1){ // octal; so long as there are additional digits
            radix = 8;
            pos++;
        } // default is to treat as decimal

        final BigInteger value = new BigInteger(str.substring(pos), radix);
        return negate ? value.negate() : value;
    }

    /**
     * <p>
     * Convert a {@code String} to a {@code BigDecimal}.
     * </p>
     *
     * <p>
     * Returns {@code null} if the string is {@code null}.
     * </p>
     *
     * @param str
     *            a {@code String} to convert, may be null
     * @return converted {@code BigDecimal} (or null if the input is null)
     * @throws NumberFormatException
     *             if the value cannot be converted
     */
    public static BigDecimal createBigDecimal(final String str){
        if (str == null){
            return null;
        }
        // handle JDK1.3.1 bug where "" throws IndexOutOfBoundsException
        if (StringUtils.isBlank(str)){
            throw new NumberFormatException("A blank string is not a valid number");
        }
        return new BigDecimal(str);
    }

    // Max in array
    //--------------------------------------------------------------------
    /**
     * <p>
     * Returns the maximum value in an array.
     * </p>
     *
     * @param array
     *            an array, must not be null or empty
     * @return the maximum value in the array
     * @throws IllegalArgumentException
     *             if {@code array} is {@code null}
     * @throws IllegalArgumentException
     *             if {@code array} is empty
     * @since 3.4 Changed signature from max(long[]) to max(long...)
     */
    public static long max(final long...array){
        // Validates input
        validateArray(array);

        // Finds and returns max
        long max = array[0];
        for (int j = 1; j < array.length; j++){
            if (array[j] > max){
                max = array[j];
            }
        }

        return max;
    }

    /**
     * Checks if the specified array is neither null nor empty.
     *
     * @param array
     *            the array to check
     * @throws IllegalArgumentException
     *             if {@code array} is either {@code null} or empty
     */
    private static void validateArray(final Object array){
        Validate.notNull(array, "The Array must not be null");
        Validate.isTrue(Array.getLength(array) != 0, "Array cannot be empty.");
    }

    //-----------------------------------------------------------------------
    /**
     * <p>
     * Checks whether the {@code String} contains only
     * digit characters.
     * </p>
     *
     * <p>
     * {@code Null} and empty String will return
     * {@code false}.
     * </p>
     *
     * @param str
     *            the {@code String} to check
     * @return {@code true} if str contains only Unicode numeric
     */
    public static boolean isDigits(final String str){
        return StringUtils.isNumeric(str);
    }

    /**
     * <p>
     * Compares two {@code int} values numerically. This is the same functionality as provided in Java 7.
     * </p>
     *
     * @param x
     *            the first {@code int} to compare
     * @param y
     *            the second {@code int} to compare
     * @return the value {@code 0} if {@code x == y};
     *         a value less than {@code 0} if {@code x < y}; and
     *         a value greater than {@code 0} if {@code x > y}
     * @since 3.4
     */
    public static int compare(final int x,final int y){
        if (x == y){
            return 0;
        }
        return x < y ? -1 : 1;
    }

    /**
     * <p>
     * Compares to {@code long} values numerically. This is the same functionality as provided in Java 7.
     * </p>
     *
     * @param x
     *            the first {@code long} to compare
     * @param y
     *            the second {@code long} to compare
     * @return the value {@code 0} if {@code x == y};
     *         a value less than {@code 0} if {@code x < y}; and
     *         a value greater than {@code 0} if {@code x > y}
     * @since 3.4
     */
    public static int compare(final long x,final long y){
        if (x == y){
            return 0;
        }
        return x < y ? -1 : 1;
    }

    /**
     * <p>
     * Compares to {@code short} values numerically. This is the same functionality as provided in Java 7.
     * </p>
     *
     * @param x
     *            the first {@code short} to compare
     * @param y
     *            the second {@code short} to compare
     * @return the value {@code 0} if {@code x == y};
     *         a value less than {@code 0} if {@code x < y}; and
     *         a value greater than {@code 0} if {@code x > y}
     * @since 3.4
     */
    public static int compare(final short x,final short y){
        if (x == y){
            return 0;
        }
        return x < y ? -1 : 1;
    }
}
