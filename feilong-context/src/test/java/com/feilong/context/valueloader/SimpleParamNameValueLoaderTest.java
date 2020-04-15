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
package com.feilong.context.valueloader;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.feilong.context.ValueLoader;

@ContextConfiguration("classpath:spring-SimpleParamNameValueLoader.xml")
public class SimpleParamNameValueLoaderTest extends AbstractJUnit4SpringContextTests{

    @Autowired
    @Qualifier("testSimpleParamNameValueLoaderStringToInteger")
    private ValueLoader<Integer> testSimpleParamNameValueLoaderStringToInteger;

    @Autowired
    @Qualifier("testSimpleParamNameValueLoaderString")
    private ValueLoader<Integer> testSimpleParamNameValueLoaderString;

    @Autowired
    @Qualifier("testSimpleParamNameValueLoaderDefaultStringToInteger")
    private ValueLoader<Integer> testSimpleParamNameValueLoaderDefaultStringToInteger;

    //---------------------------------------------------------------

    @Test
    public void testDefault(){
        assertEquals((Object) 20, testSimpleParamNameValueLoaderDefaultStringToInteger.load());
    }

    @Test
    public void test(){
        assertEquals((Object) 1, testSimpleParamNameValueLoaderStringToInteger.load());
    }

    @Test
    public void testString(){
        assertEquals("1", testSimpleParamNameValueLoaderString.load());
    }
}
