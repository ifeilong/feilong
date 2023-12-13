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

import static com.feilong.core.bean.ConvertUtil.toArray;
import static com.feilong.core.bean.ConvertUtil.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.feilong.core.util.CollectionsUtil;
import com.feilong.lib.lang3.tuple.Pair;
import com.feilong.store.member.User;
import com.feilong.store.member.UserInfo;

public class SplitDuplicatePropertyNamesTest{

    @Test
    public void testSplitDuplicate(){
        User user1 = new User(1L, 15);
        User user2 = new User(1L, 16);
        User user3 = new User(1L, 15);
        List<User> list = toList(user1, user2, user3);

        Pair<List<User>, List<User>> splitDuplicate = CollectionsUtil.splitDuplicate(list, "id", "age");

        List<User> newDuplicate = splitDuplicate.getLeft();
        List<User> rightDuplicate = splitDuplicate.getRight();

        assertSame(2, newDuplicate.size());
        assertThat(newDuplicate, contains(user1, user2));

        //---------------------------------------------------------------

        assertSame(1, rightDuplicate.size());
        assertThat(rightDuplicate, contains(user3));
    }

    @Test
    public void testSplitDuplicate1(){
        User user1 = new User(1L);
        user1.setUserInfo(new UserInfo(15));

        User user2 = new User(1L);
        user2.setUserInfo(new UserInfo(16));

        User user3 = new User(1L);
        user3.setUserInfo(new UserInfo(15));

        //---------------------------------------------------------------

        List<User> list = toList(user1, user2, user3);

        Pair<List<User>, List<User>> splitDuplicate = CollectionsUtil.splitDuplicate(list, "id", "userInfo.age");

        List<User> newDuplicate = splitDuplicate.getLeft();
        List<User> rightDuplicate = splitDuplicate.getRight();

        assertSame(2, newDuplicate.size());
        assertThat(newDuplicate, contains(user1, user2));

        //---------------------------------------------------------------

        assertSame(1, rightDuplicate.size());
        assertThat(rightDuplicate, contains(user3));
    }

    //---------------------------------------------------------------

    @Test
    public void testSplitDuplicateNullCollection(){
        assertEquals(null, CollectionsUtil.splitDuplicate(null, (String[]) null));
    }

    @Test
    public void testSplitDuplicateNullCollection12(){
        assertEquals(null, CollectionsUtil.splitDuplicate(null, toArray("")));
    }

    //---------------------------------------------------------------

    @Test
    public void testSplitDuplicateEmptyCollection(){
        assertEquals(null, CollectionsUtil.splitDuplicate(new ArrayList<>(), (String[]) null));
    }

    @Test
    public void testSplitDuplicateEmptyCollection333(){
        assertEquals(null, CollectionsUtil.splitDuplicate(new ArrayList<>(), toArray("")));
    }

    //---------------------------------------------------------------

    @Test
    public void testSplitDuplicateEmptyCollection1(){
        assertEquals(null, CollectionsUtil.splitDuplicate(toList(), (String[]) null));
    }

    @Test
    public void testSplitDuplicateEmptyCollection1333(){
        assertEquals(null, CollectionsUtil.splitDuplicate(toList(), toArray("")));
    }
}
