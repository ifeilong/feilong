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
package com.feilong.core.util.collectionsutil.remove;

import static com.feilong.core.bean.ConvertUtil.toArray;
import static com.feilong.core.bean.ConvertUtil.toList;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.feilong.core.util.CollectionsUtil;
import com.feilong.store.member.User;
import com.feilong.store.member.UserInfo;

/**
 * {@link CollectionsUtil#removeDuplicate(java.util.Collection, String...)} 的单元测试.
 *
 * <h3>覆盖的分支:</h3>
 * <blockquote>
 * <ol>
 * <li><code>propertyNames</code> 是null或者empty,委托给 {@link CollectionsUtil#removeDuplicate(java.util.Collection)}</li>
 * <li><code>objectCollection</code> 是null或者empty,返回 {@link java.util.Collections#emptyList()}</li>
 * <li><code>propertyNames</code> 只有1个元素,委托给 {@link CollectionsUtil#removeDuplicate(java.util.Collection, String)}<br>
 * (注意:{@code toArray("")} 是长度为1的数组,不是empty)</li>
 * <li>多属性组合去重:保留第一次出现的元素 / 顺序和原集合一致 / 属性值是null / 属性名顺序不影响结果 / 3个属性</li>
 * <li>原集合不变,返回的是新的、可以修改的 list</li>
 * <li>属性名是blank 抛 {@link IllegalArgumentException};集合里面有null元素 抛 {@link NullPointerException}</li>
 * </ol>
 * </blockquote>
 */
public class RemoveDuplicatePropertyNamesTest{

    @Test
    public void testRemoveDuplicate(){
        User user1 = new User(1L, 15);
        User user2 = new User(1L, 16);
        User user3 = new User(1L, 15);
        List<User> list = toList(user1, user2, user3);

        List<User> removeDuplicate = CollectionsUtil.removeDuplicate(list, "id", "age");

        assertThat(removeDuplicate, contains(user1, user2));
        assertSame(2, removeDuplicate.size());
    }

    @Test
    public void testRemoveDuplicate1(){
        User user1 = new User(1L);
        user1.setUserInfo(new UserInfo(15));

        User user2 = new User(1L);
        user2.setUserInfo(new UserInfo(16));

        User user3 = new User(1L);
        user3.setUserInfo(new UserInfo(15));

        List<User> list = toList(user1, user2, user3);

        List<User> removeDuplicate = CollectionsUtil.removeDuplicate(list, "id", "userInfo.age");

        assertThat(removeDuplicate, contains(user1, user2));
        assertSame(2, removeDuplicate.size());
    }

    //---------------------------------------------------------------

    @Test
    public void testRemoveDuplicateNullCollection(){
        assertEquals(emptyList(), CollectionsUtil.removeDuplicate(null, (String[]) null));
    }

    @Test
    public void testRemoveDuplicateNullCollection12(){
        assertEquals(emptyList(), CollectionsUtil.removeDuplicate(null, toArray("")));
    }

    //---------------------------------------------------------------

    @Test
    public void testRemoveDuplicateEmptyCollection(){
        assertEquals(emptyList(), CollectionsUtil.removeDuplicate(new ArrayList<>(), (String[]) null));
    }

    @Test
    public void testRemoveDuplicateEmptyCollection333(){
        assertEquals(emptyList(), CollectionsUtil.removeDuplicate(new ArrayList<>(), toArray("")));
    }

    //---------------------------------------------------------------

    @Test
    public void testRemoveDuplicateEmptyCollection1(){
        assertEquals(emptyList(), CollectionsUtil.removeDuplicate(toList(), (String[]) null));
    }

    @Test
    public void testRemoveDuplicateEmptyCollection1333(){
        assertEquals(emptyList(), CollectionsUtil.removeDuplicate(toList(), toArray("")));
    }

    //---------------------------------------------------------------
    //propertyNames 为 null/empty (非空集合), 委托给 removeDuplicate(Collection)
    //---------------------------------------------------------------

    @Test
    public void testRemoveDuplicatePropertyNamesIsNullAndCollectionNotEmpty(){
        List<String> list = toList("feilong1", "feilong2", "feilong2", "feilong3");

        List<String> removeDuplicate = CollectionsUtil.removeDuplicate(list, (String[]) null);

        assertEquals(toList("feilong1", "feilong2", "feilong3"), removeDuplicate);
    }

    //---------------------------------------------------------------
    //propertyNames 只有1个元素
    //---------------------------------------------------------------

    @Test
    public void testRemoveDuplicatePropertyNamesLengthIsOne(){
        Item item1 = new Item(1L, "feilong1", "A");
        Item item2 = new Item(1L, "feilong2", "B");
        Item item3 = new Item(2L, "feilong1", "A");
        List<Item> list = toList(item1, item2, item3);

        //只有1个属性, 内部会走 removeDuplicate(Collection, String)
        List<Item> removeDuplicate = CollectionsUtil.removeDuplicate(list, toArray("id"));

        assertThat(removeDuplicate, contains(item1, item3));
        assertSame(2, removeDuplicate.size());
    }

