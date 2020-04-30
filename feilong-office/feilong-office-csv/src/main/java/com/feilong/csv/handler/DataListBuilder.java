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
package com.feilong.csv.handler;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.util.CollectionsUtil.getPropertyValueList;
import static com.feilong.core.util.CollectionsUtil.newArrayList;
import static com.feilong.core.util.MapUtil.newLinkedHashMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.bean.ConvertUtil;
import com.feilong.core.bean.PropertyUtil;
import com.feilong.csv.entity.CsvColumnEntity;
import com.feilong.json.JsonUtil;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.7
 */
public class DataListBuilder{

    private static final Logger LOGGER = LoggerFactory.getLogger(DataListBuilder.class);

    /** Don't let anyone instantiate this class. */
    private DataListBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Builds the data list.
     *
     * @param <T>
     *            the generic type
     * @param iterable
     *            the iterable
     * @param csvColumnEntityList
     *            the csv column entity list
     * @return the list< object[]>
     */
    public static <T> List<Object[]> build(Iterable<T> iterable,List<CsvColumnEntity> csvColumnEntityList){
        List<String> propertyNameList = getPropertyValueList(csvColumnEntityList, "propertyName");
        if (LOGGER.isTraceEnabled()){
            LOGGER.trace("propertyNameList:{}", JsonUtil.format(propertyNameList, 0, 0));
        }

        //---------------------------------------------------------------

        List<Object[]> dataList = newArrayList();
        for (T t : iterable){
            dataList.add(toObjectArray(t, propertyNameList));
        }
        return dataList;
    }

    /**
     * To object array.
     *
     * @param <T>
     *            the generic type
     * @param bean
     *            the t
     * @param propertyNameList
     *            the property name list
     * @return the object[]
     */
    private static <T> Object[] toObjectArray(T bean,List<String> propertyNameList){
        Map<String, Object> propertyValueMap = newLinkedHashMap();
        PropertyUtil.copyProperties(propertyValueMap, bean, ConvertUtil.toStrings(propertyNameList));

        if (LOGGER.isTraceEnabled()){
            LOGGER.trace("propertyValueMap:{}", JsonUtil.format(propertyValueMap, 0, 0));
        }

        //---------------------------------------------------------------

        int j = 0;
        Object[] rowData = new Object[propertyNameList.size()];
        for (Map.Entry<String, Object> entry : propertyValueMap.entrySet()){
            Object value = entry.getValue();
            rowData[j] = isNullOrEmpty(value) ? EMPTY : ConvertUtil.toString(value);
            j++;
        }
        return rowData;
    }
}
