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

import static com.feilong.core.util.MapUtil.newHashMap;

import java.util.Map;

import org.junit.Test;

import com.feilong.json.processor.CapitalizePropertyNameProcessor;
import com.feilong.lib.json.processors.PropertyNameProcessor;
import com.feilong.json.JavaToJsonConfig;
import com.feilong.json.JsonUtil;
import com.feilong.json.entity.CrmAddpointCommand;
import com.feilong.test.AbstractTest;

public class CrmAddpointCommandJsonTest extends AbstractTest{

    /**
     * TestJsonTest.
     */
    @Test
    public void testJsonTest(){
        CrmAddpointCommand crmAddpointCommand = new CrmAddpointCommand();

        crmAddpointCommand.setBuyerId("123456");
        crmAddpointCommand.setConsumptionChannel("feilongstore");
        crmAddpointCommand.setOpenId("feilong888888ky");
        crmAddpointCommand.setOrderCode("fl123456");

        //---------------------------------------------------------------

        JavaToJsonConfig jsonFormatConfig = new JavaToJsonConfig();

        Map<Class<?>, PropertyNameProcessor> targetClassAndPropertyNameProcessorMap = newHashMap(1);
        targetClassAndPropertyNameProcessorMap.put(CrmAddpointCommand.class, CapitalizePropertyNameProcessor.INSTANCE);

        jsonFormatConfig.setJsonTargetClassAndPropertyNameProcessorMap(targetClassAndPropertyNameProcessorMap);

        //---------------------------------------------------------------

        LOGGER.debug(JsonUtil.format(crmAddpointCommand, jsonFormatConfig));
    }
}
