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
package com.feilong.net.filetransfer.sftp;

import static com.feilong.core.bean.ConvertUtil.toLong;
import static com.feilong.core.util.MapUtil.newHashMap;
import static com.feilong.io.entity.FileType.DIRECTORY;
import static com.feilong.io.entity.FileType.FILE;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.lang.StringUtil;
import com.feilong.io.FileUtil;
import com.feilong.io.entity.FileInfoEntity;
import com.feilong.json.JsonUtil;
import com.feilong.lib.lang3.StringUtils;
import com.feilong.net.filetransfer.AbstractFileTransfer;
import com.feilong.net.filetransfer.FileTransferException;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

/**
 * SFTP工具类.
 *
 * <p>
 * sftp是Secure File Transfer Protocol的缩写,安全文件传送协议。 <br>
 * 可以为传输文件提供一种安全的加密方法。 sftp 与 ftp 有着几乎一样的语法和功能。
 * </p>
 *
 * <p>
 * 注意:需要自行依赖 jsch.
 * </p>
 *
 * <pre class="code">
 * {@code
 * <dependency>
 * <groupId>com.jcraft</groupId>
 * <artifactId>jsch</artifactId>
 * </dependency>
 * }
 * </pre>
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.0.5
 */
@SuppressWarnings("squid:S1192") //String literals should not be duplicated
public class SFTPFileTransfer extends AbstractFileTransfer{

    /**
     * The Constant LOGGER.
     */
    private static final Logger    LOGGER = LoggerFactory.getLogger(SFTPFileTransfer.class);

    /**
     * The sftp file transfer config.
     */
    private SFTPFileTransferConfig sftpFileTransferConfig;

    //---------------------------------------------------------------

    /**
     * The channel sftp.
     */
    private ChannelSftp            channelSftp;

    /**
     * The session.
     */
    private Session                session;

    //---------------------------------------------------------------

    /**
     * 创建 链接.
     *
     * @return true, if successful
     */
    @Override
    protected boolean connect(){
        // If the client is already connected, disconnect
        if (channelSftp != null){
            LOGGER.warn("channelSftp is not null,will disconnect first....");
            disconnect();
        }
        try{
            session = SFTPUtil.connect(sftpFileTransferConfig);

            LOGGER.trace("open [sftp] session channel...");
            channelSftp = (ChannelSftp) session.openChannel("sftp");

            LOGGER.trace("channel connecting...");
            channelSftp.connect();

            //---------------------------------------------------------------
            boolean isSuccess = channelSftp.isConnected();

            logAfterConnected(isSuccess, session);

            return isSuccess;
        }catch (Exception e){
            throw new FileTransferException(
                            StringUtil.formatPattern("sftpFileTransferConfig:{}", JsonUtil.toString(sftpFileTransferConfig)),
                            e);
        }
    }

