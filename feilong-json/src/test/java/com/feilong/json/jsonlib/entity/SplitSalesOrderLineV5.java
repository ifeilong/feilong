/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.feilong.json.jsonlib.entity;

import java.io.Serializable;

/**
 * 拆单的子订单行结构
 * 
 * @author hailong yu
 *
 */

public class SplitSalesOrderLineV5 implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = -1739318072484239435L;

    /**
     * oms与bsSKU匹配唯一标识
     * 
     */
    private String            extentionCode;

    /**
     * 商城订单行商品名称(可以为空)
     * 
     */
    private String            bsSkuName;

    /**
     * 数量
     * 
     */
    private Integer           qty;

    /**
     * 拆单后用，原始订单号id
     */
    private Long              platformLineId;

    public String getExtentionCode(){
        return extentionCode;
    }

    public void setExtentionCode(String extentionCode){
        this.extentionCode = extentionCode;
    }

    public String getBsSkuName(){
        return bsSkuName;
    }

    public void setBsSkuName(String bsSkuName){
        this.bsSkuName = bsSkuName;
    }

    public Integer getQty(){
        return qty;
    }

    public void setQty(Integer qty){
        this.qty = qty;
    }

    public Long getPlatformLineId(){
        return platformLineId;
    }

    public void setPlatformLineId(Long platformLineId){
        this.platformLineId = platformLineId;
    }
}
