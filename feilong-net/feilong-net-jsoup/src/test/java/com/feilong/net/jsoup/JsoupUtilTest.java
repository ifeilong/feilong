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
package com.feilong.net.jsoup;

import static com.feilong.core.CharsetType.UTF8;
import static java.lang.System.lineSeparator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.io.FileUtil;

public class JsoupUtilTest{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(JsoupUtilTest.class);

    @Test
    public void testJsoupUtilTest(){
        String xml = " <config>\n" + "        <Request name=\"ValidateEmailRequest\">\n"
                        + "            <requestqueue>emailrequest</requestqueue>\n"
                        + "            <responsequeue>emailresponse</responsequeue>\n" + "        </Request>\n"
                        + "        <Request name=\"CleanEmail\">\n" + "            <requestqueue>Cleanrequest</requestqueue>\n"
                        + "            <responsequeue>Cleanresponse</responsequeue>\n" + "        </Request>\n" + "    </config>";
        Document doc = Jsoup.parse(xml, "", Parser.xmlParser());
        for (Element e : doc.select("requestqueue")){
            System.out.println(e);
        }
    }

    @Test
    public void testJsoupUtilTest1() throws IOException{
        String fileName = "E:/Workspaces/feilong/feilong-project/feilong-novel/src/main/resources/feilong-novel.xml";
        FileInputStream fileInputStream = FileUtil.getFileInputStream(fileName);
        Document document = Jsoup.parse(fileInputStream, UTF8, "", Parser.xmlParser());

        Element elementById = document.getElementById("catalog_suimeng_重生九二之商业大亨");
        LOGGER.debug("" + elementById);
        Elements elementsByTag = elementById.getElementsByTag("webSite");
        LOGGER.debug("" + elementsByTag);
        LOGGER.debug("" + elementsByTag.attr("ref"));

    }

    @Test
    public void getDocument() throws JsoupUtilException{
        String urlString = "http://www.suimeng.com/files/article/html/76/76841/";
        Document document = JsoupUtil.getDocument(urlString);
    }

    @Test
    public void getDocument1() throws JsoupUtilException{
        String urlString = "http://langeasy.com.cn/m/player?f=20140512125056100000041";
        Document document = JsoupUtil.getDocument(urlString);

        Element select = document.getElementById("lrcContent");

        LOGGER.debug("" + _getContent(select));
    }

    /**
     * _get content.
     * 
     * @param element_content
     *            the element_content
     * @return the string builder
     */
    private StringBuilder _getContent(Element element_content){
        StringBuilder sb = new StringBuilder();

        List<Node> childNodes = element_content.childNodes();
        for (Node node : childNodes){
            String nodeOuterHtml = node.outerHtml();
            LOGGER.debug("node:[{}],nodeOuterHtml:[{}]", node.getClass().getName(), nodeOuterHtml);

            if (node instanceof TextNode || node instanceof Element){
                // node.
                if (node instanceof TextNode){
                    sb.append(nodeOuterHtml);
                    //sbContent.append(lineSeparator());
                }
                // 递归
                else if (node instanceof Element){
                    Element element = (Element) node;
                    String tagName = element.tagName();

                    LOGGER.debug("the param tagName:[{}]", tagName);

                    //jdk 1.7
                    switch (tagName) {
                        case "br":
                            sb.append(lineSeparator());
                            break;

                        case "a":
                            // only write log
                            LOGGER.warn("skip [{}]", nodeOuterHtml);
                            break;

                        default:
                            sb.append(_getContent(element));
                            break;
                    }
                }
            }
        }
        return sb;
    }

}