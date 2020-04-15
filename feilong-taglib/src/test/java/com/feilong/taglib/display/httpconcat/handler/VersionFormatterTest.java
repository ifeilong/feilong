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
package com.feilong.taglib.display.httpconcat.handler;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.feilong.security.oneway.MD5Util;

/**
 * The Class VersionFormatterTest.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.12.6
 */
public class VersionFormatterTest{

    /**
     * Test version.
     */
    @Test
    public void testVersion(){
        assertEquals(MD5Util.encode("20180808" + VersionEncodeUtil.class.getName()), VersionFormatter.format("20180808"));
    }

    //---------------------------------------------------------------

    /**
     * Test version formatter test null.
     */
    @Test
    public void testVersionFormatterTestNull(){
        assertEquals(EMPTY, VersionFormatter.format(null));
    }

    /**
     * Test version formatter test empty.
     */
    @Test
    public void testVersionFormatterTestEmpty(){
        assertEquals(EMPTY, VersionFormatter.format(""));
    }

    /**
     * Test version formatter test blank.
     */
    @Test
    public void testVersionFormatterTestBlank(){
        assertEquals(EMPTY, VersionFormatter.format(" "));
    }
}
