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
package com.feilong.context.invoker.http;

import com.feilong.context.format.JsonStringFormatter;
import com.feilong.json.JavaToJsonConfig;
import com.feilong.json.JsonUtil;

/**
 * 简单的 json格式的.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @param <T>
 *            the generic type
 * @see JsonStringFormatter
 * @since 1.12.4
 */
public class SimpleJsonRequestBodyBuilder<T> implements RequestBodyBuilder<T>{

    /**
     * java格式化成json的一些配置.
     * 
     * @since 1.12.6
     */
    private JavaToJsonConfig javaToJsonConfig;

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.context.invoker.http.RequestBodyBuilder#build(java.lang.Object)
     */
    @Override
    public String build(T request){
        return JsonUtil.format(request, javaToJsonConfig, 0, 0);
    }

    //---------------------------------------------------------------

    /**
     * 获得 java格式化成json的一些配置.
     *
     * @return the javaToJsonConfig
     * @since 1.12.6
     */
    public JavaToJsonConfig getJavaToJsonConfig(){
        return javaToJsonConfig;
    }

    /**
     * 设置 java格式化成json的一些配置.
     *
     * @param javaToJsonConfig
     *            the javaToJsonConfig to set
     * @since 1.12.6
     */
    public void setJavaToJsonConfig(JavaToJsonConfig javaToJsonConfig){
        this.javaToJsonConfig = javaToJsonConfig;
    }
}
