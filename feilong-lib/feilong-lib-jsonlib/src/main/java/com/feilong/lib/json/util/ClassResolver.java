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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.0.0
 */
public class ClassResolver{

    /** Don't let anyone instantiate this class. */
    private ClassResolver(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------
    public static Class<?> resolve(String key,String name,Map<String, Class<?>> classMap){
        Class<?> targetClass = TargetClassFinder.findTargetClass(key, classMap);
        return targetClass == null ? TargetClassFinder.findTargetClass(name, classMap) : targetClass;
    }

    public static Class<?> resolve(String key,String name,Map<String, Class<?>> classMap,Class<?> targetType){
        if (targetType == Object.class || targetType.isInterface()){
            Class<?> resolve = resolve(key, name, classMap);
            if (null != resolve){
                return resolve;
            }
            if (targetType.isInterface()){
                return targetType;
            }
        }
        return targetType;
    }

    /**
     * Resolve class.
     * 
     * @param key
     *            the key
     * @param name
     *            the name
     * @param type
     *            the type
     * @param classMap
     *            the class map
     *
     * @return the class
     */
    public static Class<?> resolve(String key,String name,Class<?> type,Map<String, Class<?>> classMap){
        Class<?> targetClass = resolve(key, name, classMap);
        if (targetClass != null){
            return targetClass;
        }

        //---------------------------------------------------------------
        if (type != null){
            if (List.class.equals(type)){
                return ArrayList.class;
            }
            if (Map.class.equals(type)){
                return LinkedHashMap.class;
            }
            if (Set.class.equals(type)){
                return LinkedHashSet.class;
            }
            if (!type.isInterface() && !Object.class.equals(type)){
                return type;
            }
        }
        return null;
    }
}
