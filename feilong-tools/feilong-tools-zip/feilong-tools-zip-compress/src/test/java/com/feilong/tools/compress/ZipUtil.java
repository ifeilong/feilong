package com.feilong.tools.compress;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.IOUtils;

import com.feilong.io.FileUtil;

public class ZipUtil{

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

    /**
     * 
     * @param zipArchiveOutputStream
     *            流
     * @param filePath
     *            需要打包的目录
     * @param pathName
     *            打包到pathName的目录下
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void zip(ZipArchiveOutputStream zipArchiveOutputStream,String filePath,String pathName)
                    throws FileNotFoundException,IOException{
        File file2zip = new File(filePath);
        if (file2zip.isFile()){
            zipArchiveOutputStream.putArchiveEntry(new ZipArchiveEntry(pathName + file2zip.getName()));
            IOUtils.copy(FileUtil.getFileInputStream(file2zip.getAbsolutePath()), zipArchiveOutputStream);
            zipArchiveOutputStream.closeArchiveEntry();
        }else{
            File[] files = file2zip.listFiles();
            if (files == null || files.length < 1){
                return;
            }
            for (int i = 0; i < files.length; i++){
                if (files[i].isDirectory()){
                    zip(zipArchiveOutputStream, files[i].getAbsolutePath(), pathName + files[i].getName() + File.separator);
                }else{
                    zipArchiveOutputStream.putArchiveEntry(new ZipArchiveEntry(pathName + files[i].getName()));
                    IOUtils.copy(FileUtil.getFileInputStream(files[i].getAbsolutePath()), zipArchiveOutputStream);
                    zipArchiveOutputStream.closeArchiveEntry();
                }
            }
        }
    }

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
        zip(zipArchiveOutputStream, filePath, pathName);

        zipArchiveOutputStream.flush();
        zipArchiveOutputStream.close();
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
        outputStream.flush();
        outputStream.close();
    }
}
