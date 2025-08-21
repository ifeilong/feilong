package com.feilong.json.format.sdk;

import static com.feilong.core.bean.ConvertUtil.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

import org.junit.Test;

import com.feilong.json.JavaToJsonConfig;
import com.feilong.json.JsonUtil;

@lombok.extern.slf4j.Slf4j

public class PropertyFilterTest{

    private static final OrderDetails ORDER_DETAILS = build();

    //---------------------------------------------------------------
    @Test
    public void testNew1(){
        JavaToJsonConfig javaToJsonConfig = new JavaToJsonConfig();
        javaToJsonConfig.setPropertyFilter((source,name,value) -> {

            if ("discountDetials".equals(name)){
                DiscountDetials discountDetials = (DiscountDetials) value;

                if ("0".equals(discountDetials.getDiscountInfo().getDiscountAmt())){
                    return true;
                }
            }

            return false;
        });
        String format = JsonUtil.format(ORDER_DETAILS, javaToJsonConfig);
        log.debug(format);

        assertThat(
                        format,
                        allOf(
                                        containsString("{\"chargeDetials\": \"24121\"}"), //
                                        containsString("\"discountAmt\": \"8888\"")));

    }

    //---------------------------------------------------------------

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
