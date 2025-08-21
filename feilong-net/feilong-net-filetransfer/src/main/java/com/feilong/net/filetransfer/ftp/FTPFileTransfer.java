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
@lombok.extern.slf4j.Slf4j
public class FTPFileTransfer extends AbstractFileTransfer{

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
            log.debug(log("connect hostName:[{}]", hostName));

            String userName = ftpFileTransferConfig.getUserName();
            String password = ftpFileTransferConfig.getPassword();
            boolean loginResult = ftpClient.login(userName, password);

            String message = log(
                            "login:[{}],params:[{}],port:[{}]~~~",
                            loginResult,
                            JsonUtil.toString(ftpFileTransferConfig),
                            ftpClient.getDefaultPort());
            log.debug(message);

            //---------------------------------------------------------------
            if (!loginResult){
                throw new FileTransferException(message);
            }

            //---------------------------------------------------------------
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)){
                log.error(log("FTP 服务拒绝连接！ReplyCode is:{},will ftpClient.disconnect()", replyCode));
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
            log.debug(log("ftpClient systemName:[{}]", systemName));

            //---------------------------------------------------------------
            // 没有异常则 确定成功
            log.info(log("connect:[{}]", true));
            return true;
        }catch (Exception e){
            throw new FileTransferException(log("ftpFileTransferConfig:{}", JsonUtil.toString(ftpFileTransferConfig)), e);
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
                log.info(log("ftpClient logout:[{}]", ftpClient.logout()));

                ftpClient.disconnect();

                //---------------------------------------------------------------
                if (log.isInfoEnabled()){
                    log.info(StringUtils.center(log("ftpClient disconnect..."), 50, "------"));
                }
            }catch (IOException e){
                throw new FileTransferException(log("disconnectException"), e);
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
            log.debug(log("cd [{}]", remoteDirectory));
        }else{
            throw new FileTransferException(log(StringUtils.trim(ftpClient.getReplyString().trim())));
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
                log.info(log("mkdir [{}] success~~", remoteDirectory));
            }else{
                log.error(log("mkdir [{}] error,ReplyString :[{}]", remoteDirectory, ftpClient.getReplyString()));
            }
            return flag;
        }catch (IOException e){
            throw new FileTransferException(log("can't mkdir,remoteDirectory:[{}]", remoteDirectory), e);
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
                log.debug(log("put [{}] success~~", toFileName));
            }else{
                log.error(log("store file error,ReplyString :{}", ftpClient.getReplyString()));
            }
            return flag;
        }catch (IOException e){
            throw new FileTransferException(log("can't upload fileInputStream,toFileName:[{}]", toFileName), e);
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
        }catch (Exception e){
            throw new FileTransferException(log("remotePath:[{}]", remotePath), e);
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
            log.info(log("ftpClient cwd root ....."));
            cd("/");

            // if empty
            boolean flag = ftpClient.removeDirectory(remotePath);

            if (flag){
                log.info(log("ftpClient removeDirectory,remotePath [{}]:[{}]", remotePath, flag));
            }else{
                log.warn(log("ftpClient removeDirectory,remotePath [{}]:[{}] ReplyCode:{}", remotePath, flag, ftpClient.getReplyCode()));
            }
            return flag;
        }catch (IOException e){
            throw new FileTransferException(log("remotePath:[{}]", remotePath), e);
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
            log.info(log("remotePath:[{}] is [not directory],deleteFile.....", remotePath));
            boolean flag = ftpClient.deleteFile(remotePath);

            if (flag){
                log.info(log("ftpClient deleteFile,remotePath[{}] : [{}]", remotePath, flag));
            }else{
                log.warn(log("ftpClient deleteFile,remotePath[{}] : [{}] , ReplyCode:[{}]", remotePath, flag, ftpClient.getReplyCode()));
            }
            return flag;
        }catch (Exception e){
            throw new FileTransferException(log("remotePath:[{}]", remotePath), e);
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
            throw new FileTransferException(log("remoteSingleFile:[{}],filePath:[{}]", remoteSingleFile, filePath), e);
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