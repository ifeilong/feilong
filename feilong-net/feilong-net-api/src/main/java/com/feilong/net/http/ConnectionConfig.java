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
package com.feilong.net.http;

import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.core.TimeInterval.MILLISECOND_PER_SECONDS;

import javax.net.ssl.HostnameVerifier;

import com.feilong.core.CharsetType;
import com.feilong.core.TimeInterval;
import com.feilong.lib.lang3.builder.EqualsBuilder;
import com.feilong.lib.lang3.builder.HashCodeBuilder;

/**
 * 连接参数配置,比如超时时间,代理等等.
 * 
 * <h3>说明:</h3>
 * <blockquote>
 * <ol>
 * <li>对于httpclient4, 专门有2个参数 {@link #setMaxConnPerRoute(int)} 针对一个域名,同时正在使用的最多的连接数 和 {@link #setMaxConnTotal(int)} 同时间正在用的最多连接数</li>
 * <li>默认提供了2个重要的超时参数, 20s的连接超时,你可以通过 {@link #setConnectTimeout(int)} 来修改,20s的读取超时,你可以通过 {@link #setReadTimeout(int)} 来修改</li>
 * <li>默认编码字符集是 {@link CharsetType#UTF8},你也可以通过设置 {@link #setContentCharset(String)} 来修改</li>
 * <li>默认是关闭{@link HostnameVerifier}校验的,如果你有需要,你可以通过设置 {@link #setTurnOffHostnameVerifier(boolean)} 来修改</li>
 * <li>如果你的系统访问外部系统需要代理, 可以设置 {@link #setProxyAddress(String)}和 {@link #setProxyPort(Integer)}</li>
 * <li>如果你的系统访问外部系统需要basic权限认证, 可以设置 {@link #setUserName(String)}和 {@link #setPassword(String)}</li>
 * </ol>
 * </blockquote>
 * 
 * <h3>关于 {@link #setMaxConnPerRoute(int)},{@link #setMaxConnTotal(int)}</h3>
 * <blockquote>
 * 
 * <dl>
 * <dt>{@link <a href=
 * "https://github.com/funaiy/captcha-java-demo/blob/master/src/main/java/com/netease/is/nc/sdk/utils/HttpClient4Utils.java">网易易盾</a>}</dt>
 * <dd>默认是 20,20</dd>
 * 
 * <dt>{@link <a href="org.springframework.remoting.httpinvoker.HttpComponentsHttpInvokerRequestExecutor">spring</a>}</dt>
 * <dd>默认是 5,10</dd>
 * 
 * <dt>{@link <a href="org.apache.solr.client.solrj.impl.HttpClientUtil#createClient">solr7</a>}</dt>
 * <dd>10000,10000</dd>
 * 
 * <dt>{@link <a href="com.aliyun.oss.common.comm.DefaultServiceClient.createHttpClientConnectionManager()">ali oss</a>}</dt>
 * <dd>1024,1024</dd>
 * </dl>
 * 
 * </blockquote>
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.3.0
 */
public final class ConnectionConfig{

    /** Static instance. */
    // the static instance works for all types
    public static final ConnectionConfig INSTANCE                = new ConnectionConfig();

    //---------------------------------------------------------------

    /**
     * 打开到 URLConnection资源的通信连接时超时值 (<span style="color:red">以毫秒为单位</span>).
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>默认 20秒</li>
     * <li>0 表示无穷大超时.</li>
     * <li>如果在建立连接之前超时期满,会引发 {@link java.net.SocketTimeoutException}.</li>
     * </ol>
     * </blockquote>
     * 
     * @see TimeInterval#MILLISECOND_PER_SECONDS
     */
    private int                          connectTimeout          = 20 * MILLISECOND_PER_SECONDS;

    /**
     * 读超时时间,在建立连接后从 Input流读入时的超时时间,<span style="color:red">以毫秒为单位</span>.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>默认 20秒</li>
     * <li>0 表示无穷大超时.</li>
     * <li>如果在数据可读取之前超时期满,则会引发一个 {@link java.net.SocketTimeoutException}.</li>
     * </ol>
     * </blockquote>
     * 
     * @see TimeInterval#MILLISECOND_PER_SECONDS
     */
    private int                          readTimeout             = 20 * MILLISECOND_PER_SECONDS;

    //---------------------------------------------------------------

    /**
     * 针对一个域名,同时正在使用的最多的连接数.
     * 
     * <p>
     * httpclient4,系统默认是2, feilong 设置为200
     * </p>
     *
     * @see "org.springframework.remoting.httpinvoker.HttpComponentsHttpInvokerRequestExecutor"
     * @see "org.apache.solr.client.solrj.impl.HttpClientUtil.createClient(SolrParams, PoolingHttpClientConnectionManager, boolean, HttpRequestExecutor)"
     * @since 2.1.0
     */
    private int                          maxConnPerRoute         = 200;

