package com.feilong.json.format.sdk;

import com.feilong.lib.lang3.builder.ToStringBuilder;
import com.feilong.lib.lang3.builder.ToStringStyle;

public class DiscountInfo{

    private String discountAmt;

    private String discountCode;

    public String getDiscountAmt(){
        return discountAmt;
    }

    public void setDiscountAmt(String discountAmt){
        this.discountAmt = discountAmt;
    }

    public String getDiscountCode(){
        return discountCode;
    }

    public void setDiscountCode(String discountCode){
        this.discountCode = discountCode;
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
