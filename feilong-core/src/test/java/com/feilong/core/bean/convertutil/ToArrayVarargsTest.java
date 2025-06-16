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
package com.feilong.core.bean.convertutil;

import static com.feilong.core.bean.ConvertUtil.toArray;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import com.feilong.core.bean.ConvertUtil;
import com.feilong.store.member.User;

public class ToArrayVarargsTest{

    @Test
    public void testToArray(){
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);

        User[] users = { user1, user2 };
        assertArrayEquals(users, toArray(user1, user2));
    }

    @Test
    public void testToArray1(){
        assertArrayEquals(new String[] { "xinge", "feilong" }, toArray("xinge", "feilong"));
    }

    @Test
    public void testToArrayNull(){
        Object[] array = ConvertUtil.toArray(null);
        assertArrayEquals(null, array);
    }

    @Test
    public void testToArray4(){
        assertArrayEquals(new String[] {}, ConvertUtil.<String> toArray());
        assertArrayEquals(new Integer[] {}, ConvertUtil.<Integer> toArray());
    }

    @Test
    public void testToArrayNull1(){
        String[] array = ConvertUtil.toArray((String) null);
        assertArrayEquals(new String[] { null }, array);
    }
}
