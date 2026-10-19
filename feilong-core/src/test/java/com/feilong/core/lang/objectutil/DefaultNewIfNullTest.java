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
package com.feilong.core.lang.objectutil;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.feilong.core.lang.ObjectUtil;
import com.feilong.store.member.User;

/**
 * {@link ObjectUtil#defaultNewIfNull(Object, Class)} 的单元测试.
 *
 * <p>
 * 覆盖: object 是null时创建新实例 / object 非null时原样返回(不创建) / 每次都创建新实例 / klass 是null 抛异常
 * </p>
 */
public class DefaultNewIfNullTest{

    @Test
    public void testObjectIsNull(){
        User user = ObjectUtil.defaultNewIfNull(null, User.class);

        assertNotNull(user);
        assertThat(user, allOf(hasProperty("id", is(0L)), hasProperty("name", is("feilong"))));
    }

    @Test
    public void testObjectIsNotNull(){
        User user = new User(5L);

        //不会重新创建, 原样返回
        assertSame(user, ObjectUtil.defaultNewIfNull(user, User.class));
    }

    @Test
    public void testNewInstanceEveryTime(){
        //每次都创建新的实例, 不做缓存
        assertNotSame(ObjectUtil.defaultNewIfNull(null, User.class), ObjectUtil.defaultNewIfNull(null, User.class));
    }

    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testKlassIsNull(){
        ObjectUtil.defaultNewIfNull(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testKlassIsNullAndObjectIsNotNull(){
        //klass 是null 属于调用方错误, 即使是 object 非null 也要 fail fast
        ObjectUtil.defaultNewIfNull(new User(5L), null);
    }
}
