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
package com.feilong.office.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import com.feilong.io.FileUtil;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.4
 */
public class ZipUtilTemp{

    /**
     * 
     * @param zipPath
     *            得到的zip文件的名称（含路径）
     * @param filePath
     *            需要压缩的文件所在的目录
     * @param pathName
     *            压缩到pathName文件夹下
     * @throws Exception
     */
    public void zip(String zipPath,String filePath,String pathName) throws Exception{
        File file = new File(zipPath);

        OutputStream outputStream = FileUtil.getFileOutputStream(zipPath);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
        ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(bufferedOutputStream);

        zipArchiveOutputStream.setEncoding("UTF-8");
        if (!"".equals(pathName) && null != pathName){
            pathName = pathName + File.separator;
        }else{
            pathName = file.getName().substring(0, file.getName().length() - 4) + File.separator;
        }
        ZipUtil.zip(zipArchiveOutputStream, filePath, pathName);

        zipArchiveOutputStream.flush();
        zipArchiveOutputStream.close();
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
        outputStream.flush();
        outputStream.close();
    }

    /**
     * 
     * @param path
     *            要压缩的目录或文件
     * @param baseindex
     *            去掉压缩根目录以上的路径串，一面ZIP文件中含有压缩根目录父目录的层次结构
     * @param zipOutputStream
     *            输出到指定的路径
     * @throws IOException
     */
    public static void zip(String path,int baseindex,ZipOutputStream zipOutputStream) throws IOException{
        // 要压缩的目录或文件
        File file = new File(path);
        File[] files;
        if (file.isDirectory()){// 若是目录，则列出所有的子目录和文件
            files = file.listFiles();
        }else{// 若为文件，则files数组只含有一个文件
            files = new File[1];
            files[0] = file;
        }

        for (File f : files){
            if (f.isDirectory()){
                // 去掉压缩根目录以上的路径串，一面ZIP文件中含有压缩根目录父目录的层次结构
                String pathname = f.getPath().substring(baseindex + 1);
                // 空目录也要新建哟个项
                zipOutputStream.putNextEntry(new ZipEntry(pathname + "/"));
                // 递归
                zip(f.getPath(), baseindex, zipOutputStream);
            }else{
                // 去掉压缩根目录以上的路径串，一面ZIP文件中含有压缩根目录父目录的层次结构
                String pathname = f.getPath().substring(baseindex + 1);
                // 新建项为一个文件
                zipOutputStream.putNextEntry(new ZipEntry(pathname));
                // 读文件
                BufferedInputStream in = new BufferedInputStream(FileUtil.getFileInputStream(f));
                int c;
                while ((c = in.read()) != -1){
                    zipOutputStream.write(c);
                }
                in.close();
            }
        }
    }
}
