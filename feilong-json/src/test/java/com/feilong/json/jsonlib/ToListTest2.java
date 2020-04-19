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
package com.feilong.json.jsonlib;

import org.junit.Test;

import com.feilong.store.member.Person;

/**
 * The Class JsonUtilToListTest.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 */
public class ToListTest2{

    @Test
    public void testToList(){
        String json = "{'name11':'get'},{'nam112e':'set'}";
        JsonUtil.toList(json, Person.class);
    }

    @Test
    public void testToMap(){
        String json = "2{'name11':'get'},{'nam112e':'set'}";
        JsonUtil.toMap(json);
    }

    @Test
    public void testToBean(){
        String json = "2{'name11':'get'},{'nam112e':'set'}";
        JsonUtil.toBean(json, Person.class);
    }

    @Test
    public void testToArrayBean(){
        String json = "2{'name11':'get'},{'nam112e':'set'}";
        JsonUtil.toArray(json, new JsonToJavaConfig(Person.class));
    }

}
