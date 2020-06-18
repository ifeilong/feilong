/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.feilong.lib.beanutils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

/**
 * Utility methods for using Java Reflection APIs to facilitate generic
 * property getter and setter operations on Java objects.
 *
 * <p>
 * The implementations for these methods are provided by <code>PropertyUtilsBean</code>.
 * For more details see {@link PropertyUtilsBean}.
 * </p>
 *
 * @version $Id$
 * @see PropertyUtilsBean
 * @see com.feilong.lib.beanutils.expression.Resolver
 */
public class PropertyUtils{

    /**
     * <p>
     * Copy property values from the "origin" bean to the "destination" bean
     * for all cases where the property names are the same (even though the
     * actual getter and setter methods might have been customized via
     * <code>BeanInfo</code> classes).
     * </p>
     *
     * <p>
     * For more details see <code>PropertyUtilsBean</code>.
     * </p>
     *
     * @param dest
     *            Destination bean whose properties are modified
     * @param orig
     *            Origin bean whose properties are retrieved
     *
     * @throws IllegalAccessException
     *             if the caller does not have
     *             access to the property accessor method
     * @throws IllegalArgumentException
     *             if the <code>dest</code> or
     *             <code>orig</code> argument is null
     * @throws InvocationTargetException
     *             if the property accessor method
     *             throws an exception
     * @throws NoSuchMethodException
     *             if an accessor method for this
     *             propety cannot be found
     * @see PropertyUtilsBean#copyProperties
     */
    public static void copyProperties(final Object dest,final Object orig)
                    throws IllegalAccessException,InvocationTargetException,NoSuchMethodException{
        PropertyUtilsBean.getInstance().copyProperties(dest, orig);
    }

    /**
     * <p>
     * Return the entire set of properties for which the specified bean
     * provides a read method.
     * </p>
     *
     * <p>
     * For more details see <code>PropertyUtilsBean</code>.
     * </p>
     *
     * @param bean
     *            Bean whose properties are to be extracted
     * @return The set of properties for the bean
     *
     * @throws IllegalAccessException
     *             if the caller does not have
     *             access to the property accessor method
     * @throws IllegalArgumentException
     *             if <code>bean</code> is null
     * @throws InvocationTargetException
     *             if the property accessor method
     *             throws an exception
     * @throws NoSuchMethodException
     *             if an accessor method for this
     *             propety cannot be found
     * @see PropertyUtilsBean#describe
     */
    public static Map<String, Object> describe(final Object bean)
                    throws IllegalAccessException,InvocationTargetException,NoSuchMethodException{
        return (PropertyUtilsBean.getInstance().describe(bean));
    }

    /**
     * <p>
     * Return the mapped property descriptors for this bean class.
     * </p>
     *
     * <p>
     * For more details see <code>PropertyUtilsBean</code>.
     * </p>
     *
     * @param beanClass
     *            Bean class to be introspected
     * @return the mapped property descriptors
     * @see PropertyUtilsBean#getMappedPropertyDescriptors(Class)
     * @deprecated This method should not be exposed
     */
    @Deprecated
    public static FastHashMap getMappedPropertyDescriptors(final Class<?> beanClass){
        return PropertyUtilsBean.getInstance().getMappedPropertyDescriptors(beanClass);
    }

    /**
     * <p>
     * Return the value of the specified property of the specified bean,
     * no matter which property reference format is used, with no
     * type conversions.
     * </p>
     *
     * <p>
     * For more details see <code>PropertyUtilsBean</code>.
     * </p>
     *
     * @param bean
     *            Bean whose property is to be extracted
     * @param name
     *            Possibly indexed and/or nested name of the property
     *            to be extracted
     * @return the property value
     *
     * @throws IllegalAccessException
     *             if the caller does not have
     *             access to the property accessor method
     * @throws IllegalArgumentException
     *             if <code>bean</code> or
     *             <code>name</code> is null
     * @throws InvocationTargetException
     *             if the property accessor method
     *             throws an exception
     * @throws NoSuchMethodException
     *             if an accessor method for this
     *             propety cannot be found
     * @see PropertyUtilsBean#getProperty
     */
    public static Object getProperty(final Object bean,final String name)
                    throws IllegalAccessException,InvocationTargetException,NoSuchMethodException{
        return (PropertyUtilsBean.getInstance().getProperty(bean, name));
    }

