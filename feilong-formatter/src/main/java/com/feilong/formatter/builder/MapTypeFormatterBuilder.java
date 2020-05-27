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
package com.feilong.formatter.builder;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.util.CollectionsUtil.newArrayList;
import static com.feilong.lib.lang3.ArrayUtils.EMPTY_OBJECT_ARRAY;

import java.util.List;
import java.util.Map;

import com.feilong.core.util.MapUtil;
import com.feilong.formatter.entity.BeanFormatterConfig;
import com.feilong.formatter.entity.FormatterColumnEntity;

/**
 * List 里面元素是 Map 的相关构建.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.10.4
 */
public class MapTypeFormatterBuilder extends AbstractFormatterBuilder{

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.formatter.builder.DataBuilder#build(java.lang.Object, java.util.List)
     */
    @Override
    public <T> Object[] buildLineData(T bean,List<String> propertyNameList,BeanFormatterConfig beanFormatterConfig){
        if (isNullOrEmpty(bean)){
            return EMPTY_OBJECT_ARRAY;
        }

        //---------------------------------------------------------------

        @SuppressWarnings("unchecked")
        Map<String, Object> subMap = MapUtil.getSubMap((Map<String, Object>) bean, propertyNameList);
        return buildLineData(subMap, beanFormatterConfig);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.formatter.builder.AbstractBuilder#buildFormatterColumnEntityList(java.lang.Object,
     * com.feilong.formatter.entity.BeanFormatterConfig)
     */
    @Override
    protected <T> List<FormatterColumnEntity> buildFormatterColumnEntityList(T bean,BeanFormatterConfig beanFormatterConfig){
        @SuppressWarnings("unchecked")
        Map<String, Object> map = MapUtil.getSubMapExcludeKeys((Map<String, Object>) bean, beanFormatterConfig.getExcludePropertyNames());

        //---------------------------------------------------------------

        int i = 0;

        List<FormatterColumnEntity> list = newArrayList();
        for (Map.Entry<String, Object> entry : map.entrySet()){
            String key = entry.getKey();

            FormatterColumnEntity formatterColumnEntity = new FormatterColumnEntity(key, key, i);
            list.add(formatterColumnEntity);

            i++;
        }
        return list;
    }
}
