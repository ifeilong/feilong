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
package com.feilong.servlet.http.requestutil;

import static com.feilong.core.bean.ConvertUtil.toArray;
import static org.apache.commons.lang3.ArrayUtils.EMPTY_STRING_ARRAY;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.servlet.http.RequestUtil;

public class IsSupportMethodTest{

    private static final Logger LOGGER = LoggerFactory.getLogger(IsSupportMethodTest.class);

    @Test
    public void testIsSupportMethod(){
        assertTrue(RequestUtil.isSupportMethod(toArray("get"), "get"));
    }

    @Test
    public void testIsSupportMethod1222(){
        assertTrue(RequestUtil.isSupportMethod(toArray("get"), "GET"));
    }

    //---------------------------------------------------------------

    @Test
    public void testIsSupportMethodNullSupportHttpMethods(){
        assertFalse(RequestUtil.isSupportMethod(null, "get"));
    }

    @Test
    public void testIsSupportMethod1(){
        assertFalse(RequestUtil.isSupportMethod(EMPTY_STRING_ARRAY, "get"));

    }
    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testIsSupportMethodTestNull(){
        RequestUtil.isSupportMethod(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsSupportMethodTestEmpty(){
        RequestUtil.isSupportMethod(null, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsSupportMethodTestBlank(){
        RequestUtil.isSupportMethod(null, " ");
    }
}
