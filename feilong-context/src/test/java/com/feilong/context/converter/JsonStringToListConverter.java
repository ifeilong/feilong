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
package com.feilong.context.converter;

import java.util.List;

import com.feilong.json.JsonToJavaConfig;
import com.feilong.json.JsonUtil;

/**
 * json 字符串结果转换成list.
 * 
 * <p>
 * <b>场景:</b>
 * 比如doku 支付方式, 在GeneratePayCode流程中,返回的结果json字符串是
 * </p>
 * 
 * <pre class="code">
 * 原始结果:
   {
   "res_pay_code":"00001014",
   "res_pairing_code":"280618203138833110",
   "res_response_msg":"SUCCESS",
   "res_payment_code":"5511",
   "res_response_code":"0000",
   "res_session_id":"888"
   }
 * </pre>
 *
 * 此时有java bean
 * 
 * <pre class="code">
 * public class DokuGeneratePayCodeResultCommand implements GeneratePayCodeResultCommand{
 * 
 *     private String resPayCode;
 * 
 *     private String resPairingCode;
 * 
 *     private String resResponseMsg;
 * 
 *     private String resPaymentCode;
 * 
 *     private String resResponseCode;
 * 
 *     private String resSessionId;
 * 
 *     //setter getter省略
 * }
 * </pre>
 * 
 * 返回的json字符串属性带有下划线, 而javabean 是标准的驼峰命名,你可以使用以下配置:
 * 
 * <pre>
{@code
<property name="stringToBeanConverter">
    <bean class="com.feilong.context.converter.JsonStringToBeanConverter">
       <property name="jsonToJavaConfig">
           <bean class="com.feilong.json.jsonlib.JsonToJavaConfig" p:rootClass=
"com.feilong.netpay.doku.generatepaycode.DokuGeneratePayCodeResultCommand">
                <property name="javaIdentifierTransformer">
                      <bean class="com.feilong.json.jsonlib.transformer.SeparatorToCamelCaseJavaIdentifierTransformer" />
                </property>
            </bean>
        </property>
     </bean>
</property>
}
 * </pre>
 * 
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @param <T>
 *            the generic type
 * @since 4.0.8
 */
public class JsonStringToListConverter<T> extends AbstractStringToBeanConverter<List<T>>{

    /** The json to java config. */
    private JsonToJavaConfig jsonToJavaConfig;

    //---------------------------------------------------------------

    /**
     * Instantiates a new json string to bean converter.
     */
    public JsonStringToListConverter(){
        super();
    }

    /**
     * Instantiates a new json string to bean converter.
     *
     * @param rootClass
     *            the root class
     */
    public JsonStringToListConverter(Class<T> rootClass){
        super();
        this.jsonToJavaConfig = new JsonToJavaConfig(rootClass);
    }

    /**
     * Instantiates a new json string to bean converter.
     *
     * @param jsonToJavaConfig
     *            the json to java config
     */
    public JsonStringToListConverter(JsonToJavaConfig jsonToJavaConfig){
        super();
        this.jsonToJavaConfig = jsonToJavaConfig;
    }

    //---------------------------------------------------------------
    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.bind.AbstractBeanClassStringToBeanConverter#handler(java.lang.Class, java.lang.String)
     */
    @Override
    protected List<T> handler(String inputJsonString){
        return JsonUtil.toList(inputJsonString, jsonToJavaConfig);
    }

    //---------------------------------------------------------------

    @Override
    protected String formatValue(String value){
        return JsonUtil.toString(value);
    }

    //---------------------------------------------------------------

    /**
     * Sets the json to java config.
     *
     * @param jsonToJavaConfig
     *            the jsonToJavaConfig to set
     */
    public void setJsonToJavaConfig(JsonToJavaConfig jsonToJavaConfig){
        this.jsonToJavaConfig = jsonToJavaConfig;
    }
}
