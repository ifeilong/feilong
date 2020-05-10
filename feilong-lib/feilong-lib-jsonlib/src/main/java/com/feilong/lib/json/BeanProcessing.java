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

import com.feilong.lib.beanutils.PropertyUtils;
import com.feilong.lib.json.processors.JsonValueProcessor;
import com.feilong.lib.json.processors.JsonVerifier;
import com.feilong.lib.json.processors.PropertyNameProcessor;
import com.feilong.lib.json.util.CycleSetUtil;
import com.feilong.lib.json.util.IsIgnoreUtil;
import com.feilong.lib.json.util.JSONExceptionUtil;
import com.feilong.lib.json.util.KeyUpdate;
import com.feilong.lib.json.util.PropertyFilter;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 3.0.0
 */
public class BeanProcessing{

    /** Don't let anyone instantiate this class. */
    private BeanProcessing(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Default bean processing.
     *
     * @param bean
     *            the bean
     * @param jsonConfig
     *            the json config
     * @return the JSON object
     */
    static JSONObject defaultBeanProcessing(Object bean,JsonConfig jsonConfig){
        Class<?> beanClass = bean.getClass();
        PropertyNameProcessor propertyNameProcessor = jsonConfig.findJsonPropertyNameProcessor(beanClass);
        Collection<String> exclusions = jsonConfig.getMergedExcludes();
        PropertyFilter jsonPropertyFilter = jsonConfig.getJsonPropertyFilter();

        //---------------------------------------------------------------
        JSONObject jsonObject = new JSONObject();
        try{
            PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(beanClass);

            for (int i = 0; i < propertyDescriptors.length; i++){
                PropertyDescriptor propertyDescriptor = propertyDescriptors[i];

                if (IsIgnoreUtil.isIgnore(beanClass, propertyDescriptor, exclusions)){
                    continue;
                }

                //---------------------------------------------------------------
                String key = propertyDescriptor.getName();
                Object value = PropertyUtils.getSimpleProperty(bean, key);
                if (jsonPropertyFilter != null && jsonPropertyFilter.apply(bean, key, value)){
                    continue;
                }

                //FIXME
                //                if (bean.getClass().getName().equals("com.feilong.store.member.User")){
                //                    continue;
                //                }
                //---------------------------------------------------------------
                Class<?> type = propertyDescriptor.getPropertyType();
                //FIXME
                //                if (bean.getClass().getName().equals("1com.feilong.store.member.User")){
                //                    return null;
                //                }

                //FIXME
                //if (!bean.getClass().getName().equals("com.feilong.store.member.User")){
                set(jsonObject, beanClass, key, value, type, jsonConfig, propertyNameProcessor);
                //}
            }

            //---------------------------------------------------------------
        }catch (Exception e){
            CycleSetUtil.removeInstance(bean);
            throw JSONExceptionUtil.build("", e);
        }
        return jsonObject;
    }

    private static void set(
                    JSONObject jsonObject,

                    Class<?> beanClass,
                    String key,
                    Object value,
                    Class<?> type,

                    JsonConfig jsonConfig,
                    PropertyNameProcessor propertyNameProcessor){
        JsonValueProcessor jsonValueProcessor = jsonConfig.findJsonValueProcessor(beanClass, type, key);
        boolean bypass = false;
        if (jsonValueProcessor != null){
            value = jsonValueProcessor.processObjectValue(key, value, jsonConfig);
            bypass = true;
            if (!JsonVerifier.isValidJsonValue(value)){
                throw new JSONException("Value is not a valid JSON value. " + value);
            }
        }
        //---------------------------------------------------------------
        key = KeyUpdate.update(beanClass, key, propertyNameProcessor);
        JSONObjectSetValueCore.setValue(jsonObject, key, value, type, jsonConfig, bypass);
    }

}
