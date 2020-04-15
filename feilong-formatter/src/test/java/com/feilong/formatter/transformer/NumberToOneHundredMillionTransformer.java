/**
 * Copyright (c) 2017 TianYan All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TianYan.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with TianYan.
 *
 * TianYan MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. TianYan SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.feilong.formatter.transformer;

import static com.feilong.core.bean.ConvertUtil.toBigDecimal;

import org.apache.commons.collections4.Transformer;

import com.feilong.core.lang.NumberUtil;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.0
 */
public class NumberToOneHundredMillionTransformer implements Transformer<Number, String>{

    /** Static instance. */
    // the static instance works for all types
    public static final NumberToOneHundredMillionTransformer INSTANCE = new NumberToOneHundredMillionTransformer();

    //---------------------------------------------------------------
    /*
     * (non-Javadoc)
     * 
     * @see org.apache.commons.collections4.Transformer#transform(java.lang.Object)
     */
    @Override
    public String transform(Number value){
        return null == value ? null : (NumberUtil.getDivideValue(toBigDecimal(value), NumberUtil.HUNDRED_MILLION, 2) + "亿");
    }
}
