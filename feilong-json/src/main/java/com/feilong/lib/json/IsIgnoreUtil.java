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
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 3.0.0
 */
public class IsIgnoreUtil{

    private static final Logger LOGGER = LoggerFactory.getLogger(IsIgnoreUtil.class);

    /** Don't let anyone instantiate this class. */
    private IsIgnoreUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    static boolean isIgnore(PropertyDescriptor propertyDescriptor,Collection exclusions,Class beanClass,JsonConfig jsonConfig){
        String key = propertyDescriptor.getName();
        if (exclusions.contains(key)){
            return true;
        }

        if (jsonConfig.isIgnoreTransientFields() && isTransientField(key, beanClass, jsonConfig)){
            return true;
        }

        //---------------------------------------------------------------
        Class type = propertyDescriptor.getPropertyType();

        Method readMethod = null;
        try{
            readMethod = propertyDescriptor.getReadMethod();
        }catch (Exception e){
            // bug 2565295
            LOGGER.info("Property '{}' of {} has no read method. SKIPPED", key, beanClass);
            return true;
        }

        //---------------------------------------------------------------
        if (readMethod == null){
            LOGGER.trace("Property '{}' of {} has no read method. SKIPPED", key, beanClass);
            return true;
        }
        if (isTransient(readMethod, jsonConfig)){
            return true;
        }
        return false;
    }

    static boolean isIgnore(Field field,Collection exclusions,JsonConfig jsonConfig){
        String key = field.getName();
        if (exclusions.contains(key)){
            return true;
        }

        if (jsonConfig.isIgnoreTransientFields() && isTransient(field, jsonConfig)){
            return true;
        }
        return false;
    }

    //---------------------------------------------------------------

    /**
     * Checks if is transient field.
     *
     * @param name
     *            the name
     * @param beanClass
     *            the bean class
     * @param jsonConfig
     *            the json config
     * @return true, if is transient field
     */
    private static boolean isTransientField(String name,Class beanClass,JsonConfig jsonConfig){
        try{
            Field field = beanClass.getDeclaredField(name);
            if ((field.getModifiers() & Modifier.TRANSIENT) == Modifier.TRANSIENT){
                return true;
            }
            return isTransient(field, jsonConfig);
        }catch (Exception e){
            LOGGER.info("Error while inspecting field " + beanClass + "." + name + " for transient status.", e);
        }
        return false;
    }

    /**
     * Checks if is transient.
     *
     * @param element
     *            the element
     * @param jsonConfig
     *            the json config
     * @return true, if is transient
     */
    private static boolean isTransient(AnnotatedElement element,JsonConfig jsonConfig){
        for (Iterator annotations = jsonConfig.getIgnoreFieldAnnotations().iterator(); annotations.hasNext();){
            try{
                String annotationClassName = (String) annotations.next();
                if (element.getAnnotation((Class) Class.forName(annotationClassName)) != null){
                    return true;
                }
            }catch (Exception e){
                LOGGER.info("Error while inspecting " + element + " for transient status.", e);
            }
        }
        return false;
    }
}
