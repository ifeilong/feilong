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
package com.feilong.net.filetransfer.ftp;

import static com.feilong.core.lang.ObjectUtil.defaultIfNull;
import static com.feilong.core.util.MapUtil.newHashMap;
import static com.feilong.io.entity.FileType.DIRECTORY;
import static com.feilong.io.entity.FileType.FILE;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.Validate;
import com.feilong.io.FileUtil;
import com.feilong.io.entity.FileInfoEntity;
import com.feilong.json.JsonUtil;
import com.feilong.lib.lang3.StringUtils;
import com.feilong.lib.net.ftp.FTP;
import com.feilong.lib.net.ftp.FTPClient;
import com.feilong.lib.net.ftp.FTPFile;
import com.feilong.lib.net.ftp.FTPReply;
import com.feilong.net.filetransfer.AbstractFileTransfer;
import com.feilong.net.filetransfer.FileTransferException;
import com.feilong.tools.slf4j.Slf4jUtil;

/**
 * ftp 相关的工具类.
 * 
 * <p>
 * 注:依赖于
 * </p>
 * 
 * <pre class="code">
{@code
        <dependency>
            <groupId>commons-net</groupId>
            <artifactId>commons-net</artifactId>
        </dependency>
}
 * </pre>
 * 
 * <p>
 * 包括以下常用功能:
 * </p>
 * 
 * <ul>
 * <li>单个文件上传</li>
 * <li>整个文件夹上传</li>
 * <li>多个不定路径文件上传</li>
 * <li>多个不定路径文件夹上传</li>
 * </ul>
 * 
 * <p>
 * FTP(File Transfer Protocol, FTP)是TCP/IP网络上两台计算机传送文件的协议,FTP是在TCP/IP网络和INTERNET上最早使用的协议之一,它属于网络协议组的应用层.
 * </p>
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.0.0
 */
@SuppressWarnings("squid:S1192") //String literals should not be duplicated
public class FTPFileTransfer extends AbstractFileTransfer{

    /** The Constant LOGGER. */
    private static final Logger   LOGGER = LoggerFactory.getLogger(FTPFileTransfer.class);

    //---------------------------------------------------------------

    /** The ftp file transfer config. */
    private FTPFileTransferConfig ftpFileTransferConfig;

    /** The ftp client. */
    private FTPClient             ftpClient;

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.net.FileTransfer#connect()
     */
    @Override
    protected boolean connect(){
        try{
            // 连接
            String hostName = ftpFileTransferConfig.getHostName();
            Validate.notBlank(hostName, "hostName can't be blank!");

            //---------------------------------------------------------------
            ftpClient.connect(hostName, defaultIfNull(ftpFileTransferConfig.getPort(), 21));
            LOGGER.debug("connect hostName:[{}]", hostName);

            String userName = ftpFileTransferConfig.getUserName();
            String password = ftpFileTransferConfig.getPassword();
            boolean loginResult = ftpClient.login(userName, password);

            String message = Slf4jUtil.format(
                            "login:[{}],params:[{}],port:[{}]~~~",
                            loginResult,
                            JsonUtil.format(ftpFileTransferConfig),
                            ftpClient.getDefaultPort());
            LOGGER.debug(message);

            //---------------------------------------------------------------
            if (!loginResult){
                throw new FileTransferException(message);
            }

            //---------------------------------------------------------------
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)){
                LOGGER.error("FTP 服务拒绝连接！ReplyCode is:{},will ftpClient.disconnect()", replyCode);
                ftpClient.disconnect();
                return false;
            }

            //---------------------------------------------------------------
            // 设置 本机被动模式 这个到不用在login之后执行, 因为它只改变FTPClient实例的内部属性.
            ftpClient.enterLocalPassiveMode();

            // FTPClient默认使用ASCII作为传输模式, 所有不能传输二进制文件
            // 设置以二进制流的方式传输,图片保真,[要在login以后执行]. 因为这个方法要向服务器发送"TYPE I"命令
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            String systemName = ftpClient.getSystemType();
            LOGGER.debug("ftpClient systemName:[{}]", systemName);

