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
package com.feilong.core.util.grouputil;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import org.junit.Test;

import com.feilong.core.util.GroupUtil;
import com.feilong.store.member.User;

/**
 * {@link GroupUtil#groupByToSet(Iterable, Function, Function, Predicate)} 的单元测试,
 * 使用 {@link User} 和三國人物数据.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 4.5.3
 */
public class GroupByToSetWithPredicateAndKeyExtractorDistinctByFunctionTest{

    // ==================== 测试数据 ====================

    private static final User zhangFei28          = new User("张飞", 28);

    private static final User liuBei32            = new User("刘备", 32);

    private static final User liuBei30            = new User("刘备", 30);

    private static final User guanYu30            = new User("关羽", 30);

    private static final User zhangFei28Duplicate = new User("张飞", 28);

    // ==================== 正常分组去重场景 ====================

    /**
     * 测试按姓名分组, 按年龄去重的基本功能.
     * 输入包含张飞(28), 刘备(32), 刘备(30), 关羽(30), 张飞(28重复).
     * 预期: 张飞组1人, 刘备组2人, 关羽组1人.
     */
    @Test
    public void testNormalGroupByNameDistinctByAge(){
        List<User> users = Arrays.asList(zhangFei28, liuBei32, liuBei30, guanYu30, zhangFei28Duplicate);

        Map<String, Set<User>> result = GroupUtil.groupByToSet(users, User::getName, User::getAge, null);

        assertThat(result.size(), is(3));
        assertThat(result, hasKey("张飞"));
        assertThat(result, hasKey("刘备"));
        assertThat(result, hasKey("关羽"));

        assertThat(result.get("张飞"), hasSize(1));
        assertThat(result.get("张飞"), hasItem(zhangFei28));

        assertThat(result.get("刘备"), hasSize(2));
        assertThat(result.get("刘备"), hasItems(liuBei32, liuBei30));

        assertThat(result.get("关羽"), hasSize(1));
        assertThat(result.get("关羽"), hasItem(guanYu30));
    }

    /**
     * 测试按年龄分组, 按姓名去重的功能.
     * 
     * 预期:
     * 年龄28组1人(张飞),
     * 年龄32组1人(刘备),
     * 年龄30组2人(刘备和关羽).
     */
    @Test
    public void testGroupByAgeDistinctByName(){
        List<User> users = Arrays.asList(zhangFei28, liuBei32, liuBei30, guanYu30, zhangFei28Duplicate);

        Map<Integer, Set<User>> result = GroupUtil.groupByToSet(users, User::getAge, User::getName, null);

        assertThat(result.size(), is(3));
        assertThat(result.get(28), hasSize(1));
        assertThat(result.get(28), hasItem(zhangFei28));

        assertThat(result.get(32), hasSize(1));
        assertThat(result.get(32), hasItem(liuBei32));

        assertThat(result.get(30), hasSize(2));
        assertThat(result.get(30), hasItems(liuBei30, guanYu30));
    }

    // ==================== 空集合 / null 输入 ====================

    @Test
    public void testEmptyInputReturnsEmptyMap(){
        Map<String, Set<User>> result = GroupUtil.groupByToSet(Collections.emptyList(), User::getName, User::getAge, null);
        assertThat(result.entrySet(), empty());
    }

    @Test
    public void testNullInputReturnsEmptyMap(){
        Map<String, Set<User>> result = GroupUtil.groupByToSet(null, User::getName, User::getAge, null);
        assertThat(result.entrySet(), empty());
    }

    // ==================== 参数校验 ====================

