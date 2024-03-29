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
package com.feilong.core.util.aggregateutil;

import static com.feilong.core.bean.ConvertUtil.toBigDecimal;
import static com.feilong.core.bean.ConvertUtil.toList;
import static java.util.Collections.emptyMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.feilong.core.util.AggregateUtil;
import com.feilong.store.member.User;

public class GroupSumTest{

    /**
     * Test group count1.
     */
    @Test
    public void testGroupSum1(){
        List<User> list = toList(//
                        new User("张飞", 20),
                        new User("关羽", 20),
                        new User("刘备", 20),
                        new User("刘备", 20));

        Map<String, BigDecimal> map = AggregateUtil.groupSum(list, "name", "age");
        assertThat(
                        map,
                        allOf(//
                                        hasEntry("刘备", toBigDecimal(40)),
                                        hasEntry("张飞", toBigDecimal(20)),
                                        hasEntry("关羽", toBigDecimal(20))));
    }

    @Test
    public void testGroupSum12(){
        List<UserWithStringAge> list = toList(//
                        new UserWithStringAge("张飞", "20"),
                        new UserWithStringAge("关羽", "20"),
                        new UserWithStringAge("刘备", "20"),
                        new UserWithStringAge("刘备", ""),
                        new UserWithStringAge("刘备", "20"));

        Map<String, BigDecimal> map = AggregateUtil.groupSum(list, "name", "ageString");
        assertThat(
                        map,
                        allOf(//
                                        hasEntry("刘备", toBigDecimal(40)),
                                        hasEntry("张飞", toBigDecimal(20)),
                                        hasEntry("关羽", toBigDecimal(20))));
    }

    //---------------------------------------------------------------

    /**
     * Test group count null collection.
     */
    @Test
    public void testGroupSumNullCollection(){
        assertEquals(emptyMap(), AggregateUtil.groupSum(null, "name", "age"));
    }

    /**
     * Test group count empty collection.
     */
    @Test
    public void testGroupSumEmptyCollection(){
        assertEquals(emptyMap(), AggregateUtil.groupSum(toList(), "name", "age"));
    }

    //---------------------------------------------------------------

    /**
     * Test group count null property name.
     */
    @Test(expected = NullPointerException.class)
    public void testGroupSumNullPropertyName(){
        AggregateUtil.groupSum(getTestUserList(), (String) null, "age");
    }

    /**
     * Test group count blank property name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGroupSumBlankPropertyName(){
        AggregateUtil.groupSum(getTestUserList(), "   ", "age");
    }

    /**
     * Test group count empty property name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGroupSumEmptyPropertyName(){
        AggregateUtil.groupSum(getTestUserList(), "", "age");
    }

    //---------------------------------------------------------------
    @Test(expected = NullPointerException.class)
    public void testGroupSumNullSumPropertyName(){
        AggregateUtil.groupSum(getTestUserList(), "name", (String) null);
    }

    /**
     * Test group count blank property name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGroupSumBlankSumPropertyName(){
        AggregateUtil.groupSum(getTestUserList(), "name", "   ");
    }

    /**
     * Test group count empty property name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGroupSumEmptySumPropertyName(){
        AggregateUtil.groupSum(getTestUserList(), "name", "");
    }

    //---------------------------------------------------------------

    private static List<User> getTestUserList(){
        User user1 = new User(2L);
        user1.setAge(18);
        return toList(user1);
    }
}
