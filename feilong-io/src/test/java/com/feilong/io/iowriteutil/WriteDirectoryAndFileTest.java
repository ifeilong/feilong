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
package com.feilong.io.iowriteutil;

import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.io.IOReaderUtil.readToString;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.feilong.io.IOWriteUtil;

public class WriteDirectoryAndFileTest{

    private static final String INPUT_MESSAGE = "feilong 我爱你";

    //---------------------------------------------------------------

    @Test
    public void test(){
        //带斜杆的
        IOWriteUtil.write(getInputStream(), "/Users/feilong/feilong/logs/", "a.txt");
        check("/Users/feilong/feilong/logs/a.txt", INPUT_MESSAGE);
    }

    @Test
    public void test12(){
        //不带斜杆的
        IOWriteUtil.write(getInputStream(), "/Users/feilong/feilong/logs", "a.txt");
        check("/Users/feilong/feilong/logs/a.txt", INPUT_MESSAGE);
    }

    @Test
    public void test1(){
        IOWriteUtil.write(getInputStream(), "/Users/feilong/feilong/logs/", "a/a.txt");
        check("/Users/feilong/feilong/logs/a/a.txt", INPUT_MESSAGE);
    }

    @Test
    public void test123(){
        IOWriteUtil.write(getInputStream(), "/Users/feilong/feilong/logs", "normalize/normalize.txt");
        check("/Users/feilong/feilong/logs/normalize/normalize.txt", INPUT_MESSAGE);
    }

    @Test
    public void testNormalize(){
        IOWriteUtil.write(getInputStream(), "/Users/feilong/feilong/logs", "../normalize/normalize2.txt");
        check("/Users/feilong/feilong/normalize/normalize2.txt", INPUT_MESSAGE);
    }

    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testInputStreamNull(){
        IOWriteUtil.write(null, "/Users/feilong/feilong/logs/", "a.txt");
    }

    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testWriteDirectoryAndFileTestNull1(){
        IOWriteUtil.write(getInputStream(), null, "a.txt");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteDirectoryAndFileTestEmpty(){
        IOWriteUtil.write(getInputStream(), "", "a.txt");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteDirectoryAndFileTestBlank(){
        IOWriteUtil.write(getInputStream(), " ", "a.txt");
    }
    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testWriteDirectoryAndFileTestNull11(){
        IOWriteUtil.write(getInputStream(), "/Users/feilong/feilong/logs/", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteDirectoryAndFileTestEmpty1(){
        IOWriteUtil.write(getInputStream(), "/Users/feilong/feilong/logs/", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteDirectoryAndFileTestBlank1(){
        IOWriteUtil.write(getInputStream(), "/Users/feilong/feilong/logs/", " ");
    }

    //---------------------------------------------------------------
    private void check(String filePath,String expectedValue){
        String content = readToString(filePath, UTF8);
        assertEquals(expectedValue, content);
    }

    private static InputStream getInputStream(){
        try{
            return IOUtils.toInputStream(INPUT_MESSAGE, UTF8);
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }

}
