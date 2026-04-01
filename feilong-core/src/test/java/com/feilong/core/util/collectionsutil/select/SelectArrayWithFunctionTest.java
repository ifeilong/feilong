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
package com.feilong.core.util.collectionsutil.select;

import static com.feilong.core.bean.ConvertUtil.toList;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.junit.Test;

import com.feilong.core.util.CollectionsUtil;
import com.feilong.store.member.User;

public class SelectArrayWithFunctionTest{

    @Test
    public void testSelectValue(){
        User zhangfei = new User("张飞", 23);
        User guanyu = new User("关羽", 24);
        User liubei = new User("刘备", 25);
        List<User> list = toList(zhangfei, guanyu, liubei, guanyu);

        assertThat(
                        CollectionsUtil.select(list, User::getName, "关羽"),
                        allOf(
                                        hasItem(guanyu),
                                        hasItem(guanyu),
                                        not(hasItem(zhangfei)), //
                                        not(hasItem(liubei))));
    }

    @Test
    public void testSelectArray(){
        User zhangfei = new User("张飞", 23);
        User guanyu = new User("关羽", 30);
        User liubei = new User("刘备", 25);
        List<User> list = toList(zhangfei, guanyu, liubei);

        List<User> select = CollectionsUtil.select(list, User::getName, "刘备", "关羽");
        assertThat(
                        select,
                        allOf(
                                        hasItem(liubei), //
                                        hasItem(guanyu),
                                        not(hasItem(zhangfei))));
    }

    /**
     * Test select null value.
     */
    @Test
    public void testSelectNullValue(){
        User zhangfei = new User("张飞", 23);
        User guanyu = new User("关羽", 30);
        User liubei = new User("刘备", 25);
        List<User> list = toList(zhangfei, guanyu, liubei);

        assertEquals(emptyList(), CollectionsUtil.select(list, User::getName, (String[]) null));
    }

    /**
     * Test select null element value.
     */
    @Test
    public void testSelectNullElementValue(){
        User zhangfei = new User("张飞", 23);
        User guanyu = new User("关羽", 30);
        User liubei = new User("刘备", 25);
        List<User> list = toList(zhangfei, guanyu, liubei);

        assertEquals(emptyList(), CollectionsUtil.select(list, User::getName, (String) null));
    }

    @Test
    public void testSelectNullElementValue1(){
        User zhangfei = new User("张飞", 23);
        User guanyu = new User("关羽", 30);
        User nullName = new User((String) null, 30);
        User liubei = new User("刘备", 25);

        List<User> list = toList(zhangfei, nullName, guanyu, liubei);

        List<User> select = CollectionsUtil.select(list, User::getName, (String) null);

        assertThat(
                        select,
                        allOf(
                                        hasItem(nullName), //

                                        not(hasItem(zhangfei)),
                                        not(hasItem(liubei)),
                                        not(hasItem(guanyu))
                        //
                        ));
    }

    //---------------------------------------------------------------

    /**
     * Test select array null collection.
     */
    @Test
    public void testSelectArrayNullCollection(){
        assertEquals(emptyList(), CollectionsUtil.select(null, User::getName, "刘备", "关羽"));
    }

    @Test
    public void testSelectArrayEmptyCollection(){
        assertEquals(emptyList(), CollectionsUtil.select(new ArrayList<>(), User::getName, "刘备", "关羽"));
    }

    //*****************

    /**
     * Test select array null property name.
     */
    @Test(expected = NullPointerException.class)
    public void testSelectArrayNullPropertyName(){
        User zhangfei = new User("张飞", 23);
        User guanyu = new User("关羽", 30);
        User liubei = new User("刘备", 25);
        List<User> list = toList(zhangfei, guanyu, liubei);
        CollectionsUtil.select(list, (Function) null, "刘备", "关羽");
    }
}
