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
package com.feilong.formatter;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.toArray;
import static com.feilong.core.lang.StringUtil.EMPTY;
import static com.feilong.core.lang.StringUtil.SPACE;
import static com.feilong.core.util.CollectionsUtil.addIgnoreNullOrEmpty;
import static com.feilong.core.util.CollectionsUtil.getPropertyValueList;
import static com.feilong.core.util.CollectionsUtil.newArrayList;
import static com.feilong.core.util.SortUtil.sortMapByKeyAsc;
import static java.lang.Math.max;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.bean.ConvertUtil;
import com.feilong.core.bean.PropertyUtil;
import com.feilong.core.util.CollectionsUtil;
import com.feilong.formatter.builder.FormatterBuilder;
import com.feilong.formatter.builder.FormatterBuilderFactory;
import com.feilong.formatter.entity.BeanFormatterConfig;
import com.feilong.formatter.entity.FormatterColumnEntity;
import com.feilong.json.JsonUtil;
import com.feilong.lib.collection4.IterableUtils;
import com.feilong.lib.lang3.StringUtils;

/**
 * Formatter相关父类.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.8.5
 */
abstract class AbstractFormatter{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFormatter.class);

    //---------------------------------------------------------------

    /**
     * 对java <code>bean</code>格式化.
     *
     * @param <T>
     *            the generic type
     * @param bean
     *            the bean
     * @return 如果 <code>bean</code> 是null,返回 {@link StringUtils#EMPTY}<br>
     */
    public <T> String format(T bean){
        return isNullOrEmpty(bean) ? EMPTY : format(PropertyUtil.describe(bean));
    }

    /**
     * 将<code>map</code> 格式化成字符串.
     *
     * @param <K>
     *            the key type
     * @param <V>
     *            the value type
     * @param map
     *            the map
     * @return 如果 <code>map</code> 是null,返回 {@link StringUtils#EMPTY}<br>
     */
    public <K, V> String format(Map<K, V> map){
        if (isNullOrEmpty(map)){
            return EMPTY;
        }

        //---------------------------------------------------------------
        //key 字符串最长的那个
        int maxKeyLength = -1;
        for (K key : map.keySet()){
            maxKeyLength = max(maxKeyLength, StringUtils.length(ConvertUtil.toString(key)));
        }
        //---------------------------------------------------------------
        List<Object[]> dataList = new ArrayList<>(map.size());
        Map<K, V> useMap = sortMapByKeyAsc(map);//不影响原map

        String separator = SPACE + ":" + SPACE;
        //---------------------------------------------------------------
        for (Map.Entry<K, V> entry : useMap.entrySet()){
            K key = entry.getKey();
            V value = entry.getValue();
            dataList.add(toArray(ConvertUtil.toString(key), separator, value));
        }
        return format(null, dataList);
    }

    //------------------Iterable-------------------------------------------

    /**
     * 将迭代对象 <code>iterable</code> 格式化.
     * 
     * @param <T>
     *            the generic type
     * @param iterable
     *            the iterable
     * @return 如果 <code>iterable</code> 是null或者empty,返回 {@link StringUtils#EMPTY}<br>
     */
    public <T> String format(Iterable<T> iterable){
        return format(iterable, null);
    }

    /**
     * 将迭代对象 <code>iterable</code> 格式化.
     *
     * @param <T>
     *            the generic type
     * @param iterable
     *            the iterable
     * @param beanFormatterConfig
     *            the bean formatter config
     * @return 如果 <code>iterable</code> 是null或者empty,返回 {@link StringUtils#EMPTY}<br>
     */
    public <T> String format(Iterable<T> iterable,BeanFormatterConfig beanFormatterConfig){
        if (isNullOrEmpty(iterable)){
            return EMPTY;
        }

        //---------------------------------------------------------------
        T t = IterableUtils.get(iterable, 0);

        FormatterBuilder formatterBuilder = FormatterBuilderFactory.create(t);

        List<FormatterColumnEntity> formatterColumnEntityList = formatterBuilder.build(t, beanFormatterConfig);

        List<Object[]> dataList = buildDataList(iterable, formatterColumnEntityList, formatterBuilder, beanFormatterConfig);
        return format(//
                        ConvertUtil.toStrings(getPropertyValueList(formatterColumnEntityList, "name")),
                        dataList);
    }

    //---------------------------------------------------------------

    /**
     * 将迭代器转成数据行list.
     *
     * @param <T>
     *            the generic type
     * @param iterable
     *            the iterable
     * @param formatterColumnEntityList
     *            the formatter column entity list
     * @param formatterBuilder
     *            the data builder
     * @return the list
     * @see CollectionsUtil#getPropertyValueList(Iterable, String)
     */
    private static <T> List<Object[]> buildDataList(
                    Iterable<T> iterable,
                    List<FormatterColumnEntity> formatterColumnEntityList,
                    FormatterBuilder formatterBuilder,
                    BeanFormatterConfig beanFormatterConfig){
        //取到所有需要被提取的属性名 list
        List<String> propertyNameList = getPropertyValueList(formatterColumnEntityList, "propertyName");
        if (LOGGER.isTraceEnabled()){
            LOGGER.trace("propertyNameList:{}", JsonUtil.format(propertyNameList, 0, 0));
        }

        //---------------------------------------------------------------
        List<Object[]> dataList = newArrayList();
        for (T bean : iterable){
            //添加每行数据到 dataList (忽略空白行)
            addIgnoreNullOrEmpty(dataList, formatterBuilder.buildLineData(bean, propertyNameList, beanFormatterConfig));
        }
        return dataList;
    }

    //-----------------------------array-----------------------------

    /**
     * 格式化.
     *
     * @param columnTitles
     *            列标题, columnTitles和dataList 不能同时为null或者empty
     * @param dataList
     *            数据数组list, columnTitles和dataList 不能同时为null或者empty;<br>
     *            object对象会调用 {@link ConvertUtil#toString(Object)} 转成字符串输出
     * @return the string
     * @since 1.8.3
     */
    protected abstract String format(String[] columnTitles,List<Object[]> dataList);

}
