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
package com.feilong.net;

import static com.feilong.core.lang.ObjectUtil.defaultIfNullOrEmpty;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import com.feilong.lib.net.util.SSLContextUtils;
import com.feilong.lib.net.util.TrustManagerUtils;

/**
 * 用来构造 {@link SSLContext}, SSL全称是Secure Sockets Layer安全套接层协议层.
 * 
 * <p>
 * 它是网景（Netscape）公司提出的基于 WEB 应用的安全协议
 * </p>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see com.feilong.lib.net.util.SSLContextUtils
 * @see javax.net.ssl.X509TrustManager
 * @since 1.10.6
 */
public class SSLContextBuilder{

    /** Don't let anyone instantiate this class. */
    private SSLContextBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 构造 {@link SSLContext}.
     *
     * @param protocol
     *            协议,参见 {@link SSLProtocol} ,如果 <code>protocol</code> 是null或者empty,那么默认使用 {@link SSLProtocol#TLS}
     * @return 如果 <code>protocol</code> 是null或者empty,那么默认使用 {@link SSLProtocol#TLS}<br>
     * @see SSLProtocol
     * @see com.feilong.lib.net.util.SSLContextUtils#createSSLContext(String, KeyManager[], TrustManager[])
     * @see com.feilong.lib.net.util.TrustManagerUtils#getAcceptAllTrustManager()
     */
    public static SSLContext build(String protocol){
        try{
            return SSLContextUtils.createSSLContext(
                            defaultIfNullOrEmpty(protocol, SSLProtocol.TLS),
                            null,
                            TrustManagerUtils.getAcceptAllTrustManager());
        }catch (Exception e){
            throw new UncheckedHttpException(e);
        }
    }

}
