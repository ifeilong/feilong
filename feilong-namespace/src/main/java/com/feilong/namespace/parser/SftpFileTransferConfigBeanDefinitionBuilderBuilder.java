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
package com.feilong.namespace.parser;

import static com.feilong.namespace.BeanDefinitionParserUtil.addPropertyValue;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;

import com.feilong.net.filetransfer.sftp.SFTPFileTransferConfig;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * // <!-- sftp scope="prototype" -->
 * // <bean id="sftpFileTransfer" class="com.feilong.net.filetransfer.sftp.SFTPFileTransfer" scope="prototype">
 * // <property name="sftpFileTransferConfig">
 * // <bean class="com.feilong.net.filetransfer.sftp.SFTPFileTransferConfig">
 * // <property name="hostName" value="#{p_sftp['fileTransfer.sftp.hostName']}" />
 * // <property name="userName" value="#{p_sftp['fileTransfer.sftp.userName']}" />
 * // <property name="password">
 * // <value><![CDATA[#{p_sftp['fileTransfer.sftp.password']}]]></value>
 * // </property>
 * // <property name="port" value="#{p_sftp['fileTransfer.sftp.port']}" />
 * // </bean>
 * // </property>
 * // </bean>
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.0.8
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SftpFileTransferConfigBeanDefinitionBuilderBuilder{

    /**
     * Builds the.
     *
     * @param element
     *            the element
     * @return the bean definition builder
     */
    public static BeanDefinitionBuilder build(Element element){
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(SFTPFileTransferConfig.class);

        addPropertyValue(element, beanDefinitionBuilder, "hostName", true);
        addPropertyValue(element, beanDefinitionBuilder, "userName", true);
        addPropertyValue(element, beanDefinitionBuilder, "password", true);

        //, "sshConfig"
        addPropertyValue(element, beanDefinitionBuilder, "port", "sessionTimeout");

        return beanDefinitionBuilder;
    }
}
