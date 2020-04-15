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

import static com.feilong.core.CharsetType.UTF8;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;

/**
 * 解压缩的工具类.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.4
 */
public class CompressUnzipHandler extends AbstractUnzipHandler{

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.compress.AbstractUnzipManager#handler(java.lang.String, java.lang.String)
     */
    @Override
    protected void handle(String unZipFilePath,String outputDirectory) throws IOException{
        //默认 utf-8
        try (ZipFile zipFile = new ZipFile(unZipFilePath, UTF8)){
            Enumeration<ZipArchiveEntry> enumeration = zipFile.getEntries();

            while (enumeration.hasMoreElements()){
                ZipArchiveEntry zipArchiveEntry = enumeration.nextElement();
                if (zipArchiveEntry.isDirectory()){
                    continue;
                }

                //---------------------------------------------------------------
                InputStream inputStream = zipFile.getInputStream(zipArchiveEntry);
                write(zipArchiveEntry.getName(), inputStream, outputDirectory);
            }
        }
    }
}
