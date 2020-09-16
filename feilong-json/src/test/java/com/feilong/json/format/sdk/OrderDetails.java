package com.feilong.json.format.sdk;

import java.util.List;

import com.feilong.lib.lang3.builder.ToStringBuilder;
import com.feilong.lib.lang3.builder.ToStringStyle;

public class OrderDetails{

    private List<OrderInfo> orderInfoList;

    private String          ssd;

    public List<OrderInfo> getOrderInfoList(){
        return orderInfoList;
    }

    public void setOrderInfoList(List<OrderInfo> orderInfoList){
        this.orderInfoList = orderInfoList;
    }

    public String getSsd(){
        return ssd;
    }

    public void setSsd(String ssd){
        this.ssd = ssd;
    }

    //---------------------------------------------------------------
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString(){
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
