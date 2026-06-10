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

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.Test;

import com.feilong.core.util.GroupUtil;

public class GroupByToSetWithPredicateAndKeyExtractorTest{
    // ==================== 测试数据 ====================

    private static final SimpleUser zhangFei28          = new SimpleUser("张飞", 28);

    private static final SimpleUser liuBei32            = new SimpleUser("刘备", 32);

    private static final SimpleUser liuBei30            = new SimpleUser("刘备", 30);

    private static final SimpleUser guanYu35            = new SimpleUser("关羽", 35);

    private static final SimpleUser zhangFei28Duplicate = new SimpleUser("张飞", 28); // 与zhangFei28完全相同的对象

    // ==================== 正常分组去重场景 ====================

    /**
     * 测试按姓名分组, 基于 User 的 equals/hashCode 去重.
     * 输入包含张飞(28), 刘备(32), 刘备(30), 关羽(35), 张飞(28重复).
     * 预期: 张飞组1人, 刘备组2人, 关羽组1人.
     */
    @Test
    public void testGroupByNameWithDeduplication(){
        List<SimpleUser> users = Arrays.asList(zhangFei28, liuBei32, liuBei30, guanYu35, zhangFei28Duplicate);

        Map<String, Set<SimpleUser>> result = GroupUtil.groupByToSet(users, SimpleUser::getName, null);

        assertThat(result.size(), is(3));
        assertThat(result, hasKey("张飞"));
        assertThat(result, hasKey("刘备"));
        assertThat(result, hasKey("关羽"));

        assertThat(result.get("张飞"), hasSize(1));
        assertThat(result.get("张飞"), hasItem(zhangFei28));

        assertThat(result.get("刘备"), hasSize(2));
        assertThat(result.get("刘备"), hasItems(liuBei32, liuBei30));

        assertThat(result.get("关羽"), hasSize(1));
        assertThat(result.get("关羽"), hasItem(guanYu35));
    }

    // ==================== 过滤场景 ====================

    /**
     * 测试 includePredicate 过滤功能: 只保留年龄 >= 30 的用户.
     * 输入包含张飞(28), 刘备(32), 刘备(30), 关羽(35).
     * 预期: 张飞被过滤, 刘备组2人, 关羽组1人.
     */
    @Test
    public void testIncludePredicateFiltersElements(){
        List<SimpleUser> users = Arrays.asList(zhangFei28, liuBei32, liuBei30, guanYu35);
        Predicate<SimpleUser> filter = u -> u.getAge() != null && u.getAge() >= 30;

        Map<String, Set<SimpleUser>> result = GroupUtil.groupByToSet(users, SimpleUser::getName, filter);

        assertThat(result, not(hasKey("张飞")));
        assertThat(result.get("刘备"), hasSize(2));
        assertThat(result.get("关羽"), hasSize(1));
    }

    /**
     * 测试 includePredicate 过滤掉所有元素时返回空 Map.
     */
    @Test
    public void testIncludePredicateAllFiltered(){
        List<SimpleUser> users = Arrays.asList(zhangFei28, liuBei32);
        Predicate<SimpleUser> filter = u -> false;

        Map<String, Set<SimpleUser>> result = GroupUtil.groupByToSet(users, SimpleUser::getName, filter);

        assertThat(result.entrySet(), empty());
    }

    // ==================== 空集合 / null 输入 ====================

    /**
     * 测试输入为空集合时返回空 Map.
     */
    @Test
    public void testEmptyInputReturnsEmptyMap(){
        Map<String, Set<SimpleUser>> result = GroupUtil.groupByToSet(Collections.emptyList(), SimpleUser::getName, null);
        assertThat(result.entrySet(), empty());
    }

    /**
     * 测试输入为 null 时返回空 Map.
     */
    @Test
    public void testNullInputReturnsEmptyMap(){
        Map<String, Set<SimpleUser>> result = GroupUtil.groupByToSet(null, SimpleUser::getName, null);
        assertThat(result.entrySet(), empty());
    }

    // ==================== 参数校验 ====================

