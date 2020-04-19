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

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.json.jsonlib.entity.BeanInt;

public class FormatBeanIntTypeTest{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(FormatBeanIntTypeTest.class);

    @Test
    public void test1(){
        BeanInt beanInt = new BeanInt();
        //{"age": 0}
        LOGGER.debug(JsonUtil.format(beanInt));
    }

    @Test
    public void test12(){
        BeanInt beanInt = new BeanInt();
        //{"age": null}
        String format = JsonUtil.format(beanInt);

        BeanInt bean = JsonUtil.toBean(format, BeanInt.class);
        LOGGER.debug("" + bean.getAge());
    }

}
