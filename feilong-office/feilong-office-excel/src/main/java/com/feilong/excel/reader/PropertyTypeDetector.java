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
package com.feilong.excel.reader;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.excel.DataConvertorConfigurator;
import com.feilong.excel.definition.ExcelCell;
import com.feilong.core.Validate;

import ognl.OgnlException;
import ognl.OgnlRuntime;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 3.0.0
 */
class PropertyTypeDetector{

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyTypeDetector.class);

    //---------------------------------------------------------------
    /** Don't let anyone instantiate this class. */
    private PropertyTypeDetector(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------
    /**
     * 获得 property type.
     *
     * @param object
     *            the object
     * @param excelCell
     *            the cell definition
     * @return the property type
     * @throws Exception
     *             the exception
     */
    static Class<?> detect(Object object,ExcelCell excelCell) throws Exception{
        Validate.notNull(object, "object can't be null!");

        //---------------------------------------------------------------
        //有type 先使用type  , 没有type 基于属性名字去探测
        String type = excelCell.getType();
        Class<?> clazz = DataConvertorConfigurator.getInstance().getSupportedClass(type);
        if (clazz != null){
            return clazz;
        }
        String dataName = excelCell.getDataName();
        return detect(object, dataName);
    }

    /**
     * 获得 property type.
     *
     * @param object
     *            the object
     * @param dataName
     *            the data name
     * @return the property type
     * @throws Exception
     *             the exception
     */
    private static Class<?> detect(Object object,String dataName) throws Exception{
        if (object instanceof Map){
            LOGGER.debug("detect for Map[{}] with Key {}.", object, dataName);
            Map<String, Object> map = (Map<String, Object>) object;
            int delim = dataName.indexOf('.');
            if (delim > 0){
                return detect(map.get(dataName.substring(0, delim)), dataName.substring(delim + 1));
            }
            return map.get(dataName).getClass();
        }

        //---------------------------------------------------------------
        LOGGER.trace("getPropertyType [{}] property {}.", object, dataName);
        return getPropertyTypeWithClass(object.getClass(), dataName);
    }

    //---------------------------------------------------------------

    /**
     * 获得 property type with class.
     *
     * @param clazz
     *            the clazz
     * @param dataName
     *            the data name
     * @return the property type with class
     * @throws Exception
     *             the exception
     */
    private static Class<? extends Object> getPropertyTypeWithClass(Class<? extends Object> clazz,String dataName) throws Exception{
        Validate.notNull(clazz, "clazz can't be null!");

        //---------------------------------------------------------------
        int delim = dataName.indexOf('.');
        if (delim < 0){
            return extracted(clazz, dataName);
        }
        Class<?> propertyType = extracted(clazz, dataName.substring(0, delim));
        return getPropertyTypeWithClass(propertyType, dataName.substring(delim + 1));
    }

    private static Class<?> extracted(Class<?> clazz,String dataName) throws IntrospectionException,OgnlException{
        PropertyDescriptor propertyDescriptor = OgnlRuntime.getPropertyDescriptor(clazz, dataName);
        Validate.notNull(propertyDescriptor, "propertyDescriptor can't be null!");
        return propertyDescriptor.getPropertyType();
    }
}
