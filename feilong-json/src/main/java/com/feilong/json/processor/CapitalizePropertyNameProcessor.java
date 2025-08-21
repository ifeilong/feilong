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

import com.feilong.lib.json.processors.PropertyNameProcessor;
import com.feilong.lib.lang3.StringUtils;

/**
 * 将指定类型下面所有属性名字<b>首字母变大写</b>的处理器.
 * 
 * <h3>说明:</h3>
 * <blockquote>
 * 
 * 我们这边的代码
 * 
 * <pre class="code">
 * public class CrmAddpointCommand implements Serializable{
 * 
 *     <span style="color:green">// 用户编码</span>
 *     private String openId;
 * 
 *     <span style="color:green">// 渠道：Tmall - 天猫 JD - 京东</span>
 *     private String consumptionChannel;
 * 
 *     <span style="color:green">// 淘宝/京东买家账号</span>
 *     private String buyerId;
 * 
 *     <span style="color:green">// 电商订单编号 </span>
 *     private String orderCode;
 * 
 *     <span style="color:green">// setter getter</span>
 * }
 * 
 * </pre>
 * 
 * 符合标准的java代码规范,如果直接使用 {@link com.feilong.json.JsonUtil#format(Object)}
 * 
 * <pre class="code">
 * 
 * public void testJsonTest(){
 *     CrmAddpointCommand crmAddpointCommand = new CrmAddpointCommand();
 * 
 *     crmAddpointCommand.setBuyerId("123456");
 *     crmAddpointCommand.setConsumptionChannel("feilongstore");
 *     crmAddpointCommand.setOpenId("feilong888888ky");
 *     crmAddpointCommand.setOrderCode("fl123456");
 * 
 *     log.debug(JsonUtil.format(crmAddpointCommand));
 * }
 * 
 * </pre>
 * 
 * <b>输出结果:</b>
 * 
 * <pre class="code">
 * {
 * "orderCode": "fl123456",
 * "buyerId": "123456",
 * "consumptionChannel": "feilongstore",
 * "openId": "feilong888888ky"
 * }
 * 
 * </pre>
 * 
 * 输出的属性大小写和 crmAddpointCommand 对象里面字段的大小写相同,但是对方接口要求首字符要大写:
 * 
 * <p>
 * <img src="https://cloud.githubusercontent.com/assets/3479472/19713507/434572a8-9b79-11e6-987a-07e572df5bf9.png" alt="json">
 * </p>
 * 
 * 此时,你可以使用
 * 
 * <pre class="code">
 * 
 * public void testJsonTest(){
 *     CrmAddpointCommand crmAddpointCommand = new CrmAddpointCommand();
 * 
 *     crmAddpointCommand.setBuyerId("123456");
 *     crmAddpointCommand.setConsumptionChannel("feilongstore");
 *     crmAddpointCommand.setOpenId("feilong888888ky");
 *     crmAddpointCommand.setOrderCode("fl123456");
 * 
 *         //---------------------------------------------------------------
 * 
 *     JavaToJsonConfig javaToJsonConfig = new JavaToJsonConfig();
 * 
 *     Map{@code <Class<?>, PropertyNameProcessor>} targetClassAndPropertyNameProcessorMap = newHashMap(1);
 *     targetClassAndPropertyNameProcessorMap.put(CrmAddpointCommand.class, CapitalizePropertyNameProcessor.INSTANCE);
 * 
 *     <span style=
"color:red">javaToJsonConfig.setJsonTargetClassAndPropertyNameProcessorMap(targetClassAndPropertyNameProcessorMap);</span>
 * 
 *     log.debug(JsonUtil.format(crmAddpointCommand, javaToJsonConfig));
 * }
 * </pre>
 * 
 * <b>输出结果:</b>
 * 
 * <pre class="code">
 * {
 * "<span style="color:red">O</span>rderCode": "fl123456",
 * "<span style="color:red">B</span>uyerId": "123456",
 * "<span style="color:red">C</span>onsumptionChannel": "feilongstore",
 * "<span style="color:red">O</span>penId": "feilong888888ky"
 * }
 * </pre>
 * 
 * </blockquote>
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see <a href="https://github.com/venusdrogon/feilong-core/issues/505">json format 需要支持修改key的名字</a>
 * @since 1.9.3
 */
public class CapitalizePropertyNameProcessor implements PropertyNameProcessor{

    /** Singleton instance. */
    public static final PropertyNameProcessor INSTANCE = new CapitalizePropertyNameProcessor();

    /**
     * Instantiates a new capitalize property name processor.
     */
    private CapitalizePropertyNameProcessor(){
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see net.sf.json.processors.PropertyNameProcessor#processPropertyName(java.lang.Class, java.lang.String)
     */
    @Override
    public String processPropertyName(@SuppressWarnings("rawtypes") Class beanClass,String currentPropertyName){
        return StringUtils.capitalize(currentPropertyName);
    }
}
