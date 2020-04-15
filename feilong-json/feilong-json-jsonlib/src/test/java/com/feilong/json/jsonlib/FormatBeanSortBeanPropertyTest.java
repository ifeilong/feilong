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

import com.feilong.store.member.Address;

/**
 * test https://github.com/venusdrogon/feilong-json/issues/30
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.14.0
 * @deprecated json format bean 支持排序输出 放弃吧,
 */
@Deprecated
public class FormatBeanSortBeanPropertyTest{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(FormatBeanSortBeanPropertyTest.class);

    //---------------------------------------------------------------

    @Test
    public void test(){
        //json format bean 支持排序输出 放弃吧,
        //参见 https://github.com/venusdrogon/feilong-json/issues/30
        Address user = new Address("china", "shanghai", "216000", "wenshui wanrong.lu 888");
        LOGGER.debug(JsonUtil.format(user));
    }

}
