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

import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.lib.springframework.util.ResourceUtils.CLASSPATH_URL_PREFIX;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.UncheckedIOException;

import org.junit.Test;

import com.feilong.io.IOReaderUtil;

public class ReadToStringStringFilePathAndCharsetNameTest extends AbstractReadFileToStringTest{

    @Test
    public void testReadFile(){
        String readFileToString = IOReaderUtil.readToString(filePath, UTF8);
        assertEquals("feilong 我爱你\n" + "feilong", readFileToString);
    }

    @Test
    public void testReadFile12(){
        String readFileToString = IOReaderUtil.readToString(CLASSPATH_URL_PREFIX + "readFileToString.txt", UTF8);
        assertEquals("feilong 我爱你\n" + "feilong", readFileToString);
    }

    //---------------------------------------------------------------

    @Test(expected = UncheckedIOException.class)
    public void testReadFile1(){
        IOReaderUtil.readToString("/Users/feilong/feilong/logs2222/readFileToString.txt", UTF8);
    }

    @Test(expected = NullPointerException.class)
    public void testReadFileToStringFilePathAndCharsetNameTestNull(){
        IOReaderUtil.readToString((File) null, UTF8);
    }
}
