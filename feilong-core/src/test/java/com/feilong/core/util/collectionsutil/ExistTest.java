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

import com.feilong.core.util.CollectionsUtil;
import com.feilong.store.member.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.feilong.core.bean.ConvertUtil.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

public class ExistTest {

    @Test
    public void testExist() {
        User zhangfei = new User("张飞", 23);
        User guanyu24 = new User("关羽", 24);
        User liubei = new User("刘备", 25);
        User guanyu50 = new User("关羽", 50);
        List<User> list = toList(zhangfei, guanyu24, liubei, guanyu50);

        assertEquals(true, CollectionsUtil.exist(list, "name", "关羽"));
    }

    @Test
    public void testExistNotExist() {
        User zhangfei = new User("张飞", 23);
        List<User> list = toList(zhangfei);

        assertEquals(false, CollectionsUtil.exist(list, "name", "关羽"));
    }

    //---------------------------------------------------------------

    @Test
    public void testExistNullIterable() {
        assertEquals(false, CollectionsUtil.exist(null, "name", "关羽"));
    }

    @Test(expected = NullPointerException.class)
    public void testExistNullPropertyName() {
        CollectionsUtil.exist(new ArrayList<>(), null, "关羽");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExistEmptyPropertyName() {
        CollectionsUtil.exist(new ArrayList<>(), "", "关羽");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExistBlankPropertyName() {
        CollectionsUtil.exist(new ArrayList<>(), " ", "关羽");
    }
}
