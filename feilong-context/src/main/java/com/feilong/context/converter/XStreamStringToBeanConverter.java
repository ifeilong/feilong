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

import com.feilong.xml.XmlUtil;

/**
 * 使用 Xstream 来将 字符串转换成对象.
 * 
 * <h3>使用:</h3>
 * 
 * <blockquote>
 * 首先自己项目需要依赖 xstream,<span style="color:green">版本号自行使用最新的版本</span>
 * 
 * <pre class="code">
 * {@code
 <dependency>
        <groupId>com.thoughtworks.xstream</groupId>
        <artifactId>xstream</artifactId>
        <version>1.4.20</version> 
      </dependency>
 * }
 * </pre>
 * 
 * </blockquote>
 * 
 * <h3>使用场景:</h3>
 * 
 * <blockquote>
 * <p>
 * 比如weixin接口返回的response:
 * </p>
 * 
 * <blockquote>
 * 
 * <pre class="code">
{@code <xml>
   <return_code><![CDATA[SUCCESS]]></return_code>
   <return_msg><![CDATA[OK]]></return_msg>
   <appid><![CDATA[wx2421b1c4370ec43b]]></appid>
   <mch_id><![CDATA[10000100]]></mch_id>
   <nonce_str><![CDATA[BFK89FC6rxKCOjLX]]></nonce_str>
   <sign><![CDATA[72B321D92A7BFA0B2509F3D13C7B1631]]></sign>
   <result_code><![CDATA[SUCCESS]]></result_code>
   <result_msg><![CDATA[OK]]></result_msg>
</xml>}
 * </pre>
 * 
 * </blockquote>
 * 
 * 
 * 
 * <b>现在有对应的bean:</b>
 * 
 * <pre class="code">
 * {@code @XStreamAlias("xml")}
 * public class WechatCloseResultCommand extends AbstractWechatResponse implements CloseResultCommand{
 * {@code @XStreamAlias("result_msg")}
 *     private String resultMsg;
 * 
 * }
 * </pre>
 * 
 * 此时可以直接配置成
 * 
 * <pre>
{@code
 * 
 * <property name="stringToBeanConverter">
            <bean class="com.feilong.context.converter.XStreamStringToBeanConverter" p:beanClass=
"com.feilong.netpay.wechat.close.WechatCloseResultCommand" />
        </property>
}
 * </pre>
 * 
 * 
 * </blockquote>
 * 
 * 
 * 
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @param <T>
 *            the generic type
 * @see XmlUtil#toBean(String, Class)
 * @since 1.11.3
 */
public class XStreamStringToBeanConverter<T> extends AbstractBeanClassStringToBeanConverter<T>{

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.bind.AbstractStringToBeanConverter#handler(java.lang.String)
     */
    @Override
    protected T handler(String inputString){
        return XmlUtil.toBean(inputString, beanClass);
    }
}
