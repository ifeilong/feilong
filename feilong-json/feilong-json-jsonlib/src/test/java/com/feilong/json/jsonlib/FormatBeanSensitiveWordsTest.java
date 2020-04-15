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
package com.feilong.json.jsonlib;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.json.jsonlib.entity.BeanWithSensitiveWords;

public class FormatBeanSensitiveWordsTest{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(FormatBeanSensitiveWordsTest.class);

    /**
     * Name1.
     */
    @Test
    public void test(){
        BeanWithSensitiveWords beanWithSensitiveWords = new BeanWithSensitiveWords("34567889", "sadadad&^%", "567");
        beanWithSensitiveWords.setCvv2("456");

        LOGGER.debug(JsonUtil.format(beanWithSensitiveWords));

        //maven install , 顺序会有问题
        //        assertEquals(
        //                        "{\"pattern\":\"34567889\",\"cvv\":\"******\",\"cvv2\":\"******\",\"key\":\"******\"}",
        //                        JsonUtil.format(beanWithSensitiveWords, 0, 0));

        String result = JsonUtil.format(beanWithSensitiveWords, 0, 0);

        assertTrue(result.contains("\"key\":\"******\""));
        assertTrue(result.contains("\"cvv\":\"******\""));
        assertTrue(result.contains("\"cvv2\":\"******\""));
    }

}
