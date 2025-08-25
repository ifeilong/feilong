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

import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.Map;

import com.feilong.core.bean.PropertyUtil;
import com.feilong.lib.beanutils.PropertyUtils;
import com.feilong.lib.json.processors.JsonValueProcessor;
import com.feilong.lib.json.processors.JsonVerifier;
import com.feilong.lib.json.processors.PropertyNameProcessor;
import com.feilong.lib.json.processors.PropertyNameProcessorMatcher;
import com.feilong.lib.json.util.IsIgnoreUtil;
import com.feilong.lib.json.util.KeyUpdater;
import com.feilong.lib.json.util.PropertyFilter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 基于 bean 对象的解析成JSONObject.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DefaultBeanProcesser{

    static JSONObject process(Object bean,JSONObject jsonObject,JsonConfig jsonConfig) throws Exception{
        Class<?> beanClass = bean.getClass();
        PropertyNameProcessor propertyNameProcessor = findJsonPropertyNameProcessor(beanClass, jsonConfig);

        Collection<String> exclusions = jsonConfig.getMergedExcludes();
        PropertyFilter jsonPropertyFilter = jsonConfig.getJsonPropertyFilter();

        //---------------------------------------------------------------
        PropertyDescriptor[] propertyDescriptors = PropertyUtil.getPropertyDescriptors(beanClass);
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors){
            if (IsIgnoreUtil.isIgnore(beanClass, propertyDescriptor, exclusions)){
                continue;
            }
            //---------------------------------------------------------------
            String key = propertyDescriptor.getName();
            Object value = PropertyUtils.getSimpleProperty(bean, key);
            if (jsonPropertyFilter != null && jsonPropertyFilter.apply(bean, key, value)){
                continue;
            }
            set(jsonObject, beanClass, propertyDescriptor, value, jsonConfig, propertyNameProcessor);
        }
        return jsonObject;
    }

    //---------------------------------------------------------------

    private static void set(
                    JSONObject jsonObject,
                    Class<?> beanClass,

                    PropertyDescriptor propertyDescriptor,
                    Object value,

                    JsonConfig jsonConfig,
                    PropertyNameProcessor propertyNameProcessor){
        String propertyName = propertyDescriptor.getName();
        Class<?> type = propertyDescriptor.getPropertyType();
        JsonValueProcessor jsonValueProcessor = jsonConfig.findJsonValueProcessor(type, propertyName);

        boolean bypass = false;
        if (jsonValueProcessor != null){
            value = jsonValueProcessor.processObjectValue(propertyName, value, jsonConfig);
            bypass = true;
            if (!JsonVerifier.isValidJsonValue(value)){
                throw new JSONException("Value is not a valid JSON value. " + value);
            }
        }
        //---------------------------------------------------------------
        propertyName = KeyUpdater.update(beanClass, propertyName, propertyNameProcessor);
        JSONObjectValueSetter.set(jsonObject, propertyName, value, type, jsonConfig, bypass);
    }

    /**
     * Finds a PropertyNameProcessor registered to the target class.<br>
     * Returns null if none is registered.<br>
     * [Java -&gt; JSON]
     *
     * @param beanClass
     *            the bean class
     * @param jsonConfig
     * @return the property name processor
     */
    private static PropertyNameProcessor findJsonPropertyNameProcessor(Class<?> beanClass,JsonConfig jsonConfig){
        Map<Class<?>, PropertyNameProcessor> jsonPropertyNameProcessorMap = jsonConfig.getJsonPropertyNameProcessorMap();
        if (!jsonPropertyNameProcessorMap.isEmpty()){
            Object key = PropertyNameProcessorMatcher.getMatch(beanClass, jsonPropertyNameProcessorMap.keySet());
            return jsonPropertyNameProcessorMap.get(key);

        }
        return null;
    }

}
