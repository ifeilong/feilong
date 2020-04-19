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
package com.feilong.json.jsonlib.entity;

import java.io.Serializable;
import java.util.List;

import com.feilong.core.Validator;

/**
 * 拆单场景订单状态.
 *
 * @author hailong yu
 */

public class SplitSalesOrderV5 implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long           serialVersionUID = -1739318072484239437L;

    /** 商城订单编码. */
    private String                      bsOrderCode;

    /** 商城退换货编码，商城发起退换货时非空. */
    private String                      bsRaCode;

    /** 子单号，PAC订单编码（拆单后的子订单code）. */
    private String                      scmOrderCode;

    /** 物流单号 快递单号：当出库时会提供. */
    private String                      transCode;

    /** 物流商编码. */
    private String                      logisticsProviderCode;

    /** 物流商名称. */
    private String                      logisticsProviderName;

    /** 备注(相关明细信息拼接成的JSON字符串). */
    private String                      remark;

    /** pac拆单后的订单行. */
    private List<SplitSalesOrderLineV5> splitOrderLine;

    /**
     * <p>
     * 情景 ：一个订单 既包括 普通商品 也 包括 ESD商品 , 在商城端 只会生成一个订单,然而在后端OMS里面被拆分成了两个订单
     * </p>
     * <p>
     * 列如 混合订单 ：商城端的orderCode : 73923422023127040 ; 后端拆分orderCode : 实物 73923422023127040-SJ ; ESD 73923422023127040-XN
     * </p>
     * .
     *
     * @return the 商城订单编码
     */
    public String getBsOrderCode(){
        if (Validator.isNotNullOrEmpty(this.bsOrderCode)){
            int index = bsOrderCode.indexOf("-");
            bsOrderCode = index == -1 ? bsOrderCode : bsOrderCode.substring(0, index);
        }
        return bsOrderCode;
    }

    /**
     * 设置 商城订单编码.
     *
     * @param bsOrderCode
     *            the new 商城订单编码
     */
    public void setBsOrderCode(String bsOrderCode){
        this.bsOrderCode = bsOrderCode;
    }

    /**
     * 获得 商城退换货编码，商城发起退换货时非空.
     *
     * @return the 商城退换货编码，商城发起退换货时非空
     */
    public String getBsRaCode(){
        return bsRaCode;
    }

    /**
     * 设置 商城退换货编码，商城发起退换货时非空.
     *
     * @param bsRaCode
     *            the new 商城退换货编码，商城发起退换货时非空
     */
    public void setBsRaCode(String bsRaCode){
        this.bsRaCode = bsRaCode;
    }

    /**
     * 获得 子单号，PAC订单编码（拆单后的子订单code）.
     *
     * @return the 子单号，PAC订单编码（拆单后的子订单code）
     */
    public String getScmOrderCode(){
        return scmOrderCode;
    }

    /**
     * 设置 子单号，PAC订单编码（拆单后的子订单code）.
     *
     * @param scmOrderCode
     *            the new 子单号，PAC订单编码（拆单后的子订单code）
     */
    public void setScmOrderCode(String scmOrderCode){
        this.scmOrderCode = scmOrderCode;
    }

    /**
     * 获得 pac拆单后的订单行.
     *
     * @return the pac拆单后的订单行
     */
    public List<SplitSalesOrderLineV5> getSplitOrderLine(){
        return splitOrderLine;
    }

    /**
     * 设置 pac拆单后的订单行.
     *
     * @param splitOrderLine
     *            the new pac拆单后的订单行
     */
    public void setSplitOrderLine(List<SplitSalesOrderLineV5> splitOrderLine){
        this.splitOrderLine = splitOrderLine;
    }

    /**
     * 获得 物流单号 快递单号：当出库时会提供.
     *
     * @return the 物流单号 快递单号：当出库时会提供
     */
    public String getTransCode(){
        return transCode;
    }

    /**
     * 设置 物流单号 快递单号：当出库时会提供.
     *
     * @param transCode
     *            the new 物流单号 快递单号：当出库时会提供
     */
    public void setTransCode(String transCode){
        this.transCode = transCode;
    }

    /**
     * 获得 物流商编码.
     *
     * @return the 物流商编码
     */
    public String getLogisticsProviderCode(){
        return logisticsProviderCode;
    }

    /**
     * 设置 物流商编码.
     *
     * @param logisticsProviderCode
     *            the new 物流商编码
     */
    public void setLogisticsProviderCode(String logisticsProviderCode){
        this.logisticsProviderCode = logisticsProviderCode;
    }

    /**
     * 获得 物流商名称.
     *
     * @return the 物流商名称
     */
    public String getLogisticsProviderName(){
        return logisticsProviderName;
    }

    /**
     * 设置 物流商名称.
     *
     * @param logisticsProviderName
     *            the new 物流商名称
     */
    public void setLogisticsProviderName(String logisticsProviderName){
        this.logisticsProviderName = logisticsProviderName;
    }

    /**
     * 获得 备注(相关明细信息拼接成的JSON字符串).
     *
     * @return the 备注(相关明细信息拼接成的JSON字符串)
     */
    public String getRemark(){
        return remark;
    }

    /**
     * 设置 备注(相关明细信息拼接成的JSON字符串).
     *
     * @param remark
     *            the new 备注(相关明细信息拼接成的JSON字符串)
     */
    public void setRemark(String remark){
        this.remark = remark;
    }
}
