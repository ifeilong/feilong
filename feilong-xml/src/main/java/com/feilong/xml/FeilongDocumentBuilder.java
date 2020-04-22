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

import static com.feilong.core.CharsetType.UTF8;

import java.io.File;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

/**
 * 用来构造 {@link Document}.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 3.0.0
 */
class FeilongDocumentBuilder{

    /** The Constant log. */
    private static final Logger          LOGGER                  = LoggerFactory.getLogger(FeilongDocumentBuilder.class);

    //---------------------------------------------------------------

    /** Static instance. */
    // the static instance works for all types
    private static final DocumentBuilder DEFAULT_DOCUMENTBUILDER = buildDefaultDocumentBuilder();

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private FeilongDocumentBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Construct document.
     *
     * @param xml
     *            the xml
     * @return the document
     */
    static Document buildDocument(Object xml){
        Validate.notNull(xml, "xml can't be null!");

        if (xml instanceof String){
            Validate.notBlank((String) xml, "xmlString can't be blank!");
        }

        if (xml instanceof File && !((File) xml).exists()){
            throw new IllegalArgumentException(((File) xml).getPath() + "file not exits");
        }

        //---------------------------------------------------------------
        try{
            if (xml instanceof String){
                String xmlString = StringUtils.trim((String) xml);

                //普通的xml 字符串
                if (isXmlString(xmlString)){
                    InputStream inputStream = IOUtils.toInputStream(xmlString, UTF8);
                    return DEFAULT_DOCUMENTBUILDER.parse(inputStream);
                }
                //---------------------------------------------------------------
                LOGGER.debug("will parse use uri:[{}] for document", xmlString);
                //uri style 注意这里是  xmlFileUriString
                return DEFAULT_DOCUMENTBUILDER.parse(xmlString);
            }

            //---------------------------------------------------------------
            // file style
            if (xml instanceof File){
                return DEFAULT_DOCUMENTBUILDER.parse((File) xml);
            }
            //---------------------------------------------------------------
            //inputstream style
            if (xml instanceof InputStream){
                return DEFAULT_DOCUMENTBUILDER.parse((InputStream) xml);
            }
            throw new UnsupportedOperationException("xml:[" + xml + "] not support!");
        }catch (Exception e){
            throw new UncheckedXmlParseException("input xml:" + xml, e);
        }
    }

    private static boolean isXmlString(String xmlString){
        //<?xml version=\"1.0\" encoding=\"UTF-8\"?>
        //<xml
        return xmlString.startsWith("<");
    }

    //---------------------------------------------------------------

    private static DocumentBuilder buildDefaultDocumentBuilder(){
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        //设置将空白字符忽略  
        documentBuilderFactory.setIgnoringElementContentWhitespace(true);

        //since 1.12.1
        XXEUtil.disableXXE(documentBuilderFactory);
        //---------------------------------------------------------------
        try{
            return documentBuilderFactory.newDocumentBuilder();
        }catch (ParserConfigurationException e){
            throw new UncheckedXmlParseException(e);
        }
    }
}
