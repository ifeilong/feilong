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
package com.feilong.json.builder;

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.toMapUseEntrys;

import java.io.File;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.Map;

import com.feilong.core.DatePattern;
import com.feilong.json.JavaToJsonConfig;
import com.feilong.json.processor.CalendarJsonValueProcessor;
import com.feilong.json.processor.DateJsonValueProcessor;
import com.feilong.json.processor.SensitiveWordsJsonValueProcessor;
import com.feilong.json.processor.ToStringJsonValueProcessor;
import com.feilong.lib.json.JsonConfig;
import com.feilong.lib.json.processors.JsonValueProcessor;
import com.feilong.lib.json.processors.PropertyNameProcessor;
import com.feilong.lib.json.util.CycleDetectionStrategy;
import com.feilong.lib.json.util.PropertyFilter;
import com.feilong.lib.lang3.tuple.Pair;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * {@link JsonConfig} 构造器.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.10.3
 * @since 1.11.0 change package
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JsonConfigBuilder{

    /**
     * The Constant SENSITIVE_WORDS_PROPERTY_NAMES.
     * 
     * @since 1.12.6 move from JsonHelper
     */
    private static final String[]                          SENSITIVE_WORDS_PROPERTY_NAMES     = { "password", "key" };

    /**
     * 直接转成 string 字符串的类型.
     * 
     * @since 3.0.7
     */
    private static final Class<?>[]                        TO_STRING_JSON_VALUE_CLASSS        = {
                                                                                                  //since 1.13.0                                                                                   
                                                                                                  File.class,

                                                                                                  //since 1.13.0
                                                                                                  //see https://github.com/venusdrogon/feilong-json/issues/24
                                                                                                  BigDecimal.class,

                                                                                                  //3.0.7 https://github.com/ifeilong/feilong/issues/282
                                                                                                  URL.class,
                                                                                                  URI.class,

                                                                                                  //since 1.14.0 
                                                                                                  //@see https://github.com/venusdrogon/feilong-json/issues/29 json format 支持 javax.xml.datatype.XMLGregorianCalendar
                                                                                                  //@see com.fasterxml.jackson.databind.ext.CoreXMLSerializers.XMLGregorianCalendarSerializer   
                                                                                                  javax.xml.datatype.XMLGregorianCalendar.class

    };

    //---------------------------------------------------------------

    /**
     * 初始化
     * 
     * @since 3.0.0
     */
    private static final Map<Class<?>, JsonValueProcessor> DEFAULT_CLASS_JSON_VALUE_PROCESSOR = toMapUseEntrys(                                                                                                      //
                    // 注册日期处理器
                    Pair.of(Date.class, DateJsonValueProcessor.DEFAULT_INSTANCE),

                    //since 1.14.0 
                    //https://github.com/venusdrogon/feilong-json/issues/32 优化对 Calendar 的 format #32
                    Pair.of(java.util.Calendar.class, CalendarJsonValueProcessor.DEFAULT_INSTANCE));

    static{
        for (Class<?> klass : TO_STRING_JSON_VALUE_CLASSS){
            DEFAULT_CLASS_JSON_VALUE_PROCESSOR.put(klass, ToStringJsonValueProcessor.DEFAULT_INSTANCE);
        }
    }

    //---------------------------------------------------------------

    /** The Constant DEFAULT_JAVA_TO_JSON_CONFIG. */
    public static final JsonConfig DEFAULT_JAVA_TO_JSON_CONFIG = buildDefaultJavaToJsonConfig();

    //---------------------------------------------------------------

    /**
     * Builds the.
     *
     * @param obj
     *            the obj
     * @param javaToJsonConfig
     *            the java to json config
     * @return 如果 <code>javaToJsonConfig</code> 是null,返回 {@link #DEFAULT_JAVA_TO_JSON_CONFIG}<br>
     */
    public static JsonConfig build(Object obj,JavaToJsonConfig javaToJsonConfig){
        JavaToJsonConfig useJavaToJsonConfig = JavaToJsonConfigBuilder.buildUseJavaToJsonConfig(obj, javaToJsonConfig);

        //since 1.12.6
        if (null == useJavaToJsonConfig){
            return DEFAULT_JAVA_TO_JSON_CONFIG;
        }

        //-----------------------------------------------------------------
        JsonConfig jsonConfig = buildDefaultJavaToJsonConfig();

        //---------------------------------------------------------------
        //property name处理器.
        registerJsonPropertyNameProcessor(useJavaToJsonConfig, jsonConfig);

        //value处理器
        registerJsonValueProcessor(useJavaToJsonConfig, jsonConfig);

        //---------------------------------------------------------------

        //排除
        String[] excludes = useJavaToJsonConfig.getExcludes();
        if (isNotNullOrEmpty(excludes)){
            jsonConfig.setExcludes(excludes);
        }

        //---------------------------------------------------------------
        //since 2.0.0
        PropertyFilter propertyFilter = JsonPropertyFilterBuilder.build(useJavaToJsonConfig);
        if (null != propertyFilter){
            jsonConfig.setJsonPropertyFilter(propertyFilter);
        }

        //---------------------------------------------------------------
        //since 1.12.6
        if (useJavaToJsonConfig.getIsMaskDefaultSensitiveWords()){
            registerDefaultJsonValueProcessor(jsonConfig);
        }
        return jsonConfig;
    }

    //---------------------------------------------------------------

    /**
     * 默认的处理器.
     *
     * @param jsonConfig
     *            the json config
     * @since 1.12.6 move from JsonHelper
     */
    private static void registerDefaultJsonValueProcessor(JsonConfig jsonConfig){
        for (String propertyName : SENSITIVE_WORDS_PROPERTY_NAMES){
            jsonConfig.registerJsonValueProcessor(propertyName, SensitiveWordsJsonValueProcessor.INSTANCE);
        }
    }

    /**
     * Register json value processor.
     *
     * @param useJavaToJsonConfig
     *            the use java to json config
     * @param jsonConfig
     *            the json config
     */
    private static void registerJsonValueProcessor(JavaToJsonConfig useJavaToJsonConfig,JsonConfig jsonConfig){
        Map<String, ? extends JsonValueProcessor> propertyNameAndJsonValueProcessorMap = useJavaToJsonConfig
                        .getPropertyNameAndJsonValueProcessorMap();
        if (isNotNullOrEmpty(propertyNameAndJsonValueProcessorMap)){
            for (Map.Entry<String, ? extends JsonValueProcessor> entry : propertyNameAndJsonValueProcessorMap.entrySet()){
                jsonConfig.registerJsonValueProcessor(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * property name处理器.
     *
     * @param useJavaToJsonConfig
     *            the use java to json config
     * @param jsonConfig
     *            the json config
     */
    private static void registerJsonPropertyNameProcessor(JavaToJsonConfig useJavaToJsonConfig,JsonConfig jsonConfig){
        //property name处理器
        Map<Class<?>, PropertyNameProcessor> targetClassAndPropertyNameProcessorMap = useJavaToJsonConfig
                        .getJsonTargetClassAndPropertyNameProcessorMap();
        if (isNotNullOrEmpty(targetClassAndPropertyNameProcessorMap)){
            for (Map.Entry<Class<?>, PropertyNameProcessor> entry : targetClassAndPropertyNameProcessorMap.entrySet()){
                jsonConfig.registerJsonPropertyNameProcessor(entry.getKey(), entry.getValue());
            }
        }
    }

    //---------------------------------------------------------------

    /**
     * 默认的java to json JsonConfig.
     * 
     * <h3>含有以下的特性:</h3>
     * <blockquote>
     * <ol>
     * <li>{@link CycleDetectionStrategy#LENIENT} 避免循环引用</li>
     * 
     * <li>no IgnoreDefaultExcludes,默认过滤几个key "class", "declaringClass","metaClass"</li>
     * 
     * <li>
     * {@link DateJsonValueProcessor},如果是日期,自动渲染成 {@link DatePattern#COMMON_DATE_AND_TIME} 格式类型,如有需要可以使用
     * {@link JavaToJsonConfig#setPropertyNameAndJsonValueProcessorMap(Map)}覆盖此属性
     * </li>
     * 
     * <li>AllowNonStringKeys,允许非 string类型的key</li>
     * </ol>
     * </blockquote>
     *
     * @return the default json config
     * @see see net.sf.json.JsonConfig#DEFAULT_EXCLUDES
     * @see com.feilong.lib.json.util.CycleDetectionStrategy#LENIENT
     * 
     * @see <a href="http://feitianbenyue.iteye.com/blog/2046877">通过setAllowNonStringKeys解决java.lang.ClassCastException: JSON keys must be
     *      strings</a>
     */
    static JsonConfig buildDefaultJavaToJsonConfig(){
        JsonConfig jsonConfig = new JsonConfig();
        for (Map.Entry<Class<?>, JsonValueProcessor> entry : DEFAULT_CLASS_JSON_VALUE_PROCESSOR.entrySet()){
            jsonConfig.registerJsonValueProcessor(entry.getKey(), entry.getValue());
        }
        return jsonConfig;
    }
}