    /**
     * 同时间正在用的最多连接数.
     * 
     * <p>
     * httpclient4 ,系统默认是20, feilong 设置为1000
     * </p>
     *
     * @see "org.springframework.remoting.httpinvoker.HttpComponentsHttpInvokerRequestExecutor"
     * @see "org.apache.solr.client.solrj.impl.HttpClientUtil.createClient(SolrParams, PoolingHttpClientConnectionManager, boolean, HttpRequestExecutor)"
     * @since 2.1.0
     */
    private int                          maxConnTotal            = 1000;

    //---------------------------------------------------------------

    /** 内容的字符集. */
    private String                       contentCharset          = UTF8;

    //---------------------------------------------------------------

    /**
     * Http Basic Authentication 基本认证的用户名.
     * 
     * <p>
     * 暂时不支持 微软的windows系统使用的一种凭据
     * </p>
     * 
     * <p>
     * 如果值是null或者blank,将会自动忽略
     * </p>
     * 
     * @since 1.10.6
     */
    private String                       userName;

    /**
     * Http Basic Authentication 基本认证的密码.
     * <p>
     * 暂时不支持 微软的windows系统使用的一种凭据
     * </p>
     * <p>
     * 如果值是null或者blank,将会自动忽略
     * </p>
     * 
     * @since 1.10.6
     */
    private String                       password;

    //---------------------------------------------------------------

    /** 代理地址. */
    private String                       proxyAddress;

    /**
     * 代理端口.
     * <p>
     * A valid port value is between 0 ~ 65535. <br>
     * A port number of zero will let the system pick up an ephemeral port in a bind operation.
     * </p>
     */
    private Integer                      proxyPort;

    //---------------------------------------------------------------

    /**
     * 是否关闭 HostnameVerifier essentially turns hostname 校验.
     * 
     * <p>
     * 默认 true 表示关闭
     * </p>
     * 
     * @see "org.apache.http.conn.ssl.NoopHostnameVerifier"
     * @since 2.0.0
     */
    private boolean                      turnOffHostnameVerifier = true;

    //---------------------------------------------------------------
    /**
     * Instantiates a new connection config.
     * 
     * @since 1.11.0
     */
    public ConnectionConfig(){
        super();
    }

    /**
     * Instantiates a new connection config.
     *
     * @param userName
     *            Http Basic Authentication 基本认证用户名
     * @param password
     *            Http Basic Authentication 基本认证密码
     * @since 1.11.0
     */
    public ConnectionConfig(String userName, String password){
        super();
        this.userName = userName;
        this.password = password;
    }

    /**
     * 打开到 URLConnection资源的通信连接时超时值 (<span style="color:red">以毫秒为单位</span>).
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>默认 20秒</li>
     * <li>0 表示无穷大超时.</li>
     * <li>如果在建立连接之前超时期满,会引发 {@link java.net.SocketTimeoutException}.</li>
     * </ol>
     * </blockquote>
     *
     * @param connectTimeout
     *            the connect timeout
     * @see TimeInterval#MILLISECOND_PER_SECONDS
     * @since 2.1.0
     */
    public ConnectionConfig(int connectTimeout){
        super();
        this.connectTimeout = connectTimeout;
    }

    /**
     * Instantiates a new connection config.
     *
     * @param maxConnPerRoute
     *            针对一个域名,同时正在使用的最多的连接数.
     * 
     *            <p>
     *            httpclient4,系统默认是2, feilong 设置为200
     *            </p>
     * @param maxConnTotal
     *            同时间正在用的最多连接数.
     * 
     *            <p>
     *            httpclient4 ,系统默认是20, feilong 设置为1000
     *            </p>
     * @since 2.1.0
     */
    public ConnectionConfig(int maxConnPerRoute, int maxConnTotal){
        super();
        this.maxConnPerRoute = maxConnPerRoute;
        this.maxConnTotal = maxConnTotal;
    }

    /**
     * Instantiates a new connection config.
     *
     * @param turnOffHostnameVerifier
     *            the turn off hostname verifier
     * @since 2.0.0
     */
    public ConnectionConfig(boolean turnOffHostnameVerifier){
        super();
        this.turnOffHostnameVerifier = turnOffHostnameVerifier;
    }
    //---------------------------------------------------------------

    /**
     * 打开到 URLConnection资源的通信连接时超时值 (<span style="color:red">以毫秒为单位</span>).
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>默认 20秒</li>
     * <li>0 表示无穷大超时.</li>
     * <li>如果在建立连接之前超时期满,会引发 {@link java.net.SocketTimeoutException}.</li>
     * </ol>
     * </blockquote>
     *
     * @return the 打开到 URLConnection资源的通信连接时超时值 (<span style="color:red">以毫秒为单位</span>)
     * @see TimeInterval#MILLISECOND_PER_SECONDS
     */
    public int getConnectTimeout(){
        return connectTimeout;
    }

