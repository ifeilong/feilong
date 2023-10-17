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
package com.feilong.io.ioreaderutil;

import java.io.UncheckedIOException;

import org.junit.Ignore;
import org.junit.Test;

import com.feilong.io.IOReaderUtil;

public class ReadToByteArrayTest extends AbstractReadFileToStringTest{

    @Test(expected = NullPointerException.class)
    public void testReadToByteArrayTestNull(){
        IOReaderUtil.readToByteArray((String) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadToByteArrayTestEmpty(){
        IOReaderUtil.readToByteArray("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadToByteArrayTestBlank(){
        IOReaderUtil.readToByteArray(" ");
    }

    @Test(expected = UncheckedIOException.class)
    public void testReadToByteArrayTestUncheckedIOException(){
        IOReaderUtil.readToByteArray("/Users/feilong/Downloads/11111111111111.java");
    }

    //---------------------------------------------------------------

    /**
     * Test resolver reader and reader config test 2.
     */
    @Test()
    @Ignore
    public void testResolverReaderAndReaderConfigTest2(){
        //        assertThat(codes, allOf(hasItem("123456"), hasItem("23456")));

        byte[] readToByteArray = IOReaderUtil.readToByteArray("classpath:readtest.txt");
        System.out.println(readToByteArray);
    }

}
