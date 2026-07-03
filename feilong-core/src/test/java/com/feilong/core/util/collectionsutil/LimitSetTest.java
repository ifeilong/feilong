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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Test;

import com.feilong.core.util.CollectionsUtil;

public class LimitSetTest{

    /**
     * 测试 maxSize 小于集合大小时, 返回包含前 maxSize 个元素的新集合.
     */
    @Test
    public void testLimitLessThanSize(){
        Set<String> set = new LinkedHashSet<>(Arrays.asList("a", "b", "c", "d", "e"));
        Set<String> result = CollectionsUtil.limit(set, 3);
        assertThat(result.size(), is(3));
        // 由于 Collectors.toSet() 返回 HashSet, 不保证顺序, 只验证元素存在
        assertThat(result, hasItems("a", "b", "c"));
    }

    /**
     * 测试 maxSize 等于集合大小时, 返回全部元素.
     */
    @Test
    public void testLimitEqualToSize(){
        Set<String> set = new LinkedHashSet<>(Arrays.asList("a", "b", "c"));
        Set<String> result = CollectionsUtil.limit(set, 3);
        assertThat(result.size(), is(3));
        assertThat(result, hasItems("a", "b", "c"));
    }

    /**
     * 测试 maxSize 大于集合大小时, 返回全部元素.
     */
    @Test
    public void testLimitGreaterThanSize(){
        Set<String> set = new LinkedHashSet<>(Arrays.asList("a", "b"));
        Set<String> result = CollectionsUtil.limit(set, 10);
        assertThat(result.size(), is(2));
        assertThat(result, hasItems("a", "b"));
    }

    /**
     * 测试 maxSize 为 0 时, 返回 {@link Collections#emptySet()}.
     */
    @Test
    public void testLimitWithMaxSizeZero(){
        Set<String> set = new LinkedHashSet<>(Arrays.asList("a", "b", "c"));
        Set<String> result = CollectionsUtil.limit(set, 0);
        assertThat(result, empty());
        assertSame(result, Collections.emptySet());
    }

    /**
     * 测试输入为空集合时, 返回原集合实例.
     */
    @Test
    public void testLimitWithEmptySet(){
        Set<String> set = Collections.emptySet();
        Set<String> result = CollectionsUtil.limit(set, 5);
        assertThat(result, empty());
        assertSame(result, set);
    }

    /**
     * 测试输入为 null 时, 返回 null.
     */
    @Test
    public void testLimitWithNullSet(){
        Set<String> result = CollectionsUtil.limit((Set<String>) null, 5);
        assertNull(result);
    }

    /**
     * 测试 maxSize 为负数时, 抛出 {@link IllegalArgumentException}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLimitWithMaxSizeNegative(){
        Set<String> set = new LinkedHashSet<>(Arrays.asList("a"));
        CollectionsUtil.limit(set, -1);
    }

    /**
     * 测试返回的集合是新对象, 且原集合未被修改.
     */
    @Test
    public void testReturnNewSet(){
        Set<String> original = new LinkedHashSet<>(Arrays.asList("a", "b", "c", "d"));
        Set<String> result = CollectionsUtil.limit(original, 2);
        assertThat(result, not(sameInstance(original)));
        assertThat(original.size(), is(4));
    }

    /**
     * 测试泛型为 Integer 时的行为.
     */
    @Test
    public void testLimitWithInteger(){
        Set<Integer> set = new LinkedHashSet<>(Arrays.asList(1, 2, 3, 4, 5));
        Set<Integer> result = CollectionsUtil.limit(set, 2);
        assertThat(result.size(), is(2));
        assertThat(result, hasItems(1, 2));
    }

    /**
     * 测试泛型为自定义对象时的行为.
     */
    @Test
    public void testLimitWithCustomObject(){
        Set<Object> set = new LinkedHashSet<>(Arrays.asList(new Object(), new Object(), new Object()));
        Set<Object> result = CollectionsUtil.limit(set, 1);
        assertThat(result.size(), is(1));
    }

    /**
     * 测试空集合且 maxSize 为 0 时, 先走 isNullOrEmpty 分支, 返回原集合.
     */
    @Test
    public void testLimitWithMaxSizeZeroOnEmptySet(){
        Set<String> set = Collections.emptySet();
        Set<String> result = CollectionsUtil.limit(set, 0);
        assertThat(result, empty());
        assertSame(result, set);
    }

    /**
     * 测试 null 且 maxSize 为 0 时, 返回 null.
     */
    @Test
    public void testLimitWithMaxSizeZeroOnNullSet(){
        Set<String> result = CollectionsUtil.limit((Set<String>) null, 0);
        assertNull(result);
    }

}
