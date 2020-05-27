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
package com.feilong.spring.expression;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.feilong.store.order.SalesOrder;

public class BeanTest{

    @Test
    public void getValue4(){
        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setLogisticsStatus(10);

        //---------------------------------------------------------------
        Object Object = SpelUtil.getValue("logisticsStatus", salesOrder);
        assertEquals(10, Object);
    }

    @Test
    public void getValue44(){
        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setLogisticsStatus(10);

        assertEquals("系统超时取消", SpelUtil.getValue("logisticsStatus==10?\"系统超时取消\":\"用户主动取消\"", salesOrder));
    }

}
