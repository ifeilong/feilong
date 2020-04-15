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
import java.util.Date;
import java.util.List;

import com.feilong.core.Validator;

/**
 * 拆单场景订单状态.
 *
 * @author hailong yu
 */
public class SplitOrderStatusV5 implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long       serialVersionUID = -1739318072484239436L;

    /** 商城订单编码. */
    private String                  bsOrderCode;

    /** 商城退换货编码，商城发起退换货时非空. */
    private String                  bsRaCode;

    /**
     * 操作类型
     * 1.订单创建成功
     * 2.订单取消
     * 3.过单到仓库
     * 4.拆单
     * 5.销售出库
     * 7.退货已入库
     * 8.换货已入库
     * 9.换货已出库
     * 10.交易完成(订单)
     * 13:系统刷款成功
     * 6.退换申请创建成功
     * 18.退换申请创建失败(只有前端退换货反馈失败)
     * 21.退换货申请取消
     * 22:退换货申请取消成功
     * 23:退换货申请取消失败
     * 参照：OrderStatusV5Constants
     */
    private Integer                 opType;

    /** 状态变更时间. */
    private Date                    opTime;

    /** 备注(相关明细信息拼接成的JSON字符串). */
    private String                  remark;

    /** 拆单的子订单list. */
    private List<SplitSalesOrderV5> splitOrderList;

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
     * 获得 操作类型 1.
     *
     * @return the 操作类型 1
     */
    public Integer getOpType(){
        return opType;
    }

    /**
     * 设置 操作类型 1.
     *
     * @param opType
     *            the new 操作类型 1
     */
    public void setOpType(Integer opType){
        this.opType = opType;
    }

    /**
     * 获得 状态变更时间.
     *
     * @return the 状态变更时间
     */
    public Date getOpTime(){
        return opTime;
    }

    /**
     * 设置 状态变更时间.
     *
     * @param opTime
     *            the new 状态变更时间
     */
    public void setOpTime(Date opTime){
        this.opTime = opTime;
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

    /**
     * 获得 拆单的子订单list.
     *
     * @return the 拆单的子订单list
     */
    public List<SplitSalesOrderV5> getSplitOrderList(){
        return splitOrderList;
    }

    /**
     * 设置 拆单的子订单list.
     *
     * @param splitOrderList
     *            the new 拆单的子订单list
     */
    public void setSplitOrderList(List<SplitSalesOrderV5> splitOrderList){
        this.splitOrderList = splitOrderList;
    }
}
