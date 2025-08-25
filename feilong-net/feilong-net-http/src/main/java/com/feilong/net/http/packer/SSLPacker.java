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
package com.feilong.net.http.packer;

import javax.net.ssl.SSLContext;

import com.feilong.lib.org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import com.feilong.lib.org.apache.http.conn.ssl.NoopHostnameVerifier;
import com.feilong.net.SSLProtocol;
import com.feilong.net.http.ConnectionConfig;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * ssl 封装器.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 2.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SSLPacker{

    /**
     * 设置 SSL.
     * 
     * @param customHttpClientBuilder
     *            the custom http client builder
     * @param connectionConfig
     *            the connection config
     * @param layeredConnectionSocketFactory
     *            the layered connection socket factory
     *
     * @see com.feilong.lib.org.apache.http.conn.ssl.NoopHostnameVerifier
     * @see javax.net.ssl.HostnameVerifier
     */
    public static void pack(
                    com.feilong.lib.org.apache.http.impl.client.HttpClientBuilder customHttpClientBuilder,
                    ConnectionConfig connectionConfig,
                    LayeredConnectionSocketFactory layeredConnectionSocketFactory){
        if (null != layeredConnectionSocketFactory){
            customHttpClientBuilder.setSSLSocketFactory(layeredConnectionSocketFactory);
        }
        //---------------------------------------------------------------
        //这代码比 SSLContext sslContext = buildHttpClient4SSLContext() 简洁
        SSLContext sslContext = com.feilong.net.SSLContextBuilder.build(SSLProtocol.TLSv12);
        customHttpClientBuilder.setSSLContext(sslContext);

        //---------------------------------------------------------------
        //since 2.0.0
        if (connectionConfig.getTurnOffHostnameVerifier()){
            customHttpClientBuilder.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE);
        }
    }
}
