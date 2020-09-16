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

package com.feilong.lib.json.filters;

import com.feilong.lib.json.util.PropertyFilter;

/**
 * The Class OrPropertyFilter.
 */
public class OrPropertyFilter implements PropertyFilter{

    /** The property filters. */
    private final PropertyFilter[] propertyFilters;

    //---------------------------------------------------------------

    /**
     * Instantiates a new or property filter.
     *
     * @param propertyFilters
     *            the property filters
     * @since 3.0.10 changge to 动态参数
     */
    public OrPropertyFilter(PropertyFilter...propertyFilters){
        this.propertyFilters = propertyFilters;
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
    @Override
    //或者的关系,  只要有一个是true ,那么就返回true
    public boolean apply(Object source,String name,Object value){
        for (PropertyFilter propertyFilter : propertyFilters){
            if (propertyFilter != null && propertyFilter.apply(source, name, value)){
                return true;
            }
        }
        return false;
    }
}