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
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.junit.Test;

import com.feilong.core.util.CollectionsUtil;
import com.feilong.store.member.User;

public class SelectRejectedFunctionCollectionTest{

    /**
     * Test select rejected.
     */
    @Test
    public void testSelectRejected(){
        User zhangfei = new User("张飞", 23);
        User guanyu = new User("关羽", 24);
        User liubei = new User("刘备", 25);
        List<User> list = toList(zhangfei, guanyu, liubei);

        List<User> selectRejected = CollectionsUtil.selectRejected(list, User::getName, toList("张飞", "刘备"));
        assertThat(selectRejected, hasSize(1));
        assertThat(
                        selectRejected,
                        allOf(//
                                        hasItem(guanyu),
                                        not(hasItem(zhangfei)),
                                        not(hasItem(liubei))));
    }

    //---------------------------------------------------------------

    @Test
    public void testSelectNullValue(){
        User zhangfei = new User("张飞", 23);
        User guanyu = new User("关羽", 30);
        User liubei = new User("刘备", 25);
        List<User> list = toList(zhangfei, guanyu, liubei);

        List<User> result = CollectionsUtil.selectRejected(list, User::getName, (List<String>) null);
        assertEquals(emptyList(), result);
        //        assertEquals(list, result);
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

        assertEquals(list, CollectionsUtil.selectRejected(list, User::getName, toList((String) null)));
    }

    @Test
    public void testSelectRejectedCollectionNullCollection(){
        assertEquals(emptyList(), CollectionsUtil.selectRejected(null, User::getName, toList("张飞", "刘备")));
    }

    @Test
    public void testSelectRejectedCollectionEmptyCollection(){
        assertEquals(emptyList(), CollectionsUtil.selectRejected(new ArrayList<>(), User::getName, toList("张飞", "刘备")));
    }

    @Test(expected = NullPointerException.class)
    public void testSelectRejectedCollectionNullPropertyName(){
        User zhangfei = new User("张飞", 23);
        User guanyu = new User("关羽", 30);
        User liubei = new User("刘备", 25);
        List<User> list = toList(zhangfei, guanyu, liubei);
        CollectionsUtil.selectRejected(list, (Function<User, String>) null, toList("张飞", "刘备"));
    }
}
