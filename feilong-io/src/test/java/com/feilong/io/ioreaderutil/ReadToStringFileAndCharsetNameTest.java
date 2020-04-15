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

import java.io.File;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.UncheckedIOException;
import com.feilong.io.IOReaderUtil;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.5
 */
public class ReadToStringFileAndCharsetNameTest extends AbstractReadFileToStringTest{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ReadToStringFileAndCharsetNameTest.class);

    //---------------------------------------------------------------

    @Test
    public void testReadFile(){
        String readFileToString = IOReaderUtil.readToString(new File(filePath), UTF8);
        LOGGER.debug(readFileToString);

    }

    @Test(expected = UncheckedIOException.class)
    public void testReadFile1(){
        IOReaderUtil.readToString(new File("/Users/feilong/feilong/logs2222/readFileToString.txt"), UTF8);
    }

    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testReadFileToStringFilePathAndCharsetNameTestNull(){
        IOReaderUtil.readToString((String) null, UTF8);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadFileToStringFilePathAndCharsetNameTestEmpty(){
        IOReaderUtil.readToString("", UTF8);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadFileToStringFilePathAndCharsetNameTestBlank(){
        IOReaderUtil.readToString(" ", UTF8);
    }
}
