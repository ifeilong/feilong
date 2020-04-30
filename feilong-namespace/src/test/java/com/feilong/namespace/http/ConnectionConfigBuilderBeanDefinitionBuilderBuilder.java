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
package com.feilong.namespace.http;

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.toInteger;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.feilong.context.invoker.http.ConnectionConfigBuilder;
import com.feilong.namespace.RuntimeBeanReferenceBuilder;
import com.feilong.net.http.ConnectionConfig;

/**
 * The Class HttpRequestBuilderParserUtil.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 4.0.6
 */
public class ConnectionConfigBuilderBeanDefinitionBuilderBuilder{

    public static BeanDefinitionBuilder build(Element element,ParserContext parserContext){
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
                        .genericBeanDefinition("com.feilong.context.invoker.http.DefaultHttpRequestBuilder");

        beanDefinitionBuilder.addPropertyValue(
                        "httpTypeBeanProperty",
                        RuntimeBeanReferenceBuilder
                                        .build(parserContext, SimpleHttpTypeBeanPropertyBeanDefinitionBuilderBuilder.build(element)));

        return beanDefinitionBuilder;
    }

    public static ConnectionConfigBuilder build(Element element){

        final String userName = element.getAttribute("userName");
        final String password = element.getAttribute("password");
        final String proxyAddress = element.getAttribute("proxyAddress");
        final String proxyPort = element.getAttribute("proxyPort");
        final String connectTimeout = element.getAttribute("connectTimeout");
        final String readTimeout = element.getAttribute("readTimeout");
        final String contentCharset = element.getAttribute("contentCharset");

        if (StringUtils.isAllBlank(userName, password, proxyAddress, proxyPort, connectTimeout, readTimeout, contentCharset)){
            return null;
        }

        //---------------------------------------------------------------

        ConnectionConfigBuilder connectionConfigBuilder = new ConnectionConfigBuilder(){

            @Override
            public ConnectionConfig build(Object request){

                ConnectionConfig connectionConfig = new ConnectionConfig();
                if (isNotNullOrEmpty(userName)){
                    connectionConfig.setUserName(userName);
                }

                if (isNotNullOrEmpty(password)){
                    connectionConfig.setPassword(password);
                }

                //---------------------------------------------------------------

                if (isNotNullOrEmpty(proxyAddress)){
                    connectionConfig.setProxyAddress(proxyAddress);
                }

                if (isNotNullOrEmpty(proxyPort)){
                    connectionConfig.setProxyPort(toInteger(proxyPort));
                }

                //---------------------------------------------------------------

                if (isNotNullOrEmpty(connectTimeout)){
                    connectionConfig.setConnectTimeout(toInteger(connectTimeout));
                }

                if (isNotNullOrEmpty(readTimeout)){
                    connectionConfig.setReadTimeout(toInteger(readTimeout));
                }

                if (isNotNullOrEmpty(contentCharset)){
                    connectionConfig.setContentCharset(contentCharset);
                }

                return connectionConfig;
            }
        };
        return connectionConfigBuilder;
    }

}
