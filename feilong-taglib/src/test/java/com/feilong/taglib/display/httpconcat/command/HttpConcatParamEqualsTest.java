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
package com.feilong.taglib.display.httpconcat.command;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.feilong.taglib.display.httpconcat.BaseHttpConcatTest;

/**
 * The Class HttpConcatParamTest.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.7
 */
public class HttpConcatParamEqualsTest extends BaseHttpConcatTest{

    /** The domain. */
    private static final String domain = "http://www.feilong.com";

    @Test
    @SuppressWarnings("static-method")
    public void testEqualsObject(){
        HttpConcatParam pagerParams1 = new HttpConcatParam();

        HttpConcatParam pagerParams2 = new HttpConcatParam();

        assertEquals(true, pagerParams1.equals(pagerParams1));
        assertEquals(false, pagerParams1.equals(null));

        pagerParams2.setDomain(domain);
        assertEquals(false, pagerParams1.equals(pagerParams2));

        pagerParams1.setDomain(domain);
        assertEquals(true, pagerParams1.equals(pagerParams2));

        pagerParams1.setContent("1.js");
        assertEquals(false, pagerParams1.equals(pagerParams2));

        pagerParams2.setContent("1.js");
        assertEquals(true, pagerParams1.equals(pagerParams2));

        pagerParams1.setDomain(null);
        assertEquals(false, pagerParams1.equals(pagerParams2));
    }

    /**
     * Name.
     */
    @Test
    public void name(){
        HttpConcatParam httpConcatParam1 = getHttpConcatParam();
        HttpConcatParam httpConcatParam2 = getHttpConcatParam();
        assertEquals(true, httpConcatParam1.equals(httpConcatParam2));
    }
}
