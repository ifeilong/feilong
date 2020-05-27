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
package com.feilong.context.codecreator;

/**
 * 简单的相关配置.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.12.9
 */
public class SimpleMultiSellerOrderCodeCreatorConfig{

    /** 前缀, 以示区分, 比如,1 表示退单号, 4代表订单号;后期视业务需要可以将 b店 和C店 也区分出来. */
    private String  prefix;

    /** 截取用户id 后面位数. */
    private int     buyerIdLastLength  = 3;

    /** 截取shop id 后面位数. */
    private int     shopIdLastLength   = 3;

    //---------------------------------------------------------------

    /** 随机数位数. */
    private int     randomNumberLength = 2;

    /** 是否开启debug模式. */
    private boolean isDebug            = false;

    //---------------------------------------------------------------

    /**
     * 获得 前缀, 以示区分, 比如,1 表示退单号, 4代表订单号;后期视业务需要可以将 b店 和C店 也区分出来.
     *
     * @return the prefix
     */
    public String getPrefix(){
        return prefix;
    }

    /**
     * 设置 前缀, 以示区分, 比如,1 表示退单号, 4代表订单号;后期视业务需要可以将 b店 和C店 也区分出来.
     *
     * @param prefix
     *            the prefix to set
     */
    public void setPrefix(String prefix){
        this.prefix = prefix;
    }

    /**
     * 获得 截取用户id 后面位数.
     *
     * @return the buyerIdLastLength
     */
    public int getBuyerIdLastLength(){
        return buyerIdLastLength;
    }

    /**
     * 设置 截取用户id 后面位数.
     *
     * @param buyerIdLastLength
     *            the buyerIdLastLength to set
     */
    public void setBuyerIdLastLength(int buyerIdLastLength){
        this.buyerIdLastLength = buyerIdLastLength;
    }

    /**
     * 获得 截取shop id 后面位数.
     *
     * @return the shopIdLastLength
     */
    public int getShopIdLastLength(){
        return shopIdLastLength;
    }

    /**
     * 设置 截取shop id 后面位数.
     *
     * @param shopIdLastLength
     *            the shopIdLastLength to set
     */
    public void setShopIdLastLength(int shopIdLastLength){
        this.shopIdLastLength = shopIdLastLength;
    }

    /**
     * 获得 随机数位数.
     *
     * @return the randomNumberLength
     */
    public int getRandomNumberLength(){
        return randomNumberLength;
    }

    /**
     * 设置 随机数位数.
     *
     * @param randomNumberLength
     *            the randomNumberLength to set
     */
    public void setRandomNumberLength(int randomNumberLength){
        this.randomNumberLength = randomNumberLength;
    }

    /**
     * 获得 是否开启debug模式.
     *
     * @return the isDebug
     */
    public boolean getIsDebug(){
        return isDebug;
    }

    /**
     * 设置 是否开启debug模式.
     *
     * @param isDebug
     *            the isDebug to set
     */
    public void setIsDebug(boolean isDebug){
        this.isDebug = isDebug;
    }

}
