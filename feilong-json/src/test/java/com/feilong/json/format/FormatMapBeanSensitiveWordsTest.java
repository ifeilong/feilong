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
package com.feilong.json.format;

import static com.feilong.core.util.MapUtil.newHashMap;

import java.util.Map;

import org.junit.Test;

import com.feilong.json.JsonUtil;
import com.feilong.json.entity.BeanWithSensitiveWords;
import com.feilong.test.AbstractTest;

@lombok.extern.slf4j.Slf4j
public class FormatMapBeanSensitiveWordsTest extends AbstractTest{

    @Test
    public void test(){
        BeanWithSensitiveWords beanWithSensitiveWords = new BeanWithSensitiveWords("34567889", "sadadad&^%", "567");

        Map<String, Object> map = newHashMap();
        map.put("beanWithSensitiveWords", beanWithSensitiveWords);

        log.debug(JsonUtil.format(map));
    }

}
