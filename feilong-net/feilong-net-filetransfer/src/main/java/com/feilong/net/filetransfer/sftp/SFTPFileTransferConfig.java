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

import java.net.SocketOptions;
import java.util.Properties;

import com.feilong.core.TimeInterval;
import com.feilong.net.filetransfer.AbstractFileTransferConfig;

/**
 * sftp 文件传输的配置.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.7.1
 */
public class SFTPFileTransferConfig extends AbstractFileTransferConfig{

    /** The ssh config. */
    private Properties sshConfig;

    /**
     * 超时时间,用于设置 Socket setSoTimeout ,单位 毫秒, 可以使用 {@link TimeInterval}常量.
     * 
     * <p>
     * Enable/disable {@link SocketOptions#SO_TIMEOUT SO_TIMEOUT} With this option set to a non-zero timeout, a read() call on the
     * InputStream associated with this Socket will block for only this amount of time.
     * 
     * If the timeout expires, a <B>java.net.SocketTimeoutException</B> is raised, though the Socket is still valid.
     * 
     * The option <B>must</B> be enabled prior to entering the blocking operation to have effect.
     * </p>
     * 
     * 如果是0,将不设置超时时间.
     * 
     * @since 3.0.8 将默认超时0 ,改成 3 秒
     */
    private int        sessionTimeout = 3 * TimeInterval.MILLISECOND_PER_SECONDS;

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
     * 超时时间,用于设置 Socket setSoTimeout ,单位 毫秒, 可以使用 {@link TimeInterval}常量.
     * 
     * <p>
     * Enable/disable {@link SocketOptions#SO_TIMEOUT SO_TIMEOUT} With this option set to a non-zero timeout, a read() call on the
     * InputStream associated with this Socket will block for only this amount of time.
     * 
     * If the timeout expires, a <B>java.net.SocketTimeoutException</B> is raised, though the Socket is still valid.
     * 
     * The option <B>must</B> be enabled prior to entering the blocking operation to have effect.
     * </p>
     * 
     * 如果是0,将不设置超时时间.
     *
     * @return the 超时时间,用于设置 Socket setSoTimeout ,单位 毫秒, 可以使用 {@link TimeInterval}常量
     * @since 3.0.8 将默认超时0 ,改成 3 秒
     */
    public int getSessionTimeout(){
        return sessionTimeout;
    }

    /**
     * 超时时间,用于设置 Socket setSoTimeout ,单位 毫秒, 可以使用 {@link TimeInterval}常量.
     * 
     * <p>
     * Enable/disable {@link SocketOptions#SO_TIMEOUT SO_TIMEOUT} With this option set to a non-zero timeout, a read() call on the
     * InputStream associated with this Socket will block for only this amount of time.
     * 
     * If the timeout expires, a <B>java.net.SocketTimeoutException</B> is raised, though the Socket is still valid.
     * 
     * The option <B>must</B> be enabled prior to entering the blocking operation to have effect.
     * </p>
     * 
     * 如果是0,将不设置超时时间.
     *
     * @param sessionTimeout
     *            the new 超时时间,用于设置 Socket setSoTimeout ,单位 毫秒, 可以使用 {@link TimeInterval}常量
     * @since 3.0.8 将默认超时0 ,改成 3 秒
     */
    public void setSessionTimeout(int sessionTimeout){
        this.sessionTimeout = sessionTimeout;
    }
}
