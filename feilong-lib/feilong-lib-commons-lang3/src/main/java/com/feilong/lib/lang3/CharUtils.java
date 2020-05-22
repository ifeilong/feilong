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

/**
 * <p>
 * Operations on char primitives and Character objects.
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
 * @since 2.1
 */
public class CharUtils{

    private static final String[] CHAR_STRING_ARRAY = new String[128];

    private static final char[]   HEX_DIGITS        = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    /**
     * Linefeed character LF ({@code '\n'}, Unicode 000a).
     *
     * @see <a href="http://docs.oracle.com/javase/specs/jls/se7/html/jls-3.html#jls-3.10.6">JLF: Escape Sequences
     *      for Character and String Literals</a>
     * @since 2.2
     */
    public static final char      LF                = '\n';

    /**
     * Carriage return characterf CR ('\r', Unicode 000d).
     *
     * @see <a href="http://docs.oracle.com/javase/specs/jls/se7/html/jls-3.html#jls-3.10.6">JLF: Escape Sequences
     *      for Character and String Literals</a>
     * @since 2.2
     */
    public static final char      CR                = '\r';

    /**
     * {@code \u0000} null control character ('\0'), abbreviated NUL.
     *
     * @since 3.6
     */
    public static final char      NUL               = '\0';

    static{
        for (char c = 0; c < CHAR_STRING_ARRAY.length; c++){
            CHAR_STRING_ARRAY[c] = String.valueOf(c);
        }
    }

    //-----------------------------------------------------------------------
    /**
     * <p>
     * Converts the character to a String that contains the one character.
     * </p>
     *
     * <p>
     * For ASCII 7 bit characters, this uses a cache that will return the
     * same String object each time.
     * </p>
     *
     * <pre>
     *   CharUtils.toString(' ')  = " "
     *   CharUtils.toString('A')  = "A"
     * </pre>
     *
     * @param ch
     *            the character to convert
     * @return a String containing the one specified character
     */
    public static String toString(final char ch){
        if (ch < 128){
            return CHAR_STRING_ARRAY[ch];
        }
        return new String(new char[] { ch });
    }

    /**
     * <p>
     * Converts the character to a String that contains the one character.
     * </p>
     *
     * <p>
     * For ASCII 7 bit characters, this uses a cache that will return the
     * same String object each time.
     * </p>
     *
     * <p>
     * If {@code null} is passed in, {@code null} will be returned.
     * </p>
     *
     * <pre>
     *   CharUtils.toString(null) = null
     *   CharUtils.toString(' ')  = " "
     *   CharUtils.toString('A')  = "A"
     * </pre>
     *
     * @param ch
     *            the character to convert
     * @return a String containing the one specified character
     */
    public static String toString(final Character ch){
        if (ch == null){
            return null;
        }
        return toString(ch.charValue());
    }

    /**
     * <p>
     * Checks whether the character is ASCII 7 bit printable.
     * </p>
     *
     * <pre>
     *   CharUtils.isAsciiPrintable('a')  = true
     *   CharUtils.isAsciiPrintable('A')  = true
     *   CharUtils.isAsciiPrintable('3')  = true
     *   CharUtils.isAsciiPrintable('-')  = true
     *   CharUtils.isAsciiPrintable('\n') = false
     *   CharUtils.isAsciiPrintable('&copy;') = false
     * </pre>
     *
     * @param ch
     *            the character to check
     * @return true if between 32 and 126 inclusive
     */
    public static boolean isAsciiPrintable(final char ch){
        return ch >= 32 && ch < 127;
    }
}
