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
package com.feilong.xml.dom;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DomUtilTest{

    /** The Constant LOGGER. */
    private static final Logger LOGGER    = LoggerFactory.getLogger(DomUtilTest.class);

    /** The xml file. */
    private static String       NOVEL_XML = "/Users/feilong/workspace/feilong/feilong/feilong-project/feilong-novel/src/test/resources/feilong-novel-for-xml-test.xml";

    /** The dom util. */
    private final DomParser     domParser = new DomParser(NOVEL_XML);

    //---------------------------------------------------------------

    @Test
    public void testGetElementById(){
        LOGGER.debug("" + domParser.getElementById("catalog_37zw_混在东汉末"));
    }

    @Test
    public void testGetNodeListByXPath(){
        String expression = "/feilong-novel/replaces/replace";
        NodeList nodeListByXPath = domParser.getNodeListByXPath(expression);
        for (int i = 0, j = nodeListByXPath.getLength(); i < j; ++i){
            Node node = nodeListByXPath.item(i);
            LOGGER.debug("" + DomParser.getAttributeValue(node, "oldText") + "===>" + DomParser.getAttributeValue(node, "newText"));
        }
    }

    @Test
    public void testGetNodeByXPath(){
        String expression = "/feilong-novel/novels/novel/catalogs/catalog[@ID='catalog_37zw_混在东汉末']";
        //expression = "//feilong-novel";
        Element element = (Element) domParser.getNodeByXPath(expression);
        LOGGER.debug("" + element.getTagName());
        LOGGER.debug("" + element.getTextContent());
    }
}
