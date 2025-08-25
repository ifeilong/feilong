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

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.feilong.lib.ezmorph.Morpher;
import com.feilong.lib.ezmorph.MorpherRegistry;
import com.feilong.lib.ezmorph.array.ObjectArrayMorpher;
import com.feilong.lib.ezmorph.bean.BeanMorpher;
import com.feilong.lib.ezmorph.object.IdentityObjectMorpher;
import com.feilong.lib.json.util.ClassResolver;
import com.feilong.lib.json.util.JSONUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PropertyValueConvertUtil{

    static List<?> toList(String key,Object value,JsonConfig jsonConfig,String name,Map<String, Class<?>> classMap){
        Class<?> targetClass = ClassResolver.resolve(key, name, classMap);

        JsonConfig jsonConfigCopy = jsonConfig.copy();
        jsonConfigCopy.setRootClass(targetClass);
        jsonConfigCopy.setClassMap(classMap);
        return (List<?>) JSONArrayToBeanUtil.toCollection((JSONArray) value, jsonConfigCopy);
    }

    static Collection<?> toCollection(
                    String key,
                    Object value,
                    JsonConfig jsonConfig,
                    String name,
                    Map<String, Class<?>> configClassMap,
                    Class<?> collectionType){
        Class<?> targetClass = ClassResolver.resolve(key, name, configClassMap);

        JsonConfig jsonConfigCopy = jsonConfig.copy();
        jsonConfigCopy.setRootClass(targetClass);
        jsonConfigCopy.setClassMap(configClassMap);
        jsonConfigCopy.setCollectionType(collectionType);
        return JSONArrayToBeanUtil.toCollection((JSONArray) value, jsonConfigCopy);
    }

    static Object toArray(String key,Object value,Class<?> targetType,JsonConfig jsonConfig,Map<String, Class<?>> configClassMap){
        Class<?> innerType = JSONUtils.getInnerComponentType(targetType);
        Class<?> targetInnerType = configClassMap.get(key);
        if (innerType.equals(Object.class) && targetInnerType != null && !targetInnerType.equals(Object.class)){
            innerType = targetInnerType;
        }

        //---------------------------------------------------------------
        JsonConfig jsonConfigCopy = jsonConfig.copy();
        jsonConfigCopy.setRootClass(innerType);
        jsonConfigCopy.setClassMap(configClassMap);

        //---------------------------------------------------------------
        MorpherRegistry morpherRegistry = JSONUtils.getMorpherRegistry();
        Object array = JSONArrayToBeanUtil.toArray((JSONArray) value, jsonConfigCopy);

        if (innerType.isPrimitive() || //
                        JSONUtils.isNumber(innerType) || //
                        Boolean.class.isAssignableFrom(innerType) || //
                        JSONUtils.isString(innerType)){
            return morpherRegistry.morph(Array.newInstance(innerType, 0).getClass(), array);
        }
        //---------------------------------------------------------------
        if (!array.getClass().equals(targetType)){
            if (!targetType.equals(Object.class)){
                Morpher morpher = morpherRegistry.getMorpherFor(Array.newInstance(innerType, 0).getClass());
                if (IdentityObjectMorpher.INSTANCE.equals(morpher)){
                    morpherRegistry.registerMorpher(new ObjectArrayMorpher(new BeanMorpher(innerType, morpherRegistry)));
                }
                array = morpherRegistry.morph(Array.newInstance(innerType, 0).getClass(), array);
            }
        }
        return array;
    }
}
