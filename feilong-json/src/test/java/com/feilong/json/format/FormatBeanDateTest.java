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

import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.date.DateUtil.toDate;

import org.junit.Test;

import com.feilong.core.DatePattern;
import com.feilong.json.JsonUtil;
import com.feilong.json.entity.BeanWithDate;
import com.feilong.test.AbstractTest;

public class FormatBeanDateTest extends AbstractTest{

    @Test
    public void test1(){
        BeanWithDate beanWithDate = new BeanWithDate("jim", toDate("2020-06-16 20:00:00", DatePattern.COMMON_DATE_AND_TIME));
        LOGGER.debug(JsonUtil.format(beanWithDate));
    }

    @Test
    public void testArray(){
        BeanWithDate beanWithDate = new BeanWithDate("jim", toDate("2020-06-16 20:00:00", DatePattern.COMMON_DATE_AND_TIME));
        LOGGER.debug(JsonUtil.format(toList(beanWithDate)));
    }

}
