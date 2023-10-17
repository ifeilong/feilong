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
package com.feilong.core.bean.beanutil;

import static com.feilong.core.bean.ConvertUtil.toArray;
import static com.feilong.core.bean.ConvertUtil.toMap;
import static com.feilong.core.util.MapUtil.newHashMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertArrayEquals;

import java.util.Map;

import org.junit.Test;

import com.feilong.core.bean.BeanUtil;
import com.feilong.store.member.User;

public class PopulateTest{

    @Test
    public void testPopulate(){
        User user = new User();
        user.setId(5L);

        Map<String, Long> properties = toMap("id", 8L);
        assertThat(BeanUtil.populate(user, properties), allOf(hasProperty("id", is(8L))));
    }

    @Test
    public void testPopulateAutoConvert(){
        User user = new User();
        user.setId(5L);

        Map<String, Object> properties = toMap("id", "8");
        assertThat(BeanUtil.populate(user, properties), allOf(hasProperty("id", is(8L))));
    }

    @Test
    public void testPopulateAutoConvert1(){
        User user = new User();

        Map<String, Object> properties = toMap("loves", "女人,漂亮女人");
        User populate = BeanUtil.populate(user, properties);

        assertArrayEquals(toArray("女人", "漂亮女人"), populate.getLoves());
    }

    @Test
    public void testPopulateBeanNotExistsPropertyName(){
        User user = new User();
        user.setId(5L);

        Map<String, Long> properties = toMap("id1", 8L);
        assertThat(BeanUtil.populate(user, properties), allOf(hasProperty("id", is(5L))));
    }

    @Test
    public void testPopulateBeanWithSpaceExistsPropertyName(){
        User user = new User();
        user.setId(5L);

        Map<String, Long> properties = toMap("i d1", 8L);
        assertThat(BeanUtil.populate(user, properties), allOf(hasProperty("id", is(5L))));
    }

    @Test
    public void testPopulateBeanWithNullExistsPropertyName(){
        User user = new User();
        user.setId(5L);

        Map<String, Long> properties = toMap(null, 8L);
        assertThat(BeanUtil.populate(user, properties), allOf(hasProperty("id", is(5L))));
    }

    @Test
    public void testPopulateMap(){
        Map<String, Object> map = newHashMap();
        Map<String, Long> properties = toMap("id", 8L);

        assertThat(BeanUtil.populate(map, properties), allOf(hasEntry("id", (Object) 8L)));
    }

    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testPopulateNullBean(){
        Map<String, Long> map = toMap("id", 8L);
        BeanUtil.populate(null, map);
    }

    @Test(expected = NullPointerException.class)
    public void testPopulateNullProperties(){
        BeanUtil.populate(new User(), null);
    }
}
