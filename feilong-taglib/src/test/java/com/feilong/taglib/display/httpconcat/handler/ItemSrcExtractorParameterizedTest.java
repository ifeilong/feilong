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
import org.junit.runners.Parameterized.Parameters;

import com.feilong.test.Abstract2ParamsAndResultParameterizedTest;

public class ItemSrcExtractorParameterizedTest extends Abstract2ParamsAndResultParameterizedTest<String, String, String>{

    @Parameters(name = "index:{index}: ItemSrcExtractor.extract({0},{1})={2}")
    public static Iterable<Object[]> data(){
        Object[][] objects = new Object[][] { //

                                              //replace domain
                                              {
                                                "<link rel=\"stylesheet\" href=\"http://css.feilong.com:8888/res/feilong/css/feilong-all.css\" type=\"text/css\"></link>",
                                                "http://css.feilong.com:8888/",
                                                "res/feilong/css/feilong-all.css" },
                                              {
                                                "<link rel=\"stylesheet\" href=\"http://css.feilong.com:8888//res/feilong/css/feilong-all.css\" type=\"text/css\"></link>",
                                                "http://css.feilong.com:8888//",
                                                "res/feilong/css/feilong-all.css" },
                                              {
                                                "<link rel=\"stylesheet\" href=\"http://css.feilong.com:8888/res/feilong/css/feilong-all.css\" type=\"text/css\"></link>",
                                                "http://css.feilong.com:8888",
                                                "res/feilong/css/feilong-all.css" },

                                              //---------------------------------------------------------------
                                              //css
                                              {
                                                "<link rel=\"stylesheet\" href=\"http://css.feilong.com:8888/res/feilong/css/feilong-all.css?version=12345666\" type=\"text/css\"></link>",
                                                "http://css.feilong.com:8888/",
                                                "res/feilong/css/feilong-all.css" },

                                              {
                                                "<link rel=\"stylesheet\" href=\"res/feilong/css/feilong-all.css?version=12345666\" type=\"text/css\"></link>",
                                                "http://css.feilong.com:8888/",
                                                "res/feilong/css/feilong-all.css" },

                                              //space
                                              {
                                                " <link rel=\"stylesheet\" href=\"http://css.feilong.com:8888/res/feilong/css/feilong-all.css?version=12345666\" type=\"text/css\"></link>",
                                                "http://css.feilong.com:8888/",
                                                "res/feilong/css/feilong-all.css" },

                                              {
                                                "<link rel=\"stylesheet\" href=\"res/feilong/css/feilong-all.css?version=12345666\" type=\"text/css\"></link> ",
                                                "http://css.feilong.com:8888/",
                                                "res/feilong/css/feilong-all.css" },

                                              //---------------------------------------------------------------

                                              //js
                                              {
                                                "<script type=\"text/javascript\" src=\"scripts/pdp/sub_salesProperties.js?2015\"></script>",
                                                "http://css.feilong.com:8888/",
                                                "scripts/pdp/sub_salesProperties.js" },

                                              {
                                                "<script type=\"text/javascript\" src=\"http://css.feilong.com:8888/scripts/pdp/sub_salesProperties.js?2015\"></script>",
                                                "http://css.feilong.com:8888/",
                                                "scripts/pdp/sub_salesProperties.js" },

                                              //space
                                              {
                                                " <script type=\"text/javascript\" src=\"scripts/pdp/sub_salesProperties.js?2015\"></script>",
                                                "http://css.feilong.com:8888/",
                                                "scripts/pdp/sub_salesProperties.js" },

                                              {
                                                "<script type=\"text/javascript\" src=\"http://css.feilong.com:8888/scripts/pdp/sub_salesProperties.js?2015\"></script> ",
                                                "http://css.feilong.com:8888/",
                                                "scripts/pdp/sub_salesProperties.js" },

                                              //---------------------------------------------------------------

                                              //no link, no script
                                              {
                                                "http://css.feilong.com:8888/scripts/pdp/sub_salesProperties.js",
                                                "http://css.feilong.com:8888/",
                                                "http://css.feilong.com:8888/scripts/pdp/sub_salesProperties.js" },
                                              {
                                                "scripts/pdp/sub_salesProperties.js",
                                                "http://css.feilong.com:8888/",
                                                "scripts/pdp/sub_salesProperties.js" },

                                              { "/scripts/pdp/sub_salesProperties.js", "", "/scripts/pdp/sub_salesProperties.js" },
                                              { "//scripts/pdp/sub_salesProperties.js", "", "//scripts/pdp/sub_salesProperties.js" },
                                              { " /scripts/pdp/sub_salesProperties.js", "", "/scripts/pdp/sub_salesProperties.js" },
                                              { "//scripts/pdp/sub_salesProperties.js ", "", "//scripts/pdp/sub_salesProperties.js" },
                //
        };

        // <link rel="stylesheet" href="http://css.feilong.com:8888/res/feilong/css/feilong-all.css" type="text/css"></link>

        //<script type="text/javascript" src="scripts/pdp/sub_salesProperties.js?2015"></script>
        //<script type="text/javascript" src="scripts/pdp/pdp.js?2015"></script>
        //<script type="text/javascript" src="scripts/pdp/sub_sns.js?2015"></script>

        return toList(objects);
    }

    @Test
    public void testBuild(){
        assertEquals(expectedValue, ItemSrcExtractor.extract(input1, input2));
    }

}