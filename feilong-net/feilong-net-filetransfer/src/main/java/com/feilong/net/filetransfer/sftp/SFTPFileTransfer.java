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
import static com.feilong.core.lang.StringUtil.formatPattern;
import static com.feilong.core.util.MapUtil.newHashMap;
import static com.feilong.io.entity.FileType.DIRECTORY;
import static com.feilong.io.entity.FileType.FILE;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

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
 * <h3>使用方式示例:</h3>
 * 
 * <blockquote>
 * 
 * <pre class="code">
 * 
 * SFTPFileTransferConfig sftpFileTransferConfig = new SFTPFileTransferConfig();
 * sftpFileTransferConfig.setHostName("dts.xxxxx.com");
 * sftpFileTransferConfig.setUserName("xxxxx");
 * sftpFileTransferConfig.setPassword("xxxxxx");
 * sftpFileTransferConfig.setPort(2222);
 * 
 * SFTPFileTransfer sftpFileTransfer = new SFTPFileTransfer();
 * sftpFileTransfer.setSftpFileTransferConfig(sftpFileTransferConfig);
 * 
 * String dateString = DateUtil.toString(getFirstDateOfYesterday(), "yyyyMMdd");
 * sftpFileTransfer.upload("/iot/" + dateString + "/", "/Users/Downloads/part-m-00001");
 * 
 * </pre>
 * 
 * <b>此外通常可以使用spring xml 配置:</b>
 * 
 * <pre>
{@code
 <util:properties id="p_sftp" location="file:/Users/xxxxx/fileTransfer-sftp-gap.properties"/>

 <bean id="sftpFileTransfer" class="com.feilong.net.filetransfer.sftp.SFTPFileTransfer" scope="prototype">
    <property name="sftpFileTransferConfig">
           <bean class="com.feilong.net.filetransfer.sftp.SFTPFileTransferConfig">
          }
 * </pre>
 * 
 * <pre class="code">
 * 
   &lt;property name="hostName" value="#{p_sftp['fileTransfer.sftp.hostName']}" />
      &lt;property name="userName" value="#{p_sftp['fileTransfer.sftp.userName']}" />
      &lt;property name="password">
          &lt;value>&lt;![CDATA[#{p_sftp['fileTransfer.sftp.password']}]]></value>
      &lt;/property>
      &lt;property name="port" value="#{p_sftp['fileTransfer.sftp.port']}" />
  &lt;/bean>
&lt;/property>
</bean>
 * 
 * </pre>
 * 
 * 
 * <b>然后调用代码:</b>
 * 
 * <pre class="code">
 * 
 * &#64;Autowired
 * &#64;Qualifier("sftpFileTransfer")
 * private FileTransfer fileTransfer;
 * 
 * private final String remoteDirectory = "/upload/Inbound/BuyableandDisplayable/";
 * 
 * &#64;Override
 * &#64;Test
 * public void upload(){
 *     String singleLocalFileFullPath = "E:\\1.txt";
 *     fileTransfer.upload(remoteDirectory, singleLocalFileFullPath);
 * }
 * </pre>
 * 
 * </blockquote>
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * 
 * @since 1.0.5
 */
@SuppressWarnings("squid:S1192") //String literals should not be duplicated
@lombok.extern.slf4j.Slf4j
public class SFTPFileTransfer extends AbstractFileTransfer{

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
            log.warn(log("channelSftp is not null,will disconnect first...."));
            disconnect();
        }
        try{
            session = SFTPUtil.connect(sftpFileTransferConfig, logTraceContext);

            log.info(log("open [sftp] session channel..."));
            channelSftp = (ChannelSftp) session.openChannel("sftp");

            log.info(log("channel connecting..."));
            channelSftp.connect();

            //---------------------------------------------------------------
            boolean isSuccess = channelSftp.isConnected();

            logAfterConnected(isSuccess, session);

            return isSuccess;
        }catch (Exception e){
            throw new FileTransferException(log("sftpFileTransferConfig:{}", JsonUtil.toString(sftpFileTransferConfig)), e);
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
            if (log.isInfoEnabled()){
                log.info(log(StringUtils.center("channelSftp exit", 50, "------")));
            }
        }
        if (session != null){
            String buildSessionPrettyString = SftpSessionUtil.buildSessionPrettyString(session);

            session.disconnect();

            //---------------------------------------------------------------
            if (log.isInfoEnabled()){
                log.info(StringUtils.center(log("session disconnect: [{}]", buildSessionPrettyString), 50, "------"));
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
            throw new FileTransferException(log("remotePath:[{}]", remotePath), e);
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
            log.debug(log("begin mkdir:[{}]~~", remoteDirectory));

            channelSftp.mkdir(remoteDirectory);

            log.info(log("mkdir:[{}] over~~", remoteDirectory));
            return true;
        }catch (SftpException e){
            String message = formatPattern("can't mkdir,remoteDirectory:[{}]", remoteDirectory);
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
        log.debug(log("cd:[{}]", remoteDirectory));
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
            throw new FileTransferException(log("can't upload fileInputStream,toFileName:[{}]", toFileName), e);
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
            log.info(log("remoteFile:[{}] is [{}]", remoteFile, isDir ? "directory" : "file"));
            return isDir;
        }catch (SftpException e){
            throw new FileTransferException(log("remoteFile:[{}] ", remoteFile), e);
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
            throw new FileTransferException(log("remotePath:[{}]", remotePath), e);
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
            throw new FileTransferException(log("remotePath:[{}]", remotePath), e);
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
            throw new FileTransferException(log("remoteSingleFile:[{}],filePath:[{}]", remoteSingleFile, filePath), e);
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
    private void logAfterConnected(boolean isSuccess,Session useSession){
        //如果失败, 那么error级别日志
        if (!isSuccess || log.isInfoEnabled()){
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