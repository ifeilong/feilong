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
package com.feilong.xml.temp.annotation;

import java.util.Map;

import org.junit.Test;

import com.feilong.core.lang.SystemUtil;
import com.feilong.json.JsonUtil;
import com.feilong.test.AbstractTest;
import com.feilong.xml.XmlUtil;

public class WeatherResponseTest extends AbstractTest{

    @Test
    public void test(){
        String xml = SystemUtil.USER_HOME + "/workspace/feilong/feilong/feilong-xml/src/test/resources/weather-response.xml";

        Map<String, String> map = XmlUtil.getNodeNameAndStringValueMap(xml, "//string");
        LOGGER.debug(JsonUtil.format(map));
    }

}
