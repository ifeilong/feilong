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
package com.feilong.spring.expression;

import static com.feilong.core.bean.ConvertUtil.toList;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.feilong.store.member.Member;

public class FunctionTest{

    String expressionString = "T(com.feilong.core.lang.StringUtil).tokenizeToStringArray('xin,jin',',')";

    @Test
    public void getValue22222(){
        String[] values = SpelUtil.getValue(expressionString);
        assertThat(toList(values), allOf(hasItem("xin"), hasItem("jin")));
    }

    //---------------------------------------------------------------

    @Test
    public void getValue2(){
        String[] values = SpelUtil.getValue(expressionString, null);
        assertThat(toList(values), allOf(hasItem("xin"), hasItem("jin")));
    }

    //---------------------------------------------------------------

    @Test
    public void getValue222(){
        String[] values = SpelUtil.getValue(expressionString, new Member());
        assertThat(toList(values), allOf(hasItem("xin"), hasItem("jin")));
    }

}
