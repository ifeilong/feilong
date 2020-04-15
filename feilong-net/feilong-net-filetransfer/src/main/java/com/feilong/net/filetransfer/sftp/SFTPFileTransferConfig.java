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

import java.util.Properties;

import com.feilong.net.filetransfer.AbstractFileTransferConfig;

/**
 * sftp 文件传输的配置.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.7.1
 */
public class SFTPFileTransferConfig extends AbstractFileTransferConfig{

    /** The ssh config. */
    private Properties sshConfig;

    /** The connect timeout. */
    private int        sessionTimeout = 0;

    //---------------------------------------------------------------

    /**
     * 获得 the ssh config.
     *
     * @return the sshConfig
     */
    public Properties getSshConfig(){
        return sshConfig;
    }

    /**
     * 设置 the ssh config.
     *
     * @param sshConfig
     *            the sshConfig to set
     */
    public void setSshConfig(Properties sshConfig){
        this.sshConfig = sshConfig;
    }

    /**
     * 获得 the connect timeout.
     *
     * @return the sessionTimeout
     */
    public int getSessionTimeout(){
        return sessionTimeout;
    }

    /**
     * 设置 the connect timeout.
     *
     * @param sessionTimeout
     *            the sessionTimeout to set
     */
    public void setSessionTimeout(int sessionTimeout){
        this.sessionTimeout = sessionTimeout;
    }

}
