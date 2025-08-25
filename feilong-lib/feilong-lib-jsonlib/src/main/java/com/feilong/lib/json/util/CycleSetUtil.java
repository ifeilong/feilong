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

import java.lang.ref.SoftReference;
import java.util.HashSet;
import java.util.Set;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CycleSetUtil{

    private static class CycleSet extends ThreadLocal<SoftReference<Set<Object>>>{

        /**
         * Initial value.
         *
         * @return the object
         */
        @Override
        protected SoftReference<Set<Object>> initialValue(){
            return new SoftReference<>(new HashSet<>());
        }

        /**
         * Gets the 设置.
         *
         * @return the 设置
         */
        public Set<Object> getSet(){
            Set<Object> set = get().get();
            if (set == null){
                set = new HashSet<>();
                set(new SoftReference<>(set));
            }
            return set;
        }
    }

    /** The cycle set. */
    private static CycleSet cycleSet = new CycleSet();

    //---------------------------------------------------------------

    /**
     * Adds a reference for cycle detection check.
     *
     * @param instance
     *            the reference to add
     * @return true if the instance has not been added previously, false
     *         otherwise.
     */
    public static boolean addInstance(Object instance){
        // return true;
        return getCycleSet().add(instance);
    }

    //---------------------------------------------------------------

    /**
     * Removes a reference for cycle detection check.
     *
     * @param instance
     *            the instance
     */
    public static void removeInstance(Object instance){
        Set<Object> set = getCycleSet();
        set.remove(instance);

        if (set.size() == 0){
            cycleSet.remove();
        }
    }

    //---------------------------------------------------------------

    /**
     * Gets the cycle set.
     *
     * @return the cycle set
     */
    private static Set<Object> getCycleSet(){
        return cycleSet.getSet();
    }
}
