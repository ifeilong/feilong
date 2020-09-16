package com.feilong.json.format.sdk;

import com.feilong.lib.lang3.builder.ToStringBuilder;
import com.feilong.lib.lang3.builder.ToStringStyle;

public class OrderInfo{

    private String          chargeDetials;

    private DiscountDetials discountDetials;

    public String getChargeDetials(){
        return chargeDetials;
    }

    public void setChargeDetials(String chargeDetials){
        this.chargeDetials = chargeDetials;
    }

    public DiscountDetials getDiscountDetials(){
        return discountDetials;
    }

    public void setDiscountDetials(DiscountDetials discountDetials){
        this.discountDetials = discountDetials;
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
