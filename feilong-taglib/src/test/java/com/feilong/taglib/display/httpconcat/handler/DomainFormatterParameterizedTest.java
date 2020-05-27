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
package com.feilong.taglib.display.httpconcat.handler;

import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.lang.StringUtil.EMPTY;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import com.feilong.test.Abstract1ParamAndResultParameterizedTest;

/**
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.10.5
 */
public class DomainFormatterParameterizedTest extends Abstract1ParamAndResultParameterizedTest<String, String>{

    @Parameters(name = "index:{index}: DomainResolver.format({0})={1}")
    public static Iterable<Object[]> data(){
        Object[][] objects = new Object[][] { //
                                              { "", EMPTY },
                                              { null, EMPTY },
                                              { "  ", EMPTY },
                                              { "http://css.feilong.com", "http://css.feilong.com/" },
                                              { "http://css.feilong.com/", "http://css.feilong.com/" },
                                              { "http://css.feilong.com//", "http://css.feilong.com/" },
                //{ "http://css.feilong.com//////", "http://css.feilong.com/" },
                //
        };
        return toList(objects);
    }

    @Test
    public void testResolver(){
        assertEquals(expectedValue, DomainFormatter.format(input1));
    }
}
