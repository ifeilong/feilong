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

import java.util.Iterator;
import java.util.Map;

import com.feilong.lib.json.regexp.RegexpUtils;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 3.0.0
 */
class TargetClassFinder{

    /** Don't let anyone instantiate this class. */
    private TargetClassFinder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Locates a Class associated to a specifi key.
     * <p>
     * The key may be a regexp.
     * </p>
     *
     * @param key
     *            the key
     * @param classMap
     *            the class map
     * @return the class
     */
    static Class<?> findTargetClass(String key,Map classMap){
        // try get first
        Class<?> targetClass = (Class<?>) classMap.get(key);

        if (targetClass == null){
            // try with regexp
            // this will hit performance as it must iterate over all the keys and create a RegexpMatcher for each key
            for (Iterator i = classMap.entrySet().iterator(); i.hasNext();){
                Map.Entry entry = (Map.Entry) i.next();
                if (RegexpUtils.getMatcher((String) entry.getKey()).matches(key)){
                    targetClass = (Class<?>) entry.getValue();
                    break;
                }
            }
        }
        return targetClass;
    }
}
