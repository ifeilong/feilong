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
package com.feilong.component;

import static com.feilong.core.CharsetType.UTF8;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.feilong.core.lang.ClassLoaderUtil;
import com.feilong.io.IOReaderUtil;
import com.feilong.test.AbstractTest;
import com.feilong.velocity.VelocityUtil;

public class SpelTest extends AbstractTest{

    private static final String CONTENT = IOReaderUtil
                    .readToString(ClassLoaderUtil.getResourceAsStream("content.vm", SpelTest.class), UTF8);

    //---------------------------------------------------------------
    @Test
    public void run(){
        String expressionString = "#{T(com.feilong.velocity.VelocityUtil).INSTANCE.parseTemplateWithClasspathResourceLoader('content.vm',null)}";
        assertEquals(CONTENT, SpelUtil.getTemplateValue(expressionString));

    }

    @Test
    public void run1(){
        String parseTemplateWithClasspathResourceLoader = VelocityUtil.INSTANCE.parseTemplateWithClasspathResourceLoader("content.vm");
        assertEquals(CONTENT, SpelUtil.getTemplateValue(parseTemplateWithClasspathResourceLoader));
    }

}
