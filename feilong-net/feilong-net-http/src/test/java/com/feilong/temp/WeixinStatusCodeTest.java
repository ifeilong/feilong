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

import org.junit.Test;

import com.feilong.json.JsonUtil;
import com.feilong.net.http.HttpClientUtil;
import com.feilong.test.AbstractTest;

@lombok.extern.slf4j.Slf4j
@SuppressWarnings("squid:S2699") //Tests should include assertions //https://stackoverflow.com/questions/10971968/turning-sonar-off-for-certain-code
public class WeixinStatusCodeTest extends AbstractTest{

    String uri = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx1d2cf5724500a21c&secret=39fd9f4c892cc49118953e6b1cb1a059&code=081Z8ZI71qO4xO1NLfJ71OceJ71Z8ZIc&grant_type=authorization_code";

    @Test
    public void testGetResponseBodyAsString(){
        log.debug(HttpClientUtil.get(uri));
    }

    @Test
    public void getResponseStatusCode(){
        log.debug("" + HttpClientUtil.getResponseStatusCode(uri));
    }

    @Test
    public void getResponseStatusCode2(){
        log.debug(JsonUtil.format(HttpClientUtil.getHttpResponse(uri)));
    }

}
