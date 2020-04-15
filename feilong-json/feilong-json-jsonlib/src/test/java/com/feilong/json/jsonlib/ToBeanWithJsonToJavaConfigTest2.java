/*
 * Copyright (C) 2008 feilong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.feilong.json.jsonlib;

import static com.feilong.core.util.MapUtil.newHashMap;

import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.json.AbstractJsonTest;
import com.feilong.json.jsonlib.entity.SplitOrderStatusV5;
import com.feilong.json.jsonlib.entity.SplitSalesOrderLineV5;
import com.feilong.json.jsonlib.entity.SplitSalesOrderV5;

/**
 * The Class JsonUtilToBeanWithJsonToJavaConfigTest.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 */
public class ToBeanWithJsonToJavaConfigTest2 extends AbstractJsonTest{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ToBeanWithJsonToJavaConfigTest2.class);

    @Test
    public void testToBean(){
        String json = "{'bsOrderCode': 'TAAA201700000002','bsRaCode': '2','opTime': '2017-12-18 12:24:44','opType': 4,'remark': '4','splitOrderList': [{'logisticsProviderCode': '55','logisticsProviderName': '66','remark': '77','scmOrderCode': '33','splitOrderLine': [{'bsSkuName': '222','extentionCode': '111','platformLineId': 444,'qty': 333},{'bsSkuName': '222','extentionCode': '111','platformLineId': 444,'qty': 333}],'transCode': '44'},{'logisticsProviderCode': '55','logisticsProviderName': '66','remark': '77','scmOrderCode': '33','splitOrderLine': [{'bsSkuName': '222','extentionCode': '111','platformLineId': 444,'qty': 333},{'bsSkuName': '222','extentionCode': '111','platformLineId': 444,'qty': 333}],'transCode': '44'}]}";

        JsonToJavaConfig jsonToJavaConfig = new JsonToJavaConfig(SplitOrderStatusV5.class);

        Map<String, Class<?>> map = newHashMap();
        map.put("splitOrderList", SplitSalesOrderV5.class);
        map.put("splitOrderLine", SplitSalesOrderLineV5.class);

        jsonToJavaConfig.setClassMap(map);

        SplitOrderStatusV5 splitOrderStatusV5 = JsonUtil.toBean(json, jsonToJavaConfig);

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug(JsonUtil.format(splitOrderStatusV5));
        }

        //        assertThat(
        //                        crmMemberInfoCommand,
        //                        allOf(//
        //                                        hasProperty("memberNo", is("11105000009")),
        //                                        hasProperty("name", is(nullValue())),
        //                                        hasProperty("gender", is("")),
        //                                        hasProperty("phone", is("15036334567")),
        //                                        hasProperty("email", is(nullValue())),
        //                                        hasProperty("birthday", is(""))
        //                        //
        //                        ));
    }
}
