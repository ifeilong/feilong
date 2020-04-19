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

import static com.feilong.core.util.CollectionsUtil.newArrayList;
import static com.feilong.core.util.CollectionsUtil.newHashSet;

import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.json.jsonlib.entity.BeanWithSensitiveWords;

public class FormatIterableBeanSensitiveWordsTest{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(FormatIterableBeanSensitiveWordsTest.class);

    @Test
    public void testList(){
        BeanWithSensitiveWords beanWithSensitiveWords = new BeanWithSensitiveWords("34567889", "sadadad&^%", "567");

        List<BeanWithSensitiveWords> list = newArrayList();
        list.add(beanWithSensitiveWords);

        LOGGER.debug(JsonUtil.format(list));
    }

    @Test
    public void testSet(){
        BeanWithSensitiveWords beanWithSensitiveWords = new BeanWithSensitiveWords("34567889", "sadadad&^%", "567");

        Set<BeanWithSensitiveWords> set = newHashSet();
        set.add(beanWithSensitiveWords);

        LOGGER.debug(JsonUtil.format(set));
    }

}
