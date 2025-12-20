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
package com.feilong.core.util.collectionsutil.get;

import static com.feilong.core.bean.ConvertUtil.toList;
import static java.util.Collections.emptyMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.feilong.core.util.CollectionsUtil;
import com.feilong.store.member.User;

public class GetPropertyValueMapFunctionTest{

    @Test
    public void testGetPropertyValueMap(){
        User zhangfei = new User("张飞", 23);
        User guanyu = new User("关羽", 24);
        User liubei = new User("刘备", 25);
        List<User> list = toList(zhangfei, guanyu, liubei);

        Map<String, Integer> map = CollectionsUtil.getPropertyValueMap(list, User::getName, User::getAge);
        assertThat(
                        map,
                        allOf(//
                                        hasEntry("张飞", 23),
                                        hasEntry("关羽", 24),
                                        hasEntry("刘备", 25)));
    }

    @Test
    public void testGetPropertyValueMap1(){
        User zhangfei = new User("张飞", 10);
        User guanyu = new User("关羽", 24);
        User liubei = new User("刘备", 25);
        List<User> list = toList(zhangfei, guanyu, liubei);

        Map<String, Integer> map = CollectionsUtil.getPropertyValueMap(
                        list, //
                        user -> user.getName() + user.getAge(),
                        user -> user.getAge() > 18 ? 1 : 0);
        assertThat(
                        map,
                        allOf(//
                                        hasEntry("张飞10", 0),
                                        hasEntry("关羽24", 1),
                                        hasEntry("刘备25", 1)));
    }

    /**
     * Test get property value map same key.
     */
    @Test
    public void testGetPropertyValueMapSameKey(){
        User zhangfei = new User("张飞", 23);
        User guanyu = new User("关羽", 24);
        User zhangfei1 = new User("张飞", 25);
        List<User> list = toList(zhangfei, guanyu, zhangfei1);

        Map<String, Integer> map = CollectionsUtil.getPropertyValueMap(list, User::getName, User::getAge);

        assertThat(map.keySet(), hasSize(2));
        assertThat(map, allOf(hasEntry("张飞", 25), hasEntry("关羽", 24)));
    }

    //---------------------------------------------------------------

    /**
     * Test get property value map null collection.
     */
    @Test
    public void testGetPropertyValueMapNullCollection(){
        assertEquals(emptyMap(), CollectionsUtil.getPropertyValueMap(null, User::getName, User::getAge));
    }

    /**
     * Test get property value map empty collection.
     */
    @Test
    public void testGetPropertyValueMapEmptyCollection(){
        assertEquals(emptyMap(), CollectionsUtil.getPropertyValueMap(new ArrayList<>(), User::getName, User::getAge));
    }

    /**
     * Test get property value map null key property name.
     */
    //*****************
    @Test(expected = NullPointerException.class)
    public void testGetPropertyValueMapNullKeyPropertyName(){
        List<User> list = toList(new User("张飞", 23));
        CollectionsUtil.getPropertyValueMap(list, null, User::getAge);
    }

    //*****************

    /**
     * Test get property value map null value property name.
     */
    @Test(expected = NullPointerException.class)
    public void testGetPropertyValueMapNullValuePropertyName(){
        List<User> list = toList(new User("张飞", 23));
        CollectionsUtil.getPropertyValueMap(list, User::getName, null);
    }
}
