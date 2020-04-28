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
package com.feilong.office.excel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.feilong.office.excel.convertor.BigDecimalConvertor;
import com.feilong.office.excel.convertor.BooleanConvertor;
import com.feilong.office.excel.convertor.DataConvertor;
import com.feilong.office.excel.convertor.DateConvertor;
import com.feilong.office.excel.convertor.DoubleConvertor;
import com.feilong.office.excel.convertor.IntegerConvertor;
import com.feilong.office.excel.convertor.LongConvertor;
import com.feilong.office.excel.convertor.StringConvertor;

/**
 * The Class DataConvertorConfigurator.
 */
public class DataConvertorConfigurator implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long                     serialVersionUID = -6172555487692156540L;

    //---------------------------------------------------------------

    /** The supports map. */
    private final Map<String, Class<?>>           supportsMap      = new HashMap<>();

    /** The convertor map. */
    private final Map<Class<?>, DataConvertor<?>> convertorMap     = new HashMap<>();

    /** The instance. */
    private static DataConvertorConfigurator      instance;

    //---------------------------------------------------------------

    /**
     * Instantiates a new data convertor configurator.
     */
    private DataConvertorConfigurator(){
        registerDataConvertor(new StringConvertor());
        registerDataConvertor(new IntegerConvertor());
        registerDataConvertor(new LongConvertor());
        registerDataConvertor(new DoubleConvertor());
        registerDataConvertor(new BigDecimalConvertor());
        registerDataConvertor(new DateConvertor());
        registerDataConvertor(new BooleanConvertor());
    }

    //---------------------------------------------------------------
    /**
     * Register data convertor.
     *
     * @param dataConvertor
     *            the dc
     */
    public void registerDataConvertor(DataConvertor<?> dataConvertor){
        supportsMap.put(dataConvertor.getDataTypeAbbr(), dataConvertor.supportClass());
        convertorMap.put(dataConvertor.supportClass(), dataConvertor);
    }

    //---------------------------------------------------------------

    /**
     * Gets the convertor.
     *
     * @param <T>
     *            the generic type
     * @param clazz
     *            the clazz
     * @return the convertor
     */
    @SuppressWarnings("unchecked")
    public <T> DataConvertor<T> getConvertor(Class<T> clazz){
        return (DataConvertor<T>) convertorMap.get(clazz);
    }

    /**
     * Gets the supported class.
     *
     * @param name
     *            the name
     * @return the supported class
     */
    public Class<?> getSupportedClass(String name){
        return supportsMap.get(name);
    }

    //---------------------------------------------------------------

    /**
     * Gets the single instance of DataConvertorConfigurator.
     *
     * @return single instance of DataConvertorConfigurator
     */
    public static DataConvertorConfigurator getInstance(){
        if (instance == null){
            instance = new DataConvertorConfigurator();
        }
        return instance;
    }
}
