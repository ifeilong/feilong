package com.feilong.json.format.sdk;

import com.feilong.lib.lang3.builder.ToStringBuilder;
import com.feilong.lib.lang3.builder.ToStringStyle;

public class DiscountDetials{

    private DiscountInfo discountInfo;

    public DiscountInfo getDiscountInfo(){
        return discountInfo;
    }

    public void setDiscountInfo(DiscountInfo discountInfo){
        this.discountInfo = discountInfo;
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
