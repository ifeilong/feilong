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
import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;

import org.junit.Test;

import com.feilong.io.FileUtil;
import com.feilong.io.IOReaderUtil;

public class ReadToStringFileInputStreamTest extends AbstractReadFileToStringTest{

    @Test
    public void testReadFile(){
        String readFileToString = IOReaderUtil.readToString(FileUtil.getFileInputStream(filePath), UTF8);
        assertEquals("feilong 我爱你\n" + "feilong", readFileToString);
    }

    @Test
    public void testReadFile1(){
        String readFileToString = IOReaderUtil.readToString("classpath:backspace.txt", UTF8);
        assertEquals("a\bc", readFileToString);
    }

    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testGetContent(){
        IOReaderUtil.readToString((FileInputStream) null, UTF8);
    }

}
