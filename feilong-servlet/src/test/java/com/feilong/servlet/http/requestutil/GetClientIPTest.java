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

import static com.feilong.core.util.MapUtil.newLinkedHashMap;
import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import com.feilong.core.lang.reflect.MethodUtil;
import com.feilong.servlet.http.HttpHeaders;
import com.feilong.servlet.http.RequestUtil;

public class GetClientIPTest{

    @Test
    public void testGetClientIPTest8(){

        Map<String, String> map = newLinkedHashMap();
        map.put(HttpHeaders.X_FORWARDED_FOR, "54.83.132.159, 199.27.72.25, 50.19.19.94");
        map.put(HttpHeaders.X_REAL_IP, "unknown");
        map.put(HttpHeaders.PROXY_CLIENT_IP, "unknown");
        map.put(HttpHeaders.WL_PROXY_CLIENT_IP, " 188.0.0.2 , 8.8.8.8,9.9.9.9");

        String result = MethodUtil.invokeStaticMethod(RequestUtil.class, "getClientIp", map);
        assertEquals("54.83.132.159", result);
    }

    @Test
    public void testGetClientIPTest5(){

        Map<String, String> map = newLinkedHashMap();
        map.put(HttpHeaders.X_FORWARDED_FOR, "");
        map.put(HttpHeaders.X_REAL_IP, "unknown");
        map.put(HttpHeaders.PROXY_CLIENT_IP, "unknown");
        map.put(HttpHeaders.WL_PROXY_CLIENT_IP, " 188.0.0.2 , 8.8.8.8,9.9.9.9");

        String result = MethodUtil.invokeStaticMethod(RequestUtil.class, "getClientIp", map);
        assertEquals("188.0.0.2", result);
    }

    @Test
    public void testGetClientIPTest4(){

        Map<String, String> map = newLinkedHashMap();
        map.put(HttpHeaders.X_FORWARDED_FOR, "");
        map.put(HttpHeaders.X_REAL_IP, "unknown");
        map.put(HttpHeaders.PROXY_CLIENT_IP, "188.0.0.6,8.8.8.8,9.9.9.9");
        map.put(HttpHeaders.WL_PROXY_CLIENT_IP, "188.0.0.2,8.8.8.8,9.9.9.9");

        String result = MethodUtil.invokeStaticMethod(RequestUtil.class, "getClientIp", map);
        assertEquals("188.0.0.6", result);
    }

    @Test
    public void testGetClientIPTest3(){

        Map<String, String> map = newLinkedHashMap();
        map.put(HttpHeaders.X_FORWARDED_FOR, "");
        map.put(HttpHeaders.X_REAL_IP, "unknown");
        map.put(HttpHeaders.PROXY_CLIENT_IP, "188.0.0.2,8.8.8.8,9.9.9.9");

        String result = MethodUtil.invokeStaticMethod(RequestUtil.class, "getClientIp", map);
        assertEquals("188.0.0.2", result);
    }

    @Test
    public void testGetClientIPTest2(){
        Map<String, String> map = newLinkedHashMap();
        map.put(HttpHeaders.X_FORWARDED_FOR, "");
        map.put(HttpHeaders.X_REAL_IP, "188.0.0.1,8.8.8.8,9.9.9.9");

        String result = MethodUtil.invokeStaticMethod(RequestUtil.class, "getClientIp", map);
        assertEquals("188.0.0.1", result);
    }

    @Test
    public void testGetClientIPTest(){

        Map<String, String> map = newLinkedHashMap();
        map.put(HttpHeaders.X_FORWARDED_FOR, "127.0.0.1,8.8.8.8,9.9.9.9");

        String result = MethodUtil.invokeStaticMethod(RequestUtil.class, "getClientIp", map);
        assertEquals("127.0.0.1", result);
    }

    //---------------------------------------------------------------

    @Test
    public void test(){
        Map<String, String> map = newLinkedHashMap();
        map.put("", "");

        String result = MethodUtil.invokeStaticMethod(RequestUtil.class, "getClientIp", map);
        assertEquals("", result);
    }

    @Test
    public void testEmpty(){
        Map<String, String> map = newLinkedHashMap();

        String result = MethodUtil.invokeStaticMethod(RequestUtil.class, "getClientIp", map);
        assertEquals("", result);
    }

}
