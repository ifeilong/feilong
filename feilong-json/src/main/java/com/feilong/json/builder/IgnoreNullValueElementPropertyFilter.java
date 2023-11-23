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

import com.feilong.lib.json.util.PropertyFilter;
import com.feilong.lib.lang3.ArrayUtils;

/**
 * {@code java --> json} 忽略 null value 属性过滤器.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 2.0.0
 */
class IgnoreNullValueElementPropertyFilter implements PropertyFilter{

    /**
     * 如果忽略null value元素,白名单.
     * 
     * @since 4.0.4
     */
    private String[] ifIgnoreNullValueElementIncludes;

    //---------------------------------------------------------------

    /**
     * Instantiates a new ignore null value element property filter.
     */
    public IgnoreNullValueElementPropertyFilter(){
        super();
    }

    /**
     * Instantiates a new ignore null value element property filter.
     *
     * @param ifIgnoreNullValueElementIncludes
     *            the if ignore null value element includes
     * @since 4.0.4
     */
    public IgnoreNullValueElementPropertyFilter(String[] ifIgnoreNullValueElementIncludes){
        super();
        this.ifIgnoreNullValueElementIncludes = ifIgnoreNullValueElementIncludes;
    }
    //---------------------------------------------------------------

    /**
     * Apply.
     *
     * @param source
     *            the source
     * @param name
     *            the name
     * @param value
     *            the value
     * @return true, if successful
     */
    /*
     * (non-Javadoc)
     * 
     * @see net.sf.json.util.PropertyFilter#apply(java.lang.Object, java.lang.String, java.lang.Object)
     */
    @Override
    //返回 true 表示过滤 
    public boolean apply(Object source,String name,Object value){
        if (null != value){
            return false;
        }

        //---------------------------------------------------------------
        //是null的情况

        //没有设置白名单,那么就过滤
        if (isNullOrEmpty(ifIgnoreNullValueElementIncludes)){
            return true;
        }
        //如果在白名单里面就不过滤
        if (ArrayUtils.contains(ifIgnoreNullValueElementIncludes, name)){
            return false;
        }
        //不在白名单就过滤
        return true;
    }

    //---------------------------------------------------------------

    /**
     * 获得 如果忽略null value元素,白名单.
     *
     * @return the ifIgnoreNullValueElementIncludes
     * @since 4.0.4
     */
    public String[] getIfIgnoreNullValueElementIncludes(){
        return ifIgnoreNullValueElementIncludes;
    }

    /**
     * 设置 如果忽略null value元素,白名单.
     *
     * @param ifIgnoreNullValueElementIncludes
     *            the ifIgnoreNullValueElementIncludes to set
     * @since 4.0.4
     */
    public void setIfIgnoreNullValueElementIncludes(String[] ifIgnoreNullValueElementIncludes){
        this.ifIgnoreNullValueElementIncludes = ifIgnoreNullValueElementIncludes;
    }
}
