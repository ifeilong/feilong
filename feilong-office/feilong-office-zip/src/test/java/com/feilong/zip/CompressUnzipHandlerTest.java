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
package com.feilong.zip;

import static com.feilong.core.date.DateUtil.nowTimestamp;

import org.junit.Test;

import com.feilong.lib.lang3.SystemUtils;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.4
 */
public class CompressUnzipHandlerTest{

    private final UnzipHandler unzipHandler  = new CompressUnzipHandler();

    String                     unZipFilePath = SystemUtils.USER_HOME
                    + "/workspace/feilong/feilong/feilong-office/feilong-office-zip/src/test/resources/for-unzip.zip";

    @Test
    public void test(){
        unzipHandler.unzip(
                        unZipFilePath, //需要被解压的zip文件
                        SystemUtils.USER_HOME + "/feilong/zip-unzip/" + nowTimestamp() + "Compress"); // 解压到文件路径
    }

    @Test(expected = NullPointerException.class)
    public void testAntUnzipHandlerTestNull(){
        unzipHandler.unzip(null, SystemUtils.USER_HOME + "/feilong/zip-unzip/" + nowTimestamp() + "antUnzip3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAntUnzipHandlerTestEmpty(){
        unzipHandler.unzip("", SystemUtils.USER_HOME + "/feilong/zip-unzip/" + nowTimestamp() + "antUnzip3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAntUnzipHandlerTestBlank(){
        unzipHandler.unzip(" ", SystemUtils.USER_HOME + "/feilong/zip-unzip/" + nowTimestamp() + "antUnzip3");
    }
    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testAntUnzipHandlerTestNull1(){
        unzipHandler.unzip(unZipFilePath, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAntUnzipHandlerTestEmpty1(){
        unzipHandler.unzip(unZipFilePath, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAntUnzipHandlerTestBlank1(){
        unzipHandler.unzip(unZipFilePath, " ");
    }
}
