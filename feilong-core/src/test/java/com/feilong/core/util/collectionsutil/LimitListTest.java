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
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.feilong.core.util.CollectionsUtil;

public class LimitListTest{

    /**
     * 测试 maxSize 小于列表长度时, 返回前 maxSize 个元素.
     */
    @Test
    public void testLimitLessThanSize(){
        List<String> list = Arrays.asList("a", "b", "c", "d", "e");
        List<String> result = CollectionsUtil.limit(list, 3);
        assertThat(result, contains("a", "b", "c"));
        assertThat(result.size(), is(3));
    }

    /**
     * 测试 maxSize 等于列表长度时, 返回全部元素.
     */
    @Test
    public void testLimitEqualToSize(){
        List<String> list = Arrays.asList("a", "b", "c");
        List<String> result = CollectionsUtil.limit(list, 3);
        assertThat(result, contains("a", "b", "c"));
        assertThat(result.size(), is(3));
    }

    /**
     * 测试 maxSize 大于列表长度时, 返回全部元素.
     */
    @Test
    public void testLimitGreaterThanSize(){
        List<String> list = Arrays.asList("a", "b");
        List<String> result = CollectionsUtil.limit(list, 10);
        assertThat(result, contains("a", "b"));
        assertThat(result.size(), is(2));
    }

    /**
     * 测试 maxSize 为 0 时, 返回 {@link Collections#emptyList()}.
     */
    @Test
    public void testLimitWithMaxSizeZero(){
        List<String> list = Arrays.asList("a", "b", "c");
        List<String> result = CollectionsUtil.limit(list, 0);
        assertThat(result, empty());
        assertSame(result, Collections.emptyList());
    }

    /**
     * 测试输入为空列表时, 返回原列表实例.
     */
    @Test
    public void testLimitWithEmptyList(){
        List<String> list = Collections.emptyList();
        List<String> result = CollectionsUtil.limit(list, 5);
        assertThat(result, empty());
        assertSame(result, list);
    }

    /**
     * 测试输入为 null 时, 返回 null.
     */
    @Test
    public void testLimitWithNullList(){
        List<String> result = CollectionsUtil.limit((List<String>) null, 5);
        assertNull(result);
    }

    /**
     * 测试 maxSize 为负数时, 抛出 {@link IllegalArgumentException}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLimitWithMaxSizeNegative(){
        CollectionsUtil.limit(Arrays.asList("a"), -1);
    }

    /**
     * 测试返回的列表是新对象, 且原列表未被修改.
     */
    @Test
    public void testReturnNewList(){
        List<String> original = new java.util.ArrayList<>(Arrays.asList("a", "b", "c", "d"));
        List<String> result = CollectionsUtil.limit(original, 2);
        assertThat(result, not(sameInstance(original)));
        assertThat(original.size(), is(4));
    }

    /**
     * 测试泛型为 Integer 时的行为.
     */
    @Test
    public void testLimitWithInteger(){
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> result = CollectionsUtil.limit(list, 2);
        assertThat(result, contains(1, 2));
    }

    /**
     * 测试泛型为自定义对象时的行为.
     */
    @Test
    public void testLimitWithCustomObject(){
        List<Object> list = Arrays.asList(new Object(), new Object(), new Object());
        List<Object> result = CollectionsUtil.limit(list, 1);
        assertThat(result.size(), is(1));
    }

    /**
     * 测试空列表且 maxSize 为 0 时, 先走 isNullOrEmpty 分支, 返回原列表.
     */
    @Test
    public void testLimitWithMaxSizeZeroOnEmptyList(){
        List<String> list = Collections.emptyList();
        List<String> result = CollectionsUtil.limit(list, 0);
        assertThat(result, empty());
        assertSame(result, list);
    }

    /**
     * 测试 null 且 maxSize 为 0 时, 返回 null.
     */
    @Test
    public void testLimitWithMaxSizeZeroOnNullList(){
        List<String> result = CollectionsUtil.limit((List<String>) null, 0);
        assertNull(result);
    }
}
