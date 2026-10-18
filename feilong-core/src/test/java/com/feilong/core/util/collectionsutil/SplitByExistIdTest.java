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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.feilong.core.util.CollectionsUtil;
import com.feilong.lib.lang3.tuple.Pair;
import com.feilong.store.member.User;

/**
 * {@link CollectionsUtil#splitByExistId(java.util.Collection, java.util.function.Function, java.util.Collection)} 的单元测试.
 *
 * <p>
 * 覆盖: 正常拆分 / 保持原顺序 / 全部存在 / 全部不存在 / idExtractor 为null 抛异常 / existIdCollection 为null或者empty /
 * objectCollection 为null或者empty / 原集合不变
 * </p>
 */
public class SplitByExistIdTest{

    @Test
    public void testSplitByExistId(){
        User user1 = new User(1L);
        User user2 = new User(2L);
        User user3 = new User(5L);
        List<User> list = toList(user1, user2, user3);

        Pair<List<User>, List<User>> pair = CollectionsUtil.splitByExistId(list, User::getId, toList(1L, 5L));

        //已经存在的, 参照原集合顺序
        assertThat(pair.getLeft(), contains(user1, user3));
        assertSame(2, pair.getLeft().size());

        //不存在的
        assertThat(pair.getRight(), contains(user2));
        assertSame(1, pair.getRight().size());
    }

    @Test
    public void testSplitByExistIdKeepOriginOrder(){
        User user1 = new User(3L);
        User user2 = new User(1L);
        User user3 = new User(2L);
        List<User> list = toList(user1, user2, user3);

        Pair<List<User>, List<User>> pair = CollectionsUtil.splitByExistId(list, User::getId, toList(2L, 3L));

        //左边按照原集合顺序: 3 在 2 前面
        assertThat(pair.getLeft(), contains(user1, user3));
        assertThat(pair.getRight(), contains(user2));
    }

    @Test
    public void testSplitByExistIdAllExist(){
        User user1 = new User(1L);
        User user2 = new User(2L);
        List<User> list = toList(user1, user2);

        Pair<List<User>, List<User>> pair = CollectionsUtil.splitByExistId(list, User::getId, toList(1L, 2L, 3L));

        assertThat(pair.getLeft(), contains(user1, user2));
        assertThat(pair.getRight(), empty());
    }

    @Test
    public void testSplitByExistIdAllNotExist(){
        User user1 = new User(1L);
        User user2 = new User(2L);
        List<User> list = toList(user1, user2);

        Pair<List<User>, List<User>> pair = CollectionsUtil.splitByExistId(list, User::getId, toList(3L, 4L));

        assertThat(pair.getLeft(), empty());
        assertThat(pair.getRight(), contains(user1, user2));
    }

    @Test(expected = NullPointerException.class)
    public void testSplitByExistIdIdExtractorIsNull(){
        //idExtractor 不能为null
        CollectionsUtil.<Long, Long> splitByExistId(toList(1L, 2L, 5L), null, toList(1L, 5L));
    }

    @Test(expected = NullPointerException.class)
    public void testSplitByExistIdIdExtractorIsNullAndObjectCollectionIsNull(){
        //先校验 idExtractor, 再走 isEmpty 的短路返回
        CollectionsUtil.<User, Long> splitByExistId(null, null, toList(1L));
    }

    //---------------------------------------------------------------
    //existIdCollection 是 null / empty, 全部当作不存在
    //---------------------------------------------------------------

    @Test
    public void testSplitByExistIdExistIdCollectionIsNull(){
        User user1 = new User(1L);
        User user2 = new User(2L);
        List<User> list = toList(user1, user2);

        Pair<List<User>, List<User>> pair = CollectionsUtil.splitByExistId(list, User::getId, (List<Long>) null);

        assertThat(pair.getLeft(), empty());
        assertThat(pair.getRight(), contains(user1, user2));
    }

    @Test
    public void testSplitByExistIdExistIdCollectionIsEmpty(){
        User user1 = new User(1L);
        User user2 = new User(2L);
        List<User> list = toList(user1, user2);

        Pair<List<User>, List<User>> pair = CollectionsUtil.splitByExistId(list, User::getId, new ArrayList<Long>());

        assertThat(pair.getLeft(), empty());
        assertThat(pair.getRight(), contains(user1, user2));

        //返回的必须是新的 list, 不能把原集合直接返回(否则调用方修改 right 会改到自己的入参)
        assertNotSame(list, pair.getRight());
    }

    //---------------------------------------------------------------
    //objectCollection 是 null / empty
    //---------------------------------------------------------------

    @Test
    public void testSplitByExistIdObjectCollectionIsNull(){
        Pair<List<User>, List<User>> pair = CollectionsUtil.<User, Long> splitByExistId(null, User::getId, toList(1L));

        assertThat(pair.getLeft(), empty());
        assertThat(pair.getRight(), empty());
    }

    @Test
    public void testSplitByExistIdObjectCollectionIsEmpty(){
        Pair<List<User>, List<User>> pair = CollectionsUtil.splitByExistId(new ArrayList<User>(), User::getId, toList(1L));

        assertThat(pair.getLeft(), empty());
        assertThat(pair.getRight(), empty());
    }

    //---------------------------------------------------------------

    @Test
    public void testSplitByExistIdOriginCollectionNotChange(){
        User user1 = new User(1L);
        User user2 = new User(2L);
        List<User> list = toList(user1, user2);

        Pair<List<User>, List<User>> pair = CollectionsUtil.splitByExistId(list, User::getId, toList(1L));

        //返回的是新的 list, 原集合不变
        assertNotSame(list, pair.getLeft());
        assertNotSame(list, pair.getRight());
        assertThat(list, contains(user1, user2));
        assertSame(2, list.size());

        //返回的 list 可以修改
        pair.getLeft().add(user2);
        assertSame(2, pair.getLeft().size());
        assertSame(1, pair.getRight().size());
    }
}
