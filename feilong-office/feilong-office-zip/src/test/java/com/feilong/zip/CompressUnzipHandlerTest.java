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

import java.io.File;

import org.junit.Test;

import com.feilong.core.lang.ClassLoaderUtil;
import com.feilong.core.lang.SystemUtil;

public class CompressUnzipHandlerTest{

    private final UnzipHandler unzipHandler = new CompressUnzipHandler();

    @Test
    @SuppressWarnings("squid:S2699") //Tests should include assertions //https://stackoverflow.com/questions/10971968/turning-sonar-off-for-certain-code
    public void test(){
        unzipHandler.unzip(
                           getUnZipFilePath(), //需要被解压的zip文件
                           SystemUtil.USER_HOME + "/feilong/zip-unzip/" + nowTimestamp() + "Compress"); // 解压到文件路径
    }

    @Test(expected = NullPointerException.class)
    public void testAntUnzipHandlerTestNull(){
        unzipHandler.unzip(null, SystemUtil.USER_HOME + "/feilong/zip-unzip/" + nowTimestamp() + "antUnzip3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAntUnzipHandlerTestEmpty(){
        unzipHandler.unzip("", SystemUtil.USER_HOME + "/feilong/zip-unzip/" + nowTimestamp() + "antUnzip3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAntUnzipHandlerTestBlank(){
        unzipHandler.unzip(" ", SystemUtil.USER_HOME + "/feilong/zip-unzip/" + nowTimestamp() + "antUnzip3");
    }
    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testAntUnzipHandlerTestNull1(){
        unzipHandler.unzip(getUnZipFilePath(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAntUnzipHandlerTestEmpty1(){
        unzipHandler.unzip(getUnZipFilePath(), "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAntUnzipHandlerTestBlank1(){
        unzipHandler.unzip(getUnZipFilePath(), " ");
    }

    //---------------------------------------------------------------
    private String getUnZipFilePath(){
        try{
            return new File(ClassLoaderUtil.getResource("/for-unzip.zip").toURI()).getAbsolutePath();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
