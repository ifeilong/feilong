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
package com.feilong.json;

import static com.feilong.core.bean.ConvertUtil.toList;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import com.feilong.json.JsonHelper;
import com.feilong.store.member.User;
import com.feilong.test.Abstract1ParamAndResultParameterizedTest;

public class JsonHelperIsKeyValueJsonStringTest extends Abstract1ParamAndResultParameterizedTest<Object, Boolean>{

    @Parameters(name = "index:{index}: JsonHelper.isCommonString({0})={1}")
    public static Iterable<Object[]> data(){
        Object[][] objects = new Object[][] { //
                                              {
                                                "{name=\"json\",bool:true,int:1,double:2.2,func:function(a){ return a; },array:[1,2]}",
                                                true },
                                              {
                                                "[name=\"json\",bool:true,int:1,double:2.2,func:function(a){ return a; },array:[1,2]]",
                                                false },
                                              { "", false },
                                              { "<123>", false },
                                              { null, false },
                                              { new User(), false },
                //
        };
        return toList(objects);
    }

    @Test
    public void test(){
        assertEquals(expectedValue, JsonHelper.isKeyValueJsonString(input1));
    }

}
