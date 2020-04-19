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
package com.feilong.json.jsonlib;

import static com.feilong.json.jsonlib.builder.JsonConfigBuilder.DEFAULT_JAVA_TO_JSON_CONFIG;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.lang3.ClassUtils;

import com.feilong.core.lang.ClassUtil;
import com.feilong.core.lang.ObjectUtil;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import net.sf.json.JSONString;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import net.sf.json.util.JSONTokener;
import net.sf.json.util.JSONUtils;

/**
 * json处理的辅助类.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
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

    /**
     * 转换value的值.
     *
     * @param <T>
     *            the generic type
     * @param value
     *            the value
     * @param jsonToJavaConfig
     *            the json to java config
     * @return 如果 value 是 {@link JSONNull#getInstance()} ,那么返回null,<br>
     *         如果null == jsonToJavaConfig 或者 null == jsonToJavaConfig.getRootClass() 返回value,<br>
     *         否则,使用 {@link JsonUtil#toBean(Object, JsonToJavaConfig)} 转成对应的bean
     */
    @SuppressWarnings("unchecked")
    static <T> T transformerValue(Object value,JsonToJavaConfig jsonToJavaConfig){
        if (JSONNull.getInstance().equals(value)){
            return null;
        }
        //如果rootClass是null,表示不需要转换
        boolean noRootClass = null == jsonToJavaConfig || null == jsonToJavaConfig.getRootClass();
        return noRootClass ? (T) value : JsonUtil.<T> toBean(value, jsonToJavaConfig);
    }
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
     * @see net.sf.json.JSONArray#fromObject(Object, JsonConfig)
     * @see net.sf.json.JSONObject#fromObject(Object, JsonConfig)
     * @see net.sf.json.util.JSONUtils#isArray(Object)
     * @see java.lang.Class#isEnum()
     * @see net.sf.json.JsonConfig#registerJsonValueProcessor(Class, JsonValueProcessor)
     * @see org.apache.commons.collections4.IteratorUtils#toList(Iterator)
     * @see org.apache.commons.collections4.IteratorUtils#toList(Iterator, int)
     * @see net.sf.json.JSONSerializer#toJSON(Object)
     */
    static JSON toJSON(Object obj,JsonConfig jsonConfig){
        JsonConfig useJsonConfig = defaultIfNull(jsonConfig, DEFAULT_JAVA_TO_JSON_CONFIG);

        if (isNeedConvertToJSONArray(obj)){
            Object arrayJsonObject = obj instanceof Iterator ? IteratorUtils.toList((Iterator<?>) obj) : obj;
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
     * @see net.sf.json.JSONArray#_fromJSONTokener(net.sf.json.util.JSONTokener, JsonConfig)
     * @see net.sf.json.util.JSONUtils#isArray(Object)
     */
    private static boolean isNeedConvertToJSONArray(Object obj){
        if (obj instanceof String){
            String str = (String) obj;
            if (str.startsWith("[") && str.endsWith("]")){// [] 格式的字符串 
                return true;
            }
        }
        return JSONUtils.isArray(obj) || //obj.getClass().isArray() || obj instanceof Collection || obj instanceof Object[]
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
        if (str.startsWith("[") && str.endsWith("]")){// [] 格式的字符串 
            return false;
        }
        if (str.startsWith("{") && str.endsWith("}")){// {} 格式的字符串 
            return false;
        }
        return true;
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
     * @see net.sf.json.JSONArray#fromObject(Object, JsonConfig)
     */
    static JSONArray toJSONArray(Object obj,JsonConfig useJsonConfig){
        return JSONArray.fromObject(obj, defaultIfNull(useJsonConfig, DEFAULT_JSON_CONFIG_INSTANCE));
    }

    //---------------------------------------------------------------
    //toJSONObject

    /**
     * 将 <code>object</code>转成 {@link JSONObject}.
     *
     * @param object
     *            可以是 <code>null</code>,{@link JSONObject},{@link DynaBean} ,{@link JSONTokener},{@link JSONString},
     *            {@link Map},{@link String},<code>JavaBeans</code>
     * @param useJsonConfig
     *            如果是null,将使用 {@link #DEFAULT_JSON_CONFIG_INSTANCE}
     * @return the JSON object
     * @see net.sf.json.JSONObject#fromObject(Object, JsonConfig)
     */
    static JSONObject toJSONObject(Object object,JsonConfig useJsonConfig){
        return JSONObject.fromObject(object, defaultIfNull(useJsonConfig, DEFAULT_JSON_CONFIG_INSTANCE));
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
