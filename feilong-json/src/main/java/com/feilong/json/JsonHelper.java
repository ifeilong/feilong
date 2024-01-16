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
package com.feilong.json;

import static com.feilong.core.lang.ObjectUtil.defaultIfNull;
import static com.feilong.json.builder.JsonConfigBuilder.DEFAULT_JAVA_TO_JSON_CONFIG;
import static com.feilong.lib.json.ToStringUtil.ARRAY_END_CHAR;
import static com.feilong.lib.json.ToStringUtil.ARRAY_START_CHAR;
import static com.feilong.lib.json.ToStringUtil.OBJECT_END_CHAR;
import static com.feilong.lib.json.ToStringUtil.OBJECT_START_CHAR;

import java.util.Iterator;
import java.util.Map;

import com.feilong.core.lang.ClassUtil;
import com.feilong.core.lang.ObjectUtil;
import com.feilong.lib.collection4.IteratorUtils;
import com.feilong.lib.json.JSON;
import com.feilong.lib.json.JSONArray;
import com.feilong.lib.json.JSONArrayBuilder;
import com.feilong.lib.json.JSONObject;
import com.feilong.lib.json.JSONObjectBuilder;
import com.feilong.lib.json.JSONTokener;
import com.feilong.lib.json.JsonConfig;
import com.feilong.lib.json.processors.JsonValueProcessor;
import com.feilong.lib.json.util.JSONUtils;
import com.feilong.lib.lang3.ClassUtils;

/**
 * json处理的辅助类.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.9.4
 */
public final class JsonHelper{

