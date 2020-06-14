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
import static com.feilong.core.bean.ConvertUtil.toMap;
import static com.feilong.core.util.CollectionsUtil.newArrayList;
import static com.feilong.core.util.CollectionsUtil.newCopyOnWriteArrayList;
import static com.feilong.core.util.CollectionsUtil.newHashSet;
import static com.feilong.core.util.CollectionsUtil.newLinkedList;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.feilong.core.util.CollectionsUtil;
import com.feilong.store.member.User;

public class SizeTest{

    @Test
    public void testMap1(){
        assertEquals(1, CollectionsUtil.size(toMap("name", "1")));
    }

    @Test
    public void test(){
        User zhangfei = new User("张飞", 23);
        User guanyu24 = new User("关羽", 24);
        User liubei = new User("刘备", 25);
        User guanyu50 = new User("关羽", 50);
        List<User> list = toList(zhangfei, guanyu24, liubei, guanyu50);

        assertEquals(4, CollectionsUtil.size(list));
    }

    @Test
    public void testEmptyList(){
        assertEquals(0, CollectionsUtil.size(newArrayList()));
    }

    @Test
    public void testEmptySet(){
        assertEquals(0, CollectionsUtil.size(newHashSet()));
    }

    @Test
    public void testEmptyLinkedSet(){
        assertEquals(0, CollectionsUtil.size(newLinkedList()));
    }

    @Test
    public void testEmptyCopyOnWriteArrayList(){
        assertEquals(0, CollectionsUtil.size(newCopyOnWriteArrayList()));
    }

    //---------------------------------------------------------------

    @Test
    public void testSizeNullIterable(){
        assertEquals(0, CollectionsUtil.size(null));
    }

    @Test
    public void testEmptyArray(){
        assertEquals(0, CollectionsUtil.size(new String[0]));
    }

    @Test
    public void testMap(){
        assertEquals(0, CollectionsUtil.size(newHashSet()));
    }

}
