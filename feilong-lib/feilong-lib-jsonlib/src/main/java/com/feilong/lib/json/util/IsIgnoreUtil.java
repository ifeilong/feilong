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
package com.feilong.lib.json.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Collection;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 是否忽略的工具类.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.0.0
 */
@lombok.extern.slf4j.Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IsIgnoreUtil{

    /**
     * 是否忽略.
     * 
     * @param beanClass
     * @param propertyDescriptor
     * @param exclusions
     * @return 如果忽略 返回true
     */
    public static boolean isIgnore(Class<?> beanClass,PropertyDescriptor propertyDescriptor,Collection<String> exclusions){
        String key = propertyDescriptor.getName();
        //如果属性名字在排除清单里面, 那么忽略
        if (exclusions.contains(key)){
            return true;
        }

        //---------------------------------------------------------------
        Method readMethod = null;
        try{
            //如果调用 read方法出了异常, 那么忽略
            readMethod = propertyDescriptor.getReadMethod();
        }catch (Exception e){
            // bug 2565295
            log.info("Property '{}' of {} has no read method. SKIPPED", key, beanClass);
            return true;
        }

        //---------------------------------------------------------------
        //如果 read方法是null ,也忽略
        if (readMethod == null){
            log.trace("Property '{}' of {} has no read method. SKIPPED", key, beanClass);
            return true;
        }
        return false;
    }

}
