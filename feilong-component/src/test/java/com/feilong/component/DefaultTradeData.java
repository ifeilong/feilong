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
package com.feilong.component;

import java.io.Serializable;

import com.feilong.context.Data;

/**
 * 基本的交易数据模型.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 3.0.0
 */
public class DefaultTradeData implements Serializable,Data{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -8749497591964672883L;

    //---------------------------------------------------------------

    /** 交易号. */
    private String            tradeNo;

    /** 在线支付类型,只存放在线支付类型. */
    private String            paymentType;

    //---------------------------------------------------------------

    /**
     * Instantiates a new base trade data.
     */
    public DefaultTradeData(){
        super();
    }

    /**
     * Instantiates a new base trade data.
     *
     * @param tradeNo
     *            the trade no
     * @param paymentType
     *            the payment type
     */
    public DefaultTradeData(String tradeNo, String paymentType){
        super();
        this.tradeNo = tradeNo;
        this.paymentType = paymentType;
    }

    //---------------------------------------------------------------

    /**
     * 获得 交易号.
     *
     * @return the tradeNo
     */
    public String getTradeNo(){
        return tradeNo;
    }

    /**
     * 设置 交易号.
     *
     * @param tradeNo
     *            the tradeNo to set
     */
    public void setTradeNo(String tradeNo){
        this.tradeNo = tradeNo;
    }

    //---------------------------------------------------------------

    /**
     * 获得 在线支付类型,只存放在线支付类型.
     *
     * @return the paymentType
     */
    public String getPaymentType(){
        return paymentType;
    }

    /**
     * 设置 在线支付类型,只存放在线支付类型.
     *
     * @param paymentType
     *            the paymentType to set
     */
    public void setPaymentType(String paymentType){
        this.paymentType = paymentType;
    }
}
