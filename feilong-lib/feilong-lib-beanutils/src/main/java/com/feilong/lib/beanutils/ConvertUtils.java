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

/**
 * Utility methods for converting String scalar values to objects of the
 * specified Class, String arrays to arrays of the specified Class.
 *
 * <p>
 * For more details, see <code>ConvertUtilsBean</code> which provides the
 * implementations for these methods.
 * </p>
 *
 * @version $Id$
 * @see ConvertUtilsBean
 */
public class ConvertUtils{

    // --------------------------------------------------------- Public Classes

    /**
     * Convert the specified value into a String.
     *
     * <p>
     * For more details see <code>ConvertUtilsBean</code>.
     * </p>
     *
     * @param value
     *            Value to be converted (may be null)
     * @return The converted String value or null if value is null
     *
     * @see ConvertUtilsBean#convert(Object)
     */
    public static String convert(final Object value){
        return ConvertUtilsBean.getInstance().convert(value);
    }

    /**
     * Convert the specified value to an object of the specified class (if possible). Otherwise, return a String representation of the
     * value.
     *
     * <p>
     * For more details see <code>ConvertUtilsBean</code>.
     * </p>
     *
     * @param value
     *            Value to be converted (may be null)
     * @param clazz
     *            Java class to be converted to (must not be null)
     * @return The converted value
     *
     * @see ConvertUtilsBean#convert(String, Class)
     */
    public static Object convert(final String value,final Class<?> clazz){
        return ConvertUtilsBean.getInstance().convert(value, clazz);
    }

    /**
     * Convert an array of specified values to an array of objects of the specified class (if possible).
     *
     * <p>
     * For more details see <code>ConvertUtilsBean</code>.
     * </p>
     *
     * @param values
     *            Array of values to be converted
     * @param clazz
     *            Java array or element class to be converted to (must not be null)
     * @return The converted value
     *
     * @see ConvertUtilsBean#convert(String[], Class)
     */
    public static Object convert(final String[] values,final Class<?> clazz){
        return ConvertUtilsBean.getInstance().convert(values, clazz);
    }

    /**
     * Convert the value to an object of the specified class (if possible).
     *
     * @param value
     *            Value to be converted (may be null)
     * @param targetType
     *            Class of the value to be converted to (must not be null)
     * @return The converted value
     *
     * @throws ConversionException
     *             if thrown by an underlying Converter
     */
    public static Object convert(final Object value,final Class<?> targetType){
        return ConvertUtilsBean.getInstance().convert(value, targetType);
    }

    /**
     * <p>
     * Remove all registered {@link Converter}s, and re-establish the standard Converters.
     * </p>
     *
     * <p>
     * For more details see <code>ConvertUtilsBean</code>.
     * </p>
     *
     * @see ConvertUtilsBean#deregister()
     * @deprecated 这种会影响全局, 后面版本会去掉,暂时没有替换的方法保留
     */
    @Deprecated
    public static void deregister(){
        ConvertUtilsBean.getInstance().deregister();
    }

    /**
     * <p>
     * Remove any registered {@link Converter} for the specified destination
     * <code>Class</code>.
     * </p>
     *
     * <p>
     * For more details see <code>ConvertUtilsBean</code>.
     * </p>
     *
     * @param clazz
     *            Class for which to remove a registered Converter
     * @see ConvertUtilsBean#deregister(Class)
     * @deprecated 这种会影响全局, 后面版本会去掉,暂时没有替换的方法保留
     */
    @Deprecated
    public static void deregister(final Class<?> clazz){
        ConvertUtilsBean.getInstance().deregister(clazz);
    }

    /**
     * Look up and return any registered {@link Converter} for the specified
     * destination class; if there is no registered Converter, return
     * <code>null</code>.
     *
     * <p>
     * For more details see <code>ConvertUtilsBean</code>.
     * </p>
     *
     * @param clazz
     *            Class for which to return a registered Converter
     * @return The registered {@link Converter} or <code>null</code> if not found
     * @see ConvertUtilsBean#lookup(Class)
     */
    public static Converter lookup(final Class<?> clazz){
        return ConvertUtilsBean.getInstance().lookup(clazz);
    }

    /**
     * <p>
     * Register a custom {@link Converter} for the specified destination
     * <code>Class</code>, replacing any previously registered Converter.
     * </p>
     *
     * <p>
     * For more details see <code>ConvertUtilsBean</code>.
     * </p>
     *
     * @param converter
     *            Converter to be registered
     * @param clazz
     *            Destination class for conversions performed by this
     *            Converter
     * @see ConvertUtilsBean#register(Converter, Class)
     * @deprecated 这种会影响全局, 后面版本会去掉,暂时没有替换的方法保留
     */
    @Deprecated
    public static void register(final Converter converter,final Class<?> clazz){
        ConvertUtilsBean.getInstance().register(converter, clazz);
    }

    /**
     * Change primitive Class types to the associated wrapper class. This is
     * useful for concrete converter implementations which typically treat
     * primitive types like their corresponding wrapper types.
     *
     * @param <T>
     *            The type to be checked.
     * @param type
     *            The class type to check.
     * @return The converted type.
     * @since 1.9
     */
    // All type casts are safe because the TYPE members of the wrapper types
    // return their own class.
    @SuppressWarnings("unchecked")
    public static <T> Class<T> primitiveToWrapper(final Class<T> type){
        if (type == null || !type.isPrimitive()){
            return type;
        }

        if (type == Integer.TYPE){
            return (Class<T>) Integer.class;
        }else if (type == Double.TYPE){
            return (Class<T>) Double.class;
        }else if (type == Long.TYPE){
            return (Class<T>) Long.class;
        }else if (type == Boolean.TYPE){
            return (Class<T>) Boolean.class;
        }else if (type == Float.TYPE){
            return (Class<T>) Float.class;
        }else if (type == Short.TYPE){
            return (Class<T>) Short.class;
        }else if (type == Byte.TYPE){
            return (Class<T>) Byte.class;
        }else if (type == Character.TYPE){
            return (Class<T>) Character.class;
        }else{
            return type;
        }
    }
}
