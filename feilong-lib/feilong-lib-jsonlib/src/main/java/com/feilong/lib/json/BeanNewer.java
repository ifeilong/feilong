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

import static com.feilong.core.util.MapUtil.newTreeMap;

import java.util.Map;

import com.feilong.core.lang.reflect.ConstructorUtil;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.0.6
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanNewer{

    static Object newBean(Class<?> rootClass){
        if (rootClass.isInterface()){
            if (!Map.class.isAssignableFrom(rootClass)){
                throw new JSONException("beanClass is an interface. " + rootClass);
            }
            return newTreeMap();
        }
        return ConstructorUtil.newInstance(rootClass);
    }
}
