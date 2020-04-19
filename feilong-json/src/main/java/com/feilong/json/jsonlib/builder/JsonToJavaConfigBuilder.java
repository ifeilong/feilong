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

import static com.feilong.core.Validator.isNotNullOrEmpty;

import java.util.Map;

import com.feilong.json.jsonlib.JsonToJavaConfig;

import net.sf.json.JsonConfig;
import net.sf.json.util.JavaIdentifierTransformer;
import net.sf.json.util.PropertyFilter;
import net.sf.json.util.PropertySetStrategy;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.0
 */
public final class JsonToJavaConfigBuilder{

    /** Don't let anyone instantiate this class. */
    private JsonToJavaConfigBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Builds the json config.
     *
     * @param rootClass
     *            the root class
     * @param jsonToJavaConfig
     *            the json to java config
     * @return the json config
     */
    public static JsonConfig build(Class<?> rootClass,JsonToJavaConfig jsonToJavaConfig){
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setRootClass(rootClass);

        //--------------JavaIdentifierTransformer-------------------------------
        JavaIdentifierTransformer javaIdentifierTransformer = jsonToJavaConfig.getJavaIdentifierTransformer();
        if (isNotNullOrEmpty(javaIdentifierTransformer)){
            jsonConfig.setJavaIdentifierTransformer(javaIdentifierTransformer);
        }

        //--------------setClassMap-------------------------------
        //Sets the current attribute/Class Map [JSON -> Java]
        //classMap a Map of classes, every key identifies a property or a regexp
        Map<String, Class<?>> classMap = jsonToJavaConfig.getClassMap();
        if (isNotNullOrEmpty(classMap)){
            jsonConfig.setClassMap(classMap);
        }

        // Ignore missing properties with Json-Lib

        // 避免出现 Unknown property 'orderIdAndCodeMap' on class 'class com.trade.web.result.command.PaymentResultEntity' 异常
        jsonConfig.setPropertySetStrategy(new PropertyStrategyWrapper(PropertySetStrategy.DEFAULT));

        //---------------------------------------------------------------
        //since 2.0.0
        PropertyFilter propertyFilter = build(jsonToJavaConfig);
        if (null != propertyFilter){
            jsonConfig.setJavaPropertyFilter(propertyFilter);
        }
        return jsonConfig;
    }

    //---------------------------------------------------------------

    /**
     * @param jsonToJavaConfig
     * @since 2.0.0
     */
    private static PropertyFilter build(JsonToJavaConfig jsonToJavaConfig){
        //排除
        String[] excludes = jsonToJavaConfig.getExcludes();
        if (isNotNullOrEmpty(excludes)){
            return new ArrayExcludePropertyNamesPropertyFilter(excludes);
        }
        return null;
    }
}
