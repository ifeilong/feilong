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

import static com.feilong.core.date.DateUtil.formatElapsedTime;
import static com.feilong.core.lang.StringUtil.formatPattern;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.feilong.core.Validate;
import com.feilong.io.IOWriteUtil;

/**
 * 抽象的解压缩.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.11.4
 */
@lombok.extern.slf4j.Slf4j
public abstract class AbstractUnzipHandler implements UnzipHandler{

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.zip.UnzipHandler#unzip(java.lang.String, java.lang.String)
     */
    @Override
    public void unzip(String unZipFilePath,String outputDirectory){
        Validate.notBlank(unZipFilePath, "unZipFilePath can't be blank!");
        Validate.notBlank(outputDirectory, "outputDirectory can't be blank!");

        //---------------------------------------------------------------
        long beginTimeMillis = System.currentTimeMillis();
        if (log.isInfoEnabled()){
            log.info("begin unzip:[{}] to outputDirectory:[{}]", unZipFilePath, outputDirectory);
        }

        //---------------------------------------------------------------

        try{
            handle(unZipFilePath, outputDirectory);
        }catch (IOException e){
            String message = formatPattern("unZipFilePath:[{}],outputDirectory:[{}]", unZipFilePath, outputDirectory);
            throw new UncheckedIOException(message, e);
        }

        //---------------------------------------------------------------
        if (log.isInfoEnabled()){
            log.info(
                     "use time:[{}],end unzip:[{}],outputDirectory:[{}]",
                     formatElapsedTime(beginTimeMillis),
                     unZipFilePath,
                     outputDirectory);
        }
    }

    //---------------------------------------------------------------

    /**
     * 处理.
     *
     * @param unZipFilePath
     *            the un zip file path
     * @param outputDirectory
     *            the output directory
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @since 1.11.4
     */
    protected abstract void handle(String unZipFilePath,String outputDirectory) throws IOException;

    //---------------------------------------------------------------

    /**
     * Handler.
     *
     * @param zipEntryName
     *            the zip entry name
     * @param inputStream
     *            the input stream
     * @param outputDirectory
     *            the output directory
     * @throws IllegalArgumentException
     *             如果 <code>zipEntryName</code> 会逃出 <code>outputDirectory</code>(避免 zip slip)
     * @see com.feilong.io.IOWriteUtil#write(InputStream, String, String)
     */
    protected static void write(String zipEntryName,InputStream inputStream,String outputDirectory){
        //① 可信的基准目录：基于 outputDirectory，转成绝对路径并化简
        Path basePath = Paths.get(outputDirectory).toAbsolutePath().normalize();

        //② 按同样的方式算出"我准备写到哪个路径"（等价于 IOWriteUtil 内部拼出来的那个路径）
        Path targetPath = basePath.resolve(zipEntryName).normalize();

        //③ 关键校验：最终路径必须仍在基准目录内，否则抛 IllegalArgumentException
        Validate.isTrue(targetPath.startsWith(basePath), "zipEntryName:[%s] is out of outputDirectory:[%s]", zipEntryName, outputDirectory);

        //④ 校验通过才真正写
        IOWriteUtil.write(inputStream, outputDirectory, zipEntryName);

        //---------------------------------------------------------------

        if (log.isDebugEnabled()){
            log.debug("unzip [{}] to [{}]", zipEntryName, outputDirectory + File.separator + zipEntryName);
        }
    }

}
