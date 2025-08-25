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

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * XML外部实体注入漏洞(XML External Entity Injection，简称XXE)，该安全问题是由XML组件默认没有禁用外部实体引用导致.
 * 
 * <ul>
 * <li>XXE的攻击原理：外界攻击者通过模拟回调通知，在回调通知中引入不安全的XML，商户服务器上执行系统命令</li>
 * <li>XXE漏洞可能带来的危害：可读取任意文件；执行系统命令；探测内网端口；攻击内网网站</li>
 * </ul>
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.12.1
 */
@lombok.extern.slf4j.Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class XXEUtil{

    /**
     * 关闭XXE，避免漏洞攻击.
     * 
     * <p>
     * XML外部实体注入漏洞(XML External Entity Injection，简称XXE)，该安全问题是由XML组件默认没有禁用外部实体引用导致。
     * </p>
     * 
     * see:
     * https://www.owasp.org/index.php/XML_External_Entity_(XXE)_Prevention_Cheat_Sheet#JAXP_DocumentBuilderFactory.2C_SAXParserFactory_and_DOM4J
     * 
     * <p>
     * Note: The above defenses require Java 7 update 67, Java 8 update 20, or above,<br>
     * because the above countermeasures for DocumentBuilderFactory and SAXParserFactory are broken in earlier Java versions, per:
     * CVE-2014-6517.
     * </p>
     * 
     * @param documentBuilderFactory
     *            DocumentBuilderFactory
     * @return DocumentBuilderFactory
     * @see <a href="https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=23_5">关于XML解析存在的安全问题指引</a>
     * @see <a href=
     *      "https://cheatsheetseries.owasp.org/cheatsheets/XML_External_Entity_Prevention_Cheat_Sheet.html#C.2FC.2B.2B">https://www.owasp.org/index.php/XML_External_Entity_(XXE)_Prevention_Cheat_Sheet#C.2FC.2B.2B</a>
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

            //            documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, ""); // Compliant
            //            documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, ""); // compliant
        }catch (ParserConfigurationException e){
            // This should catch a failed setFeature feature
            log.info(
                            "ParserConfigurationException was thrown.The feature '[{}]' is probably not supported by your XML processor.",
                            feature);
        }catch (Exception e){
            log.warn("", e);
        }
        return documentBuilderFactory;
    }
}