    /** 单利模式. */
    private static final JsonConfig DEFAULT_JSON_CONFIG_INSTANCE = new JsonConfig();

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private JsonHelper(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    // [start]toJSON

    /**
     * 把实体Bean、Map对象、数组、列表集合转换成{@link JSON}.
     * 
     * <h3>说明:</h3>
     * 
     * <blockquote>
     * <ol>
     * <li>如果 <code>null==jsonConfig</code>,将使用 {@link #DEFAULT_JAVA_TO_JSON_CONFIG}</li>
     * 
     * <li>
     * 
     * <p>
     * 以下类型将转成{@link JSONArray}:
     * </p>
     * 
     * <ul>
     * <li>如果是字符串,当是以"["符号开头,"]"符号结尾的时候</li>
     * <li>数组 obj.getClass().isArray()||obj instanceof Object[]</li>
     * <li>集合 obj instanceof Collection</li>
     * <li>枚举 obj instanceof Enum</li>
     * <li>迭代器 obj instanceof Iterator</li>
     * </ul>
     * 
     * </li>
     * <li>其他类型转成 {@link JSONObject}</li>
     * </ol>
     * </blockquote>
     *
     * @param obj
     *            可以是数组,字符串,枚举,集合,map,Java bean,Iterator等类型,内部自动识别转成{@link JSONArray}还是{@link JSONObject}
     * @param jsonConfig
     *            the json config
     * @return the json
     * @see com.feilong.lib.json.util.JSONUtils#isArray(Object)
     * @see java.lang.Class#isEnum()
     * @see com.feilong.lib.json.JsonConfig#registerJsonValueProcessor(Class, JsonValueProcessor)
     * @see "org.apache.commons.collections4.IteratorUtils#toList(Iterator)"
     * @see "org.apache.commons.collections4.IteratorUtils#toList(Iterator, int)"
     * @see com.feilong.lib.json.JSONSerializer#toJSON(Object)
     */
    public static JSON toJSON(Object obj,JsonConfig jsonConfig){
        JsonConfig useJsonConfig = defaultIfNull(jsonConfig, DEFAULT_JAVA_TO_JSON_CONFIG);

        if (isNeedConvertToJSONArray(obj)){
            Object arrayJsonObject = obj instanceof Iterator//
                            ? IteratorUtils.toList((Iterator<?>) obj)
                            : obj;
            return toJSONArray(arrayJsonObject, useJsonConfig);
        }
        return toJSONObject(obj, useJsonConfig);
    }

    //---------------------------------------------------------------

    /**
     * 是否需要转成 {@link JSONArray}类型.
     * 
     * <h3>以下类型将转成{@link JSONArray}:</h3>
     * <blockquote>
     * <ol>
     * <li>如果是字符串,当是以"["符号开头,"]"符号结尾的时候</li>
     * <li>数组 obj.getClass().isArray()||obj instanceof Object[]</li>
     * <li>集合 obj instanceof Collection</li>
     * <li>枚举 obj instanceof Enum</li>
     * <li>迭代器 obj instanceof Iterator</li>
     * </ol>
     * </blockquote>
     *
     * @param obj
     *            the obj
     * @return true, if is need convert to JSON array
     * @see com.feilong.lib.json.util.JSONUtils#isArray(Object)
     */
    private static boolean isNeedConvertToJSONArray(Object obj){
        if (obj instanceof String){
            String str = (String) obj;
            if (str.startsWith(ARRAY_START_CHAR) && str.endsWith(ARRAY_END_CHAR)){// [] 格式的字符串 
                return true;
            }
        }
        return JSONUtils.isArray(obj) || //
                        obj instanceof Enum || // obj.getClass().isEnum()这么写 null会报错// object' is an Enum. Use JSONArray instead
                        obj instanceof Iterator;
    }

    //---------------------------------------------------------------

    /**
     * 是否是普通的字符串.
     * 
     * <blockquote>
     * <ol>
     * <li>如果不是字符串,返回 false</li>
     * 
     * <li>如果是字符串,以"["符号开头,"]"符号结尾的时候,返回 false</li>
     * <li>如果是字符串,以"{"符号开头,"}"符号结尾的时候,返回 false</li>
     * 
     * <li>其他, 返回 true</li>
     * </ol>
     * </blockquote>.
     *
     * @param obj
     *            the obj
     * @return true, if is common string
     * @since 1.14.0
     */
    public static boolean isCommonString(Object obj){
        if (!ClassUtil.isInstance(obj, String.class)){
            return false;
        }

        //---------------------------------------------------------------
        String str = (String) obj;
        if (str.startsWith(ARRAY_START_CHAR) && str.endsWith(ARRAY_END_CHAR)){// [] 格式的字符串 
            return false;
        }
        if (str.startsWith(OBJECT_START_CHAR) && str.endsWith(OBJECT_END_CHAR)){// {} 格式的字符串 
            return false;
        }
        return true;
    }

    /**
     * 是否是 <code>{}</code> key-value 格式的字符串.
     * 
     * <blockquote>
     * <ol>
     * <li>如果不是字符串,返回 false</li>
     * <li>如果是null,返回false</li>
     * <li>如果是字符串,以"{"符号开头,"}"符号结尾的时候,返回 true</li>
     * <li>其他,返回 false</li>
     * </ol>
     * </blockquote>.
     *
     * @param obj
     *            the obj
     * @return 如果是字符串,以"{"符号开头,"}"符号结尾的时候,返回 true
     * @since 3.0.0
     */
    static boolean isKeyValueJsonString(Object obj){
        if (!ClassUtil.isInstance(obj, String.class)){
            return false;
        }
        if (null == obj){
            return false;
        }

        //---------------------------------------------------------------
        String str = (String) obj;
        return str.startsWith(OBJECT_START_CHAR) && str.endsWith(OBJECT_END_CHAR);
    }

    // [end]

    //---------------------------------------------------------------

    /**
     * 将 <code>obj</code>转成 {@link JSONArray}.
     *
     * @param obj
     *            Accepts JSON formatted strings, arrays, Collections and Enums.
     * @param useJsonConfig
     *            如果是null,将使用 {@link #DEFAULT_JSON_CONFIG_INSTANCE}
     * @return the JSON array
     */
    static JSONArray toJSONArray(Object obj,JsonConfig useJsonConfig){
        return JSONArrayBuilder.build(obj, defaultIfNull(useJsonConfig, DEFAULT_JSON_CONFIG_INSTANCE));
    }

    //---------------------------------------------------------------
    //toJSONObject

    /**
     * 将 <code>object</code>转成 {@link JSONObject}.
     *
     * @param object
     *            可以是 <code>null</code>,{@link JSONObject},{@link org.apache.commons.beanutils.DynaBean} ,{@link JSONTokener},
     *            {@link Map},{@link String},<code>JavaBeans</code>
     * @param useJsonConfig
     *            如果是null,将使用 {@link #DEFAULT_JSON_CONFIG_INSTANCE}
     * @return the JSON object
     */
    static JSONObject toJSONObject(Object object,JsonConfig useJsonConfig){
        return JSONObjectBuilder.build(object, defaultIfNull(useJsonConfig, DEFAULT_JSON_CONFIG_INSTANCE));
    }

    //---------------------------------------------------------------

    /**
     * 是否允许被json format的类型.
     * 
     * <h3>如果是以下类型是被允许的:</h3>
     * <blockquote>
     * <ol>
     * <li>{@link ClassUtils#isPrimitiveOrWrapper(Class)}</li>
     * <li>{@link String}</li>
     * <li>{@link ObjectUtil#isArray(Object)}</li>
     * <li>{@link ClassUtil#isInstanceAnyClass(Object, Class...)}</li>
     * </ol>
     * </blockquote>
     *
     * @param <V>
     *            the value type
     * @param value
     *            the value
     * @param allowClassTypes
     *            the allow class types
     * @return true, if checks if is allow format type
     */
    static <V> boolean isAllowFormatType(V value,Class<?>...allowClassTypes){
        if (null == value){//null 是可以 format的
            return true;
        }

        //---------------------------------------------------------------
        Class<?> klassClass = value.getClass();
        return ClassUtils.isPrimitiveOrWrapper(klassClass) //
                        || String.class == klassClass //
                        || ObjectUtil.isArray(value)//XXX 数组一般 是可以转换的 
                        || ClassUtil.isInstanceAnyClass(value, allowClassTypes)//
        ;
    }
}
