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
package com.feilong.taglib.display.httpconcat.handler;

import static com.feilong.core.bean.ConvertUtil.toList;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.feilong.taglib.display.httpconcat.command.HttpConcatParam;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.12.5
 */
public class ConcatLinkResolverTest{

    @Test
    public void test(){
        HttpConcatParam httpConcatParam = buildHttpConcatParam();
        assertEquals("http://www.feilong.com/a.js", ConcatLinkResolver.resolver(toList("a.js"), httpConcatParam));
    }

    @Test
    public void test2(){
        HttpConcatParam httpConcatParam = buildHttpConcatParam();

        assertEquals("http://www.feilong.com/??a.js,b.js", ConcatLinkResolver.resolver(toList("a.js", "b.js"), httpConcatParam));
    }

    @Test
    public void test23(){
        HttpConcatParam httpConcatParam = buildHttpConcatParam();

        assertEquals(
                        "http://www.feilong.com/??a.js,b.js,a/b.js,b/a.js",
                        ConcatLinkResolver.resolver(toList("a.js", "b.js", "a/b.js", "b/a.js"), httpConcatParam));
    }

    //---------------------------------------------------------------

    //    @Test
    //    public void testDefaultPartition2(){
    //        HttpConcatParam httpConcatParam = buildHttpConcatParam();
    //
    //        assertEquals(
    //                        "http://www.feilong.com/??1.js,2.js,3.js,4.js,5.js,6.js,7.js,8.js,9.js\nhttp://www.feilong.com/??10.js,11.js,12.js\n",
    //                        ConcatLinkResolver.resolver(
    //                                        toList(
    //                                                        "1.js",
    //                                                        "2.js",
    //                                                        "3.js",
    //                                                        "4.js",
    //                                                        "5.js",
    //                                                        "6.js",
    //                                                        "7.js",
    //                                                        "8.js",
    //                                                        "9.js",
    //                                                        "10.js",
    //                                                        "11.js",
    //                                                        "12.js"),
    //                                        httpConcatParam));
    //    }

    //---------------------------------------------------------------
    //    @Test
    //    public void testPartition(){
    //        HttpConcatParam httpConcatParam = buildHttpConcatParam();
    //
    //        assertEquals(
    //                        "http://www.feilong.com/??a.js,b.js\nhttp://www.feilong.com/??a/b.js,b/a.js\n",
    //                        ConcatLinkResolver.resolverPartitionResult(toList("a.js", "b.js", "a/b.js", "b/a.js"), 2, httpConcatParam));
    //    }
    //
    //    @Test
    //    public void testPartition1(){
    //        HttpConcatParam httpConcatParam = buildHttpConcatParam();
    //
    //        assertEquals(
    //                        "http://www.feilong.com/??a.js,b.js\nhttp://www.feilong.com/a/b.js\n",
    //                        ConcatLinkResolver.resolverPartitionResult(toList("a.js", "b.js", "a/b.js"), 2, httpConcatParam));
    //    }

    //---------------------------------------------------------------

    @Test
    public void testNullElement(){
        HttpConcatParam httpConcatParam = buildHttpConcatParam();

        assertEquals(
                        "http://www.feilong.com/??a.js,b.js,a/b.js,b/a.js",
                        ConcatLinkResolver.resolver(toList("a.js", "b.js", "a/b.js", null, "b/a.js"), httpConcatParam));
    }

    @Test
    public void testEmptyElement(){
        HttpConcatParam httpConcatParam = buildHttpConcatParam();

        assertEquals(
                        "http://www.feilong.com/??a.js,b.js,a/b.js,b/a.js",
                        ConcatLinkResolver.resolver(toList("a.js", "b.js", "a/b.js", "", "b/a.js"), httpConcatParam));
    }

    @Test
    public void testBlankElement(){
        HttpConcatParam httpConcatParam = buildHttpConcatParam();

        assertEquals(
                        "http://www.feilong.com/??a.js,b.js,a/b.js,b/a.js",
                        ConcatLinkResolver.resolver(toList("a.js", "b.js", "a/b.js", " ", "b/a.js"), httpConcatParam));
    }

    //---------------------------------------------------------------

    private HttpConcatParam buildHttpConcatParam(){
        HttpConcatParam httpConcatParam = new HttpConcatParam();
        httpConcatParam.setVersion("");
        httpConcatParam.setType("js");
        httpConcatParam.setRoot("");
        httpConcatParam.setHttpConcatSupport(true);
        httpConcatParam.setDomain("http://www.feilong.com/");
        return httpConcatParam;
    }
}
