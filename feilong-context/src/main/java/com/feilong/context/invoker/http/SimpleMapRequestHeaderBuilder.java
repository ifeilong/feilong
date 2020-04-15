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

import java.util.Map;

/**
 * 简单的 map式的.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @param <T>
 *            the generic type
 * @see <a href="https://github.com/venusdrogon/feilong-platform/issues/227">RequestHeaderBuilder 提供一个默认的可xml 直接配置式的实现</a>
 * @since 1.12.4
 */
public class SimpleMapRequestHeaderBuilder<T> implements RequestHeaderBuilder<T>{

    /** The header map. */
    private Map<String, String> headerMap;

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.context.invoker.http.RequestHeaderBuilder#build(java.lang.Object)
     */
    @Override
    public Map<String, String> build(T request){
        return headerMap;
    }

    //---------------------------------------------------------------
    /**
     * 获得 header map.
     *
     * @return the headerMap
     */
    public Map<String, String> getHeaderMap(){
        return headerMap;
    }

    /**
     * 设置 header map.
     *
     * @param headerMap
     *            the headerMap to set
     */
    public void setHeaderMap(Map<String, String> headerMap){
        this.headerMap = headerMap;
    }

}
