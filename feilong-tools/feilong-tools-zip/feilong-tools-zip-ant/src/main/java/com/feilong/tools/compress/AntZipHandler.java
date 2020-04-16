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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;

import org.apache.commons.io.IOUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.UncheckedIOException;
import com.feilong.io.FileUtil;

/**
 * 压缩工具类.
 * 
 * <p>
 * org.apache.tools.zip.ZipOutputStream 和ZipEntry在ant.jar里,能解决中文问题, <br>
 * 当然,你也可以使用java.util.zip下的,只是中文文件的话.会产生乱码,并且压缩后文件损坏
 * </p>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 2.1.0
 */
public class AntZipHandler extends AbstractZipHandler{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AntZipHandler.class);

    //---------------------------------------------------------------

    @Override
    protected void handle(String tobeZipFilePath,String outputZipPath) throws IOException{
        //生成ZipOutputStream，会把压缩的内容全都通过这个输出流输出，最后写到压缩文件中去  
        FileOutputStream fileOutputStream = FileUtil.getFileOutputStream(outputZipPath);

        ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(fileOutputStream));

        //---------------------------------------------------------------
        //设置压缩的注释  
        //zipOutputStream.setComment("comment");
        //设置压缩的编码，如果要压缩的路径中有中文，就用下面的编码  
        zipOutputStream.setEncoding(UTF8);

        //启用压缩   
        zipOutputStream.setMethod(ZipOutputStream.DEFLATED);
        //压缩级别为最强压缩，但时间要花得多一点   
        zipOutputStream.setLevel(Deflater.BEST_COMPRESSION);

        //---------------------------------------------------------------
        zip(new File(tobeZipFilePath), zipOutputStream, "");

        //---------------------------------------------------------------
        //处理完成后关闭我们的输出流  
        zipOutputStream.close();
    }

    //---------------------------------------------------------------

    /**
     * 由doZip调用,递归完成目录文件读取.
     *
     * @param willFile
     *            the zip file
     * @param zipOutputStream
     *            the zip output stream
     * @param dirName
     *            这个主要是用来记录压缩文件的一个目录层次结构的
     */
    private void zip(File willFile,ZipOutputStream zipOutputStream,String dirName) throws IOException{
        String fileName = willFile.getName();
        LOGGER.debug("will zip :[{}] to zip file", fileName);

        //---------------------------------------------------------------
        //文件style
        if (willFile.isFile()){
            //放入一个ZipEntry  
            putNextEntry(zipOutputStream, dirName + fileName);

            IOUtils.copyLarge(FileUtil.getFileInputStream(willFile), zipOutputStream);
            //关闭ZipEntry，完成一个文件的压缩  
            zipOutputStream.closeEntry();
            return;
        }

        //---------------------------------------------------------------
        //如果是一个目录，则遍历  
        File[] files = willFile.listFiles();
        // 如果目录为空,则单独创建之.  
        if (files.length == 0){
            //只是放入了空目录的名字  
            putNextEntry(zipOutputStream, dirName + fileName + File.separator);
            zipOutputStream.closeEntry();
        }else{
            // 如果目录不为空,则进入递归，处理下一级文件  
            for (File file : files){
                // 进入递归，处理下一级的文件  
                zip(file, zipOutputStream, dirName + fileName + File.separator);
            }
        }
    }

    /**
     * 开始写入新的 ZIP 文件条目并将流定位到条目数据的开始处..
     * 
     * @param zipOutputStream
     *            此类为以 ZIP 文件格式写入文件实现输出流过滤器.包括对已压缩和未压缩条目的支持.
     * @param name
     *            此类用于表示 ZIP 文件条目
     */
    private static void putNextEntry(ZipOutputStream zipOutputStream,String name){
        // 此类用于表示 ZIP 文件条目
        // 开始写入新的 ZIP 文件条目并将流定位到条目数据的开始处
        try{
            zipOutputStream.putNextEntry(new ZipEntry(name));
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }

}