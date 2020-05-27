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
package com.feilong.net.mail.setter;

import static com.feilong.core.bean.ConvertUtil.toArray;
import static com.feilong.core.bean.ConvertUtil.toList;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import com.feilong.core.bean.ConvertUtil;
import com.feilong.core.lang.reflect.MethodUtil;
import com.feilong.net.mail.builder.setter.RecipientsSetter;
import com.feilong.test.Abstract3ParamsAndResultParameterizedTest;

/**
 * 
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 2.1.0
 * @see com.feilong.net.mail.builder.setter.RecipientsSetter#buildCC(String[], String, boolean)
 */
public class RecipientsSetterTest extends Abstract3ParamsAndResultParameterizedTest<String, String, String, String[]>{

    @Parameters(name = "index:{index}:RecipientsSetter.buildCC({0},\"{1}\",{2})={3}")
    public static Iterable<Object[]> data(){
        return toList(//
                        ConvertUtil.<Object> toArray(null, "a@163.com", false, null),
                        toArray(null, "a@163.com", true, toArray("a@163.com")),

                        //contains
                        toArray(toArray("a@163.com"), "a@163.com", false, toArray("a@163.com")),
                        toArray(toArray("a@163.com"), "a@163.com", true, toArray("a@163.com")),

                        toArray(toArray("a@163.com"), "b@163.com", false, toArray("a@163.com")),
                        toArray(toArray("a@163.com"), "b@163.com", true, toArray("a@163.com", "b@163.com"))

        //,
        );
    }

    @Test
    public void test(){
        String[] array = MethodUtil.invokeStaticMethod(RecipientsSetter.class, "buildCC", input1, input2, input3);
        assertArrayEquals(expectedValue, array);
    }

}
