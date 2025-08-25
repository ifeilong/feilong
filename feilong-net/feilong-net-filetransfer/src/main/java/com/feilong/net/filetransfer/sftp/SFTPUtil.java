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

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.toMapUseEntrys;
import static com.feilong.core.bean.ConvertUtil.toProperties;
import static com.feilong.core.lang.ObjectUtil.defaultIfNull;
import static com.feilong.core.lang.StringUtil.formatPattern;

import java.util.Properties;

import com.feilong.core.Validate;
import com.feilong.json.JsonUtil;
import com.feilong.lib.lang3.StringUtils;
import com.feilong.lib.lang3.tuple.Pair;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * SFTP 工具类.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.7.1
 */
@lombok.extern.slf4j.Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class SFTPUtil{

    /**
     * 默认的 config.
     *
     * @since 3.0.8
     */
    private static final Properties DEFAULT_CONFIG_PROPERTIES = toProperties(
                    toMapUseEntrys( //
                                    //设置第一次登陆的时候提示，可选值：(ask | yes | no)  
                                    Pair.of("StrictHostKeyChecking", "no")));

    //---------------------------------------------------------------

    /**
     * 连接session.
     *
     * @param sftpFileTransferConfig
     *            sftp 文件传输的配置
     * @return 如果 <code>sftpFileTransferConfig</code> 是null,抛出 {@link NullPointerException}<br>
     * 
     *         如果 <code>sftpFileTransferConfig.hostName</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>sftpFileTransferConfig.hostName</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>sftpFileTransferConfig.userName</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>sftpFileTransferConfig.userName</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * 
     * @throws JSchException
     *             the j sch exception
     */
    static Session connect(SFTPFileTransferConfig sftpFileTransferConfig,String logTraceContext) throws JSchException{
        Validate.notNull(sftpFileTransferConfig, "sftpFileTransferConfig can't be null!");

        String hostName = sftpFileTransferConfig.getHostName();
        String userName = sftpFileTransferConfig.getUserName();

        Validate.notBlank(hostName, "hostName can't be blank!");
        Validate.notBlank(userName, "userName can't be blank!");

        if (log.isDebugEnabled()){
            log.debug(
                            log(
                                            logTraceContext,
                                            "will use [sftpFileTransferConfig]:[{}] to create session",
                                            JsonUtil.toString(sftpFileTransferConfig)));
        }
        //---------------------------------------------------------------
        JSch jsch = new JSch();

        Session session = jsch.getSession(userName, hostName, defaultIfNull(sftpFileTransferConfig.getPort(), 22));

        //---------------------------------------------------------------
        session.setPassword(sftpFileTransferConfig.getPassword());
        session.setTimeout(sftpFileTransferConfig.getSessionTimeout());

        Properties useConfig = build(sftpFileTransferConfig.getSshConfig());
        if (isNotNullOrEmpty(useConfig)){
            session.setConfig(useConfig);
        }
        if (log.isInfoEnabled()){
            log.info(StringUtils.center(log(logTraceContext, "session connecting......"), 50, "------"));
        }

        //---------------------------------------------------------------
        session.connect();
        return session;
    }

    /**
     * Builds the.
     *
     * @param customConfig
     *            the custom config
     * @return the properties
     * @since 3.0.8
     */
    private static Properties build(Properties customConfig){
        //如果自定义的是 null 那么使用默认的
        if (isNullOrEmpty(customConfig)){
            return DEFAULT_CONFIG_PROPERTIES;
        }

        //---------------------------------------------------------------
        //否则融合,以自定义的覆盖默认的
        Properties properties = new Properties();
        properties.putAll(DEFAULT_CONFIG_PROPERTIES);
        properties.putAll(customConfig);
        return properties;
    }

    /**
     * 记录带特殊表示的日志,方便搜索日志.
     * 
     * @since 4.1.1
     */
    private static String log(String logTraceContext,String messagePattern,Object...params){
        StringBuilder sb = new StringBuilder();
        //拼接 logTraceContext 日志
        if (isNotNullOrEmpty(logTraceContext)){
            sb.append("logTraceContext:[" + logTraceContext + "] ");
        }

        //拼接业务日志
        if (isNotNullOrEmpty(messagePattern)){
            sb.append(formatPattern(messagePattern, params));
        }
        return sb.toString();
    }
}