            //---------------------------------------------------------------
            // 没有异常则 确定成功
            LOGGER.info("connect:[{}]", true);
            return true;
        }catch (Exception e){
            String message = Slf4jUtil.format("ftpFileTransferConfig:{}", JsonUtil.format(ftpFileTransferConfig));
            throw new FileTransferException(message, e);
        }
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.net.FileTransfer#disconnect()
     */
    @Override
    protected void disconnect(){
        if (ftpClient != null && ftpClient.isConnected()){
            try{
                LOGGER.info("ftpClient logout:[{}]", ftpClient.logout());

                ftpClient.disconnect();

                //---------------------------------------------------------------
                if (LOGGER.isInfoEnabled()){
                    LOGGER.info(StringUtils.center("ftpClient disconnect...", 50, "------"));
                }
            }catch (IOException e){
                throw new FileTransferException("disconnect exception", e);
            }
        }
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.net.filetransfer.AbstractFileTransfer#tryCd(java.lang.String)
     */
    @Override
    protected void tryCd(String remoteDirectory) throws Exception{
        // 转移到FTP服务器目录
        boolean flag = ftpClient.changeWorkingDirectory(remoteDirectory);
        if (flag){
            LOGGER.debug("cd [{}]", remoteDirectory);
        }else{
            throw new FileTransferException(StringUtils.trim(ftpClient.getReplyString().trim()));
        }
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.net.FileTransfer#mkdir(java.lang.String)
     */
    @Override
    protected boolean mkdir(String remoteDirectory){
        try{
            boolean flag = ftpClient.makeDirectory(remoteDirectory);
            if (flag){
                LOGGER.info("mkdir [{}] success~~", remoteDirectory);
            }else{
                LOGGER.error("mkdir [{}] error,ReplyString :[{}]", remoteDirectory, ftpClient.getReplyString());
            }
            return flag;
        }catch (IOException e){
            String message = Slf4jUtil.format("can't mkdir,remoteDirectory:[{}]", remoteDirectory);
            throw new FileTransferException(message, e);
        }
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.net.filetransfer.FileTransfer#upload(java.io.FileInputStream, java.lang.String)
     */
    @Override
    protected boolean upload(FileInputStream fileInputStream,String toFileName){
        try{
            boolean flag = ftpClient.storeFile(toFileName, fileInputStream);
            if (flag){
                LOGGER.debug("put [{}] success~~", toFileName);
            }else{
                LOGGER.error("store file error,ReplyString :{}", ftpClient.getReplyString());
            }
            return flag;
        }catch (IOException e){
            String message = Slf4jUtil.format("can't upload fileInputStream,toFileName:[{}]", toFileName);
            throw new FileTransferException(message, e);
        }
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.net.FileTransfer#getLsFileMap(java.lang.String)
     */
    @Override
    protected Map<String, FileInfoEntity> getLsFileMap(String remotePath){
        try{
            Map<String, FileInfoEntity> map = newHashMap();
            FTPFile[] ftpFiles = ftpClient.listFiles(remotePath);
            for (FTPFile ftpFile : ftpFiles){
                map.put(ftpFile.getName(), buildFileInfoEntity(ftpFile));
            }
            return map;
        }catch (IOException e){
            String message = Slf4jUtil.format("remotePath:[{}]", remotePath);
            throw new FileTransferException(message, e);
        }
    }

    //---------------------------------------------------------------

    /**
     * Builds the file info entity.
     *
     * @param ftpFile
     *            the ftp file
     * @return the file info entity
     * @since 1.7.1
     */
    private static FileInfoEntity buildFileInfoEntity(FTPFile ftpFile){
        FileInfoEntity fileInfoEntity = new FileInfoEntity();
        fileInfoEntity.setFileType(ftpFile.isDirectory() ? DIRECTORY : FILE);
        fileInfoEntity.setName(ftpFile.getName());
        fileInfoEntity.setSize(ftpFile.getSize());
        fileInfoEntity.setLastModified(ftpFile.getTimestamp().getTimeInMillis());
        return fileInfoEntity;
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.net.FileTransfer#rmdir(java.lang.String)
     */
    @Override
    protected boolean rmdir(String remotePath){
        try{
            //XXX 不知道为什么, 此处需要先 cd / 否则 删除不了文件夹
            LOGGER.info("ftpClient cwd root .....");
            cd("/");

            // if empty
            boolean flag = ftpClient.removeDirectory(remotePath);

            if (flag){
                LOGGER.info("ftpClient removeDirectory,remotePath [{}]:[{}]", remotePath, flag);
            }else{
                LOGGER.warn("ftpClient removeDirectory,remotePath [{}]:[{}] ReplyCode:{}", remotePath, flag, ftpClient.getReplyCode());
            }
            return flag;
        }catch (IOException e){
            String message = Slf4jUtil.format("remotePath:[{}]", remotePath);
            throw new FileTransferException(message, e);
        }
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.net.FileTransfer#rm(java.lang.String)
     */
    @Override
    protected boolean rm(String remotePath){
        try{
            LOGGER.info("remotePath:[{}] is [not directory],deleteFile.....", remotePath);
            boolean flag = ftpClient.deleteFile(remotePath);

            if (flag){
                LOGGER.info("ftpClient deleteFile,remotePath[{}] : [{}]", remotePath, flag);
            }else{
                LOGGER.warn("ftpClient deleteFile,remotePath[{}] : [{}] , ReplyCode:[{}]", remotePath, flag, ftpClient.getReplyCode());
            }
            return flag;
        }catch (IOException e){
            String message = Slf4jUtil.format("remotePath:[{}]", remotePath);
            throw new FileTransferException(message, e);
        }
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.net.filetransfer.FileTransfer#isDirectory(java.lang.String)
     */
    @Override
    protected boolean isDirectory(String remotePath){
        // 可以成功进入代表是文件夹
        // 经测试如果remotePath 不存在返回false ,如果是文件返回false
        return cd(remotePath);
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.net.FileTransfer#_downRemoteSingleFile(java.lang.String, java.lang.String)
     */
    @Override
    protected boolean downRemoteSingleFile(String remoteSingleFile,String filePath){
        try (FileOutputStream fileOutputStream = FileUtil.getFileOutputStream(filePath);
                        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream)){

            boolean success = ftpClient.retrieveFile(remoteSingleFile, bufferedOutputStream);
            bufferedOutputStream.flush();
            return success;
        }catch (IOException e){
            String message = Slf4jUtil.format("remoteSingleFile:[{}],filePath:[{}]", remoteSingleFile, filePath);
            throw new FileTransferException(message, e);
        }
    }

    //---------------------------------------------------------------

    /**
     * Sets the ftp client.
     * 
     * @param ftpClient
     *            the ftpClient to set
     */
    public void setFtpClient(FTPClient ftpClient){
        this.ftpClient = ftpClient;
    }

    /**
     * 设置 ftp file transfer config.
     *
     * @param ftpFileTransferConfig
     *            the ftpFileTransferConfig to set
     */
    public void setFtpFileTransferConfig(FTPFileTransferConfig ftpFileTransferConfig){
        this.ftpFileTransferConfig = ftpFileTransferConfig;
    }

}