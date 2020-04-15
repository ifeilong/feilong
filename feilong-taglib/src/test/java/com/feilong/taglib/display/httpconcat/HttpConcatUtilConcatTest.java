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
package com.feilong.taglib.display.httpconcat;

import static com.feilong.taglib.display.httpconcat.HttpConcatUtil.getWriteContent;
import static java.lang.System.lineSeparator;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.taglib.display.httpconcat.command.HttpConcatParam;

/**
 * The Class HttpConcatUtilTest.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.7
 */
public class HttpConcatUtilConcatTest extends BaseHttpConcatTest{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpConcatUtilConcatTest.class);

    @Test
    public void testGetWriteContent12(){
        HttpConcatParam httpConcatParam = new HttpConcatParam();
        //        httpConcatParam.setType("js");
        //        httpConcatParam.setRoot("/js/");
        httpConcatParam.setHttpConcatSupport(true);
        httpConcatParam.setContent("<script type=\"text/javascript\" src=\"//img.mapemall.com/resources/js/plp.js\"></script>");
        httpConcatParam.setDomain("//img.mapemall.com");

        LOGGER.debug(getWriteContent(httpConcatParam));
    }

    @Test
    public void testGetWriteContent122(){
        HttpConcatParam httpConcatParam = new HttpConcatParam();
        httpConcatParam.setHttpConcatSupport(true);
        httpConcatParam.setContent(read("concat-full-concat-single.vm"));
        httpConcatParam.setDomain("//img.mapemall.com");

        assertEquals(
                        "<script type=\"text/javascript\" src=\"//img.mapemall.com/resources/js/app-bridge.js\"></script>"
                                        + lineSeparator(),
                        getWriteContent(httpConcatParam));
    }

    @Test
    public void testGetWriteContentCache(){
        HttpConcatParam httpConcatParam = new HttpConcatParam();
        httpConcatParam.setHttpConcatSupport(true);
        httpConcatParam.setContent(read("concat-full-concat-single.vm"));
        httpConcatParam.setDomain("//img.mapemall.com");

        assertEquals(
                        "<script type=\"text/javascript\" src=\"//img.mapemall.com/resources/js/app-bridge.js\"></script>"
                                        + lineSeparator(),
                        getWriteContent(httpConcatParam));

        //from cache
        assertEquals(
                        "<script type=\"text/javascript\" src=\"//img.mapemall.com/resources/js/app-bridge.js\"></script>"
                                        + lineSeparator(),
                        getWriteContent(httpConcatParam));
    }

    @Test
    public void testGetWriteContent1222(){
        HttpConcatParam httpConcatParam = new HttpConcatParam();
        httpConcatParam.setHttpConcatSupport(true);
        httpConcatParam.setContent(read("concat-full-concat-2.vm"));
        httpConcatParam.setDomain("//img.mapemall.com");

        assertEquals(
                        "<script type=\"text/javascript\" src=\"//img.mapemall.com/??resources/js/app-bridge.js,resources/js/scriptBridgenative.js\"></script>"
                                        + lineSeparator(),
                        getWriteContent(httpConcatParam));

    }

    @Test
    public void testGetWriteContent122222(){
        HttpConcatParam httpConcatParam = new HttpConcatParam();
        httpConcatParam.setHttpConcatSupport(true);
        httpConcatParam.setContent(read("concat-full-concat-2.vm"));
        httpConcatParam.setDomain("//img.mapemall.com/");

        assertEquals(
                        "<script type=\"text/javascript\" src=\"//img.mapemall.com/??resources/js/app-bridge.js,resources/js/scriptBridgenative.js\"></script>"
                                        + lineSeparator(),
                        getWriteContent(httpConcatParam));

    }

    @Test
    public void testGetWriteContent122222333(){
        HttpConcatParam httpConcatParam = new HttpConcatParam();
        httpConcatParam.setHttpConcatSupport(true);
        httpConcatParam.setContent(read("concat-full-concat-3.vm"));
        httpConcatParam.setDomain("//img.mapemall.com//");

        assertEquals(
                        "<script type=\"text/javascript\" src=\"//img.mapemall.com/??resources/js/app-bridge.js,resources/js/scriptBridgenative.js\"></script>"
                                        + lineSeparator(),
                        getWriteContent(httpConcatParam));

    }

    @Test
    public void testGetWriteContent1222223334(){
        HttpConcatParam httpConcatParam = new HttpConcatParam();
        httpConcatParam.setHttpConcatSupport(true);
        httpConcatParam.setContent(read("concat-full-concat-3.vm"));
        httpConcatParam.setDomain("//img.mapemall.com/");

        assertEquals(
                        "<script type=\"text/javascript\" src=\"//img.mapemall.com/??resources/js/app-bridge.js,resources/js/scriptBridgenative.js\"></script>"
                                        + lineSeparator(),
                        getWriteContent(httpConcatParam));

    }
}
