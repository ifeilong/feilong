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

import java.io.InputStream;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.io.IOReaderUtil;

public class ReadToStringInputStreamTest{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ReadToStringInputStreamTest.class);

    //---------------------------------------------------------------

    //    @Test
    //    public void testGetContent(){
    //        String spec = "https://www.jd.com/robots.txt";
    //        InputStream openStream = URLUtil.openStream(URLUtil.toURL(spec));
    //        LOGGER.debug(IOReaderUtil.readToString(openStream, UTF8));
    //    }

    @Test(expected = NullPointerException.class)
    public void testGetContentNull(){
        IOReaderUtil.readToString((InputStream) null, UTF8);
    }

}
