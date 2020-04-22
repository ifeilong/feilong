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
package com.feilong.office.csv.handler;

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.util.CollectionsUtil.newArrayList;
import static com.feilong.core.util.CollectionsUtil.select;
import static com.feilong.core.util.SortUtil.sortListByPropertyNamesValue;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.lang3.Validate;

import com.feilong.core.lang.reflect.FieldUtil;
import com.feilong.office.csv.entity.BeanCsvConfig;
import com.feilong.office.csv.entity.CsvColumnEntity;

/**
 * The Class CsvColumnEntityListBuilder.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.7
 */
public class CsvColumnEntityListBuilder{

    /** Don't let anyone instantiate this class. */
    private CsvColumnEntityListBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 获得 csv column entity list.
     *
     * @param <T>
     *            the generic type
     * @param beanCsvConfig
     *            the bean csv config
     * @return the csv column entity list
     * @see com.feilong.core.lang.reflect.FieldUtil#getAllFieldList(Class, String...)
     * @see org.apache.commons.lang3.reflect.FieldUtils#getAllFieldsList(Class)
     * @since 1.7.1
     */
    public static <T> List<CsvColumnEntity> build(BeanCsvConfig<T> beanCsvConfig){
        Validate.notNull(beanCsvConfig, "beanCsvConfig can't be null!");

        //---------------------------------------------------------------
        List<CsvColumnEntity> csvColumnEntityList = buildCsvColumnEntityList(beanCsvConfig);
        if (isNotNullOrEmpty(beanCsvConfig.getIncludePropertyNames())){
            csvColumnEntityList = select(csvColumnEntityList, "name", beanCsvConfig.getIncludePropertyNames());
        }
        return sortListByPropertyNamesValue(csvColumnEntityList, "order", "propertyName");
    }

    /**
     * To csv column entity list.
     *
     * @param <T>
     *            the generic type
     * @param beanCsvConfig
     *            the bean csv config
     * @return the list
     * @since 1.8.1
     */
    private static <T> List<CsvColumnEntity> buildCsvColumnEntityList(BeanCsvConfig<T> beanCsvConfig){
        List<Field> fieldsList = FieldUtil.getAllFieldList(beanCsvConfig.getBeanClass(), beanCsvConfig.getExcludePropertyNames());

        //---------------------------------------------------------------
        List<CsvColumnEntity> list = newArrayList();
        for (Field field : fieldsList){
            list.add(CsvColumnEntityBuilder.buildCsvColumnEntity(field));
        }
        return list;
    }
}
