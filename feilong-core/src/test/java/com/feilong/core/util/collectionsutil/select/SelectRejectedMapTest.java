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
import static com.feilong.core.bean.ConvertUtil.toMap;
import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.feilong.core.util.CollectionsUtil;
import com.feilong.store.member.User;

public class SelectRejectedMapTest{

    @Test
    public void testSelect2(){
        User guanyu301 = new User("关羽", 30);//
        User guanyu30 = new User("关羽", 30);//

        User zhangfei = new User("张飞", 23);
        User liubei = new User("刘备", 25);
        User guanyu24 = new User("关羽", 24);

        List<User> list = toList(//
                        zhangfei,
                        guanyu24,
                        guanyu301,
                        liubei,
                        guanyu30);

        Map<String, ?> map = toMap("name", "关羽", "age", 30);

        List<User> selectRejected = CollectionsUtil.selectRejected(list, map);
        assertThat(
                        selectRejected,
                        allOf(//
                                        hasItem(zhangfei),
                                        hasItem(guanyu24),
                                        hasItem(liubei)));
    }

    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testSelectNullPredicate(){
        List<User> list = toList(new User("张飞", 23));
        CollectionsUtil.selectRejected(list, (Map<String, ?>) null);
    }

    @Test
    public void testSelectNullPredicate1(){
        assertEquals(emptyList(), CollectionsUtil.<Object> selectRejected(null, (Map<String, ?>) null));
    }

    @Test
    public void testSelectNullIterable(){
        Object Select = CollectionsUtil.selectRejected(null, toMap("name", "关羽"));
        assertEquals(emptyList(), Select);
    }

    //---------------------------------------------------------------

    @Test
    public void testSelectNotSelect(){
        User user = new User("张飞", 23);
        List<User> list = toList(user);
        List<User> selectRejected = CollectionsUtil.selectRejected(list, toMap("name", "关羽"));
        assertThat(list, allOf(hasItem(user)));
    }
}
