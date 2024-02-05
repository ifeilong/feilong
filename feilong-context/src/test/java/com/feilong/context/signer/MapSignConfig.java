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
package com.feilong.context.signer;

import java.util.List;

import com.feilong.security.oneway.OnewayType;

/**
 * 专门用来生成签名的工具类.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 4.0.8
 */
public class MapSignConfig{

    /** 拼接盐值参数key. */
    private String       appendSecretParamName;

    /** 拼接盐值参数value. */
    private String       appendSecretParamValue;

    //---------------------------------------------------------------
    /** 是否结果转大写. */
    private boolean      isResultUpperCase;

    /** 单向加密算法. */
    private OnewayType   onewayType = OnewayType.MD5;

    /** 不需要参与签名的参数名字. */
    private List<String> noNeedSignParamNameList;
    //---------------------------------------------------------------

    /**
     * Instantiates a new map sign config.
     */
    public MapSignConfig(){
        super();
    }

    /**
     * Instantiates a new map sign config.
     *
     * @param appendSecretParamName
     *            the append secret param name
     * @param appendSecretParamValue
     *            the append secret param value
     */
    public MapSignConfig(String appendSecretParamName, String appendSecretParamValue){
        super();
        this.appendSecretParamName = appendSecretParamName;
        this.appendSecretParamValue = appendSecretParamValue;
    }

    //---------------------------------------------------------------

    /**
     * 获得 是否结果转大写.
     *
     * @return the isResultUpperCase
     */
    public boolean getIsResultUpperCase(){
        return isResultUpperCase;
    }

    /**
     * 设置 是否结果转大写.
     *
     * @param isResultUpperCase
     *            the isResultUpperCase to set
     */
    public void setIsResultUpperCase(boolean isResultUpperCase){
        this.isResultUpperCase = isResultUpperCase;
    }

    /**
     * 获得 拼接盐值参数key.
     *
     * @return the appendSecretParamName
     */
    public String getAppendSecretParamName(){
        return appendSecretParamName;
    }

    /**
     * 设置 拼接盐值参数key.
     *
     * @param appendSecretParamName
     *            the appendSecretParamName to set
     */
    public void setAppendSecretParamName(String appendSecretParamName){
        this.appendSecretParamName = appendSecretParamName;
    }

    /**
     * 获得 拼接盐值参数value.
     *
     * @return the appendSecretParamValue
     */
    public String getAppendSecretParamValue(){
        return appendSecretParamValue;
    }

    /**
     * 设置 拼接盐值参数value.
     *
     * @param appendSecretParamValue
     *            the appendSecretParamValue to set
     */
    public void setAppendSecretParamValue(String appendSecretParamValue){
        this.appendSecretParamValue = appendSecretParamValue;
    }

    /**
     * 获得 不需要参与签名的参数名字.
     *
     * @return the noNeedSignParamNameList
     */
    public List<String> getNoNeedSignParamNameList(){
        return noNeedSignParamNameList;
    }

    /**
     * 设置 不需要参与签名的参数名字.
     *
     * @param noNeedSignParamNameList
     *            the noNeedSignParamNameList to set
     */
    public void setNoNeedSignParamNameList(List<String> noNeedSignParamNameList){
        this.noNeedSignParamNameList = noNeedSignParamNameList;
    }

    /**
     * Gets the oneway type.
     *
     * @return the onewayType
     */
    public OnewayType getOnewayType(){
        return onewayType;
    }

    /**
     * Sets the oneway type.
     *
     * @param onewayType
     *            the onewayType to set
     */
    public void setOnewayType(OnewayType onewayType){
        this.onewayType = onewayType;
    }

}
