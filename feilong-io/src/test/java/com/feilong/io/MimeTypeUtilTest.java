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
package com.feilong.io;

import static com.feilong.io.MimeTypeUtil.getContentTypeByFileName;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.feilong.io.entity.MimeType;
import com.feilong.test.AbstractTest;

@lombok.extern.slf4j.Slf4j
public class MimeTypeUtilTest extends AbstractTest{

    @Test
    public void test(){
        log.debug(getContentTypeByFileName("E:\\2009 阿凡达 詹姆斯·卡梅隆 178分钟加长收藏版.mkv"));

        assertEquals(MimeType.OXT.getMime(), getContentTypeByFileName("E:\\2009 阿凡达 詹姆斯·卡梅隆 178分钟加长收藏版.oxt"));
        assertEquals(MimeType.JPG.getMime(), getContentTypeByFileName("E:\\2009 阿凡达 詹姆斯·卡梅隆 178分钟加长收藏版.jpg"));
        assertEquals(MimeType.JS.getMime(), getContentTypeByFileName("E:\\2009 阿凡达 詹姆斯·卡梅隆 178分钟加长收藏版.js"));
        assertEquals(MimeType.CSS.getMime(), getContentTypeByFileName("E:\\2009 阿凡达 詹姆斯·卡梅隆 178分钟加长收藏版.css"));
        assertEquals(MimeType.PDF.getMime(), getContentTypeByFileName("E:\\2009 阿凡达 詹姆斯·卡梅隆 178分钟加长收藏版.pdf"));
    }
}
