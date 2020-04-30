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

import com.feilong.net.http.HttpClientUtil;
import com.feilong.test.AbstractTest;

public class GetSleepTest extends AbstractTest{

    @Test
    public void testGetResponseBodyAsString11(){
        String uri = "http://127.0.0.1:8084/sleep?name=jinxin&age=18";
        LOGGER.debug(HttpClientUtil.get(uri, toMap("country", "china")));
    }

}
