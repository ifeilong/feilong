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
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.json.jsonlib.JsonUtil;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * The Class SFTPUtil.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.7.1
 */
class SFTPUtil{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SFTPUtil.class);

    /** Don't let anyone instantiate this class. */
    private SFTPUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Connect session.
     *
     * @param sftpFileTransferConfig
     *            the sftp file transfer config
     * @return the session
     * @throws JSchException
     *             the j sch exception
     * @since 1.7.1
     */
    static Session connectSession(SFTPFileTransferConfig sftpFileTransferConfig) throws JSchException{
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("will use [sftpFileTransferConfig]:[{}] to create session", JsonUtil.format(sftpFileTransferConfig));
        }
        //---------------------------------------------------------------
        JSch jsch = new JSch();

        Session session = jsch.getSession(
                        sftpFileTransferConfig.getUserName(),
                        sftpFileTransferConfig.getHostName(),
                        defaultIfNull(sftpFileTransferConfig.getPort(), 22));

        //---------------------------------------------------------------
        session.setPassword(sftpFileTransferConfig.getPassword());

        if (isNotNullOrEmpty(sftpFileTransferConfig.getSshConfig())){
            session.setConfig(sftpFileTransferConfig.getSshConfig());
        }
        session.setTimeout(sftpFileTransferConfig.getSessionTimeout());

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug(StringUtils.center("session connecting......", 50, "------"));
        }

        session.connect();
        return session;
    }
}
