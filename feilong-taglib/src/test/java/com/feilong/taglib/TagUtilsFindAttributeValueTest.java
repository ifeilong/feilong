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
package com.feilong.taglib;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.12.8
 */
public class TagUtilsFindAttributeValueTest{

    TestPageContext pageContext = new TestPageContext();

    @Test
    public void test(){
        assertEquals(null, TagUtils.<Object> findAttributeValue(pageContext, "name", "request"));
    }

    @Test
    public void test1(){
        assertEquals(null, TagUtils.<Object> findAttributeValue(pageContext, "name", null));
    }

    @Test
    public void test2(){
        assertEquals(null, TagUtils.<Object> findAttributeValue(pageContext, "name", ""));
    }

    @Test
    public void test3(){
        assertEquals(null, TagUtils.<Object> findAttributeValue(pageContext, "name", " "));
    }

    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testTagUtilsTestNullPageContext(){
        TagUtils.findAttributeValue(null, null, "request");
    }

    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testTagUtilsTestNullFindAttributeValue(){
        TagUtils.findAttributeValue(pageContext, null, "request");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTagUtilsTestEmptyFindAttributeValue(){
        TagUtils.findAttributeValue(pageContext, "", "request");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTagUtilsTestBlankFindAttributeValue(){
        TagUtils.findAttributeValue(pageContext, " ", "request");
    }
}
