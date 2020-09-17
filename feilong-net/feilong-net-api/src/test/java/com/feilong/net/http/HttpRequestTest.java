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
package com.feilong.net.http;

import static com.feilong.core.lang.StringUtil.EMPTY;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HttpRequestTest{

    @Test
    public void test(){
        HttpRequest httpRequest = new HttpRequest(null);
        assertEquals(EMPTY, httpRequest.getUri());
    }

    @Test
    public void test1(){
        HttpRequest httpRequest = new HttpRequest("https://github.com/ifeilong/fei long");
        assertEquals("https://github.com/ifeilong/fei%20long", httpRequest.getUri());
    }

    @Test
    public void test12(){
        HttpRequest httpRequest = new HttpRequest("https://github.com/ifei long/fei long");
        assertEquals("https://github.com/ifei%20long/fei%20long", httpRequest.getUri());
    }

    @Test
    public void test1222(){
        HttpRequest httpRequest = new HttpRequest("https://github.com/ifeilong/fei long ");
        assertEquals("https://github.com/ifeilong/fei%20long", httpRequest.getUri());
    }

    @Test
    public void test12333(){
        HttpRequest httpRequest = new HttpRequest("https://github.com/ifei long/fei long ");
        httpRequest.setIsTrimUri(false);
        assertEquals("https://github.com/ifei%20long/fei%20long%20", httpRequest.getUri());
    }
}
