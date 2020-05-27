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
package com.feilong.formatter.entity;

import java.util.Map;

import org.apache.commons.collections4.Transformer;

/**
 * 提供格式化的时候,相关参数控制.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.8.5
 */
public class BeanFormatterConfig{

    //*************************通常二者选其一设置**********************
    /** 排除的属性名字, 会提取所有的属性, 然后剔除 exclude部分. */
    private String[]                                 excludePropertyNames;

    /** 包含的属性名字,会提取所有的属性,然后仅取 include部分. */
    private String[]                                 includePropertyNames;

    //---------------------------------------------------------------
    /** 显示属性(列 )的顺序. */
    private String[]                                 sorts;

    //---------------------------------------------------------------
    /**
     * 指定属性类型转换.
     *
     * @since 1.10.7
     */
    private Map<String, Transformer<Object, String>> propertyNameAndTransformerMap;

    //---------------------------------------------------------------
    /**
     * 获得 排除的属性名字, 会提取所有的属性, 然后剔除 exclude部分.
     *
     * @return the excludePropertyNames
     */
    public String[] getExcludePropertyNames(){
        return excludePropertyNames;
    }

    /**
     * 设置 排除的属性名字, 会提取所有的属性, 然后剔除 exclude部分.
     *
     * @param excludePropertyNames
     *            the excludePropertyNames to set
     */
    public void setExcludePropertyNames(String...excludePropertyNames){
        this.excludePropertyNames = excludePropertyNames;
    }

    /**
     * 获得 包含的属性名字,会提取所有的属性,然后仅取 include部分.
     *
     * @return the includePropertyNames
     */
    public String[] getIncludePropertyNames(){
        return includePropertyNames;
    }

    /**
     * 设置 包含的属性名字,会提取所有的属性,然后仅取 include部分.
     *
     * @param includePropertyNames
     *            the includePropertyNames to set
     */
    public void setIncludePropertyNames(String...includePropertyNames){
        this.includePropertyNames = includePropertyNames;
    }

    /**
     * 获得 显示属性(列 )的顺序.
     *
     * @return the sorts
     */
    public String[] getSorts(){
        return sorts;
    }

    /**
     * 设置 显示属性(列 )的顺序.
     *
     * @param sorts
     *            the sorts to set
     */
    public void setSorts(String...sorts){
        this.sorts = sorts;
    }

    /**
     * 获得 指定属性类型转换.
     *
     * @return the propertyNameAndTransformerMap
     * @since 1.10.7
     */
    public Map<String, Transformer<Object, String>> getPropertyNameAndTransformerMap(){
        return propertyNameAndTransformerMap;
    }

    /**
     * 设置 指定属性类型转换.
     *
     * @param propertyNameAndTransformerMap
     *            the propertyNameAndTransformerMap to set
     * @since 1.10.7
     */
    public void setPropertyNameAndTransformerMap(Map<String, Transformer<Object, String>> propertyNameAndTransformerMap){
        this.propertyNameAndTransformerMap = propertyNameAndTransformerMap;
    }

}
