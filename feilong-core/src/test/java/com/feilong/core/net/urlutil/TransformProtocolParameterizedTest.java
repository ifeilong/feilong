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
package com.feilong.core.net.urlutil;

import static com.feilong.core.bean.ConvertUtil.toList;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import com.feilong.core.net.URLUtil;
import com.feilong.test.Abstract3ParamsAndResultParameterizedTest;

public class TransformProtocolParameterizedTest extends Abstract3ParamsAndResultParameterizedTest<String, String, String, String>{

    @Parameters(name = "index:{index}: URLUtil.transformProtocol({0})={1}")
    public static Iterable<Object[]> data(){
        Object[][] objects = build();
        return toList(objects);
    }

    private static Object[][] build(){
        return new Object[][] { //
                                { "", "https:", "http:", "" },
                                { " ", "https:", "http:", " " },
                                { null, "https:", "http:", null },

                                {
                                  "http://aod.baidu.com/storay41w-aacv2-48K.m4a",
                                  "https:",
                                  "http:",
                                  "http://aod.baidu.com/storay41w-aacv2-48K.m4a" },

                                {
                                  "https://aod.baidu.com/storay41w-aacv2-48K.m4a",
                                  "https:",
                                  "http:",
                                  "http://aod.baidu.com/storay41w-aacv2-48K.m4a" },

                                {
                                  "https://aod.baidu.com/storay41w-aacv2-48K.m4a?http",
                                  "https:",
                                  "http:",
                                  "http://aod.baidu.com/storay41w-aacv2-48K.m4a?http" },

                                {
                                  "https://aod.baidu.com/storay41w-aacv2-48K.m4a?http",
                                  "https:",
                                  "http:",
                                  "http://aod.baidu.com/storay41w-aacv2-48K.m4a?http" },

                                {
                                  " https://aod.baidu.com/storay41w-aacv2-48K.m4a",
                                  "https:",
                                  "http:",
                                  "http://aod.baidu.com/storay41w-aacv2-48K.m4a" },

                                { "//aod.baidu.com/storay41w-aacv2-48K.m4a", "https:", "http:", "//aod.baidu.com/storay41w-aacv2-48K.m4a" },
                                {
                                  "//aod.baidu.com/storay41w-aacv2-48K.m4a?https",
                                  "https:",
                                  "http:",
                                  "//aod.baidu.com/storay41w-aacv2-48K.m4a?https" },

                                {
                                  " //aod.baidu.com/storay41w-aacv2-48K.m4a",
                                  "https:",
                                  "http:",
                                  "//aod.baidu.com/storay41w-aacv2-48K.m4a" },

                //
        };
    }

    @Test
    public void test(){
        assertEquals(expectedValue, URLUtil.transformProtocol(input1, input2, input3));
    }

}
