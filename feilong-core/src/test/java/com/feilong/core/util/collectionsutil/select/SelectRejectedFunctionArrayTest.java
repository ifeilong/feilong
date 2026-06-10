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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.junit.Test;

import com.feilong.core.util.CollectionsUtil;
import com.feilong.store.member.User;

public class SelectRejectedFunctionArrayTest{

    @Test
    public void testSelectRejected1(){
        User zhangfei = new User("张飞", 23);
        User guanyu = new User("关羽", 24);
        User liubei = new User("刘备", 25);
        List<User> list = toList(zhangfei, guanyu, liubei);

        List<User> selectRejected = CollectionsUtil.selectRejected(list, User::getName, "刘备", "张飞");
        assertSame(1, selectRejected.size());
        assertThat(selectRejected.get(0), hasProperty("name", equalTo("关羽")));
    }

    @Test
    public void testSelectRejectedNull(){
        User zhangfei = new User("张飞", 23);
        User guanyu = new User("关羽", 24);
        User nullName = new User((String) null, 30);
        User liubei = new User("刘备", 25);
        List<User> list = toList(zhangfei, guanyu, liubei, nullName);

        List<User> result = CollectionsUtil.selectRejected(list, User::getName, (String) null);
        assertSame(3, result.size());
        assertThat(result, allOf(hasItem(zhangfei), hasItem(guanyu), hasItem(liubei), not(hasItem(nullName))));
    }
    ///******************

    /**
     * Test select null value.
     */
    @Test
    public void testSelectNullValue(){
        User zhangfei = new User("张飞", 23);
        User guanyu = new User("关羽", 30);
        User liubei = new User("刘备", 25);
        List<User> list = toList(zhangfei, guanyu, liubei);
        List<User> result = CollectionsUtil.selectRejected(list, User::getName, (String[]) null);

        assertEquals(emptyList(), result);

        //        assertThat(result, allOf(hasItem(zhangfei), hasItem(guanyu), hasItem(liubei)));
    }

    @Test
    public void testSelectNullElementValue(){
        User zhangfei = new User("张飞", 23);
        User guanyu = new User("关羽", 30);
        User liubei = new User("刘备", 25);
        List<User> list = toList(zhangfei, guanyu, liubei);
        assertEquals(list, CollectionsUtil.selectRejected(list, User::getName, (String) null));
    }

    @Test
    public void testSelectRejectedArrayNullCollection(){
        assertEquals(emptyList(), CollectionsUtil.selectRejected(null, User::getName, "刘备", "关羽"));
    }

    @Test
    public void testSelectRejectedArrayEmptyCollection(){
        assertEquals(emptyList(), CollectionsUtil.selectRejected(new ArrayList<>(), User::getName, "刘备", "关羽"));
    }

    //*****************
    @Test(expected = NullPointerException.class)
    public void testSelectRejectedArrayNullPropertyName(){
        User zhangfei = new User("张飞", 23);
        User guanyu = new User("关羽", 30);
        User liubei = new User("刘备", 25);
        List<User> list = toList(zhangfei, guanyu, liubei);
        CollectionsUtil.selectRejected(list, (Function<User, String>) null, "刘备", "关羽");
    }

}
