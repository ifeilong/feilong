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
package com.feilong.json.jsonlib.processor.defaultvalue;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.processors.DefaultDefaultValueProcessor;
import net.sf.json.processors.DefaultValueProcessor;
import net.sf.json.util.JSONUtils;

/**
 * 通用的默认值处理.
 * 
 * <h3>和 {@link DefaultDefaultValueProcessor} 相比:</h3>
 * <blockquote>
 * 
 * <p>
 * {@link DefaultDefaultValueProcessor} 是json-lib的默认实现
 * </p>
 * <ol>
 * <li>{@link DefaultDefaultValueProcessor} 把所有的数字类型默认值设置为0,但是实际场景中, 0和null是有区别的</li>
 * <li>{@link DefaultDefaultValueProcessor} 把 Boolean 默认值设置为了 false,但是实际场景中, false和null是有区别的</li>
 * </ol>
 * 
 * <p>
 * {@link CommonDefaultValueProcessor} 支持输出为null
 * </p>
 * 
 * </blockquote>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see net.sf.json.processors.DefaultDefaultValueProcessor
 * @since 1.11.5
 */
public class CommonDefaultValueProcessor implements DefaultValueProcessor{

    /** Static instance. */
    // the static instance works for all types
    public static final CommonDefaultValueProcessor INSTANCE = new CommonDefaultValueProcessor();

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see net.sf.json.processors.DefaultValueProcessor#getDefaultValue(java.lang.Class)
     */
    @Override
    public Object getDefaultValue(@SuppressWarnings("rawtypes") Class type){
        //数组
        if (JSONUtils.isArray(type)){
            return new JSONArray();
        }
        return JSONNull.getInstance();
    }
}
