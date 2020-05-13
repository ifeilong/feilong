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
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import org.junit.Test;

import com.feilong.core.net.URLUtil;
import com.feilong.io.FileUtil;
import com.feilong.test.AbstractTest;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.5
 */
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

    /**
     * Gets the file sizes.
     * 
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testGetFileSizes() throws IOException{
        String testFile = "E:\\DataCommon\\test\\1.png";
        testFile = "E:\\DataCommon\\JDK API 1.6.0 中文版.CHM";

        File file = new File(testFile);

        long fileSizes = FileUtil.getFileSize(file);
        LOGGER.debug(fileSizes + "");
        LOGGER.debug(FileUtil.formatSize(fileSizes) + "");
        LOGGER.debug(FileUtil.formatSize(file.length()) + "");
        LOGGER.debug("比如文件 {} 字节, 格式化大小 : {}", fileSizes, FileUtil.getFileFormatSize(file));
    }

    @Test
    public void testGetFileSizes1() throws IOException{
        String testFile = "E:\\DataCommon\\test\\1.png";
        testFile = "E:/test1";

        File file = new File(testFile);

        long fileSizes = FileUtil.getFileSize(file);
        LOGGER.debug(fileSizes + "");
        LOGGER.debug(FileUtil.formatSize(fileSizes) + "");
        LOGGER.debug(FileUtil.formatSize(file.length()) + "");
        LOGGER.debug("比如文件 {} 字节, 格式化大小 : {}", fileSizes, FileUtil.getFileFormatSize(file));
    }

    @Test
    public void formatFileSize(){
        LOGGER.debug(FileUtil.formatSize(1134));
        LOGGER.debug(FileUtil.formatSize(800000001));
        LOGGER.debug(FileUtil.formatSize(8000003333001L));
        LOGGER.debug(FileUtil.formatSize(800000333222223001L));
        LOGGER.debug(FileUtil.formatSize(8000222200333223001L));
        LOGGER.debug(FileUtil.formatSize(898152));
        LOGGER.debug(FileUtil.formatSize(8981528));
        //LOGGER.debug(org.apache.commons.io.FileUtils.byteCountToDisplaySize(8981528));
    }

    /**
     * Test get content length.
     * 
     * @throws IOException
     */
    @Test
    public void testGetContentLength() throws IOException{
        URL url = URLUtil.toURL("http://www.jinbaowang.cn/images//20110722/096718c3d1c9b4a1.jpg");
        URLConnection urlConnection = url.openConnection();
        int contentLength = urlConnection.getContentLength();
        LOGGER.debug(FileUtil.formatSize(contentLength));
    }
}
