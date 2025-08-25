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
package com.feilong.context.log;

import com.feilong.core.lang.ClassUtil;

import lombok.extern.slf4j.Slf4j;

/** The Constant log. */
@Slf4j
public abstract class AbstractFixedContextLogCreator implements FixedContextLogCreator{

    /**
     * Gets the class.
     *
     * @param className
     *            the class name
     * @return the class
     */
    protected static Class<?> getClass(String className){
        try{
            Class<?> klass = ClassUtil.getClass(className);
            log.info("findAndLoad:[{}]", className);
            return klass;
        }catch (Exception e){
            log.warn("can't load:[{}]", className, e);
            return null;
        }
    }

}
