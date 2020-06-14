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

import static com.feilong.core.bean.ConvertUtil.toArray;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

import org.junit.Test;

import com.feilong.json.AbstractJsonTest;
import com.feilong.json.JsonUtil;

public class FormatBeanExcludesTest extends AbstractJsonTest{

    @SuppressWarnings("unchecked")
    @Test
    public void testExcludes(){
        String format = JsonUtil.format(USER, toArray("name", "loves", "attrMap", "userInfo", "userAddresses"));
        assertThat(
                        format,
                        allOf(//
                                        containsString("userAddresseList"),
                                        containsString("money"),

                                        not(containsString("name")),
                                        not(containsString("loves")),
                                        not(containsString("attrMap")),
                                        not(containsString("userInfo")),
                                        not(containsString("userAddresses"))

                        //
                        ));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testExcludes2(){
        String format = JsonUtil.format(USER, "name", "loves", "attrMap", "userInfo", "userAddresses");
        assertThat(
                        format,
                        allOf(//
                                        containsString("userAddresseList"),
                                        containsString("money"),

                                        not(containsString("name")),
                                        not(containsString("loves")),
                                        not(containsString("attrMap")),
                                        not(containsString("userInfo")),
                                        not(containsString("userAddresses"))

                        //
                        ));
    }

    //---------------------------------------------------------------

    @SuppressWarnings("unchecked")
    @Test
    public void testExcludes1(){
        String format = JsonUtil.format(USER, toArray("name", "loves", "attrMap", "userInfo", "userAddresses"), 0, 0);
        assertThat(
                        format,
                        allOf(//
                                        containsString("userAddresseList"),
                                        containsString("money"),

                                        not(containsString("name")),
                                        not(containsString("loves")),
                                        not(containsString("attrMap")),
                                        not(containsString("userInfo")),
                                        not(containsString("userAddresses"))

                        //
                        ));
    }

}
