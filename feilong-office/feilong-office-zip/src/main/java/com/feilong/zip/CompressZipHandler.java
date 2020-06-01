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

import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.core.Validator.isNullOrEmpty;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.zip.Deflater;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.io.FileUtil;
import com.feilong.io.IOUtil;
import com.feilong.lib.compress.archivers.zip.ZipArchiveEntry;
import com.feilong.lib.compress.archivers.zip.ZipArchiveOutputStream;

/**
 * 压缩的工具类.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.0.0
 */
public class CompressZipHandler extends AbstractZipHandler{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CompressZipHandler.class);

    /**
     * Handle.
     *
     * @param tobeZipFilePath
     *            the tobe zip file path
     * @param outputZipPath
     *            the output zip path
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    //---------------------------------------------------------------
    @Override
    protected void handle(String tobeZipFilePath,String outputZipPath) throws IOException{
        //生成ZipOutputStream，会把压缩的内容全都通过这个输出流输出，最后写到压缩文件中去  
        FileOutputStream fileOutputStream = FileUtil.getFileOutputStream(outputZipPath);
        ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(new BufferedOutputStream(fileOutputStream));
        //---------------------------------------------------------------
        //设置压缩的编码，如果要压缩的路径中有中文，就用下面的编码  
        zipArchiveOutputStream.setEncoding(UTF8);
        zipArchiveOutputStream.setMethod(ZipArchiveOutputStream.DEFLATED);
        //压缩级别为最强压缩，但时间要花得多一点   
        zipArchiveOutputStream.setLevel(Deflater.BEST_COMPRESSION);

        zip(zipArchiveOutputStream, new File(tobeZipFilePath), "");

        //---------------------------------------------------------------
        //处理完成后关闭我们的输出流  
        zipArchiveOutputStream.close();
    }

    /**
     * 由doZip调用,递归完成目录文件读取.
     *
     * @param zipArchiveOutputStream
     *            the zip archive output stream
     * @param willFile
     *            the zip file
     * @param dirName
     *            这个主要是用来记录压缩文件的一个目录层次结构的
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private static void zip(ZipArchiveOutputStream zipArchiveOutputStream,File willFile,String dirName) throws IOException{
        String fileName = willFile.getName();
        LOGGER.debug("will zip :[{}] to zip file", fileName);
        //文件style
        if (willFile.isFile()){
            putArchiveEntry(zipArchiveOutputStream, dirName + fileName);
            IOUtil.copy(FileUtil.getFileInputStream(willFile.getAbsolutePath()), zipArchiveOutputStream);

            //java.io.UncheckedIOException: java.io.IOException: This archive contains unclosed entries.
            //        Caused by: java.io.IOException: This archive contains unclosed entries.
            //            at org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream.finish(ZipArchiveOutputStream.java:521)
            //IOWriteUtil.write(FileUtil.getFileInputStream(willFile.getAbsolutePath()), zipArchiveOutputStream)

            //关闭zipArchiveOutputStream，完成一个文件的压缩  
            zipArchiveOutputStream.closeArchiveEntry();
            return;
        }

        //---------------------------------------------------------------
        //如果是一个目录，则遍历  
        File[] files = willFile.listFiles();
        if (isNullOrEmpty(files)){
            //只是放入了空目录的名字  
            putArchiveEntry(zipArchiveOutputStream, dirName + willFile.getName() + File.separator);
            zipArchiveOutputStream.closeArchiveEntry();
            return;
        }

        //---------------------------------------------------------------

        // 如果目录不为空,则进入递归，处理下一级文件  
        for (File file : files){
            // 进入递归，处理下一级的文件  
            zip(zipArchiveOutputStream, file, dirName + fileName + File.separator);
        }
    }

    /**
     * Put archive entry.
     *
     * @param zipArchiveOutputStream
     *            the zip archive output stream
     * @param name
     *            the name
     */
    private static void putArchiveEntry(ZipArchiveOutputStream zipArchiveOutputStream,String name){
        try{
            zipArchiveOutputStream.putArchiveEntry(new ZipArchiveEntry(name));
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }

}
