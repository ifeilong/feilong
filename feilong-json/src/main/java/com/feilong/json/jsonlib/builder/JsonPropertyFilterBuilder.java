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
import static com.feilong.core.Validator.isNullOrEmpty;

import com.feilong.json.jsonlib.JavaToJsonConfig;

import net.sf.json.filters.OrPropertyFilter;
import net.sf.json.util.PropertyFilter;

/**
 * The Class JsonPropertyFilterBuilder.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 2.0.0
 */
class JsonPropertyFilterBuilder{

    /** Don't let anyone instantiate this class. */
    private JsonPropertyFilterBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Builds the.
     *
     * @param useJavaToJsonConfig
     *            the use java to json config
     * @return the property filter
     * @see net.sf.json.filters.AndPropertyFilter
     */
    static PropertyFilter build(JavaToJsonConfig useJavaToJsonConfig){
        boolean isIgnoreNullValueElement = useJavaToJsonConfig.getIsIgnoreNullValueElement();
        //包含
        String[] includes = useJavaToJsonConfig.getIncludes();
        //---------------------------------------------------------------
        //不忽视
        if (!isIgnoreNullValueElement){
            if (isNotNullOrEmpty(includes)){//不忽视 但是 需要输出指定参数
                return new ArrayContainsPropertyNamesPropertyFilter(includes);
            }
            return null;
        }

        //---------------------------------------------------------------
        //忽视
        if (isNullOrEmpty(includes)){
            //仅仅忽视
            return IgnoreNullValueElementPropertyFilter.INSTANCE;
        }

        return new OrPropertyFilter(//    
                        IgnoreNullValueElementPropertyFilter.INSTANCE, //如果是 null value 就不输出
                        new ArrayContainsPropertyNamesPropertyFilter(includes) //如果不在元素内 也不输出
        );
    }
}
