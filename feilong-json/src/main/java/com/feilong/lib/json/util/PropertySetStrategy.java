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

import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.lang.ClassUtil;
import com.feilong.lib.json.JSONException;
import com.feilong.lib.json.JsonConfig;

/**
 * Defines a custom setter to be used when setting object values.<br>
 * Specify with JsonConfig.setJsonPropertySetter().
 * 
 * @see <a
 *      href="http://javaskeleton.blogspot.com/2011/05/ignore-missing-properties-with-json-lib.html">ignore-missing-properties-with-json-lib
 *      </a>
 * @see <a href="http://envy2002.iteye.com/blog/1682738">envy2002.iteye.com</a>
 * @author Gino Miceli <ginomiceli@users.sourceforge.net>
 * @author <a href="mailto:aalmiray@users.sourceforge.net">Andres Almiray</a>
 */
public abstract class PropertySetStrategy{

    /** The Constant log. */
    private static final Logger             LOGGER  = LoggerFactory.getLogger(PropertySetStrategy.class);

    //---------------------------------------------------------------
    /** The Constant DEFAULT. */
    public static final PropertySetStrategy DEFAULT = new DefaultPropertySetStrategy();

    //---------------------------------------------------------------

    /**
     * 设置 property.
     *
     * @param bean
     *            the bean
     * @param key
     *            the key
     * @param value
     *            the value
     * @throws JSONException
     *             the JSON exception
     */
    public abstract void setProperty(Object bean,String key,Object value);

    /**
     * 设置 property.
     *
     * @param bean
     *            the bean
     * @param key
     *            the key
     * @param value
     *            the value
     * @param jsonConfig
     *            the json config
     * @throws JSONException
     *             the JSON exception
     */
    public void setProperty(Object bean,String key,Object value,JsonConfig jsonConfig){
        setProperty(bean, key, value);
    }

    //---------------------------------------------------------------

    /**
     * The Class DefaultPropertySetStrategy.
     */
    private static final class DefaultPropertySetStrategy extends PropertySetStrategy{

        /**
         * 设置 property.
         *
         * @param bean
         *            the bean
         * @param key
         *            the key
         * @param value
         *            the value
         * @throws JSONException
         *             the JSON exception
         */
        @Override
        public void setProperty(Object bean,String key,Object value){
            setProperty(bean, key, value, new JsonConfig());
        }

        /**
         * 设置 property.
         *
         * @param bean
         *            the bean
         * @param key
         *            the key
         * @param value
         *            the value
         * @param jsonConfig
         *            the json config
         * @throws JSONException
         *             the JSON exception
         */
        @Override
        public void setProperty(Object bean,String key,Object value,JsonConfig jsonConfig){
            if (bean instanceof Map){
                ((Map) bean).put(key, value);
                return;
            }

            //---------------------------------------------------------------
            try{
                PropertyUtils.setSimpleProperty(bean, key, value);
            }catch (Exception e){

                //Ignore missing properties with Json-Lib
                //避免出现 Unknown property <code>'orderIdAndCodeMap'</code> on class 'class com.trade.....PaymentResultEntity' 异常
                Throwable cause = e.getCause();
                //since 1.12.5
                if (ClassUtil.isInstance(cause, NoSuchMethodException.class)){
                    LOGGER.warn("in class:[{}],can't find property:[{}],now ignore~~", bean.getClass().getName(), key);
                }else{
                    LOGGER.warn(e.getMessage(), e);
                }
            }
        }

    }
}