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
package com.feilong.context;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.bean.ConvertUtil;

/**
 * 简单的基于参数名字配置的条件.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @param <V>
 *            the value type
 * @since 2.0.0
 */
public class SimpleParamNameValueLoader<V> implements ValueLoader<V>{

    /** The Constant log. */
    private static final Logger       LOGGER             = LoggerFactory.getLogger(SimpleParamNameValueLoader.class);

    //---------------------------------------------------------------

    /** 参数名字. */
    private String                    paramName;

    /** The target type class. */
    private Class<V>                  targetTypeClass;

    /**
     * 如果是 null 默认值.
     */
    private V                         ifNullDefaultValue = null;

    //---------------------------------------------------------------

    /** The value param loader. */
    private StringValueParamLoader<V> stringValueParamLoader;

    //---------------------------------------------------------------

    /** Post construct. */
    @PostConstruct
    protected void postConstruct(){
        Validate.notBlank(paramName, "paramName can't be blank!");
        Validate.notNull(stringValueParamLoader, "stringValueParamLoader can't be null!");
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.spring.scheduling.quartz.Condition#is()
     */
    @Override
    public V load(){
        //通过参数名字, 读取值
        V value = stringValueParamLoader.load(paramName);

        //---------------------------------------------------------------
        if (null == value){
            return doWithDefaultValue();
        }
        return doWithLoadValue(value);
    }

    //---------------------------------------------------------------

    /**
     * Do with load value.
     *
     * @param value
     *            the value
     * @return the v
     */
    private V doWithLoadValue(V value){
        String valueClassName = value.getClass().getName();
        if (null == targetTypeClass){
            LOGGER.debug("when param:[{}],return value:[{}],type:[{}],no config targetTypeClass", paramName, value, valueClassName);
            return value;
        }

        //---------------------------------------------------------------
        V convertResult = ConvertUtil.convert(value, targetTypeClass);
        LOGGER.debug(
                        "when param:[{}],load value:[{}],type:[{}],convert to:[{}],return:[{}]",
                        paramName,
                        value,
                        valueClassName,

                        targetTypeClass.getName(),
                        convertResult);
        return convertResult;
    }

    //---------------------------------------------------------------

    /**
     * Do with default value.
     *
     * @return the v
     */
    private V doWithDefaultValue(){
        if (null == ifNullDefaultValue){
            LOGGER.debug("when param:[{}],load value is null,return defaultValue null", paramName);
            return null;
        }

        //---------------------------------------------------------------
        if (null == targetTypeClass){
            LOGGER.debug("when param:[{}],load value is null,return defaultValue:[{}]", paramName, ifNullDefaultValue);
            return ifNullDefaultValue;
        }

        //---------------------------------------------------------------
        V convertResult = ConvertUtil.convert(ifNullDefaultValue, targetTypeClass);
        LOGGER.debug(
                        "when param:[{}],load value is null,use targetTypeClass:[{}],return defaultValue:[{}]",
                        paramName,
                        targetTypeClass.getName(),
                        convertResult);
        return convertResult;
    }

    //---------------------------------------------------------------

    /**
     * 设置 参数名字.
     *
     * @param paramName
     *            the paramName to set
     */
    public void setParamName(String paramName){
        this.paramName = paramName;
    }

    /**
     * 设置 target type class.
     *
     * @param targetTypeClass
     *            the targetTypeClass to set
     */
    public void setTargetTypeClass(Class<V> targetTypeClass){
        this.targetTypeClass = targetTypeClass;
    }

    /**
     * 设置 如果是 null 默认值.
     *
     * @param ifNullDefaultValue
     *            the ifNullDefaultValue to set
     */
    public void setIfNullDefaultValue(V ifNullDefaultValue){
        this.ifNullDefaultValue = ifNullDefaultValue;
    }

    /**
     * 设置 value param loader.
     *
     * @param stringValueParamLoader
     *            the stringValueParamLoader to set
     */
    public void setStringValueParamLoader(StringValueParamLoader<V> stringValueParamLoader){
        this.stringValueParamLoader = stringValueParamLoader;
    }

}
