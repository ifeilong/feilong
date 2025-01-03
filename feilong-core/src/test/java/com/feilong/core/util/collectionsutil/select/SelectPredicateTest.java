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
import static com.feilong.core.util.CollectionsUtil.select;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.Predicate;
import org.junit.Test;

import com.feilong.core.util.CollectionsUtil;
import com.feilong.core.util.equator.IgnoreCaseEquator;
import com.feilong.core.util.predicate.BeanPredicate;
import com.feilong.core.util.predicate.BeanPredicateUtil;
import com.feilong.lib.collection4.ComparatorUtils;
import com.feilong.lib.collection4.functors.ComparatorPredicate;
import com.feilong.lib.collection4.functors.ComparatorPredicate.Criterion;
import com.feilong.lib.collection4.functors.EqualPredicate;
import com.feilong.store.member.User;

public class SelectPredicateTest{

    @Test
    public void testSelectPredicateTest(){
        User zhangfei1 = new User("zhangfei", 22);
        User zhangfei2 = new User("zhangFei", 22);
        User zhangfei3 = new User("Zhangfei", 22);
        User zhangfeinull = new User((String) null, 22);
        User guanyu = new User("guanyu", 25);
        User liubei = new User("liubei", 30);

        List<User> list = toList(zhangfei1, zhangfei2, zhangfei3, zhangfeinull, guanyu, liubei);

        List<User> select = select(
                        list,
                        new BeanPredicate<User>(
                                        "name", //
                                        EqualPredicate.equalPredicate("zhangfei", IgnoreCaseEquator.INSTANCE)));

        assertThat(
                        select,
                        allOf(//
                                        hasItem(zhangfei1),
                                        hasItem(zhangfei2),
                                        hasItem(zhangfei3),

                                        not(hasItem(zhangfeinull)),
                                        not(hasItem(guanyu)),
                                        not(hasItem(liubei))
                        //
                        ));
    }

    @Test
    public void testSelectPredicateTest1(){
        User zhangfei1 = new User("zhangfei", 22);
        User zhangfei2 = new User("zhangFei", 22);
        User zhangfei3 = new User("Zhangfei", 22);
        User zhangfeinull = new User((String) null, 22);
        User guanyu = new User("guanyu", 25);
        User liubei = new User("liubei", 30);

        List<User> list = toList(zhangfei1, zhangfei2, zhangfei3, zhangfeinull, guanyu, liubei);

        List<User> select = select(list, BeanPredicateUtil.<User> equalIgnoreCasePredicate("name", "zhangfei"));

        assertThat(
                        select,
                        allOf(//
                                        hasItem(zhangfei1),
                                        hasItem(zhangfei2),
                                        hasItem(zhangfei3),

                                        not(hasItem(zhangfeinull)),
                                        not(hasItem(guanyu)),
                                        not(hasItem(liubei))
                        //
                        ));
    }

    @Test
    public void testSelectPredicate(){
        //查询 >10 的元素
        Predicate<Integer> predicate = new ComparatorPredicate<Integer>(10, ComparatorUtils.<Integer> naturalComparator(), Criterion.LESS);

        List<Integer> result = CollectionsUtil.select(toList(1, 5, 10, 30, 55, 88, 1, 12, 3), predicate);
        assertThat(result, contains(30, 55, 88, 12));
    }

    @Test
    public void testSelectPredicateEqualPredicate(){
        List<Long> list = toList(1L, 1L, 2L, 3L);
        assertThat(CollectionsUtil.select(list, new EqualPredicate<Long>(1L)), contains(1L, 1L));
    }

    @Test
    public void testSelectPredicateNullCollection(){
        assertEquals(emptyList(), CollectionsUtil.select(null, new EqualPredicate<Long>(1L)));
    }

    @Test
    public void testSelectPredicateEmptyCollection(){
        assertEquals(emptyList(), CollectionsUtil.select(new ArrayList<Long>(), new EqualPredicate<Long>(1L)));
    }

    @Test
    public void testSelectNotFindPredicate(){
        assertEquals(emptyList(), CollectionsUtil.select(toList(5L, 8L), new EqualPredicate<Long>(1L)));
    }
}
