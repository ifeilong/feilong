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

import com.feilong.lib.ezmorph.Morpher;
import com.feilong.lib.ezmorph.MorpherRegistry;
import com.feilong.lib.ezmorph.bean.BeanMorpher;
import com.feilong.lib.ezmorph.object.EnumMorpher;
import com.feilong.lib.ezmorph.object.IdentityObjectMorpher;
import com.feilong.lib.json.util.JSONUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * value 转换器 Morpher 变形器.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.0.0
 */
@lombok.extern.slf4j.Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class PropertyValueMorpher{

    /**
     * Morph property value.
     *
     * @param key
     *            the key
     * @param value
     *            the value
     * @param type
     *            the type
     * @param targetType
     *            the target type
     * @return the object
     */
    static Object morph(String key,Object value,Class<?> type,Class<?> targetType){
        MorpherRegistry morpherRegistry = JSONUtils.getMorpherRegistry();

        Morpher morpher = morpherRegistry.getMorpherFor(targetType);
        if (IdentityObjectMorpher.INSTANCE.equals(morpher)){
            log.warn(
                            "Can't transform property '{}' from {} into {}.Will register a default Morpher",
                            key,
                            type.getName(),
                            targetType.getName());
            if (Enum.class.isAssignableFrom(targetType)){
                morpherRegistry.registerMorpher(new EnumMorpher(targetType));
            }else{
                morpherRegistry.registerMorpher(new BeanMorpher(targetType, morpherRegistry));
            }
        }

        return morpherRegistry.morph(targetType, value);
    }
}