    /**
     * 打开到 URLConnection资源的通信连接时超时值 (<span style="color:red">以毫秒为单位</span>).
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>默认 20秒</li>
     * <li>0 表示无穷大超时.</li>
     * <li>如果在建立连接之前超时期满,会引发 {@link java.net.SocketTimeoutException}.</li>
     * </ol>
     * </blockquote>
     *
     * @param connectTimeout
     *            the new 打开到 URLConnection资源的通信连接时超时值 (<span style="color:red">以毫秒为单位</span>)
     * @see TimeInterval#MILLISECOND_PER_SECONDS
     */
    public void setConnectTimeout(int connectTimeout){
        this.connectTimeout = connectTimeout;
    }

    /**
     * 读超时时间,在建立连接后从 Input流读入时的超时时间,<span style="color:red">以毫秒为单位</span>.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>默认 20秒</li>
     * <li>0 表示无穷大超时.</li>
     * <li>如果在数据可读取之前超时期满,则会引发一个 {@link java.net.SocketTimeoutException}.</li>
     * </ol>
     * </blockquote>
     *
     * @return the 读超时时间,在建立连接后从 Input流读入时的超时时间,<span style="color:red">以毫秒为单位</span>
     * @see TimeInterval#MILLISECOND_PER_SECONDS
     */
    public int getReadTimeout(){
        return readTimeout;
    }

    /**
     * 读超时时间,在建立连接后从 Input流读入时的超时时间,<span style="color:red">以毫秒为单位</span>.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>默认 20秒</li>
     * <li>0 表示无穷大超时.</li>
     * <li>如果在数据可读取之前超时期满,则会引发一个 {@link java.net.SocketTimeoutException}.</li>
     * </ol>
     * </blockquote>
     *
     * @param readTimeout
     *            the new 读超时时间,在建立连接后从 Input流读入时的超时时间,<span style="color:red">以毫秒为单位</span>
     * @see TimeInterval#MILLISECOND_PER_SECONDS
     */
    public void setReadTimeout(int readTimeout){
        this.readTimeout = readTimeout;
    }

    /**
     * 获得 内容的字符集.
     *
     * @return the contentCharset
     */
    public String getContentCharset(){
        return contentCharset;
    }

    /**
     * 设置 内容的字符集.
     *
     * @param contentCharset
     *            the contentCharset to set
     */
    public void setContentCharset(String contentCharset){
        this.contentCharset = contentCharset;
    }

    /**
     * 获得 the proxy address.
     *
     * @return the proxyAddress
     */
    public String getProxyAddress(){
        return proxyAddress;
    }

    /**
     * 设置 the proxy address.
     *
     * @param proxyAddress
     *            the proxyAddress to set
     */
    public void setProxyAddress(String proxyAddress){
        this.proxyAddress = proxyAddress;
    }

    /**
     * 代理端口.
     * <p>
     * A valid port value is between 0 ~ 65535. <br>
     * A port number of zero will let the system pick up an ephemeral port in a bind operation.
     * </p>
     * 
     * @return the proxyPort
     */
    public Integer getProxyPort(){
        return proxyPort;
    }

    /**
     * 代理端口.
     * <p>
     * A valid port value is between 0 ~ 65535. <br>
     * A port number of zero will let the system pick up an ephemeral port in a bind operation.
     * </p>
     *
     * @param proxyPort
     *            the proxyPort to set
     */
    public void setProxyPort(Integer proxyPort){
        this.proxyPort = proxyPort;
    }

    /**
     * Http Basic Authentication 基本认证的用户名.
     * 
     * <p>
     * 暂时不支持 微软的windows系统使用的一种凭据
     * </p>
     * <p>
     * 如果值是null或者blank,将会自动忽略
     * </p>
     * 
     * @return the userName
     * @since 1.10.6
     */
    public String getUserName(){
        return userName;
    }

    /**
     * Http Basic Authentication 基本认证的用户名.
     * 
     * <p>
     * 暂时不支持 微软的windows系统使用的一种凭据
     * </p>
     * <p>
     * 如果值是null或者blank,将会自动忽略
     * </p>
     * 
     * @param userName
     *            the userName to set
     * @since 1.10.6
     */
    public void setUserName(String userName){
        this.userName = userName;
    }

    /**
     * Http Basic Authentication 基本认证的密码.
     * <p>
     * 暂时不支持 微软的windows系统使用的一种凭据
     * </p>
     * <p>
     * 如果值是null或者blank,将会自动忽略
     * </p>
     * 
     * @return the password
     * @since 1.10.6
     */
    public String getPassword(){
        return password;
    }

