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
package com.feilong.json.jsonlib;

import static com.feilong.core.bean.ConvertUtil.toBigDecimal;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.feilong.json.jsonlib.entity.MyBigDecimalBean;

public class FormatBeanBigDecimalTest{

    @Test
    public void test(){
        MyBigDecimalBean myBigDecimalBean = new MyBigDecimalBean();
        myBigDecimalBean.setMoney(toBigDecimal("99999999.00"));

        assertTrue(JsonUtil.format(myBigDecimalBean, 0, 0).contains("\"money\":\"99999999.00\""));
    }

    @Test
    public void test1(){
        MyBigDecimalBean myBigDecimalBean = new MyBigDecimalBean();
        myBigDecimalBean.setMoney(toBigDecimal("99999999.0000"));
        assertTrue(JsonUtil.format(myBigDecimalBean, 0, 0).contains("\"money\":\"99999999.0000\""));
    }

    @Test
    public void test12(){
        MyBigDecimalBean myBigDecimalBean = new MyBigDecimalBean();
        myBigDecimalBean.setMoney(toBigDecimal("99999999.0"));
        assertTrue(JsonUtil.format(myBigDecimalBean, 0, 0).contains("\"money\":\"99999999.0\""));
    }

}
