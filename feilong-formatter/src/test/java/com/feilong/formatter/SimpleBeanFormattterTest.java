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
package com.feilong.formatter;

import static com.feilong.core.bean.ConvertUtil.toBigDecimal;
import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.date.DateUtil.formatElapsedTime;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.feilong.core.lang.NumberUtil;
import com.feilong.formatter.entity.BeanFormatterConfig;
import com.feilong.store.order.OrderLine;
import com.feilong.test.AbstractTest;

@lombok.extern.slf4j.Slf4j
@SuppressWarnings("squid:S2699") //Tests should include assertions //https://stackoverflow.com/questions/10971968/turning-sonar-off-for-certain-code
public class SimpleBeanFormattterTest extends AbstractTest{

    private static Iterable<OrderLine> ITERABLE_DATA       = null;

    private BeanFormatterConfig        beanFormatterConfig = null;

    //---------------------------------------------------------------

    @Test
    public void testFormatToSimpleTable2(){
    }

    @Test
    public void testFormatToSimpleTable3(){
        beanFormatterConfig = new BeanFormatterConfig();
    }

    @Test
    public void testFormatToSimpleTable5(){
        beanFormatterConfig = new BeanFormatterConfig();
        beanFormatterConfig.setIncludePropertyNames("id", "orderId", "MSRP", "salePrice", "discountPrice", "count", "subtotal");
    }

    @Test
    public void testFormatToSimpleTable6(){
        beanFormatterConfig = new BeanFormatterConfig();
        beanFormatterConfig.setSorts("id", "orderId");
    }

    @Test
    public void testFormatToSimpleTable7(){
        beanFormatterConfig = new BeanFormatterConfig();
        beanFormatterConfig.setIncludePropertyNames("id", "orderId", "MSRP", "salePrice", "discountPrice", "count", "subtotal");
        beanFormatterConfig.setSorts("id", "orderId");
    }

    //---------------------------------------------------------------

    @BeforeClass
    public static void beforeClass(){
        OrderLine orderLine1 = new OrderLine();
        orderLine1.setId(10450L);
        orderLine1.setOrderId(888L);
        orderLine1.setItemId(65L);
        orderLine1.setSkuId(745L);

        orderLine1.setMSRP(toBigDecimal(1000));
        orderLine1.setSalePrice(toBigDecimal(800));
        orderLine1.setCount(5);
        orderLine1.setDiscountPrice(toBigDecimal(100));
        orderLine1.setSubtotal(
                        NumberUtil.getMultiplyValue(orderLine1.getSalePrice(), orderLine1.getCount(), 2)
                                        .subtract(orderLine1.getDiscountPrice()));

        //-----------------------------------------------------------------
        OrderLine orderLine2 = new OrderLine();
        orderLine2.setId(12356L);
        orderLine2.setOrderId(888L);
        orderLine2.setItemId(65L);
        orderLine2.setSkuId(745L);

        orderLine2.setMSRP(toBigDecimal(1000));
        orderLine2.setSalePrice(toBigDecimal(800));
        orderLine2.setCount(5);
        orderLine2.setDiscountPrice(toBigDecimal(100));
        orderLine2.setSubtotal(
                        NumberUtil.getMultiplyValue(orderLine2.getSalePrice(), orderLine2.getCount(), 2)
                                        .subtract(orderLine2.getDiscountPrice()));

        ITERABLE_DATA = toList(orderLine1, orderLine2);
    }

    @After
    public void after(){

        long beginTimeMillis = System.currentTimeMillis();

        log.debug(FormatterUtil.formatToSimpleTable(ITERABLE_DATA, beanFormatterConfig));
        log.info("useTime: [{}]", formatElapsedTime(beginTimeMillis));

    }
}
