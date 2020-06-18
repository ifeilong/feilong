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
package com.feilong.json.processor;

import static com.feilong.core.bean.ConvertUtil.toMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

import org.junit.Test;

import com.feilong.json.JavaToJsonConfig;
import com.feilong.json.JsonUtil;
import com.feilong.json.entity.CrmAddpointCommand;
import com.feilong.test.AbstractTest;

public class CrmAddpointCommandJsonTest extends AbstractTest{

    @Test
    public void test(){
        CrmAddpointCommand crmAddpointCommand = new CrmAddpointCommand();

        crmAddpointCommand.setBuyerId("123456");
        crmAddpointCommand.setConsumptionChannel("feilongstore");
        crmAddpointCommand.setOpenId("feilong888888ky");
        crmAddpointCommand.setOrderCode("fl123456");

        //---------------------------------------------------------------

        JavaToJsonConfig javaToJsonConfig = new JavaToJsonConfig();

        javaToJsonConfig.setJsonTargetClassAndPropertyNameProcessorMap(
                        toMap(CrmAddpointCommand.class, CapitalizePropertyNameProcessor.INSTANCE));

        //---------------------------------------------------------------

        assertThat(
                        JsonUtil.format(crmAddpointCommand, javaToJsonConfig), //
                        allOf(//
                                        containsString("\"OpenId\": \"feilong888888ky\""),
                                        containsString("\"ConsumptionChannel\": \"feilongstore\""),
                                        containsString("\"OrderCode\": \"fl123456\""),
                                        containsString("\"BuyerId\": \"123456\"")

                        ));
    }
}
