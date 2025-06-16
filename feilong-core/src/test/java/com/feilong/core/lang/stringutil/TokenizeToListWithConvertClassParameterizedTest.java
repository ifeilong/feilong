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
package com.feilong.core.lang.stringutil;

import static com.feilong.core.bean.ConvertUtil.toArray;
import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.util.CollectionsUtil.size;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;

import java.util.List;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import com.feilong.core.lang.StringUtil;
import com.feilong.test.Abstract2ParamsAndResultParameterizedTest;

public class TokenizeToListWithConvertClassParameterizedTest extends Abstract2ParamsAndResultParameterizedTest<String, Class<?>, List<?>>{

    @Parameters(name = "index:{index}:StringUtil.tokenizeToList({0},{1})={2}")
    public static Iterable<Object[]> data(){
        return toList(//

                        toArray("8  9 ,0;12;18 ", Integer.class, toList(8, 9, 0, 12, 18)),
                        toArray("8  9 ,0;12;18 ", Long.class, toList(8L, 9L, 0L, 12L, 18L))

        );
    }

    @Test
    public void tokenizeToSet(){
        List<Object> tokenizeToList = (List<Object>) StringUtil.tokenizeToList(input1, input2);

        for (int i = 0; i < size(expectedValue); ++i){
            Object item = expectedValue.get(i);
            assertThat(tokenizeToList, hasItem(item));
        }
    }

}