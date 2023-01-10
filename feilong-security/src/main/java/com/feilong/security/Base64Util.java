/*
 * Copyright (C) 2008 feilong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.feilong.security;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import com.feilong.core.DefaultRuntimeException;
import com.feilong.lib.codec.binary.Base64;

/**
 * The Class Base64Util.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.4.0
 */
public class Base64Util{

    /**
     * Encode base 64.
     *
     * @param str
     *            the str
     * @param charset
     *            the charset
     * @return the string
     */
    public static String encodeBase64(String str,Charset charset){
        byte[] bytes = str.getBytes(charset);
        return encodeBase64(bytes);
    }

    /**
     * Encode base 64.
     *
     * @param str
     *            the str
     * @param charsetName
     *            the charset name
     * @return the string
     */
    public static String encodeBase64(String str,String charsetName){
        try{
            byte[] bytes = str.getBytes(charsetName);
            return encodeBase64(bytes);
        }catch (UnsupportedEncodingException e){
            throw new DefaultRuntimeException(e);
        }
    }

    //---------------------------------------------------------------

    /**
     * Encode base 64.
     *
     * @param bytes
     *            the bytes
     * @return the string
     */
    public static String encodeBase64(byte[] bytes){
        return new String(Base64.encodeBase64(bytes));
    }
}
