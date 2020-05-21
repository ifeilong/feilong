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
package com.feilong.net.filetransfer;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.date.DateUtil.formatDuration;
import static com.feilong.core.date.DateUtil.now;
import static com.feilong.io.entity.FileType.DIRECTORY;
import static com.feilong.core.lang.StringUtil.EMPTY;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.util.MapUtil;
import com.feilong.io.FileUtil;
import com.feilong.io.FilenameUtil;
import com.feilong.io.IOUtil;
import com.feilong.io.entity.FileInfoEntity;
import com.feilong.json.JsonUtil;
import com.feilong.lib.lang3.Validate;
import com.feilong.tools.slf4j.Slf4jUtil;

/**
 * 通用的文件传输.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.5
 */
public abstract class AbstractFileTransfer implements FileTransfer{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFileTransfer.class);

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.net.filetransfer.FileTransfer#download(java.lang.String, java.lang.String[])
     */
    @Override
    public void download(String localAbsoluteDirectoryPath,String...remotePaths){
        Validate.notEmpty(remotePaths, "remotePaths can't be null/empty!");
        for (String remotePath : remotePaths){
            Validate.notBlank(remotePath, "remotePath can't be blank!");
        }
        Validate.notBlank(localAbsoluteDirectoryPath, "localAbsoluteDirectoryPath can't be blank!");

        //---------------------------------------------------------------

        boolean isConnectSuccess = connect();
        if (isConnectSuccess){
            for (String remotePath : remotePaths){
                isConnectSuccess = downloadDontClose(remotePath, localAbsoluteDirectoryPath);
                if (!isConnectSuccess){
                    break;
                }
            }
            // 关闭连接
            disconnect();
        }
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.net.filetransfer.FileTransfer#upload(java.lang.String, java.lang.String[])
     */
    @Override
    public boolean upload(String remoteDirectory,String...batchLocalFileFullPaths){
        Validate.notBlank(remoteDirectory, "remoteDirectory can't be blank!");
        Validate.notEmpty(batchLocalFileFullPaths, "batchLocalFileFullPaths can't be null/empty!");
        for (String localFileFullPath : batchLocalFileFullPaths){
            Validate.notBlank(localFileFullPath, "localFileFullPath can't be blank!");

            // 文件必需存在
            Validate.isTrue(FileUtil.isExistFile(localFileFullPath), "localFileFullPath:" + localFileFullPath + "  not exist");
        }

        //---------------------------------------------------------------
        LOGGER.debug("will put:[{}] to:[{}]", batchLocalFileFullPaths, remoteDirectory);
        boolean isConnectSuccess = connect();

        //XXX 远程文件夹不存在, 那么自动级联创建
        checkOrMkdirs(remoteDirectory);

        if (isConnectSuccess){
            for (String singleLocalFileFullPath : batchLocalFileFullPaths){
                isConnectSuccess = uploadDontClose(singleLocalFileFullPath, remoteDirectory);
                if (!isConnectSuccess){
                    break;
                }
            }
            // 关闭连接
            disconnect();
            return isConnectSuccess;
        }
        return false;
    }

    //---------------------------------------------------------------

    /**
     * 如果远程目录不存在,那么级联创建.
     *
     * @param remoteDirectory
     *            the remote directory
     * @since 1.7.1
     */
    protected void checkOrMkdirs(String remoteDirectory){
        LOGGER.info("begin checkOrMkdirs remoteDirectory:[{}]", remoteDirectory);

        try{
            tryCd(remoteDirectory);
        }catch (Exception e){
            LOGGER.warn("can't cd:[{}],cause by:[{}],will try [mkdirs]~~", remoteDirectory, e.getMessage());
            List<String> list = FilenameUtil.getParentPathList(remoteDirectory);

            Collections.reverse(list);//自顶而下
            list.add(remoteDirectory);

            for (String folder : list){
                try{
                    tryCd(folder);
                }catch (Exception e1){
                    LOGGER.info("can't cd:[{}],cause by:[{}],will try mkdir", folder, e1.getMessage());
                    mkdir(folder);
                    cd(folder);
                }
            }
        }
    }

    //---------------------------------------------------------------

    /**
     * Try cd.
     *
     * @param remoteDirectory
     *            the remote directory
     * @throws Exception
     *             the exception
     * @since 1.7.1
     */
    protected abstract void tryCd(String remoteDirectory) throws Exception;

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.net.filetransfer.FileTransfer#getFileEntityMap(java.lang.String, java.lang.String[])
     */
    @Override
    public Map<String, FileInfoEntity> getFileEntityMap(String remotePath,String...fileNames){
        Validate.notBlank(remotePath, "remotePath can't be blank!");

        //---------------------------------------------------------------
        boolean isConnectSuccess = connect();
        if (!isConnectSuccess){
            return null;
        }

        //---------------------------------------------------------------
        Map<String, FileInfoEntity> lsFileMap = getLsFileMap(remotePath);
        Map<String, FileInfoEntity> fileEntityMap = MapUtil.getSubMap(lsFileMap, fileNames);

        //---------------------------------------------------------------
        disconnect();
        return fileEntityMap;
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.net.filetransfer.FileTransfer#delete(java.lang.String[])
     */
    @Override
    public boolean delete(String...remoteAbsolutePaths){
        Validate.notEmpty(remoteAbsolutePaths, "remoteAbsolutePaths can't be null/empty!");
        for (String remoteAbsolutePath : remoteAbsolutePaths){
            // 一旦 有一个文件 null或者empty 抛错
            Validate.notBlank(remoteAbsolutePath, "remoteAbsolutePath can't be blank!");
            // 不支持 删除全部 危险
            if ("/".equals(remoteAbsolutePath)){
                throw new UnsupportedOperationException("un supported delete '/'remotePath ");
            }
        }

        //---------------------------------------------------------------

        boolean isConnectSuccess = connect();
        if (isConnectSuccess){
            try{
                return deleteDontClose(remoteAbsolutePaths);
            }catch (Exception e){
                throw new FileTransferException("deleteDontClose exception", e);
            }finally{
                disconnect();
            }
        }
        return false;
    }

    //---------------------------------------------------------------
    /**
     * 打开链接.
     *
     * @return true, if connect
     */
    protected abstract boolean connect();

    /**
     * 关闭链接.
     */
    protected abstract void disconnect();

    /**
     * 删除文件.
     *
     * @param remoteAbsolutePath
     *            远程绝对路径
     * @return 成功返回true 否则 返回false
     */
    protected abstract boolean rm(String remoteAbsolutePath);

    /**
     * 删除 文件夹.
     *
     * @param remoteAbsolutePath
     *            远程绝对路径
     * @return 成功返回true 否则 返回false
     */
    protected abstract boolean rmdir(String remoteAbsolutePath);

    /**
     * 判断 一个远程地址 是否是文件夹.
     *
     * @param remoteAbsolutePath
     *            远程绝对路径
     * @return 是文件夹返回true,否则false
     */
    protected abstract boolean isDirectory(String remoteAbsolutePath);

    /**
     * 获得 ls map.
     * 
     * <p>
     * key 为文件名称(不是全路径),<br>
     * value FileEntity.
     * </p>
     *
     * @param remotePath
     *            远程路径
     * @return the ls file map
     */
    protected abstract Map<String, FileInfoEntity> getLsFileMap(String remotePath);

    //---------------------------------------------------------------

    /**
     * 切换远程操作目录.
     *
     * @param remoteDirectory
     *            the remote directory
     * @return true, if cd
     */
    protected boolean cd(String remoteDirectory){
        try{
            tryCd(remoteDirectory);
            return true;
        }catch (Exception e){
            String message = Slf4jUtil.format("can't cd:[{}]", remoteDirectory);
            throw new FileTransferException(message, e);
        }
    }

    //---------------------------------------------------------------

    /**
     * 创建远程文件夹.
     *
     * @param remoteDirectory
     *            the remote directory
     * @return true, if mkdir
     */
    protected abstract boolean mkdir(String remoteDirectory);

    /**
     * 将指定<code>fileInputStream</code>上传到指定的文件<code>toFileName</code>.
     * 
     * <p>
     * 该方法不会关闭<code>fileInputStream</code>,请自行关闭
     * </p>
     *
     * @param fileInputStream
     *            the file input stream
     * @param toFileName
     *            the to file name
     * @return true, if put
     */
    protected abstract boolean upload(FileInputStream fileInputStream,String toFileName);

    /**
     * 下载远程单个文件.
     *
     * @param remoteSingleFile
     *            远程单个文件
     * @param filePath
     *            保存到本地的路径
     * @return true, if _down remote single file
     */
    protected abstract boolean downRemoteSingleFile(String remoteSingleFile,String filePath);

    //---------------------------------------------------------------

    /**
     * 不关闭链接删除 一组文件.
     *
     * @param remoteAbsolutePaths
     *            the remote absolute paths
     * @return true, if _delete dont close
     */
    private boolean deleteDontClose(String...remoteAbsolutePaths){
        boolean isSuccess = false;
        for (String remotePath : remoteAbsolutePaths){
            boolean isDirectory = isDirectory(remotePath);
            if (isDirectory){
                LOGGER.debug("remotePath :[{}] is [directory],will removeDirectory.....", remotePath);

                Map<String, FileInfoEntity> map = getLsFileMap(remotePath);

                //删除子文件
                for (Map.Entry<String, FileInfoEntity> entry : map.entrySet()){
                    String key = entry.getKey();
                    // 级联删除
                    deleteDontClose(joinPath(remotePath, key));
                }

                LOGGER.info("channelSftp rmdir,remotePath [{}]", remotePath);
                isSuccess = rmdir(remotePath);
            }else{
                LOGGER.trace("remotePath:[{}] is [not directory],will rm....", remotePath);
                isSuccess = rm(remotePath);

                logInfoOrError(isSuccess, "remove remotePath:[{}] [{}]", remotePath, buildResultString(isSuccess));
            }
        }
        return isSuccess;
    }

    //---------------------------------------------------------------

    /**
     * 不关闭连接下载.
     *
     * @param remotePath
     *            远程文件远程路径,可以是文件也可以文件夹
     * @param localAbsoluteDirectoryPath
     *            本地绝对的目录,如果不存在 支持级联创建
     * @return 如果成功下载返回true
     */
    private boolean downloadDontClose(String remotePath,String localAbsoluteDirectoryPath){
        boolean isSuccess = false;

        File file = new File(remotePath);
        // 远程文件的文件名
        String remoteFileName = file.getName();
        String filePath = localAbsoluteDirectoryPath + "/" + remoteFileName;

        //---------------------------------------------------------------
        // 如果远程路径是文件夹
        if (isDirectory(remotePath)){
            LOGGER.debug("will create directory:[{}]", localAbsoluteDirectoryPath);
            FileUtil.createDirectory(localAbsoluteDirectoryPath);

            Map<String, FileInfoEntity> lsFileMap = getLsFileMap(remotePath);
            for (Map.Entry<String, FileInfoEntity> entry : lsFileMap.entrySet()){
                String key = entry.getKey();

                // 级联下载
                isSuccess = downloadDontClose(remotePath + "/" + key, filePath);
            }
        }else{
            Date beginDate = now();

            // 下载到本地的文件路径
            LOGGER.trace("remotePath:[{}] will be download to [{}]", remotePath, filePath);
            FileUtil.createDirectoryByFilePath(filePath);
            isSuccess = downRemoteSingleFile(remotePath, filePath);

            logAfterDownRemoteSingleFile(remotePath, isSuccess, filePath, beginDate);
        }
        return isSuccess;
    }

    //---------------------------------------------------------------

    /**
     * Log after down remote single file.
     *
     * @param remotePath
     *            the remote path
     * @param isSuccess
     *            the is success
     * @param filePath
     *            the file path
     * @param beginDate
     *            the begin date
     * @since 1.10.4
     */
    private void logAfterDownRemoteSingleFile(String remotePath,boolean isSuccess,String filePath,Date beginDate){
        if (LOGGER.isInfoEnabled()){
            String pattern = "downRemoteSingleFile remotePath:[{}] to [{}] [{}], use time: [{}]";
            logInfoOrError(isSuccess, pattern, remotePath, filePath, buildResultString(isSuccess), formatDuration(beginDate));
        }
    }

    //---------------------------------------------------------------

    /**
     * Builds the result string.
     *
     * @param isSuccess
     *            the is success
     * @return the string
     * @since 1.10.4
     */
    protected String buildResultString(boolean isSuccess){
        return isSuccess ? "success" : "fail!!";
    }

    /**
     * Log info or error.
     *
     * @param isSuccess
     *            the is success
     * @param messagePattern
     *            the message pattern
     * @param args
     *            the args
     */
    protected void logInfoOrError(boolean isSuccess,String messagePattern,Object...args){
        if (LOGGER.isInfoEnabled()){
            String message = Slf4jUtil.format(messagePattern, args);

            if (isSuccess){
                LOGGER.info(message);
            }else{
                LOGGER.error(message);
            }
        }
    }

    //---------------------------------------------------------------

    /**
     * 递归传递文件夹/文件.
     * 
     * <p>
     * 自动辨别 是否是文件还是文件夹<br>
     * 如果是文件夹,先判断remoteDirectory下面是否已经有同名的文件夹,如果没有,则在对应的 remoteDirectory创建文件夹.
     * </p>
     *
     * @param localFileFullPath
     *            要上传的单个文件 全路径 ,自动辨别 是否是文件还是文件夹<br>
     *            如果是文件夹,先判断remoteDirectory 下面是否已经有同名的文件夹,如果没有,则在对应的 remoteDirectory 创建文件夹
     * @param remoteDirectory
     *            远程保存目录
     * @return true, if successful
     */
    private boolean uploadDontClose(String localFileFullPath,String remoteDirectory){
        File localFile = new File(localFileFullPath);

        LOGGER.trace("localFile absolutePath:[{}],remoteDirectory:[{}]", localFile.getAbsolutePath(), remoteDirectory);

        // 转移到FTP服务器目录
        boolean isSuccess = cd(remoteDirectory);
        if (!isSuccess){
            LOGGER.error("cd:[{}] error~~~~", remoteDirectory);
            return false;
        }

        //---------------------------------------------------------------

        LOGGER.debug("cd:[{}] success~~~~", remoteDirectory);
        String localFileName = localFile.getName();

        if (localFile.isFile()){ // 文件
            isSuccess = uploadFile(localFileFullPath, remoteDirectory, localFileName);
        }else if (localFile.isDirectory()){ // 文件夹
            uploadDirectory(remoteDirectory, localFile, localFileName);
        }
        return isSuccess;
    }

    //---------------------------------------------------------------

    /**
     * Do with file.
     *
     * @param localFileFullPath
     *            the local file full path
     * @param remoteDirectory
     *            the remote directory
     * @param localFileName
     *            the local file name
     * @return true, if do with file
     * @since 1.7.1
     */
    private boolean uploadFile(String localFileFullPath,String remoteDirectory,String localFileName){
        LOGGER.debug("begin put:[{}] to remoteDirectory:[{}]", localFileName, remoteDirectory);

        FileInputStream fileInputStream = FileUtil.getFileInputStream(localFileFullPath);

        boolean isSuccess = upload(fileInputStream, localFileName);

        logInfoOrError(isSuccess, "put [{}] to [{}] [{}]", localFileFullPath, remoteDirectory, buildResultString(isSuccess));

        IOUtil.closeQuietly(fileInputStream);
        return isSuccess;
    }

    //---------------------------------------------------------------

    /**
     * Do with directory.
     *
     * @param remoteDirectory
     *            the remote directory
     * @param localFile
     *            the local file
     * @param localFileName
     *            the local file name
     * @since 1.7.1
     */
    private void uploadDirectory(String remoteDirectory,File localFile,String localFileName){
        // 是否存在同名且同类型的文件,
        // 如果不存在,则创建一个文件夹
        if (!isExistsSameNameAndTypeFile(remoteDirectory, localFile)){
            String dirNameToCreate = localFileName;
            boolean isSuccess = mkdir(dirNameToCreate);

            logInfoOrError(isSuccess, "mkdir:[{}] [{}]~~~~", dirNameToCreate, buildResultString(isSuccess));
        }

        //---------------------------------------------------------------
        // 递归
        for (File childrenFile : localFile.listFiles()){
            uploadDontClose(childrenFile.getAbsolutePath(), remoteDirectory + "/" + localFileName);
        }
    }

    //---------------------------------------------------------------

    /**
     * 是否存在同名且同类型的文件.
     * 
     * @param remotePath
     *            远程路径
     * @param file
     *            the file
     *
     * @return 存在 返回true,否则返回false
     */
    private boolean isExistsSameNameAndTypeFile(String remotePath,File file){
        Map<String, FileInfoEntity> lsFileMap = getLsFileMap(remotePath);

        //远程没有文件,返回false
        if (isNullOrEmpty(lsFileMap)){
            return false;
        }

        //---------------------------------------------------------------
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug(JsonUtil.format(lsFileMap));
        }
        //---------------------------------------------------------------

        String fileName = file.getName();
        FileInfoEntity fileInfoEntity = lsFileMap.get(fileName);
        if (null == fileInfoEntity){ // 判断同名
            return false;
        }
        return isExistsSameNameAndTypeFile(file, fileName, fileInfoEntity);
    }

    //---------------------------------------------------------------

    /**
     * Checks if is exists same name and type file.
     *
     * @param file
     *            the file
     * @param fileName
     *            the file name
     * @param fileInfoEntity
     *            the file info entity
     * @return true, if checks if is exists same name and type file
     * @since 1.7.1
     */
    private static boolean isExistsSameNameAndTypeFile(File file,String fileName,FileInfoEntity fileInfoEntity){
        boolean isFile = file.isFile();
        String type = isFile ? "isFile" : "isDirectory";
        LOGGER.debug("Input fileName:[{}],type:[{}]", fileName, type);

        boolean isDirectoryFileInfoEntity = fileInfoEntity.getFileType() == DIRECTORY; // 远程是否是文件夹
        // 判断同类型
        boolean sameFile = isFile && !isDirectoryFileInfoEntity;
        boolean sameDirectory = file.isDirectory() && isDirectoryFileInfoEntity;
        if (sameFile || sameDirectory){
            LOGGER.debug("hasSameNameAndTypeFile,filename:[{}],type:[{}]", type, fileName);
            return true;
        }
        return false;
    }

    //---------------------------------------------------------------

    /**
     * 文件夹 和文件 拼接路径.
     *
     * @param directoryPath
     *            文件夹路径,有得时候 在远程ftp 或者sftp 上面 不知道 这个路径到底是否是文件夹
     * @param ftpFileName
     *            the ftp file name
     * @return the string
     */
    private static String joinPath(String directoryPath,String ftpFileName){
        return directoryPath + (directoryPath.endsWith("/") ? EMPTY : "/") + ftpFileName;
    }
}
