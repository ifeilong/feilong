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

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.util.CollectionsUtil.select;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.formatter.entity.BeanFormatterConfig;
import com.feilong.formatter.entity.FormatterColumnEntity;
import com.feilong.json.JsonUtil;

/**
 * 格式化构造器的父类, 含有公共方法.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.4
 */
public abstract class AbstractFormatterBuilder implements FormatterBuilder{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFormatterBuilder.class);

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.formatter.builder.DataBuilder#build(java.lang.Iterable, com.feilong.formatter.entity.BeanFormatterConfig)
     */
    @Override
    public <T> List<FormatterColumnEntity> build(T bean,BeanFormatterConfig beanFormatterConfig){
        BeanFormatterConfig useBeanFormatterConfig = null == beanFormatterConfig ? new BeanFormatterConfig() : beanFormatterConfig;

        List<FormatterColumnEntity> formatterColumnEntityList = buildFormatterColumnEntityList(bean, useBeanFormatterConfig);
        return handler(formatterColumnEntityList, useBeanFormatterConfig);
    }

    //---------------------------------------------------------------

    /**
     * Handler.
     *
     * @param formatterColumnEntityList
     *            the formatter column entity list
     * @param useBeanFormatterConfig
     *            the use bean formatter config
     * @return the list
     */
    private static List<FormatterColumnEntity> handler(
                    List<FormatterColumnEntity> formatterColumnEntityList,
                    BeanFormatterConfig useBeanFormatterConfig){
        Validate.notNull(useBeanFormatterConfig, "useBeanFormatterConfig can't be null!");

        //---------------------------------------------------------------
        String[] includePropertyNames = useBeanFormatterConfig.getIncludePropertyNames();
        List<FormatterColumnEntity> useFormatterColumnEntityList = isNotNullOrEmpty(includePropertyNames)
                        ? select(formatterColumnEntityList, "name", includePropertyNames)
                        : formatterColumnEntityList;

        if (LOGGER.isTraceEnabled()){
            LOGGER.trace("before sort:{}", JsonUtil.format(useFormatterColumnEntityList));
        }

        //---------------------------------------------------------------
        List<FormatterColumnEntity> result = FormatterColumnEntityListSorter
                        .sortFormatterColumnEntityList(useFormatterColumnEntityList, useBeanFormatterConfig);

        if (LOGGER.isTraceEnabled()){
            LOGGER.trace("after sort:{}", JsonUtil.format(result));
        }
        return result;
    }

    /**
     * 构造行数据.
     *
     * @param propertyValueMap
     *            the property value map
     * @param beanFormatterConfig
     *            the bean formatter config
     * @return the object[]
     * @since 1.10.6
     */
    protected static Object[] buildLineData(Map<String, Object> propertyValueMap,BeanFormatterConfig beanFormatterConfig){
        if (LOGGER.isTraceEnabled()){
            LOGGER.trace("propertyValueMap:{}", JsonUtil.format(propertyValueMap, 0, 0));
        }

        //---------------------------------------------------------------

        int j = 0;
        Object[] rowData = new Object[propertyValueMap.size()];
        for (Map.Entry<String, Object> entry : propertyValueMap.entrySet()){
            String propertyName = entry.getKey();
            Object value = entry.getValue();
            rowData[j] = isNullOrEmpty(value) ? EMPTY : ValueHandler.getValue(propertyName, value, beanFormatterConfig);
            j++;
        }
        return rowData;
    }

    //---------------------------------------------------------------

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
    protected abstract <T> List<FormatterColumnEntity> buildFormatterColumnEntityList(T bean,BeanFormatterConfig beanFormatterConfig);

}
