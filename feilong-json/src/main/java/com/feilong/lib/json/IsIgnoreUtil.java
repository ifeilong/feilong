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
package com.feilong.lib.json;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 3.0.0
 */
public class IsIgnoreUtil{

    private static final Logger LOGGER = LoggerFactory.getLogger(IsIgnoreUtil.class);

    /** Don't let anyone instantiate this class. */
    private IsIgnoreUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    static boolean isIgnore(Class<?> beanClass,PropertyDescriptor propertyDescriptor,Collection exclusions){
        String key = propertyDescriptor.getName();
        if (exclusions.contains(key)){
            return true;
        }

        //---------------------------------------------------------------
        Method readMethod = null;
        try{
            readMethod = propertyDescriptor.getReadMethod();
        }catch (Exception e){
            // bug 2565295
            LOGGER.info("Property '{}' of {} has no read method. SKIPPED", key, beanClass);
            return true;
        }

        //---------------------------------------------------------------
        if (readMethod == null){
            LOGGER.trace("Property '{}' of {} has no read method. SKIPPED", key, beanClass);
            return true;
        }
        return false;
    }

}
