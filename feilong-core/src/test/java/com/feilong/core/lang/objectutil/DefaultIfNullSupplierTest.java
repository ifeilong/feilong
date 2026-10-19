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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import org.junit.Test;

import com.feilong.core.lang.ObjectUtil;
import com.feilong.store.member.User;

/**
 * {@link ObjectUtil#defaultIfNull(Object, Supplier)} 的单元测试.
 *
 * <p>
 * 核心点: <code>object</code> 不为null的时候, <code>supplier</code> <b>不能被调用</b>(也就是不会创建对象).
 * </p>
 */
public class DefaultIfNullSupplierTest{

    @Test
    public void testObjectIsNull(){
        User expected = new User(6L);

        assertSame(expected, ObjectUtil.<User> defaultIfNull(null, () -> expected));
    }

    /**
     * object 不为null: 原样返回, 并且 supplier 根本没有被调用.
     */
    @Test
    public void testObjectIsNotNullAndSupplierNotCalled(){
        User user = new User(5L);

        AtomicBoolean supplierCalled = new AtomicBoolean(false);
        User result = ObjectUtil.<User> defaultIfNull(user, () -> {
            supplierCalled.set(true);
            return new User(6L);
        });

        assertSame(user, result);
        //关键: 没有调用 supplier, 也就没有执行 new User(6L)
        assertFalse(supplierCalled.get());
    }

    /**
     * 方法引用方式, 也就是 issue https://github.com/ifeilong/feilong/issues/628 里的用法.
     */
    @Test
    public void testMethodReference(){
        assertNotNull(ObjectUtil.<User> defaultIfNull(null, User::new));
    }

    @Test
    public void testSupplierReturnNull(){
        assertNull(ObjectUtil.<User> defaultIfNull(null, () -> null));
    }

    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testSupplierIsNull(){
        ObjectUtil.<User> defaultIfNull(new User(5L), (Supplier<User>) null);
    }
}
