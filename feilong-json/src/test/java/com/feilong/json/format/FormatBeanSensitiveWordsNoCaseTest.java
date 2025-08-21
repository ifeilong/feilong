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

import org.junit.Test;

import com.feilong.json.JavaToJsonConfig;
import com.feilong.json.JsonUtil;
import com.feilong.json.entity.BeanWithSensitiveWordsCase;
import com.feilong.json.entity.BeanWithSensitiveWordsCaseInput;

@lombok.extern.slf4j.Slf4j
public class FormatBeanSensitiveWordsNoCaseTest{

    @Test
    public void test(){
        BeanWithSensitiveWordsCase beanWithSensitiveWordsCase = new BeanWithSensitiveWordsCase();
        beanWithSensitiveWordsCase.setCvv("12222");
        beanWithSensitiveWordsCase.setPattern("pattern");
        beanWithSensitiveWordsCase.setBeanWithSensitiveWordsCaseInput(new BeanWithSensitiveWordsCaseInput("2222222"));

        JavaToJsonConfig javaToJsonConfig = new JavaToJsonConfig();
        javaToJsonConfig.setIsMaskDefaultSensitiveWords(false);

        String result = JsonUtil.format(beanWithSensitiveWordsCase, javaToJsonConfig);

        //---------------------------------------------------------------

        log.debug(result);
        //
        //        //---------------------------------------------------------------
        //
        //        assertTrue(result.contains("\"key\":\"sadadad&^%\""));
        //        assertTrue(result.contains("\"cvv\":\"******\""));
        //        assertTrue(result.contains("\"cvv2\":\"******\""));
    }
}
