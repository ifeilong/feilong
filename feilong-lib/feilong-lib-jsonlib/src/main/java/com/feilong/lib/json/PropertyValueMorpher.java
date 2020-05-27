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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.lib.ezmorph.Morpher;
import com.feilong.lib.ezmorph.MorpherRegistry;
import com.feilong.lib.ezmorph.bean.BeanMorpher;
import com.feilong.lib.ezmorph.object.EnumMorpher;
import com.feilong.lib.ezmorph.object.IdentityObjectMorpher;
import com.feilong.lib.json.util.JSONUtils;

/**
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.0.0
 */
class PropertyValueMorpher{

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyValueMorpher.class);

    /** Don't let anyone instantiate this class. */
    private PropertyValueMorpher(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

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
    static Object morphPropertyValue(String key,Object value,Class<?> type,Class<?> targetType){
        MorpherRegistry morpherRegistry = JSONUtils.getMorpherRegistry();

        Morpher morpher = morpherRegistry.getMorpherFor(targetType);
        if (IdentityObjectMorpher.INSTANCE.equals(morpher)){
            LOGGER.warn(
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
