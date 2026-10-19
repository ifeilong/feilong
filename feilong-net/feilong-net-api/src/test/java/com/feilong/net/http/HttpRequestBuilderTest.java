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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * {@link HttpRequest#builder()} 的测试.
 * 
 * <h3>说明:</h3>
 * <blockquote>
 * <ol>
 * <li>8 个字段都要能从 builder 透传到 build 出来的 {@link HttpRequest}</li>
 * <li>没有设置的字段, 默认值必须和 {@code new HttpRequest()} 一致</li>
 * <li>builder 出来的对象, {@link HttpRequest#getUri()} 的 trim / 空格转义行为不变</li>
 * </ol>
 * </blockquote>
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 4.5.6
 */
public class HttpRequestBuilderTest{

    /**
     * 8 个字段都要能透传到 build 出来的对象.
     */
    @Test
    public void testBuildAllField(){
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("name", "feilong");

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("user-agent", HttpRequest.DEFAULT_USER_AGENT);

        RequestByteArrayBody requestByteArrayBody = new RequestByteArrayBody(new byte[] { 1, 2 });

        HttpRequest httpRequest = HttpRequest.builder()//
            .uri("http://www.feilong.com")//
            .httpMethodType(HttpMethodType.POST)//
            .paramMap(paramMap)//
            .headerMap(headerMap)//
            .requestBody("{}")//
            .requestByteArrayBody(requestByteArrayBody)//
            .trimUri(false)//
            .logTraceContext("traceId")//
            .build();

        assertEquals("http://www.feilong.com", httpRequest.getUri());
        assertEquals(HttpMethodType.POST, httpRequest.getHttpMethodType());
        assertEquals(paramMap, httpRequest.getParamMap());
        assertEquals(headerMap, httpRequest.getHeaderMap());
        assertEquals("{}", httpRequest.getRequestBody());
        assertEquals(requestByteArrayBody, httpRequest.getRequestByteArrayBody());
        assertFalse(httpRequest.getIsTrimUri());
        assertEquals("traceId", httpRequest.getLogTraceContext());
    }

    /**
     * 一个字段都不设置时, 默认值必须和 {@code new HttpRequest()} 完全一致.
     */
    @Test
    public void testBuildWithDefaultValue(){
        HttpRequest httpRequest = HttpRequest.builder().build();

        assertEquals(EMPTY, httpRequest.getUri());
        assertEquals(HttpMethodType.GET, httpRequest.getHttpMethodType());
        assertNull(httpRequest.getParamMap());
        assertNull(httpRequest.getHeaderMap());
        assertNull(httpRequest.getRequestBody());
        assertNull(httpRequest.getRequestByteArrayBody());
        assertTrue(httpRequest.getIsTrimUri());
        assertEquals(EMPTY, httpRequest.getLogTraceContext());
    }

    /**
     * builder 出来的对象, 也要走 {@link HttpRequest#getUri()} 的 trim + 空格转义.
     */
    @Test
    public void testBuildWithTrimUri(){
        HttpRequest httpRequest = HttpRequest.builder().uri("https://github.com/ifeilong/fei long ").build();

        assertEquals("https://github.com/ifeilong/fei%20long", httpRequest.getUri());
    }

    /**
     * {@code trimUri} 设置为 {@code false} 时, 不 trim, 和 setter 行为一致.
     */
    @Test
    public void testBuildWithTrimUriFalse(){
        HttpRequest httpRequest = HttpRequest.builder().uri("https://github.com/ifei long/fei long ").trimUri(false).build();

        assertEquals("https://github.com/ifei%20long/fei%20long%20", httpRequest.getUri());
    }

    /**
     * {@code paramMap} 要参与 {@link HttpRequest#getFullEncodedUrl()} 的拼接.
     */
    @Test
    public void testBuildWithParamMapToFullEncodedUrl(){
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("name", "feilong");

        HttpRequest httpRequest = HttpRequest.builder().uri("http://www.feilong.com").paramMap(paramMap).build();

        assertEquals("http://www.feilong.com?name=feilong", httpRequest.getFullEncodedUrl());
    }

    /**
     * {@link HttpRequest.Builder#build()} 每次返回新的对象, 不是 builder 自身.
     */
    @Test
    public void testBuildReturnNewInstance(){
        HttpRequest.Builder builder = HttpRequest.builder().uri("http://www.feilong.com");

        HttpRequest first = builder.build();
        HttpRequest second = builder.build();

        assertNotSame(first, second);
        assertEquals(first.getUri(), second.getUri());
    }

    /**
     * 每个方法都要返回 builder 自身, 否则无法链式调用.
     */
    @Test
    public void testMethodReturnBuilderSelf(){
        HttpRequest.Builder builder = HttpRequest.builder();

        assertSame(builder, builder.uri("http://www.feilong.com"));
        assertSame(builder, builder.httpMethodType(HttpMethodType.GET));
        assertSame(builder, builder.paramMap(new HashMap<String, String>()));
        assertSame(builder, builder.headerMap(new HashMap<String, String>()));
        assertSame(builder, builder.requestBody("{}"));
        assertSame(builder, builder.requestByteArrayBody(new RequestByteArrayBody(new byte[] { 1 })));
        assertSame(builder, builder.trimUri(true));
        assertSame(builder, builder.logTraceContext("traceId"));
    }
}