    @Test
    public void testRemoveDuplicatePropertyNamesLengthIsOneAndBlank(){
        List<String> list = toList("feilong1", "feilong2", "feilong2", "feilong3");

        //只有1个属性, 并且是blank, 会退化成 removeDuplicate(Collection), 按元素自身 equals 去重
        List<String> removeDuplicate = CollectionsUtil.removeDuplicate(list, toArray(""));

        assertEquals(toList("feilong1", "feilong2", "feilong3"), removeDuplicate);
    }

    //---------------------------------------------------------------
    //多属性组合去重
    //---------------------------------------------------------------

    @Test
    public void testRemoveDuplicateNullPropertyValue(){
        Item item1 = new Item(null, "feilong1", "A");
        Item item2 = new Item(null, "feilong1", "A"); //和 item1 重复
        Item item3 = new Item(null, "feilong1", "B");
        Item item4 = new Item(5L, null, "A");
        Item item5 = new Item(null, null, null);
        Item item6 = new Item(null, null, null); //和 item5 重复

        List<Item> list = toList(item1, item2, item3, item4, item5, item6);

        List<Item> removeDuplicate = CollectionsUtil.removeDuplicate(list, "id", "name", "type");

        //属性值是null, 也能正常去重
        assertThat(removeDuplicate, contains(item1, item3, item4, item5));
        assertSame(4, removeDuplicate.size());
    }

    @Test
    public void testRemoveDuplicateKeepOriginOrder(){
        Item item1 = new Item(1L, "feilong1", "A");
        Item item2 = new Item(2L, "feilong2", "B");
        Item item3 = new Item(3L, "feilong3", "C");
        Item item4 = new Item(1L, "feilong1", "A"); //和 item1 重复
        Item item5 = new Item(4L, "feilong4", "D");
        Item item6 = new Item(2L, "feilong2", "B"); //和 item2 重复

        List<Item> list = toList(item1, item2, item3, item4, item5, item6);

        //3个属性组合去重, 保留第一次出现的元素, 并且顺序和原集合一致
        List<Item> removeDuplicate = CollectionsUtil.removeDuplicate(list, "id", "name", "type");

        assertThat(removeDuplicate, contains(item1, item2, item3, item5));
        assertSame(4, removeDuplicate.size());

        //---------------------------------------------------------------

        //属性名的顺序不影响去重结果
        List<Item> removeDuplicate1 = CollectionsUtil.removeDuplicate(list, "type", "name", "id");

        assertThat(removeDuplicate1, contains(item1, item2, item3, item5));
        assertSame(4, removeDuplicate1.size());
    }

    @Test
    public void testRemoveDuplicateOriginCollectionNotChange(){
        Item item1 = new Item(1L, "feilong1", "A");
        Item item2 = new Item(1L, "feilong1", "A"); //和 item1 重复
        Item item3 = new Item(2L, "feilong2", "B");
        List<Item> list = toList(item1, item2, item3);

        List<Item> removeDuplicate = CollectionsUtil.removeDuplicate(list, "id", "name", "type");

        //返回的是新的 list, 原集合不变
        assertNotSame(list, removeDuplicate);
        assertThat(list, contains(item1, item2, item3));
        assertSame(3, list.size());

        //返回的 list 可以修改
        removeDuplicate.add(item1);
        assertSame(3, removeDuplicate.size());
    }

    //---------------------------------------------------------------
    //异常
    //---------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveDuplicateBlankPropertyName(){
        CollectionsUtil.removeDuplicate(toList(new Item(1L, "feilong1", "A")), "id", " ");
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveDuplicateNullElement(){
        CollectionsUtil.removeDuplicate(toList(new Item(1L, "feilong1", "A"), null), "id", "name");
    }

    //---------------------------------------------------------------

    /**
     * 测试用的 bean.
     */
    public static class Item{

        /** The id. */
        private final Long   id;

        /** The name. */
        private final String name;

        /** The type. */
        private final String type;

        /**
         * The constructor.
         *
         * @param id
         *            the id
         * @param name
         *            the name
         * @param type
         *            the type
         */
        public Item(Long id, String name, String type){
            this.id = id;
            this.name = name;
            this.type = type;
        }

        /**
         * Gets the id.
         *
         * @return the id
         */
        public Long getId(){
            return id;
        }

        /**
         * Gets the name.
         *
         * @return the name
         */
        public String getName(){
            return name;
        }

        /**
         * Gets the type.
         *
         * @return the type
         */
        public String getType(){
            return type;
        }
    }
}
