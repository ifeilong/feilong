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
package com.feilong.spring.expression;

import static com.feilong.lib.springframework.util.ResourceUtils.CLASSPATH_URL_PREFIX;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.feilong.io.IOReaderUtil;
import com.feilong.template.TemplateUtil;
import com.feilong.test.AbstractTest;

public class SpelUtilTest extends AbstractTest{

    private static final String CONTENT = IOReaderUtil.readToString(CLASSPATH_URL_PREFIX + "content.vm");

    //---------------------------------------------------------------
    @Test
    public void run(){
        String expressionString = "#{T(com.feilong.template.TemplateUtil).parseTemplate('content.vm',null)}";
        assertEquals(CONTENT, SpelUtil.getTemplateValue(expressionString));

    }

    @Test
    public void run1(){
        String parseTemplateWithClasspathResourceLoader = TemplateUtil.parseTemplate("content.vm");
        assertEquals(CONTENT, SpelUtil.getTemplateValue(parseTemplateWithClasspathResourceLoader));
    }

}
