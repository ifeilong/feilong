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
package com.feilong.xml;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 普通的 XXEUtil.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.12.1
 */
public final class XXEUtil{

    private static final Logger LOGGER = LoggerFactory.getLogger(XXEUtil.class);

    /** Don't let anyone instantiate this class. */
    private XXEUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 关闭XXE，避免漏洞攻击.
     * 
     * <p>
     * XML外部实体注入漏洞(XML External Entity Injection，简称 XXE)，该安全问题是由XML组件默认没有禁用外部实体引用导致，非微信支付系统存在漏洞。
     * </p>
     * 
     * see:
     * https://www.owasp.org/index.php/XML_External_Entity_(XXE)_Prevention_Cheat_Sheet#JAXP_DocumentBuilderFactory.2C_SAXParserFactory_and_DOM4J
     * 
     * 
     * <p>
     * Note: The above defenses require Java 7 update 67, Java 8 update 20, or above,<br>
     * because the above countermeasures for DocumentBuilderFactory and SAXParserFactory are broken in earlier Java versions, per:
     * CVE-2014-6517.
     * </p>
     * 
     * 
     * @param documentBuilderFactory
     *            DocumentBuilderFactory
     * @return DocumentBuilderFactory
     */
    public static DocumentBuilderFactory disableXXE(DocumentBuilderFactory documentBuilderFactory){
        String feature = "";
        try{
            // This is the PRIMARY defense. 
            // If DTDs (doctypes) are disallowed, almost all XML entity attacks are prevented
            // Xerces 2 only - http://xerces.apache.org/xerces2-j/features.html#disallow-doctype-decl
            feature = "http://apache.org/xml/features/disallow-doctype-decl";
            documentBuilderFactory.setFeature(feature, true);

            // If you can't completely disable DTDs, then at least do the following:
            // Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-general-entities
            // Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-general-entities
            // JDK7+ - http://xml.org/sax/features/external-general-entities
            feature = "http://xml.org/sax/features/external-general-entities";
            documentBuilderFactory.setFeature(feature, false);

            // Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-parameter-entities
            // Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-parameter-entities
            // JDK7+ - http://xml.org/sax/features/external-parameter-entities
            feature = "http://xml.org/sax/features/external-parameter-entities";
            documentBuilderFactory.setFeature(feature, false);

            // Disable external DTDs as well
            feature = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
            documentBuilderFactory.setFeature(feature, false);

            // and these as well, per Timothy Morgan's 2014 paper: "XML Schema, DTD, and Entity Attacks"
            documentBuilderFactory.setXIncludeAware(false);
            documentBuilderFactory.setExpandEntityReferences(false);
        }catch (ParserConfigurationException e){
            // This should catch a failed setFeature feature
            LOGGER.info(
                            "ParserConfigurationException was thrown.The feature '[{}]' is probably not supported by your XML processor.",
                            feature);
        }catch (Exception e){
            LOGGER.warn("", e);
        }
        return documentBuilderFactory;
    }
}
