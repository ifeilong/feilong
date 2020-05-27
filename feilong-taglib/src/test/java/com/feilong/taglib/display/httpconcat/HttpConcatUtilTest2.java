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

import org.junit.Test;

import com.feilong.taglib.display.httpconcat.command.HttpConcatParam;

/**
 * The Class HttpConcatUtilTest.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.0.7
 */
public class HttpConcatUtilTest2 extends BaseHttpConcatTest{

    @Test
    public void testGetWriteContent(){
        HttpConcatParam httpConcatParam = super.getHttpConcatParam();
        LOGGER.debug(getWriteContent(httpConcatParam));
    }
}