    /**
     * 测试 keyExtractor 为 null 时抛出 NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void testNullKeyExtractorThrowsNpe(){
        GroupUtil.groupByToSet(Collections.singletonList(zhangFei28), null, null);
    }

    // ==================== 去重保留第一个 ====================

    /**
     * 测试当多个元素 equals 相同时, 只保留第一个出现的元素.
     * 输入包含三个完全相同的张飞(28)对象.
     * 预期: 张飞组只有1个元素, 且是第一个对象.
     */
    @Test
    public void testDistinctPreservesFirstOccurrence(){
        SimpleUser first = new SimpleUser("张飞", 28);
        SimpleUser second = new SimpleUser("张飞", 28);
        SimpleUser third = new SimpleUser("张飞", 28);

        Map<String, Set<SimpleUser>> result = GroupUtil.groupByToSet(Arrays.asList(first, second, third), SimpleUser::getName, null);

        assertThat(result.get("张飞"), hasSize(1));
        assertThat(result.get("张飞"), hasItem(first));
    }

    // ==================== 顺序保持 ====================

    /**
     * 测试分组内的 Set 保持元素首次出现的顺序 (插入顺序).
     * 输入包含赵云(25), 马超(27), 黄忠(45), 赵云(30).
     * 预期: 赵云组按插入顺序为赵云25, 赵云30.
     */
    @Test
    public void testResultMaintainsInsertionOrder(){
        List<SimpleUser> users = Arrays.asList(new SimpleUser("赵云", 25), new SimpleUser("马超", 27), new SimpleUser("黄忠", 45), new SimpleUser("赵云", 30));

        Map<String, Set<SimpleUser>> result = GroupUtil.groupByToSet(users, SimpleUser::getName, null);

        Set<SimpleUser> zhaoYunGroup = result.get("赵云");
        Iterator<SimpleUser> it = zhaoYunGroup.iterator();
        assertThat(it.next().getAge(), is(25));
        assertThat(it.next().getAge(), is(30));
    }

    // ==================== null group key 处理 ====================

    /**
     * 测试分组 key 为 null 时, 能正常将元素放入 null key 对应的组.
     * 输入包含张飞(28)和一个姓名为 null 的用户(20).
     * 预期: 存在 key 为 null 的分组, 包含该用户.
     */
    @Test
    public void testNullGroupKey(){
        SimpleUser noNameUser = new SimpleUser((String) null, 20);
        List<SimpleUser> users = Arrays.asList(zhangFei28, noNameUser);

        Map<String, Set<SimpleUser>> result = GroupUtil.groupByToSet(users, SimpleUser::getName, null);

        assertThat(result, hasKey(nullValue()));
        assertThat(result.get(null), hasSize(1));
        assertThat(result.get(null), hasItem(noNameUser));
    }

    // ==================== 单元素场景 ====================

    /**
     * 测试只有一个元素时, 正确分组并返回包含该元素的 Set.
     */
    @Test
    public void testSingleElement(){
        Map<String, Set<SimpleUser>> result = GroupUtil.groupByToSet(Collections.singletonList(zhangFei28), SimpleUser::getName, null);

        assertThat(result.size(), is(1));
        assertThat(result.get("张飞"), hasSize(1));
        assertThat(result.get("张飞"), hasItem(zhangFei28));
    }

    // ==================== 多分组场景 ====================

    /**
     * 测试多个不同分组, 且每个分组内无重复元素的情况.
     * 输入包含诸葛亮(24), 周瑜(22), 庞统(18).
     * 预期: 三个分组各一人.
     */
    @Test
    public void testMultipleGroupsNoDuplicates(){
        List<SimpleUser> users = Arrays.asList(new SimpleUser("诸葛亮", 24), new SimpleUser("周瑜", 22), new SimpleUser("庞统", 18));

        Map<String, Set<SimpleUser>> result = GroupUtil.groupByToSet(users, SimpleUser::getName, null);

        assertThat(result.size(), is(3));
        assertThat(result, hasKey("诸葛亮"));
        assertThat(result, hasKey("周瑜"));
        assertThat(result, hasKey("庞统"));
    }
}
