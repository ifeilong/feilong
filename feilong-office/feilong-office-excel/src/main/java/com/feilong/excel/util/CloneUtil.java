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
package com.feilong.excel.util;

import java.util.Map;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * The Class CloneUtil.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CloneUtil{

    /**
     * Clone map.
     *
     * @param map
     *            the map
     * @return the map
     * @throws InstantiationException
     *             the instantiation exception
     * @throws IllegalAccessException
     *             the illegal access exception
     * @deprecated 为什么要clone?
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    public static Map<String, Object> cloneMap(Map<String, Object> map) throws InstantiationException,IllegalAccessException{
        Map<String, Object> resultMap = map.getClass().newInstance();

        for (Map.Entry<String, Object> entry : map.entrySet()){
            String key = entry.getKey();
            Object value = entry.getValue();

            //---------------------------------------------------------------
            if (value == null){
                continue;
            }
            if (value instanceof Map){
                resultMap.put(key, cloneMap((Map<String, Object>) value));
            }else{
                resultMap.put(key, value.getClass().newInstance());
            }
        }
        return resultMap;
    }

}
