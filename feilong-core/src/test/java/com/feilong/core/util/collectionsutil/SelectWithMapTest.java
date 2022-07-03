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
package com.feilong.core.util.collectionsutil;

import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.bean.ConvertUtil.toMap;
import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.feilong.core.util.CollectionsUtil;
import com.feilong.store.member.User;

public class SelectWithMapTest{

    @Test
    public void testSelect2(){
        User guanyu301 = new User("关羽", 30);
        User guanyu30 = new User("关羽", 30);

        List<User> list = toList(//
                        new User("张飞", 23),
                        new User("关羽", 24),
                        guanyu301,
                        new User("刘备", 25),
                        guanyu30);

        Map<String, ?> map = toMap("name", "关羽", "age", 30);

        List<User> select = CollectionsUtil.select(list, map);
        assertThat(select, allOf(hasItem(guanyu30), hasItem(guanyu301)));
    }

    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testSelectNullPredicate(){
        List<User> list = toList(new User("张飞", 23));
        CollectionsUtil.select(list, (Map<String, ?>) null);
    }

    @Test
    public void testSelectNullPredicate1(){
        assertEquals(emptyList(), CollectionsUtil.<Object> select(null, (Map<String, ?>) null));
    }

    @Test
    public void testSelectNullIterable(){
        Object Select = CollectionsUtil.select(null, toMap("name", "关羽"));
        assertEquals(emptyList(), Select);
    }

    //---------------------------------------------------------------

    @Test
    public void testSelectNotSelect(){
        List<User> list = toList(new User("张飞", 23));
        assertEquals(emptyList(), CollectionsUtil.select(list, toMap("name", "关羽")));
    }
}
