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

public class SelectCollectionWithFunctionTest{

    @Test
    public void testSelect(){
        User zhangfei = new User("张飞", 23);
        User guanyu = new User("关羽", 24);
        User liubei = new User("刘备", 25);
        List<User> list = toList(zhangfei, guanyu, liubei);
        assertThat(
                        CollectionsUtil.select(list, User::getName, toList("张飞", "刘备")),
                        allOf(hasItem(zhangfei), hasItem(liubei), not(hasItem(guanyu))));
    }

    //---------------------------------------------------------------
    @Test
    public void testSelectNullValue(){
        User zhangfei = new User("张飞", 23);
        User guanyu = new User("关羽", 30);
        User liubei = new User("刘备", 25);
        List<User> list = toList(zhangfei, guanyu, liubei);

        assertEquals(emptyList(), CollectionsUtil.select(list, User::getName, (List<String>) null));
    }

    @Test
    public void testSelectNullElementValue(){
        User zhangfei = new User("张飞", 23);
        User guanyu = new User("关羽", 30);
        User liubei = new User("刘备", 25);
        List<User> list = toList(zhangfei, guanyu, liubei);

        assertEquals(emptyList(), CollectionsUtil.select(list, User::getName, toList((String) null)));
    }

    @Test
    public void testSelectNullElementValue1(){
        User zhangfei = new User("张飞", 23);
        User guanyu = new User("关羽", 30);
        User nullName = new User((String) null, 30);
        User liubei = new User("刘备", 25);

        List<User> list = toList(zhangfei, nullName, guanyu, liubei);

        List<String> searchNameList = toList((String) null);
        List<User> select = CollectionsUtil.select(list, User::getName, searchNameList);

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

    @Test
    public void testSelectCollectionNullCollection(){
        assertEquals(emptyList(), CollectionsUtil.select(null, User::getName, toList("张飞", "刘备")));
    }

    @Test
    public void testSelectCollectionEmptyCollection(){
        assertEquals(emptyList(), CollectionsUtil.select(new ArrayList<>(), User::getName, toList("张飞", "刘备")));
    }

    //*****************

    @Test(expected = NullPointerException.class)
    public void testSelectCollectionNullPropertyName(){
        User zhangfei = new User("张飞", 23);
        User guanyu = new User("关羽", 30);
        User liubei = new User("刘备", 25);
        List<User> list = toList(zhangfei, guanyu, liubei);
        CollectionsUtil.select(list, (Function<User, String>) null, toList("张飞", "刘备"));
    }

}
