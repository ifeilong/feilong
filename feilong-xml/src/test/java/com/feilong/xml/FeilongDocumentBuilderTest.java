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

import static com.feilong.xml.FeilongDocumentBuilder.buildDocument;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.Test;
import org.w3c.dom.Document;

import com.feilong.lib.lang3.SystemUtils;

public class FeilongDocumentBuilderTest{

    @Test(expected = NullPointerException.class)
    public void testFeilongDocumentBuilderTestNull(){
        FeilongDocumentBuilder.buildDocument(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFeilongDocumentBuilderTestEmpty(){
        FeilongDocumentBuilder.buildDocument("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFeilongDocumentBuilderTestBlank(){
        FeilongDocumentBuilder.buildDocument(" ");
    }

    //---------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void testFile1(){
        String xml = SystemUtils.USER_HOME + "/workspace/feilong/feilon11/feilong-xml/src/test/resources/weather-response.xml";
        FeilongDocumentBuilder.buildDocument(new File(xml));
    }

    //---------------------------------------------------------------

    @Test
    public void testFile(){
        Document buildDocument = buildDocument(
                        SystemUtils.USER_HOME + "/workspace/feilong/feilong/feilong-xml/src/test/resources/weather-response.xml");
        assertNotNull(buildDocument.getChildNodes());
    }

    @Test
    public void testString(){
        Document buildDocument = buildDocument("<string>江苏</string>");
        assertNotNull(buildDocument.getChildNodes());
    }
}