    //---------------------------------------------------------------

    /**
     * <p>
     * Retrieve the property descriptor for the specified property of the
     * specified bean, or return <code>null</code> if there is no such
     * descriptor.
     * </p>
     *
     * <p>
     * For more details see <code>PropertyUtilsBean</code>.
     * </p>
     *
     * @param bean
     *            Bean for which a property descriptor is requested
     * @param name
     *            Possibly indexed and/or nested name of the property for
     *            which a property descriptor is requested
     * @return the property descriptor
     *
     * @throws IllegalAccessException
     *             if the caller does not have
     *             access to the property accessor method
     * @throws IllegalArgumentException
     *             if <code>bean</code> or
     *             <code>name</code> is null
     * @throws IllegalArgumentException
     *             if a nested reference to a
     *             property returns null
     * @throws InvocationTargetException
     *             if the property accessor method
     *             throws an exception
     * @throws NoSuchMethodException
     *             if an accessor method for this
     *             propety cannot be found
     * @see PropertyUtilsBean#getPropertyDescriptor
     */
    public static PropertyDescriptor getPropertyDescriptor(final Object bean,final String name)
                    throws IllegalAccessException,InvocationTargetException,NoSuchMethodException{
        return PropertyUtilsBean.getInstance().getPropertyDescriptor(bean, name);
    }

    /**
     * <p>
     * Retrieve the property descriptors for the specified class,
     * introspecting and caching them the first time a particular bean class
     * is encountered.
     * </p>
     *
     * <p>
     * For more details see <code>PropertyUtilsBean</code>.
     * </p>
     *
     * @param beanClass
     *            Bean class for which property descriptors are requested
     * @return the property descriptors
     * @throws IllegalArgumentException
     *             if <code>beanClass</code> is null
     * @see PropertyUtilsBean#getPropertyDescriptors(Class)
     */
    public static PropertyDescriptor[] getPropertyDescriptors(final Class<?> beanClass){
        return PropertyUtilsBean.getInstance().getPropertyDescriptors(beanClass);
    }

    //---------------------------------------------------------------

    /**
     * <p>
     * Return an accessible property getter method for this property,
     * if there is one; otherwise return <code>null</code>.
     * </p>
     *
     * <p>
     * For more details see <code>PropertyUtilsBean</code>.
     * </p>
     *
     * @param descriptor
     *            Property descriptor to return a getter for
     * @return The read method
     * @see PropertyUtilsBean#getReadMethod(PropertyDescriptor)
     */
    public static Method getReadMethod(final PropertyDescriptor descriptor){
        return (PropertyUtilsBean.getInstance().getReadMethod(descriptor));
    }

    /**
     * <p>
     * Return the value of the specified simple property of the specified
     * bean, with no type conversions.
     * </p>
     *
     * <p>
     * For more details see <code>PropertyUtilsBean</code>.
     * </p>
     *
     * @param bean
     *            Bean whose property is to be extracted
     * @param name
     *            Name of the property to be extracted
     * @return The property value
     *
     * @throws IllegalAccessException
     *             if the caller does not have
     *             access to the property accessor method
     * @throws IllegalArgumentException
     *             if <code>bean</code> or
     *             <code>name</code> is null
     * @throws IllegalArgumentException
     *             if the property name
     *             is nested or indexed
     * @throws InvocationTargetException
     *             if the property accessor method
     *             throws an exception
     * @throws NoSuchMethodException
     *             if an accessor method for this
     *             propety cannot be found
     * @see PropertyUtilsBean#getSimpleProperty
     */
    public static Object getSimpleProperty(final Object bean,final String name)
                    throws IllegalAccessException,InvocationTargetException,NoSuchMethodException{
        return PropertyUtilsBean.getInstance().getSimpleProperty(bean, name);
    }

