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

import com.feilong.core.util.CollectionsUtil;
import com.feilong.core.util.predicate.BeanPredicateUtil;
import com.feilong.store.member.User;
import org.apache.commons.collections4.Predicate;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.bean.ConvertUtil.toMap;
import static org.junit.Assert.assertEquals;

public class ExistWithPredicateTest {

    @Test
    public void testExist2() {
        User guanyu30 = new User("关羽", 30);
        List<User> list = toList(//
                new User("张飞", 23), new User("关羽", 24), new User("刘备", 25), guanyu30);

        Map<String, ?> map = toMap("name", "关羽", "age", 30);

        Predicate<User> equalPredicate = BeanPredicateUtil.equalPredicate(map);
        assertEquals(true, CollectionsUtil.exist(list, equalPredicate));
    }

    @Test(expected = NullPointerException.class)
    public void testExistNullPredicate() {
        List<User> list = toList(new User("张飞", 23));
        CollectionsUtil.exist(list, (Predicate<User>) null);
    }

    @Test(expected = NullPointerException.class)
    public void testExistNullPredicate1() {
        CollectionsUtil.exist(null, (Predicate<User>) null);
    }

    @Test
    public void testExistNullIterable() {
        assertEquals(false, CollectionsUtil.<Object>exist(null, BeanPredicateUtil.equalPredicate("name", "关羽")));
    }

    @Test
    public void testExistNotFind() {
        List<User> list = toList(new User("张飞", 23));
        Predicate<User> equalPredicate = BeanPredicateUtil.equalPredicate("name", "关羽");
        assertEquals(false, CollectionsUtil.exist(list, equalPredicate));
    }
}