    /**
     * 关闭链接.
     */
    @Override
    protected void disconnect(){
        if (channelSftp != null){
            channelSftp.exit();

            //---------------------------------------------------------------
            if (LOGGER.isTraceEnabled()){
                LOGGER.trace(StringUtils.center("channelSftp exit", 50, "------"));
            }
        }
        if (session != null){
            String buildSessionPrettyString = SftpSessionUtil.buildSessionPrettyString(session);

            session.disconnect();

            //---------------------------------------------------------------
            if (LOGGER.isInfoEnabled()){
                String message = StringUtil.formatPattern(" session disconnect: [{}]", buildSessionPrettyString);
                LOGGER.info(StringUtils.center(message, 50, "------"));
            }
        }
        channelSftp = null;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.feilong.tools.net.FileTransfer#getLsFileMap(java.lang.String)
     */
    @Override
    protected Map<String, FileInfoEntity> getLsFileMap(String remotePath){
        Map<String, FileInfoEntity> map = newHashMap();

        try{
            @SuppressWarnings("unchecked")
            List<LsEntry> lsEntryList = channelSftp.ls(remotePath);
            for (LsEntry lsEntry : lsEntryList){
                String fileName = lsEntry.getFilename();

                if (".".equals(fileName)// 本文件夹
                                || "..".equals(fileName)// 上层目录均过滤掉
                ){
                    continue;
                }
                map.put(fileName, buildFileInfoEntity(remotePath, fileName, lsEntry.getAttrs()));
            }
            return map;
        }catch (SftpException e){
            throw new FileTransferException(StringUtil.formatPattern("remotePath:[{}]", remotePath), e);
        }
    }

    /**
     * 构造 FileInfoEntity.
     *
     * @param remotePath
     *            远程路径
     * @param fileName
     *            下面的文件
     * @param attrs
     *            the attrs
     * @return the file info entity
     * @since 1.7.1
     */
    private FileInfoEntity buildFileInfoEntity(String remotePath,String fileName,SftpATTRS attrs){
        FileInfoEntity fileInfoEntity = new FileInfoEntity();
        fileInfoEntity.setFileType(isDirectory(remotePath + "/" + fileName) ? DIRECTORY : FILE);
        fileInfoEntity.setName(fileName);
        fileInfoEntity.setSize(attrs.getSize());

        // returns the last modification time.
        fileInfoEntity.setLastModified(toLong(attrs.getMTime()));
        return fileInfoEntity;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.feilong.tools.net.AbstractFileTransfer#mkdir(java.lang.String)
     */
    @Override
    protected boolean mkdir(String remoteDirectory){
        try{
            LOGGER.debug("begin mkdir:[{}]~~", remoteDirectory);

            channelSftp.mkdir(remoteDirectory);

            LOGGER.debug("mkdir:[{}] over~~", remoteDirectory);
            return true;
        }catch (SftpException e){
            String message = StringUtil.formatPattern("can't mkdir,remoteDirectory:[{}]", remoteDirectory);
            throw new FileTransferException(message, e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.feilong.tools.net.filetransfer.AbstractFileTransfer#tryCd(java.lang.String)
     */
    @Override
    protected void tryCd(String remoteDirectory) throws Exception{
        LOGGER.debug("cd:[{}]", remoteDirectory);
        channelSftp.cd(remoteDirectory);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.feilong.tools.net.filetransfer.FileTransfer#upload(java.io.FileInputStream, java.lang.String)
     */
    @Override
    protected boolean upload(FileInputStream fileInputStream,String toFileName){
        try{
            channelSftp.put(fileInputStream, toFileName);
            return true;
        }catch (SftpException e){
            String message = StringUtil.formatPattern("can't upload fileInputStream,toFileName:[{}]", toFileName);
            throw new FileTransferException(message, e);
        }
    }

    /**
     * 判断路径是否是文件夹.
     *
     * @param remoteFile
     *            远程路径
     * @return 如果是文件夹返回true
     */
    @Override
    protected boolean isDirectory(String remoteFile){
        try{
            SftpATTRS sftpATTRS = channelSftp.stat(remoteFile);

            boolean isDir = sftpATTRS.isDir();
            LOGGER.debug("remoteFile:[{}] is [{}]", remoteFile, isDir ? "directory" : "file");
            return isDir;
        }catch (SftpException e){
            String message = StringUtil.formatPattern("remoteFile:[{}] ", remoteFile);
            throw new FileTransferException(message, e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.feilong.tools.net.FileTransfer#rmdir(java.lang.String)
     */
    @Override
    protected boolean rmdir(String remotePath){
        try{
            channelSftp.rmdir(remotePath);
            return true;
        }catch (SftpException e){
            String message = StringUtil.formatPattern("remotePath:[{}]", remotePath);
            throw new FileTransferException(message, e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.feilong.tools.net.FileTransfer#rm(java.lang.String)
     */
    @Override
    protected boolean rm(String remotePath){
        try{
            channelSftp.rm(remotePath);
            return true;
        }catch (SftpException e){
            String message = StringUtil.formatPattern("remotePath:[{}]", remotePath);
            throw new FileTransferException(message, e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.feilong.tools.net.FileTransfer#_downRemoteSingleFile(java.lang.String, java.lang.String)
     */
    @Override
    protected boolean downRemoteSingleFile(String remoteSingleFile,String filePath){
        try (OutputStream outputStream = FileUtil.getFileOutputStream(filePath)){//use try-with-resources
            channelSftp.get(remoteSingleFile, outputStream);
            return true;
        }catch (SftpException | IOException e){
            String message = StringUtil.formatPattern("remoteSingleFile:[{}],filePath:[{}]", remoteSingleFile, filePath);
            throw new FileTransferException(message, e);
        }
    }

    //---------------------------------------------------------------

    /**
     * Log after connected.
     *
     * @param isSuccess
     *            the is success
     * @param useSession
     *            the session
     * @since 1.10.4
     */
    private static void logAfterConnected(boolean isSuccess,Session useSession){
        if (LOGGER.isInfoEnabled()){
            String sessionPrettyString = SftpSessionUtil.buildSessionPrettyString(useSession);

            String sessionInfo = JsonUtil.toString(SftpSessionUtil.getMapForLog(useSession));
            String pattern = "connect [{}]---[{}]{}";

            logInfoOrError(isSuccess, pattern, sessionPrettyString, toResultString(isSuccess), sessionInfo);
        }
    }

    //---------------------------------------------------------------

    /**
     * 设置 sftp file transfer config.
     *
     * @param sftpFileTransferConfig
     *            the sftpFileTransferConfig to set
     */
    public void setSftpFileTransferConfig(SFTPFileTransferConfig sftpFileTransferConfig){
        this.sftpFileTransferConfig = sftpFileTransferConfig;
    }

}