    /**
     * <p>
     * Return an accessible property setter method for this property,
     * if there is one; otherwise return <code>null</code>.
     * </p>
     *
     * <p>
     * For more details see <code>PropertyUtilsBean</code>.
     * </p>
     *
     * @param descriptor
     *            Property descriptor to return a setter for
     * @return The write method
     * @see PropertyUtilsBean#getWriteMethod(PropertyDescriptor)
     */
    public static Method getWriteMethod(final PropertyDescriptor descriptor){
        return PropertyUtilsBean.getInstance().getWriteMethod(descriptor);
    }

    /**
     * <p>
     * Return <code>true</code> if the specified property name identifies
     * a writeable property on the specified bean; otherwise, return
     * <code>false</code>.
     * </p>
     *
     * <p>
     * For more details see <code>PropertyUtilsBean</code>.
     * </p>
     *
     * @param bean
     *            Bean to be examined (may be a {@link DynaBean}
     * @param name
     *            Property name to be evaluated
     * @return <code>true</code> if the property is writeable,
     *         otherwise <code>false</code>
     *
     * @throws IllegalArgumentException
     *             if <code>bean</code>
     *             or <code>name</code> is <code>null</code>
     * @see PropertyUtilsBean#isWriteable
     * @since BeanUtils 1.6
     */
    public static boolean isWriteable(final Object bean,final String name){
        return PropertyUtilsBean.getInstance().isWriteable(bean, name);
    }

    /**
     * <p>
     * Set the value of the specified property of the specified bean,
     * no matter which property reference format is used, with no
     * type conversions.
     * </p>
     *
     * <p>
     * For more details see <code>PropertyUtilsBean</code>.
     * </p>
     *
     * @param bean
     *            Bean whose property is to be modified
     * @param name
     *            Possibly indexed and/or nested name of the property
     *            to be modified
     * @param value
     *            Value to which this property is to be set
     *
     * @throws IllegalAccessException
     *             if the caller does not have
     *             access to the property accessor method
     * @throws IllegalArgumentException
     *             if <code>bean</code> or
     *             <code>name</code> is null
     * @throws InvocationTargetException
     *             if the property accessor method
     *             throws an exception
     * @throws NoSuchMethodException
     *             if an accessor method for this
     *             propety cannot be found
     * @see PropertyUtilsBean#setProperty
     */
    public static void setProperty(final Object bean,final String name,final Object value)
                    throws IllegalAccessException,InvocationTargetException,NoSuchMethodException{
        PropertyUtilsBean.getInstance().setProperty(bean, name, value);
    }

    /**
     * <p>
     * Set the value of the specified simple property of the specified bean,
     * with no type conversions.
     * </p>
     *
     * <p>
     * For more details see <code>PropertyUtilsBean</code>.
     * </p>
     *
     * @param bean
     *            Bean whose property is to be modified
     * @param name
     *            Name of the property to be modified
     * @param value
     *            Value to which the property should be set
     *
     * @throws IllegalAccessException
     *             if the caller does not have
     *             access to the property accessor method
     * @throws IllegalArgumentException
     *             if <code>bean</code> or
     *             <code>name</code> is null
     * @throws IllegalArgumentException
     *             if the property name is
     *             nested or indexed
     * @throws InvocationTargetException
     *             if the property accessor method
     *             throws an exception
     * @throws NoSuchMethodException
     *             if an accessor method for this
     *             propety cannot be found
     * @see PropertyUtilsBean#setSimpleProperty
     */
    public static void setSimpleProperty(final Object bean,final String name,final Object value)
                    throws IllegalAccessException,InvocationTargetException,NoSuchMethodException{
        PropertyUtilsBean.getInstance().setSimpleProperty(bean, name, value);
    }

}
