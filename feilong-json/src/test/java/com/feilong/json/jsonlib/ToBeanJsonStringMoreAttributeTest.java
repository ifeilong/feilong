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

import static com.feilong.core.bean.ConvertUtil.toBigDecimal;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.feilong.store.member.User;

public class ToBeanJsonStringMoreAttributeTest{

    @Test
    public void testToBean(){
        User user = JsonUtil.toBean(
                        "{'passwordaaaaa':'123456','password':'123456','name':'feilong','money':'99999999.00','loves':['桔子', '香蕉']}",
                        User.class);
        user.setId(10L);

        assertThat(
                        user,
                        allOf(//
                                        hasProperty("id", is(10L)),
                                        hasProperty("password", is("123456")),
                                        hasProperty("name", is("feilong")),
                                        hasProperty("money", is(toBigDecimal("99999999.00"))),
                                        hasProperty("loves", arrayContaining("桔子", "香蕉"))
                        //  
                        ));
    }
}
