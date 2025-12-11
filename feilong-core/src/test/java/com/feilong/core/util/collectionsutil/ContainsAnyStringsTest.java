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
package com.feilong.core.util.collectionsutil;

import static com.feilong.core.bean.ConvertUtil.toArray;
import static com.feilong.core.bean.ConvertUtil.toList;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import com.feilong.core.util.CollectionsUtil;
import com.feilong.test.Abstract2ParamsAndResultParameterizedTest;

public class ContainsAnyStringsTest extends Abstract2ParamsAndResultParameterizedTest<List<String>, String[], Boolean>{

    @Parameters(name = "index:{index}: CollectionsUtil.containsAny({0},{1})={2}")
    public static Iterable<Object[]> data(){
        Object[][] objects = build();

        return toList(objects);
    }

    private static Object[][] build(){
        return new Object[][] { //
                                { toList("track", "debug"), toArray("debug"), true },
                                { toList("track", "debug"), toArray(" debug"), false },
                                { toList("track", "DEBUG"), toArray(" debug"), false },
                                { toList("track", " DEBUG"), toArray(" debug"), false },

                                { toList("张飞", "关羽", "刘备"), toArray("关羽"), true },
                                { toList("张飞", "关羽", "刘备"), toArray(" 关羽"), false },
                                { toList("张飞", "关羽", "刘备"), toArray(" 关羽 "), false },
                                { toList("张飞", "关羽1", "刘备"), toArray(" 关羽 "), false },
                //
        };
    }

    @Test
    public void test(){
        assertEquals(expectedValue, CollectionsUtil.containsAny(input1, input2));
    }

}
