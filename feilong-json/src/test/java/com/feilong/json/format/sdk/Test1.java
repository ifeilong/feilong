package com.feilong.json.format.sdk;

import static com.feilong.core.bean.ConvertUtil.toList;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.json.JavaToJsonConfig;
import com.feilong.json.JsonHelper;
import com.feilong.json.JsonUtil;
import com.feilong.json.builder.JavaToJsonConfigBuilder;
import com.feilong.json.builder.JsonConfigBuilder;
import com.feilong.lib.json.JSON;
import com.feilong.lib.json.JsonConfig;

public class Test1{

    private static final Logger       LOGGER        = LoggerFactory.getLogger(Test1.class);

    private static final OrderDetails ORDER_DETAILS = build();

    //---------------------------------------------------------------
    @Test
    public void testNew1(){
        LOGGER.debug(JsonUtil.format(ORDER_DETAILS));

    }

    @Test
    public void testNew12(){
        OrderInfo zeroOrderInfo = buildOrderInfo("24121", "0", "COD");

        JsonConfig jsonConfig = buildJsonConfig(zeroOrderInfo);

        format(jsonConfig, zeroOrderInfo);
    }

    //---------------------------------------------------------------
    @Test
    public void testNew(){
        JsonConfig jsonConfig = buildJsonConfig(ORDER_DETAILS);

        format(jsonConfig, ORDER_DETAILS);
    }

    private JsonConfig buildJsonConfig(Object obj){
        JavaToJsonConfig useJavaToJsonConfig = JavaToJsonConfigBuilder.buildUseJavaToJsonConfig(obj, null);
        //useJavaToJsonConfig.setIsIgnoreNullValueElement(false);

        JsonConfig jsonConfig = JsonConfigBuilder.build(obj, useJavaToJsonConfig);

        jsonConfig.setJsonPropertyFilter((source,name,value) -> {

            if ("discountDetials".equals(name)){
                DiscountDetials discountDetials = (DiscountDetials) value;

                if ("0".equals(discountDetials.getDiscountInfo().getDiscountAmt())){
                    return true;
                }
            }

            return false;
        });
        return jsonConfig;
    }

    private void format(JsonConfig jsonConfig,Object obj){
        JSON json = JsonHelper.toJSON(obj, jsonConfig);

        LOGGER.debug(json.toString(4, 4));
    }

    private static OrderDetails build(){
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setOrderInfoList(
                        toList(//
                                        buildOrderInfo("24121", "0", "COD"),
                                        buildOrderInfo("6666", "8888", "alipay")));
        orderDetails.setSsd("test");
        return orderDetails;
    }

    private static OrderInfo buildOrderInfo(String chargeDetials,String discountAmt,String discountCode){

        DiscountInfo discountInfo = new DiscountInfo();
        discountInfo.setDiscountAmt(discountAmt);
        discountInfo.setDiscountCode(discountCode);

        DiscountDetials discountDetials = new DiscountDetials();
        discountDetials.setDiscountInfo(discountInfo);

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setChargeDetials(chargeDetials);
        orderInfo.setDiscountDetials(discountDetials);
        return orderInfo;
    }

}
