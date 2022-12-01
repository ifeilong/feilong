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
package com.feilong.core.net.uriutil;

import org.junit.Test;

import com.feilong.core.net.URIParseException;
import com.feilong.core.net.URIUtil;

/**
 * The Class URIUtilDecodeTest.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 */
public class DecodeDefaultUtf8Test{

    /**
     * Test last percent.
     */
    @Test(expected = URIParseException.class)
    public void testLastPercent(){
        URIUtil.decode("%");
    }

    /**
     * Test last percent 1.
     */
    @Test(expected = URIParseException.class)
    public void testLastPercent1(){
        URIUtil.decode("%E9%A3%9E%E5%A4%A9%E5%A5%94%E6%9C%88%");
    }

    /**
     * Decode2.
     */
    @Test(expected = URIParseException.class)
    public void decode2(){
        URIUtil.decode("aaaaa%chu111");
        // java.lang.IllegalArgumentException: URLDecoder: Illegal hex characters in escape (%) pattern - For input string: "ch"
    }

    /**
     * Decode3.
     */
    @Test(expected = URIParseException.class)
    public void decode3(){
        URIUtil.decode("%c");
    }
}
