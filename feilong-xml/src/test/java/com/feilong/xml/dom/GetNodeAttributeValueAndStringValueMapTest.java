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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;

import java.util.Map;

import org.junit.Test;

import com.feilong.io.IOReaderUtil;
import com.feilong.test.AbstractTest;
import com.feilong.xml.XmlUtil;

public class GetNodeAttributeValueAndStringValueMapTest extends AbstractTest{

    @Test
    public void test(){
        String xmlString = IOReaderUtil.readToString("classpath:nodeAttributeValueAndStringValueMapTest.xml");
        String xpathExpression = "/wddxPacket/data/struct/var";

        Map<String, String> map = XmlUtil.getNodeAttributeValueAndStringValueMap(xmlString, xpathExpression, "name");

        assertThat(
                        map,
                        allOf(//
                                        hasEntry("TRANSACTIONID", "868BBC35-A5D1-FCBF-0453F134C99B5553"),
                                        hasEntry("ACQUIRERRESPONSECODE", "000"),
                                        hasEntry("SCRUBMESSAGE", "")
                        //     
                        ));

    }

    @Test(expected = NullPointerException.class)
    public void test1(){
        XmlUtil.getNodeAttributeValueAndStringValueMap(null, "/RESULT/INPOLIST/*", "a");

    }

}