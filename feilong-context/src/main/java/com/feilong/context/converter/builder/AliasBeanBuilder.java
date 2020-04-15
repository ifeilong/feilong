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
package com.feilong.context.converter.builder;

import java.util.Map;

import com.feilong.core.bean.BeanUtil;

/**
 * xml格式结果的解析,目的将xml字符串转成自定义的对象.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.2
 */
public class AliasBeanBuilder implements BeanBuilder{

    /** Static instance. */
    // the static instance works for all types
    public static final BeanBuilder INSTANCE = new AliasBeanBuilder();

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.bind.builder.BeanBuilder#populate(java.util.Map, java.lang.Object)
     */
    @Override
    public <T> T build(Map<String, String> aliasAndValueMap,T t){
        return BeanUtil.populateAliasBean(t, aliasAndValueMap);
    }
}
