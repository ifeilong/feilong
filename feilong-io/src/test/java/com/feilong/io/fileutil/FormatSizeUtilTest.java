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
package com.feilong.io.fileutil;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import com.feilong.io.FileUtil;
import com.feilong.test.AbstractTest;

@lombok.extern.slf4j.Slf4j
public class FormatSizeUtilTest extends AbstractTest{

    @Test
    public void formatFileSize1(){
        assertEquals("0Bytes", FileUtil.formatSize(0));
        assertEquals("0Bytes", FileUtil.formatSize(-0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void formatFileSize2(){
        FileUtil.formatSize(-255);
    }

    //---------------------------------------------------------------
    @Test
    public void testGetFileSizes1(){
        String testFile = "/Users/feilong/.m2/settings.xml";
        File file = new File(testFile);

        long fileSizes = FileUtil.getFileSize(file);
        log.debug(fileSizes + "");
        log.debug(FileUtil.formatSize(fileSizes) + "");
        log.debug(FileUtil.formatSize(file.length()) + "");
        log.debug("比如文件 {} 字节, 格式化大小 : {}", fileSizes, FileUtil.getFileFormatSize(file));
    }

    //---------------------------------------------------------------

    @Test
    public void formatFileSize(){
        log.debug(FileUtil.formatSize(1134));
        log.debug(FileUtil.formatSize(800000001));
        log.debug(FileUtil.formatSize(8000003333001L));
        log.debug(FileUtil.formatSize(800000333222223001L));
        log.debug(FileUtil.formatSize(8000222200333223001L));
        log.debug(FileUtil.formatSize(898152));
        log.debug(FileUtil.formatSize(8981528));
        //log.debug(org.apache.commons.io.FileUtils.byteCountToDisplaySize(8981528));
    }
}
