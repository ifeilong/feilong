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
package com.feilong.core.bean.convertutil;

import static com.feilong.core.bean.ConvertUtil.toArray;
import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.bean.ConvertUtil.toLongList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;

import java.util.List;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import com.feilong.test.Abstract1ParamAndResultParameterizedTest;

public class ToLongListParameterizedTest extends Abstract1ParamAndResultParameterizedTest<Object, List<Long>>{

    /**
     * Data.
     *
     * @return the iterable
     */
    @Parameters(name = "index:{index}: ConvertUtil.toLongList({0})={1}")
    public static Iterable<Object[]> data(){
        Object[][] objects = build();
        return toList(objects);
    }

    /**
     * @return
     * @since 1.10.3
     */
    private static Object[][] build(){
        return new Object[][] { //
                                { "1,2,3", toList(1L, 2L, 3L) },
                                { "{1,2,3}", toList(1L, 2L, 3L) },
                                { "{ 1 ,2,3}", toList(1L, 2L, 3L) },
                                { "1,2, 3", toList(1L, 2L, 3L) },
                                { "1,2 , 3", toList(1L, 2L, 3L) },
                                { new String[] { "1", "2", "3" }, toList(1L, 2L, 3L) },
                                { toList("1", "2", "3"), toList(1L, 2L, 3L) },
                                { toList(1, 2, 3), toList(1L, 2L, 3L) },
                                { toList(1f, 2f, 3f), toList(1L, 2L, 3L) },
                                { toList(1L, 2f, 3f), toList(1L, 2L, 3L) },
                                { toList(1L, 2f, 3), toList(1L, 2L, 3L) },
                                { toList(1d, 2d, 3d), toList(1L, 2L, 3L) },
                                { toList("1", "2", " 3"), toList(1L, 2L, 3L) },

                                { toArray(true, false, false), toList(1L, 0L, 0L) },
                //                                { new String[] { "1", null, "2", "3" }, toList(1L, null, 2L, 3L) },

        };
    }

    @Test
    public void testToLongs(){
        List<Long> longList = toLongList(input1);

        for (int i = 0; i < expectedValue.size(); ++i){
            assertThat(longList, allOf(hasItem(expectedValue.get(i))));
        }
    }

}
