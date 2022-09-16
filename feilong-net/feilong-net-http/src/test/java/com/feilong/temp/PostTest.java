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

import static com.feilong.core.bean.ConvertUtil.toMap;

import org.junit.Test;

import com.feilong.io.entity.MimeType;
import com.feilong.json.JsonUtil;
import com.feilong.net.http.ConnectionConfig;
import com.feilong.net.http.HttpClientUtil;
import com.feilong.net.http.HttpMethodType;
import com.feilong.net.http.HttpRequest;
import com.feilong.test.AbstractTest;

public class PostTest extends AbstractTest{

    @Test
    @SuppressWarnings("squid:S2699") //Tests should include assertions //https://stackoverflow.com/questions/10971968/turning-sonar-off-for-certain-code
    public void testGetResponseBodyAsString(){
        String uri = "http://127.0.0.1:8084/post";
        LOGGER.debug(HttpClientUtil.get(uri));
    }

    @Test
    @SuppressWarnings("squid:S2699") //Tests should include assertions //https://stackoverflow.com/questions/10971968/turning-sonar-off-for-certain-code
    public void testGetResponseBodyAsStr22ing(){
        String uri = "http://test.mapemall.com/pay/redirect/doku";
        LOGGER.debug(HttpClientUtil.get(uri));
    }

    @Test
    @SuppressWarnings("squid:S2699") //Tests should include assertions //https://stackoverflow.com/questions/10971968/turning-sonar-off-for-certain-code
    public void testGetResponseBodyAs222Str22ing(){
        String uri = "http://test.mapemall.com/pay/redirect/doku";

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setTimeoutRetryCount(1);
        LOGGER.debug(HttpClientUtil.getResponseBodyAsString(uri, connectionConfig));
    }

    @Test
    @SuppressWarnings("squid:S2699") //Tests should include assertions //https://stackoverflow.com/questions/10971968/turning-sonar-off-for-certain-code
    public void testGetResponseBodyAs222Str22in222g(){
        String uri = "http://test.mapemall.com/pay/redirect/doku";

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setTimeoutRetryCount(0);
        //    connectionConfig.setTimeoutRetryCount(1);
        LOGGER.debug(HttpClientUtil.getResponseBodyAsString(uri, connectionConfig));
    }

    @Test
    @SuppressWarnings("squid:S2699") //Tests should include assertions //https://stackoverflow.com/questions/10971968/turning-sonar-off-for-certain-code
    public void testGetResponseBodyAsString1(){
        String uri = "http://127.0.0.1:8084/post1";
        LOGGER.debug(HttpClientUtil.post(uri, toMap("name", "金鑫", "age", "18")));
    }

    @Test
    @SuppressWarnings("squid:S2699") //Tests should include assertions //https://stackoverflow.com/questions/10971968/turning-sonar-off-for-certain-code
    public void testGetResponseBodyAsString122(){
        //        "requestURL": "http://staging.mapemall.com/pay/redirect/doku", 
        //        "requestMethod": "POST", 
        //         "parameters": { 
        //               "PAYMENTCHANNEL": ["01"]
        //               (其他参数略去......)
        //            }
        String uri = "http://test.mapemall.com/pay/redirect/doku";
        LOGGER.debug(HttpClientUtil.post(uri, toMap("PAYMENTCHANNEL", "01")));
    }

    @Test
    @SuppressWarnings("squid:S2699") //Tests should include assertions //https://stackoverflow.com/questions/10971968/turning-sonar-off-for-certain-code
    public void testGetResponseBodyAsString122222(){
        String uri = "http://127.0.0.1:8223/consumestest";

        HttpRequest httpRequest = new HttpRequest(uri, HttpMethodType.POST);
        httpRequest.setHeaderMap(toMap("Content-Type", MimeType.JSON.getMime()));

        httpRequest.setRequestBody(JsonUtil.toString(toMap("name", "feilong")));

        String result = HttpClientUtil.getResponseBodyAsString(httpRequest);
        LOGGER.debug(result);
    }
}
