package com.feilong.json;

import java.io.Serializable;

/**
 * crm电商自助积分实体类
 */
public class CrmAddpointCommand implements Serializable{

    private static final long serialVersionUID = 1L;

    /** 用户编码 */
    private String            openId;

    /** 渠道：Tmall - 天猫 JD - 京东 */
    private String            consumptionChannel;

    /** 淘宝/京东买家账号 */
    private String            buyerId;

    /** 电商订单编号 */
    private String            orderCode;

    //---------------------------------------------------------------

    /**
     * @return the openId
     */
    public String getOpenId(){
        return openId;
    }

    /**
     * @param openId
     *            the openId to set
     */
    public void setOpenId(String openId){
        this.openId = openId;
    }

    /**
     * @return the consumptionChannel
     */
    public String getConsumptionChannel(){
        return consumptionChannel;
    }

    /**
     * @param consumptionChannel
     *            the consumptionChannel to set
     */
    public void setConsumptionChannel(String consumptionChannel){
        this.consumptionChannel = consumptionChannel;
    }

    /**
     * @return the buyerId
     */
    public String getBuyerId(){
        return buyerId;
    }

    /**
     * @param buyerId
     *            the buyerId to set
     */
    public void setBuyerId(String buyerId){
        this.buyerId = buyerId;
    }

    /**
     * @return the orderCode
     */
    public String getOrderCode(){
        return orderCode;
    }

    /**
     * @param orderCode
     *            the orderCode to set
     */
    public void setOrderCode(String orderCode){
        this.orderCode = orderCode;
    }

}
