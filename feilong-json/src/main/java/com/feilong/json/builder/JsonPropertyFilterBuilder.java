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

import static com.feilong.core.Validator.isNullOrEmpty;

import com.feilong.json.JavaToJsonConfig;
import com.feilong.lib.json.filters.OrPropertyFilter;
import com.feilong.lib.json.util.PropertyFilter;

/**
 * 专门用来构造 {@link PropertyFilter} .
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
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
     * @see com.feilong.lib.json.filters.AndPropertyFilter
     */
    static PropertyFilter build(JavaToJsonConfig useJavaToJsonConfig){
        String[] includes = useJavaToJsonConfig.getIncludes();

        //或者的关系,  只要有一个是true ,那么就返回true
        return new OrPropertyFilter(//    
                        useJavaToJsonConfig.getIsIgnoreNullValueElement()
                                        ? new IgnoreNullValueElementPropertyFilter(
                                                        useJavaToJsonConfig.getIfIgnoreNullValueElementIncludes())
                                        : null,

                        useJavaToJsonConfig.getPropertyFilter(),
                        isNullOrEmpty(includes) ? null : new ArrayContainsPropertyNamesPropertyFilter(includes) //如果不在元素内 也不输出
        );
    }
}
