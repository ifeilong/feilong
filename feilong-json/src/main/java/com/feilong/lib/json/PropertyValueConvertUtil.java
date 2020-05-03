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
import com.feilong.lib.ezmorph.array.ObjectArrayMorpher;
import com.feilong.lib.ezmorph.bean.BeanMorpher;
import com.feilong.lib.ezmorph.object.IdentityObjectMorpher;
import com.feilong.lib.json.util.JSONUtils;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 3.0.0
 */
public class PropertyValueConvertUtil{

    /**
     * Convert property value to list.
     *
     * @param key
     *            the key
     * @param value
     *            the value
     * @param jsonConfig
     *            the json config
     * @param name
     *            the name
     * @param classMap
     *            the class map
     * @return the list
     */
    static List toList(String key,Object value,JsonConfig jsonConfig,String name,Map classMap){
        Class targetClass = TargetClassFinder.findTargetClass(key, classMap);
        targetClass = targetClass == null ? TargetClassFinder.findTargetClass(name, classMap) : targetClass;
        JsonConfig jsc = jsonConfig.copy();
        jsc.setRootClass(targetClass);
        jsc.setClassMap(classMap);
        List list = (List) JSONArray.toCollection((JSONArray) value, jsc);
        return list;
    }

    /**
     * Convert property value to collection.
     *
     * @param key
     *            the key
     * @param value
     *            the value
     * @param jsonConfig
     *            the json config
     * @param name
     *            the name
     * @param classMap
     *            the class map
     * @param collectionType
     *            the collection type
     * @return the collection
     */
    static Collection toCollection(
                    String key,
                    Object value,
                    JsonConfig jsonConfig,
                    String name,
                    Map classMap,
                    Class collectionType){
        Class targetClass = TargetClassFinder.findTargetClass(key, classMap);
        targetClass = targetClass == null ? TargetClassFinder.findTargetClass(name, classMap) : targetClass;
        JsonConfig jsc = jsonConfig.copy();
        jsc.setRootClass(targetClass);
        jsc.setClassMap(classMap);
        jsc.setCollectionType(collectionType);
        return JSONArray.toCollection((JSONArray) value, jsc);
    }

    /**
     * Convert property value to array.
     *
     * @param key
     *            the key
     * @param value
     *            the value
     * @param targetType
     *            the target type
     * @param jsonConfig
     *            the json config
     * @param classMap
     *            the class map
     * @return the object
     */
    static Object toArray(String key,Object value,Class targetType,JsonConfig jsonConfig,Map classMap){
        Class innerType = JSONUtils.getInnerComponentType(targetType);
        Class targetInnerType = TargetClassFinder.findTargetClass(key, classMap);
        if (innerType.equals(Object.class) && targetInnerType != null && !targetInnerType.equals(Object.class)){
            innerType = targetInnerType;
        }

        JsonConfig jsc = jsonConfig.copy();
        jsc.setRootClass(innerType);
        jsc.setClassMap(classMap);

        Object array = JSONArray.toArray((JSONArray) value, jsc);
        if (innerType.isPrimitive() || JSONUtils.isNumber(innerType) || Boolean.class.isAssignableFrom(innerType)
                        || JSONUtils.isString(innerType)){
            array = JSONUtils.getMorpherRegistry().morph(Array.newInstance(innerType, 0).getClass(), array);

        }else if (!array.getClass().equals(targetType)){
            if (!targetType.equals(Object.class)){
                Morpher morpher = JSONUtils.getMorpherRegistry().getMorpherFor(Array.newInstance(innerType, 0).getClass());
                if (IdentityObjectMorpher.getInstance().equals(morpher)){
                    ObjectArrayMorpher beanMorpher = new ObjectArrayMorpher(new BeanMorpher(innerType, JSONUtils.getMorpherRegistry()));
                    JSONUtils.getMorpherRegistry().registerMorpher(beanMorpher);
                }
                array = JSONUtils.getMorpherRegistry().morph(Array.newInstance(innerType, 0).getClass(), array);
            }
        }
        return array;
    }

}
