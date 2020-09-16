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
import static com.feilong.core.bean.ConvertUtil.toInteger;
import static com.feilong.core.bean.ConvertUtil.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

import java.util.List;

import org.junit.Test;

import com.feilong.json.AbstractJsonTest;
import com.feilong.json.JavaToJsonConfig;
import com.feilong.json.JsonUtil;
import com.feilong.lib.json.util.PropertyFilter;
import com.feilong.store.member.User;

public class FormatWithIncludesTest extends AbstractJsonTest{

    private final List<User> list = buildList();

    //---------------------------------------------------------------

    @Test
    public void test(){
        String formatWithIncludes = JsonUtil.formatWithIncludes(list, "name", "age");
        // LOGGER.debug(formatWithIncludes);

        assertThat(
                        formatWithIncludes,
                        allOf(
                                        containsString(
                                                        "[\n" + "                {\n" + "            \"age\": 24,\n"
                                                                        + "            \"name\": \"feilong1\"\n" + "        },\n"
                                                                        + "                {\n" + "            \"age\": 240,\n"
                                                                        + "            \"name\": null\n" + "        }\n" + "    ]")));
    }

    //---------------------------------------------------------------

    @Test
    public void test22222(){
        JavaToJsonConfig javaToJsonConfig = new JavaToJsonConfig();
        javaToJsonConfig.setIncludes("name", "age");
        javaToJsonConfig.setIsIgnoreNullValueElement(true);

        String formatWithIncludes = JsonUtil.format(list, javaToJsonConfig);

        // LOGGER.debug(formatWithIncludes);
        assertThat(
                        formatWithIncludes,
                        allOf(
                                        containsString(
                                                        "[\n" + "                {\n" + "            \"age\": 24,\n"
                                                                        + "            \"name\": \"feilong1\"\n" + "        },\n"
                                                                        + "        {\"age\": 240}\n" + "    ]")));
    }

    @Test
    public void test22222222(){
        JavaToJsonConfig javaToJsonConfig = new JavaToJsonConfig();
        javaToJsonConfig.setIncludes("name", "age");
        javaToJsonConfig.setIsIgnoreNullValueElement(true);
        javaToJsonConfig.setPropertyFilter(new PropertyFilter(){

            @Override
            public boolean apply(Object source,String name,Object value){
                if ("age".equals(name) && toInteger(value) < 100){
                    return true;
                }
                return false;
            }
        });

        String formatWithIncludes = JsonUtil.format(list, javaToJsonConfig);

        //LOGGER.debug(formatWithIncludes);
        assertThat(
                        formatWithIncludes,
                        allOf(containsString("[\n" + "        {\"name\": \"feilong1\"},\n" + "        {\"age\": 240}\n" + "    ]")));
    }

    @Test
    public void test222(){
        String[] array = { "id", "name" };
        String formatWithIncludes = JsonUtil.formatWithIncludes(toArray(list, User.class), array);

        assertThat(
                        formatWithIncludes,
                        allOf(
                                        containsString(
                                                        "[\n" + "                {\n" + "            \"id\": 0,\n"
                                                                        + "            \"name\": \"feilong1\"\n" + "        },\n"
                                                                        + "                {\n" + "            \"id\": 0,\n"
                                                                        + "            \"name\": null\n" + "        }\n" + "    ]")));

    }

    private List<User> buildList(){
        User user1 = new User("feilong1", 24);
        user1.setNickNames(toArray("xin.jin", "shuai.ge"));
        User user2 = new User((String) null, 240);
        user2.setNickNames(toArray("xin.jin", "shuai.ge"));

        return toList(user1, user2);
    }

    @Test
    public void test1(){
        String formatWithIncludes = JsonUtil.formatWithIncludes(toList("2,5,8", "2,5,9"));
        //LOGGER.debug(formatWithIncludes);

        assertThat(formatWithIncludes, allOf(containsString("\"2,5,8\""), containsString("\"2,5,9\"")));
    }

    @Test
    public void testFormatWithIncludes(){
        Object[][] objects = { { "feilong shoe", "500", 1 }, { "feilong shoe2", "5000", 1 } };
        String formatWithIncludes = JsonUtil.formatWithIncludes(objects);
        //LOGGER.debug(formatWithIncludes);

        assertThat(formatWithIncludes, allOf(containsString("\"feilong shoe\",")));
    }

}
