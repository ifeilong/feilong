package com.feilong.office.zip;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.IOUtils;

import com.feilong.io.FileUtil;

public class ZipUtil{

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

}
