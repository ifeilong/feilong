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

import static com.feilong.core.date.DateUtil.now;
import static com.feilong.core.lang.StringUtil.EMPTY;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.feilong.json.AbstractJsonTest;
import com.feilong.json.JsonUtil;
import com.feilong.store.member.User;

public class FormatBeanIgnoreNullWithIsIgnoreNullValueElementAndIfIgnoreNullValueElementIncludesTest extends AbstractJsonTest{

    @Test
    public void test0(){
        User user = new User();

        //        user.setPassword("123456");
        user.setId(8L);
        user.setName("feilong");
        user.setDate(now());
        //        user.setMoney(toBigDecimal("99999999.00"));

        String format = JsonUtil.format(user, true, "nickNames", "money");

        LOGGER.info(format);

        assertThat(
                        format,
                        allOf(//
                                        containsString("date"),
                                        //                                        containsString("userAddresses"),

                                        containsString("nickNames"),
                                        not(containsString("password"))

                        //
                        ));
    }

    @Test
    public void test01(){
        assertEquals(EMPTY, JsonUtil.format(null, true, "password"));
    }

    @Test
    public void test012(){
        assertEquals(EMPTY, JsonUtil.format(null, false, "password"));
    }

}
