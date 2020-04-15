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
package com.feilong.json.jsonlib.builder;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.lang.ObjectUtil.defaultIfNullOrEmpty;

import java.util.Map;

import com.feilong.core.util.MapUtil;
import com.feilong.json.SensitiveWords;
import com.feilong.json.jsonlib.JavaToJsonConfig;
import com.feilong.json.jsonlib.processor.SensitiveWordsJsonValueProcessor;

import net.sf.json.processors.JsonValueProcessor;

/**
 * {@link JavaToJsonConfig} 构造器.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.3
 * @since 1.11.0 change package
 */
public final class JavaToJsonConfigBuilder{

    /** Don't let anyone instantiate this class. */
    private JavaToJsonConfigBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 构造使用的 {@link JavaToJsonConfig}.
     * 
     * <h3>代码流程:</h3>
     * <blockquote>
     * <ol>
     * <li>如果传入的javaToJsonConfig , 那么就是用默认的 defaultJavaToJsonConfig,don't care defaultJavaToJsonConfig 是否是null</li>
     * <li>如果 默认的 defaultJavaToJsonConfig 是 null, 那么就不合并,直接返回 javaToJsonConfig</li>
     * <li>否则合并</li>
     * </ol>
     * </blockquote>
     *
     * @param obj
     *            the obj
     * @param javaToJsonConfig
     *            the java to json config
     * @return 如果 javaToJsonConfig 是null,且构造的defaultJavaToJsonConfig 也是null 将会返回null
     * @since 1.11.5
     */
    public static JavaToJsonConfig buildUseJavaToJsonConfig(Object obj,JavaToJsonConfig javaToJsonConfig){
        JavaToJsonConfig defaultJavaToJsonConfig = JavaToJsonConfigBuilder.buildDefaultJavaToJsonConfig(obj);

        //如果传入的javaToJsonConfig 是 null, 那么就是用默认的 defaultJavaToJsonConfig,don't care defaultJavaToJsonConfig 是否是null
        if (isNullOrEmpty(javaToJsonConfig)){
            return defaultJavaToJsonConfig;
        }

        //如果 默认的 defaultJavaToJsonConfig 是 null, 那么就不合并,直接返回 javaToJsonConfig
        if (null == defaultJavaToJsonConfig){
            return javaToJsonConfig;
        }

        //否则合并
        return merge(defaultJavaToJsonConfig, javaToJsonConfig);
    }

    //---------------------------------------------------------------

    /**
     * 构造默认的 {@link JavaToJsonConfig}.
     *
     * @param javaBean
     *            the obj
     * @return 取到该javabean的Field,解析是否有 {@link SensitiveWords}注解,如果有,那么添加 {@link SensitiveWordsJsonValueProcessor}
     */
    public static JavaToJsonConfig buildDefaultJavaToJsonConfig(Object javaBean){
        Map<String, JsonValueProcessor> propertyNameAndJsonValueProcessorMap = SensitiveWordsPropertyNameAndJsonValueProcessorMapBuilder
                        .build(javaBean);
        return isNullOrEmpty(propertyNameAndJsonValueProcessorMap) ? null : new JavaToJsonConfig(propertyNameAndJsonValueProcessorMap);
    }

    //---------------------------------------------------------------

    /**
     * Builds the java to json config.
     *
     * @param excludes
     *            the excludes
     * @param includes
     *            the includes
     * @return 如果<code>excludes</code>是null或者是empty,并且<code>includes</code>是null或者是empty将返回null
     * @since 1.11.5 move from JsonHelper and rename
     */
    public static JavaToJsonConfig build(String[] excludes,String[] includes){
        boolean noNeedBuild = isNullOrEmpty(excludes) && isNullOrEmpty(includes);
        return noNeedBuild ? null : new JavaToJsonConfig(excludes, includes);
    }
    //---------------------------------------------------------------

    /**
     * Merge.
     *
     * @param defaultJavaToJsonConfig
     *            the default java to json config
     * @param javaToJsonConfig
     *            the java to json config
     * @return the java to json config
     * @since 1.11.5
     */
    private static JavaToJsonConfig merge(JavaToJsonConfig defaultJavaToJsonConfig,JavaToJsonConfig javaToJsonConfig){
        //        defaultJavaToJsonConfig.getExcludes();
        //        defaultJavaToJsonConfig.getIncludes();
        //        defaultJavaToJsonConfig.getJsonTargetClassAndPropertyNameProcessorMap();

        Map<String, JsonValueProcessor> propertyNameAndJsonValueProcessorMap = defaultIfNullOrEmpty(
                        javaToJsonConfig.getPropertyNameAndJsonValueProcessorMap(),
                        MapUtil.<String, JsonValueProcessor> newHashMap());

        MapUtil.putAllIfNotNull(propertyNameAndJsonValueProcessorMap, defaultJavaToJsonConfig.getPropertyNameAndJsonValueProcessorMap());

        javaToJsonConfig.setPropertyNameAndJsonValueProcessorMap(propertyNameAndJsonValueProcessorMap);
        return javaToJsonConfig;
    }

}
