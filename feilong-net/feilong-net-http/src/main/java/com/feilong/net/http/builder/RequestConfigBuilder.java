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
package com.feilong.net.http.builder;

import static com.feilong.core.TimeInterval.MILLISECOND_PER_SECONDS;
import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.lang.ObjectUtil.defaultIfNull;

import com.feilong.lib.org.apache.http.HttpHost;
import com.feilong.lib.org.apache.http.client.config.CookieSpecs;
import com.feilong.lib.org.apache.http.client.config.RequestConfig;
import com.feilong.lib.org.apache.http.client.config.RequestConfig.Builder;
import com.feilong.net.http.ConnectionConfig;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * httpclient 超时时间 等待时间 响应时间.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.10.6
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RequestConfigBuilder{

    /**
     * Builds the {@link RequestConfig}.
     *
     * @param connectionConfig
     *            the connection config
     * @return the request config
     * @see Builder
     * @see RequestConfig
     * @see com.feilong.lib.org.apache.http.client.config.CookieSpecs
     */
    public static RequestConfig build(ConnectionConfig connectionConfig){
        ConnectionConfig useConnectionConfig = defaultIfNull(connectionConfig, ConnectionConfig.INSTANCE);

        //---------------------------------------------------------------
        Builder requestConfigBuilder = RequestConfig.custom(); //RequestConfig.DEFAULT

        //设置超时时间
        setTimeout(requestConfigBuilder, useConnectionConfig);

        //设置代理
        setProxy(requestConfigBuilder, useConnectionConfig);

        //---------------------------------------------------------------
        //since 2.1.0
        requestConfigBuilder.setCookieSpec(CookieSpecs.IGNORE_COOKIES);

        //requestConfigBuilder.setContentCompressionEnabled(contentCompressionEnabled)
        //requestConfigBuilder.setAuthenticationEnabled(true)

        return requestConfigBuilder.build();
    }

    //---------------------------------------------------------------

    /**
     * 设置超时时间.
     *
     * @param requestConfigBuilder
     *            the builder
     * @param useConnectionConfig
     *            the use connection config
     * @see <a href="http://blog.csdn.net/senblingbling/article/details/43916851">httpclient 超时时间 等待时间 响应时间</a>
     */
    private static void setTimeout(Builder requestConfigBuilder,ConnectionConfig useConnectionConfig){
        //设置从connect Manager获取Connection 超时时间，单位毫秒
        //从连接池中获取连接的超时时间

        //    Caused by: org.apache.http.conn.ConnectionPoolTimeoutException: Timeout waiting for connection from pool
        //        at org.apache.http.impl.conn.PoolingHttpClientConnectionManager.leaseConnection(PoolingHttpClientConnectionManager.java:314)
        requestConfigBuilder.setConnectionRequestTimeout(1 * MILLISECOND_PER_SECONDS);

        //---------------------------------------------------------------
        //与服务器连接超时时间
        //httpclient会创建一个异步线程用以创建socket连接，此处设置该socket的连接超时时间
        //单位毫秒
        requestConfigBuilder.setConnectTimeout(useConnectionConfig.getConnectTimeout());

        //请求获取数据的超时时间，单位毫秒
        //socket读数据超时时间：从服务器获取响应数据的超时时间
        //访问一个接口，多少时间内无法返回数据，就直接放弃此次调用
        requestConfigBuilder.setSocketTimeout(useConnectionConfig.getReadTimeout());
    }

    //---------------------------------------------------------------

    /**
     * 设置代理.
     *
     * @param requestConfigBuilder
     *            the builder
     * @param connectionConfig
     *            the connection config
     */
    private static void setProxy(Builder requestConfigBuilder,ConnectionConfig connectionConfig){
        String proxyAddress = connectionConfig.getProxyAddress();
        Integer proxyPort = connectionConfig.getProxyPort();

        boolean isNeedProxy = isNotNullOrEmpty(proxyAddress) && isNotNullOrEmpty(proxyPort);
        if (isNeedProxy){
            HttpHost proxy = new HttpHost(proxyAddress, proxyPort);
            requestConfigBuilder.setProxy(proxy);
        }
    }
}
