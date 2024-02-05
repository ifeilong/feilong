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
package com.feilong.core.lang.reflect.constructorutil;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import com.feilong.core.lang.reflect.ConstructorUtil;
import com.feilong.core.lang.reflect.ReflectException;
import com.feilong.store.member.User;

/**
 * The Class ConstructorUtilNewInstanceTest.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 */
public class NewInstanceTest{

    /**
     * Test new instance.
     */
    @Test
    public void testNewInstance(){
        User user = ConstructorUtil.newInstance(User.class, 5L, 8);
        assertThat(user, allOf(hasProperty("id", is(5L)), hasProperty("age", is(8))));
    }

    /**
     * Test new instance default.
     */
    @Test
    public void testNewInstanceDefault(){
        User user = ConstructorUtil.newInstance(User.class);
        assertThat(user, allOf(hasProperty("id", is(0L)), hasProperty("name", is("feilong"))));
    }

    /**
     * Test new instance not exist.
     */
    @Test(expected = ReflectException.class)
    public void testNewInstanceNotExist(){
        ConstructorUtil.newInstance(User.class, "feilong", 8, "feilong");
    }

    /**
     * Test new instance null klass.
     */
    @Test(expected = NullPointerException.class)
    public void testNewInstanceNullKlass(){
        ConstructorUtil.newInstance(null, 5L);
    }
}