    @Test(expected = NullPointerException.class)
    public void testNullKeyExtractorThrowsNpe(){
        GroupUtil.groupByToSet(Collections.singletonList(zhangFei28), null, User::getAge, null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullDistinctByFunctionThrowsNpe(){
        GroupUtil.groupByToSet(Collections.singletonList(zhangFei28), User::getName, null, null);
    }

    // ==================== 过滤场景 ====================

    /**
     * 测试 includePredicate 过滤功能: 只保留年龄 >= 30 的用户.
     */
    @Test
    public void testIncludePredicateFiltersElements(){
        List<User> users = Arrays.asList(zhangFei28, liuBei32, liuBei30, guanYu30);
        Predicate<User> filter = u -> u.getAge() != null && u.getAge() >= 30;

        Map<String, Set<User>> result = GroupUtil.groupByToSet(users, User::getName, User::getAge, filter);

        assertThat(result, not(hasKey("张飞")));
        assertThat(result.get("刘备"), hasSize(2));
        assertThat(result.get("关羽"), hasSize(1));
    }

    @Test
    public void testIncludePredicateAllFiltered(){
        List<User> users = Arrays.asList(zhangFei28, liuBei32);
        Predicate<User> filter = u -> false;

        Map<String, Set<User>> result = GroupUtil.groupByToSet(users, User::getName, User::getAge, filter);

        assertThat(result.entrySet(), empty());
    }

    // ==================== 去重保留第一个 ====================

    @Test
    public void testDistinctPreservesFirstOccurrence(){
        User first = new User("张飞", 28);
        User second = new User("张飞", 28);
        User third = new User("张飞", 28);

        Map<String, Set<User>> result = GroupUtil.groupByToSet(Arrays.asList(first, second, third), User::getName, User::getAge, null);

        assertThat(result.get("张飞"), hasSize(1));
        assertThat(result.get("张飞"), hasItem(first));
    }

    // ==================== 顺序保持 ====================

    @Test
    public void testResultMaintainsInsertionOrder(){
        List<User> users = Arrays.asList(new User("赵云", 25), new User("马超", 27), new User("黄忠", 45), new User("赵云", 30));

        Map<String, Set<User>> result = GroupUtil.groupByToSet(users, User::getName, User::getAge, null);

        Set<User> zhaoYunGroup = result.get("赵云");
        Iterator<User> it = zhaoYunGroup.iterator();
        assertThat(it.next().getAge(), is(25));
        assertThat(it.next().getAge(), is(30));
    }

    // ==================== null key 处理 ====================

    @Test
    public void testNullGroupKey(){
        User noNameUser = new User((String) null, 20);
        List<User> users = Arrays.asList(zhangFei28, noNameUser);

        Map<String, Set<User>> result = GroupUtil.groupByToSet(users, User::getName, User::getAge, null);

        assertThat(result, hasKey(nullValue()));
        assertThat(result.get(null), hasSize(1));
        assertThat(result.get(null), hasItem(noNameUser));
    }

    @Test
    public void testNullDistinctKey(){
        User user1 = new User("曹操", null);
        User user2 = new User("曹操", null);

        Map<String, Set<User>> result = GroupUtil.groupByToSet(Arrays.asList(user1, user2), User::getName, User::getAge, null);

        assertThat(result.get("曹操"), hasSize(1));
        assertThat(result.get("曹操"), hasItem(user1));
    }

    // ==================== 单元素场景 ====================

    @Test
    public void testSingleElement(){
        Map<String, Set<User>> result = GroupUtil.groupByToSet(Collections.singletonList(zhangFei28), User::getName, User::getAge, null);

        assertThat(result.size(), is(1));
        assertThat(result.get("张飞"), hasSize(1));
        assertThat(result.get("张飞"), hasItem(zhangFei28));
    }

    // ==================== 多分组场景 ====================

    @Test
    public void testMultipleGroupsNoDuplicates(){
        List<User> users = Arrays.asList(new User("诸葛亮", 24), new User("周瑜", 22), new User("庞统", 18));

        Map<String, Set<User>> result = GroupUtil.groupByToSet(users, User::getName, User::getAge, null);

        assertThat(result.size(), is(3));
        assertThat(result, hasKey("诸葛亮"));
        assertThat(result, hasKey("周瑜"));
        assertThat(result, hasKey("庞统"));
    }
}
