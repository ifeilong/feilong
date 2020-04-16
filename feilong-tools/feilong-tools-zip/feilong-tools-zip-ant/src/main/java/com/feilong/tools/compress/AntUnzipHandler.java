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
package com.feilong.tools.compress;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

/**
 * 解压缩工具类.
 * 
 * <p>
 * org.apache.tools.zip.ZipOutputStream 和ZipEntry在ant.jar里,能解决中文问题, <br>
 * 当然,你也可以使用java.util.zip下的,只是中文文件的话.会产生乱码,并且压缩后文件损坏
 * </p>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.4
 */
public class AntUnzipHandler extends AbstractUnzipHandler{

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.compress.AbstractUnzipManager#handler(java.lang.String, java.lang.String)
     */
    @Override
    protected void handle(String unZipFilePath,String outputDirectory) throws IOException{
        try (ZipFile zipFile = new ZipFile(unZipFilePath)){
            Enumeration<ZipEntry> enumeration = zipFile.getEntries();

            while (enumeration.hasMoreElements()){
                ZipEntry zipEntry = enumeration.nextElement();
                if (zipEntry.isDirectory()){
                    continue;
                }

                //---------------------------------------------------------------
                InputStream inputStream = zipFile.getInputStream(zipEntry);
                write(zipEntry.getName(), inputStream, outputDirectory);
            }
        }
    }

}