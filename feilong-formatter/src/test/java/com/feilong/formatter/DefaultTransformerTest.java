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
package com.feilong.formatter;

import static com.feilong.core.bean.ConvertUtil.toArray;
import static com.feilong.core.bean.ConvertUtil.toBigDecimal;
import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.bean.ConvertUtil.toMap;
import static com.feilong.core.date.DateUtil.now;
import static com.feilong.formatter.FormatterUtil.formatToSimpleTable;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.store.member.User;

/**
 * The Class DefaultTransformerTest.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.7
 */
public class DefaultTransformerTest{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTransformerTest.class);

    /**
     * Test format to simple table 2.
     */
    @Test
    @SuppressWarnings("static-method")
    public void testFormatToSimpleTable2(){
        User user = new User();
        user.setDate(now());
        user.setAge(50);
        user.setLoves(toArray("yumaoqiu", "zuqiu"));
        user.setAttrMap(toMap("name", "xinjin", "a", "feilong"));
        user.setMoney(toBigDecimal(5000));
        user.setPassword("123456");

        List<User> list = toList(//
                        user

        //
        );

        LOGGER.debug(formatToSimpleTable(list));
    }
}
