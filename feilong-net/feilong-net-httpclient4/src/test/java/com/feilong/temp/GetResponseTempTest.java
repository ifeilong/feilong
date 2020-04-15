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
package com.feilong.temp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.feilong.net.HttpMethodType;
import com.feilong.net.entity.HttpRequest;
import com.feilong.net.httpclient4.HttpClientUtil;
import com.feilong.test.AbstractTest;

public class GetResponseTempTest extends AbstractTest{

    @Test
    public void testGetResponseStatusCode(){
        String urlString = "http://www.baidu.com";
        assertEquals(200, HttpClientUtil.getResponseStatusCode(urlString));
    }

    @Test
    public void testGetHttpResponse(){
        String urlString = "http://localhost:8081/member/login";
        urlString = "http://www.baidu.com";

        HttpClientUtil.getHttpResponse(urlString);
    }

    @Test
    public void testPutHttpResponse(){
        String uri = "http://127.0.0.1:8085?name=jinxin&age=18";
        HttpRequest httpRequest = new HttpRequest(uri, HttpMethodType.PUT);
        HttpClientUtil.getHttpResponse(httpRequest, null);
    }
}
