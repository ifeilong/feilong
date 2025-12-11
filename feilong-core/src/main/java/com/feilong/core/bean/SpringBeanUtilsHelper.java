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
package com.feilong.core.bean;

import com.feilong.core.lang.ClassUtil;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 用来判断 当前环境是否有 spring bean BeanUtils 类.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.12.0
 */
@lombok.extern.slf4j.Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class SpringBeanUtilsHelper{

    /** The spring bean utils class. */
    private static Class<?> springBeanUtilsClass = null;

    //---------------------------------------------------------------

    static{
        String className = "org.springframework.beans.BeanUtils";
        try{
            springBeanUtilsClass = ClassUtil.getClass(className);
            log.trace("find and load:[{}]", className);
        }catch (Throwable e){
            //just want to use e.toString
            log.warn("can't load:[{}],[{}],if you import spring, getPropertyValue will speed fast", className, e.getMessage());
        }
    }

    //---------------------------------------------------------------

    /**
     * 判断环境中是否有Spring BeanUtils.
     *
     * @return 如果 SPRING_BEAN_UTILS_CLASS 不是null ,表示有, 返回true; 否则返回false
     */
    static boolean hasSpringBeanUtilsClass(){
        return springBeanUtilsClass != null;
    }

    /**
     * 返回 Spring BeanUtils.
     *
     * @return the spring bean utils class
     */
    static Class<?> getSpringBeanUtilsClass(){
        return springBeanUtilsClass;
    }
}
