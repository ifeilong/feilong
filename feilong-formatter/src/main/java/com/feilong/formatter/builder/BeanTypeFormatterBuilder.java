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
import static com.feilong.core.util.MapUtil.newLinkedHashMap;
import static com.feilong.lib.lang3.ArrayUtils.EMPTY_OBJECT_ARRAY;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import com.feilong.core.bean.ConvertUtil;
import com.feilong.core.bean.PropertyUtil;
import com.feilong.core.lang.reflect.FieldUtil;
import com.feilong.formatter.entity.BeanFormatterConfig;
import com.feilong.formatter.entity.FormatterColumnEntity;

/**
 * List 里面元素是 bean对象 的相关构建.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.4
 */
public class BeanTypeFormatterBuilder extends AbstractFormatterBuilder{

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
        Map<String, Object> propertyValueMap = newLinkedHashMap();

        //只 copy 要的属性名字
        PropertyUtil.copyProperties(propertyValueMap, bean, ConvertUtil.toStrings(propertyNameList));

        return buildLineData(propertyValueMap, beanFormatterConfig);
    }

    /**
     * Builds the formatter column entity list.
     *
     * @param <T>
     *            the generic type
     * @param bean
     *            the bean
     * @param beanFormatterConfig
     *            the bean formatter config
     * @return the list
     */
    @Override
    protected <T> List<FormatterColumnEntity> buildFormatterColumnEntityList(T bean,BeanFormatterConfig beanFormatterConfig){
        @SuppressWarnings("unchecked")
        Class<T> klass = (Class<T>) bean.getClass();

        List<Field> fieldsList = FieldUtil.getAllFieldList(klass, beanFormatterConfig.getExcludePropertyNames());

        List<FormatterColumnEntity> list = newArrayList();
        for (Field field : fieldsList){
            list.add(BeanTypeFormatterColumnEntityBuilder.buildFormatterColumnEntity(field));
        }
        return list;
    }

}
