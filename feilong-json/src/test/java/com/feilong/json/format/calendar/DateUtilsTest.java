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
package com.feilong.json.format.calendar;

import java.util.Calendar;
import java.util.List;

import org.junit.Test;

import com.feilong.json.JsonUtil;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 2.0.3
 */
@lombok.extern.slf4j.Slf4j
public class DateUtilsTest{

    @Test
    public void test(){
        List<Calendar> build = DateUtils.build("2022-06-26 12:00:00", "2022-06-26 12:59:59");

        log.debug(JsonUtil.format(build));
    }

    @Test
    public void test1(){
        Calendar beginDate = Calendar.getInstance();

        log.debug(JsonUtil.format(beginDate));

    }

    @Test
    public void test12(){
        log.debug(JsonUtil.format(1));

    }
}
