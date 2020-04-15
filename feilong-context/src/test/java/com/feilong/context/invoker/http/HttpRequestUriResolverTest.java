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
package com.feilong.context.invoker.http;

import static com.feilong.context.invoker.http.HttpRequestUriResolver.resolve;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.feilong.store.member.Member;

public class HttpRequestUriResolverTest{

    @Test
    public void test(){
        assertEquals("http://www.feilong.com", resolve("http://www.feilong.com", new Member()));
    }

    /**
     * 
     * @since 1.14.0
     */
    @Test
    public void test222(){
        assertEquals("http://www.feilong.com/123456", resolve("http://www.feilong.com/%s", "123456"));
    }

    @Test
    public void testTemplate(){
        Member member = new Member();
        member.setId(1L);
        assertEquals("http://www.feilong.com/1", resolve("http://www.feilong.com/${member.id}", member));
    }

    @Test
    public void testTemplate1(){
        Member member = new Member();
        member.setCode("feilong");
        assertEquals("http://www.feilong.com/feilong", resolve("http://www.feilong.com/${member.code}", member));
    }

    @Test
    public void testTemplate12(){
        Member member = new Member();
        member.setCode("fei long");
        assertEquals("http://www.feilong.com/fei long", resolve("http://www.feilong.com/${member.code}", member));
    }

    @Test
    public void testTemplate123(){
        Member member = new Member();
        member.setCode("fei  long");
        assertEquals("http://www.feilong.com/fei  long", resolve("http://www.feilong.com/${member.code}", member));
    }

    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testHttpRequestUriResolverTestNull(){
        HttpRequestUriResolver.resolve(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHttpRequestUriResolverTestEmpty(){
        HttpRequestUriResolver.resolve("", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHttpRequestUriResolverTestBlank(){
        HttpRequestUriResolver.resolve(" ", null);
    }

    //---------------------------------------------------------------

    @Test
    public void testHttpRequestUriResolverTestBlank1(){
        String uri = "http://www.feilong.com";
        assertEquals(uri, HttpRequestUriResolver.resolve(uri, null));
    }

    @Test(expected = NullPointerException.class)
    public void testHttpRequestUriResolverTestBlank12(){
        HttpRequestUriResolver.resolve("http://www.feilong.com/${member.code}", null);
    }
}
