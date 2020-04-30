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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

import org.junit.Test;

import com.feilong.core.lang.ClassLoaderUtil;
import com.feilong.core.net.URLUtil;
import com.feilong.io.FileUtil;
import com.feilong.json.JsonUtil;
import com.feilong.test.AbstractTest;

/**
 * The Class FileUtilTest.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 */
public class FileUtilTest extends AbstractTest{

    /** The string. */
    private final String fString = "/home/webuser/feilong_int/johnData/${date}/feilongid_pix_${typeName}.csv";

    /**
     * TestFileUtilTest.
     * 
     * @throws IOException
     */
    @Test
    @SuppressWarnings("static-method")
    public void testFileUtilTest() throws IOException{
        URL url = URLUtil.toURL("http://localhost:8080/TestHttpURLConnectionPro/index.jsp");
        url.openConnection();
    }

    /**
     * Test get p.
     */
    @Test
    public void testGetP(){
        File file = new File(fString);
        LOGGER.debug(file.getAbsolutePath());
        LOGGER.debug(file.getParent());
    }

    /**
     * Test get p1.
     */
    @Test
    @SuppressWarnings("static-method")
    public void testGetP1(){
        URL resource = ClassLoaderUtil.getResource("org/apache/commons/collections4/map");
        URI uri = URLUtil.toURI(resource);
        File esapiDirectory = new File(uri);
        LOGGER.debug(esapiDirectory.getAbsolutePath());
    }

    /**
     * List files.
     * 
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    @SuppressWarnings("static-method")
    public void listFiles() throws IOException{
        String localPath = "E:\\DataCommon\\test";
        // 读取localPath目录下的全部properties文件
        File file = new File(localPath);
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++){
            LOGGER.debug("File:" + files[i].getCanonicalPath());
        }
    }

    /**
     * Test to ur ls.
     */
    @Test
    public void testToURLs(){
        LOGGER.debug(JsonUtil.format(FileUtil.toURLs("D:\\Program Files", "D:\\新建文件夹")));
    }

}