    /**
     * Http Basic Authentication 基本认证的密码.
     * <p>
     * 暂时不支持 微软的windows系统使用的一种凭据
     * </p>
     * <p>
     * 如果值是null或者blank,将会自动忽略
     * </p>
     * 
     * @param password
     *            the password to set
     * @since 1.10.6
     */
    public void setPassword(String password){
        this.password = password;
    }

    //---------------------------------------------------------------

    /**
     * 是否关闭 HostnameVerifier essentially turns hostname 校验.
     * 
     * <p>
     * 默认 true 表示关闭
     * </p>
     *
     * @return the turnOffHostnameVerifier
     * @see "org.apache.http.conn.ssl.NoopHostnameVerifier"
     * @since 2.0.0
     */
    public boolean getTurnOffHostnameVerifier(){
        return turnOffHostnameVerifier;
    }

    /**
     * 是否关闭 HostnameVerifier essentially turns hostname 校验.
     * 
     * <p>
     * 默认 true 表示关闭
     * </p>
     *
     * @param turnOffHostnameVerifier
     *            the turnOffHostnameVerifier to set
     * @see "org.apache.http.conn.ssl.NoopHostnameVerifier"
     * @since 2.0.0
     */
    public void setTurnOffHostnameVerifier(boolean turnOffHostnameVerifier){
        this.turnOffHostnameVerifier = turnOffHostnameVerifier;
    }

    //---------------------------------------------------------------

    /**
     * 针对一个域名,同时正在使用的最多的连接数.
     * 
     * <p>
     * httpclient4,系统默认是2, feilong 设置为200
     * </p>
     * 
     * @return the maxConnPerRoute
     * @since 2.1.0
     */
    public int getMaxConnPerRoute(){
        return maxConnPerRoute;
    }

    /**
     * 针对一个域名,同时正在使用的最多的连接数.
     * 
     * <p>
     * httpclient4,系统默认是2, feilong 设置为200
     * </p>
     * 
     * @param maxConnPerRoute
     *            the maxConnPerRoute to set
     * @since 2.1.0
     */
    public void setMaxConnPerRoute(int maxConnPerRoute){
        this.maxConnPerRoute = maxConnPerRoute;
    }

    /**
     * 同时间正在用的最多连接数.
     * 
     * <p>
     * httpclient4 ,系统默认是20, feilong 设置为1000
     * </p>
     * 
     * @return the maxConnTotal
     * @since 2.1.0
     */
    public int getMaxConnTotal(){
        return maxConnTotal;
    }

    /**
     * 同时间正在用的最多连接数.
     * 
     * <p>
     * httpclient4 ,系统默认是20, feilong 设置为1000
     * </p>
     * 
     * @param maxConnTotal
     *            the maxConnTotal to set
     * @since 2.1.0
     */
    public void setMaxConnTotal(int maxConnTotal){
        this.maxConnTotal = maxConnTotal;
    }

    //---------------------------------------------------------------
    /**
     * Equals.
     *
     * @param obj
     *            the obj
     * @return true, if successful
     */
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj){
        if (obj == null){
            return false;
        }
        if (obj == this){
            return true;
        }
        if (obj.getClass() != getClass()){
            return false;
        }

        //---------------------------------------------------------------

        ConnectionConfig t = (ConnectionConfig) obj;
        EqualsBuilder equalsBuilder = new EqualsBuilder();

        return equalsBuilder //
                        .append(this.connectTimeout, t.getConnectTimeout())//
                        .append(this.readTimeout, t.getReadTimeout())//

                        .append(this.turnOffHostnameVerifier, t.getTurnOffHostnameVerifier())//
                        .append(this.contentCharset, t.getContentCharset())//

                        .append(this.userName, t.getUserName())//
                        .append(this.password, t.getPassword())//

                        .append(this.proxyAddress, t.getProxyAddress())//
                        .append(this.proxyPort, t.getProxyPort())//

                        .append(this.maxConnPerRoute, t.getMaxConnPerRoute())//
                        .append(this.maxConnTotal, t.getMaxConnTotal())//
                        .isEquals();
    }

    /**
     * Hash code.
     *
     * @return the int
     */
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode(){
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(3, 5);
        return hashCodeBuilder//
                        .append(connectTimeout)//
                        .append(readTimeout)//
                        .append(turnOffHostnameVerifier)//
                        .append(contentCharset)//

                        .append(userName)//
                        .append(password)//

                        .append(proxyAddress)//
                        .append(proxyPort)//

                        .append(maxConnPerRoute)//
                        .append(maxConnTotal)//
                        .toHashCode();

    }
}
