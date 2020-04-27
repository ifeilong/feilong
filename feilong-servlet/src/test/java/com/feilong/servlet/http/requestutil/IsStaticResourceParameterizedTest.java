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
package com.feilong.servlet.http.requestutil;

import static com.feilong.core.bean.ConvertUtil.toList;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import com.feilong.servlet.http.RequestUtil;
import com.feilong.test.Abstract1ParamAndResultParameterizedTest;

public class IsStaticResourceParameterizedTest extends Abstract1ParamAndResultParameterizedTest<String, Boolean>{

    //.bmp,.jpe,.jpg,.jpeg,.png,.gif,.ico,.js,.css,.xml,.swf,.woff,.woff2,.ttf,.mp3,.zip,.tar
    @Parameters(name = "index:{index}: RequestUtil.isStaticResource({0})={1}")
    public static Iterable<Object[]> data(){
        Object[][] objects = new Object[][] { //
                                              { null, false },
                                              { "", false },
                                              { "/get", false },
                                              { "/post", false },
                                              { "/get/1", false },
                                              { "/post/feilong", false },

                                              { "/post/feilong.html", false },
                                              { "/post/feilong.htm", false },

                                              { "/post/feilong.bmp", true },
                                              { "/post/feilong.jpe", true },
                                              { "/post/feilong.jpg", true },
                                              { "/post/feilong.jpeg", true },
                                              { "/post/feilong.png", true },
                                              { "/post/feilong.gif", true },
                                              { "/post/feilong.ico", true },
                                              { "/post/feilong.js", true },
                                              { "/post/feilong.css", true },
                                              { "/post/feilong.xml", true },
                                              { "/post/feilong.swf", true },
                                              { "/post/feilong.woff", true },
                                              { "/post/feilong.woff2", true },
                                              { "/post/feilong.ttf", true },
                                              { "/post/feilong.mp3", true },
                                              { "/post/feilong.zip", true },
                                              { "/post/feilong.tar", true },

                                              { "/post/feilong.do", false },
                                              { "/post/feilong.jsp", false },

                //
        };
        return toList(objects);
    }

    @Test
    public void test(){
        assertEquals(expectedValue, RequestUtil.isStaticResource(input1));
    }

}
