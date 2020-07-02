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

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.feilong.namespace.RuntimeBeanReferenceBuilder;
import com.feilong.net.filetransfer.sftp.SFTPFileTransfer;

/**
 * 用来构造 {@link com.feilong.net.filetransfer.sftp.SFTPFileTransfer}.
 * 
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
public class SftpFileTransferBeanDefinitionParser extends AbstractBeanDefinitionParser{

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.beans.factory.xml.AbstractBeanDefinitionParser#parseInternal(org.w3c.dom.Element,
     * org.springframework.beans.factory.xml.ParserContext)
     */
    @Override
    protected AbstractBeanDefinition parseInternal(Element element,ParserContext parserContext){
        // create a RootBeanDefinition that will serve as configuration holder for the 'pattern' attribute and the 'lenient' attribute  
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition();
        rootBeanDefinition.setBeanClass(SFTPFileTransfer.class);
        rootBeanDefinition.setScope("prototype");//scope="prototype"

        //---------------------------------------------------------------
        rootBeanDefinition.getPropertyValues().addPropertyValue(
                        "sftpFileTransferConfig",
                        RuntimeBeanReferenceBuilder
                                        .build(parserContext, SftpFileTransferConfigBeanDefinitionBuilderBuilder.build(element)));

        return rootBeanDefinition;
    }

}