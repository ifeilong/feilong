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

import static com.feilong.core.Validator.isNotNullOrEmpty;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.net.httpclient4.HttpClientUtil;
import com.feilong.test.AbstractTest;

public class HttpClientUtil4Test extends AbstractTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil4Test.class);
	// "fullEncodedUrl":
	// "https://cps.wecommerce.com.cn/cps/broker/getToken?storeId=9&secret=991d110bc99aa4c9f151525f49eb6934",
	// "requestBody": "",
	// "httpMethodType": "GET",
	// "paramMap": {
	// "storeId": "9",
	// "secret": "991d110bc99aa4c9f151525f49eb6934"
	// },
	// "headerMap": null,
	// "uri": "https://cps.wecommerce.com.cn/cps/broker/getToken"

	@Test
	public void testGetResponseBodyAsString1() {
		String uri = "";
		uri = "https://cps.wecommerce.com.cn/cps/broker/getToken?storeId=9&secret=991d110bc99aa4c9f151525f49eb6934";

		LOGGER.debug(HttpClientUtil.get(uri));
	}

	@Test
	public void testGetResponseBodyAsString() {
		String uri = "http://127.0.0.1:8084";

		LOGGER.debug(HttpClientUtil.get(uri));
	}

	@Test
	public void testHttpClientUtilTest() {
		// 执行一个get方法,设置超时时间,并且将结果变成字符串
		// Request.Get("http://www.yeetrack.com/").connectTimeout(1000).socketTimeout(1000).execute().returnContent().asString();
	}

}
