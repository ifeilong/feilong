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
package com.feilong.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;

import com.feilong.core.UncheckedIOException;

/**
 * {@link java.io.Reader} 工具类.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see java.io.BufferedReader
 * @see java.io.CharArrayReader
 * @see java.io.FilterReader
 * @see java.io.InputStreamReader
 * @see java.io.PipedReader
 * @see java.io.StringReader
 * @see java.io.LineNumberReader
 * @since 1.0.9
 */
public final class ReaderUtil{

    /** Don't let anyone instantiate this class. */
    private ReaderUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------
    /**
     * 构造一个 {@link StringReader}.
     *
     * @param str
     *            the str
     * @return 如果 <code>str</code> 是null,抛出 {@link NullPointerException}<br>
     * @since 1.11.5
     */
    public static StringReader newStringReader(String str){
        Validate.notNull(str, "str can't be null!");

        return new StringReader(str);
    }

    //---------------------------------------------------------------

    /**
     * 将 {@link java.io.Reader} 转成 {@link java.lang.String}.
     *
     * @param reader
     *            the reader
     * @return 如果 <code>reader</code> 是null,抛出 {@link NullPointerException}<br>
     * @see org.apache.commons.io.IOUtils#toBufferedReader(Reader)
     * @see org.apache.commons.io.IOUtils#toString(Reader)
     * @since 1.10.6 call {@link org.apache.commons.io.IOUtils#toString(Reader)}
     */
    public static String toString(Reader reader){
        Validate.notNull(reader, "reader can't be null!");

        //---------------------------------------------------------------
        try{
            return IOUtils.toString(reader);
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }

    //---------------------------------------------------------------

    /**
     * 读取 {@link java.io.Reader} 第一行内容.
     *
     * @param reader
     *            the reader
     * @return 如果 <code>reader</code> 是null,抛出 {@link NullPointerException}<br>
     * @see org.apache.commons.io.IOUtils#toBufferedReader(Reader)
     */
    public static String readLine(Reader reader){
        Validate.notNull(reader, "reader can't be null!");

        try (BufferedReader bufferedReader = IOUtils.toBufferedReader(reader)){
            // 读取一个文本行.通过下列字符之一即可认为某行已终止:换行 ('\n')、回车 ('\r') 或回车后直接跟着换行.
            return bufferedReader.readLine();
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }
}
