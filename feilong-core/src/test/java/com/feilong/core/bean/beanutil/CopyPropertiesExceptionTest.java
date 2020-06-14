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

import org.junit.Test;

import com.feilong.core.bean.BeanOperationException;
import com.feilong.core.bean.BeanUtil;
import com.feilong.store.member.Person;
import com.feilong.store.member.User;

/**
 * The Class BeanUtilCopyPropertiesTest.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 */
public class CopyPropertiesExceptionTest{

    /**
     * Test copy property from bean not exist properties.
     */
    @Test(expected = BeanOperationException.class)
    
    public void testCopyPropertyFromBeanNotExistProperties(){
        User user = new User();
        user.setId(5L);

        Person person = new Person();
        BeanUtil.copyProperties(person, user, "name", "dateAttr");
    }

    /**
     * Test bean util null to bean.
     */
    @Test(expected = NullPointerException.class)
    
    public void testBeanUtilNullToBean(){
        BeanUtil.copyProperties(null, new Person());
    }

    /**
     * Test bean util null from bean.
     */
    @Test(expected = NullPointerException.class)
    
    public void testBeanUtilNullFromBean(){
        BeanUtil.copyProperties(new Person(), null);
    }
}
