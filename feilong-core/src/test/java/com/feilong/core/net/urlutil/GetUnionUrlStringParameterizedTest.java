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
import com.feilong.test.Abstract2ParamsAndResultParameterizedTest;

public class GetUnionUrlStringParameterizedTest extends Abstract2ParamsAndResultParameterizedTest<String, String, String>{

    /** <code>{@value}</code> */
    private static String domain = "http://www.feilong.com";

    @Parameters(name = "index:{index}: URLUtil.getUnionUrl({0},{1})={2}")
    public static Iterable<Object[]> data(){
        Object[][] objects = build();
        return toList(objects);
    }

    private static Object[][] build(){
        return new Object[][] {
                                { domain + "/jinyiyexing/", "/jinyiyexing/1173348/", domain + "/jinyiyexing/1173348/" },
                                { domain + "/jinyiyexing/", "jinyiyexing/1173348/", domain + "/jinyiyexing/jinyiyexing/1173348/" },
                                { domain + "/jinyiyexing/", "1173348/", domain + "/jinyiyexing/1173348/" },
                                { domain + "/jinyiyexing/", "1173348", domain + "/jinyiyexing/1173348" },
                                // { domain + "/jinyiyexing", "/1173348", domain + "/jinyiyexing/1173348" }, //http://www.feilong.com/1173348
                                //{ domain + "/jinyiyexing", "1173348", domain + "/jinyiyexing/1173348" },//http://www.feilong.com/1173348

                                { "http://www.feilong.com/", "/1173348/", domain + "/1173348/" },
                                { "http://www.feilong.com/", "/1173348", domain + "/1173348" },
                                { "http://www.feilong.com/", "1173348/", domain + "/1173348/" },
                                { "http://www.feilong.com/", "1173348", domain + "/1173348" },

                                { "http://www.feilong.com", "/1173348/", domain + "/1173348/" },
                                { "http://www.feilong.com", "/1173348", domain + "/1173348" },
                                { "http://www.feilong.com", "1173348/", domain + "/1173348/" },
                                { "http://www.feilong.com", "1173348", domain + "/1173348" },

                                {
                                  "http://www.feilong.com",
                                  "storages/60c4-audiotest/2B/5A/Ga9gAAkKOi.png",
                                  domain + "/storages/60c4-audiotest/2B/5A/Ga9gAAkKOi.png" },
                                {
                                  "http://www.feilong.com",
                                  "/storages/60c4-audiotest/2B/5A/Ga9gAAkKOi.png",
                                  domain + "/storages/60c4-audiotest/2B/5A/Ga9gAAkKOi.png" },
                                {
                                  "http://www.feilong.com/",
                                  "storages/60c4-audiotest/2B/5A/Ga9gAAkKOi.png",
                                  domain + "/storages/60c4-audiotest/2B/5A/Ga9gAAkKOi.png" },
                                {
                                  "http://www.feilong.com/",
                                  "/storages/60c4-audiotest/2B/5A/Ga9gAAkKOi.png",
                                  domain + "/storages/60c4-audiotest/2B/5A/Ga9gAAkKOi.png" },

                //
        };
    }

    @Test
    public void test(){
        assertEquals(expectedValue, URLUtil.getUnionUrl(input1, input2));
    }

}
