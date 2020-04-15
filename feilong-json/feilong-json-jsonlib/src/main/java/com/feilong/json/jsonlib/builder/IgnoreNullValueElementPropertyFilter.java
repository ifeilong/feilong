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

import net.sf.json.util.PropertyFilter;

/**
 * {@code java --> json} 忽略 null value 属性过滤器.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 2.0.0
 */
class IgnoreNullValueElementPropertyFilter implements PropertyFilter{

    /** Static instance. */
    // the static instance works for all types
    public static final PropertyFilter INSTANCE = new IgnoreNullValueElementPropertyFilter();

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see net.sf.json.util.PropertyFilter#apply(java.lang.Object, java.lang.String, java.lang.Object)
     */
    @Override
    public boolean apply(Object source,String name,Object value){
        //返回 true 表示过滤 
        return null == value;
    }
}
