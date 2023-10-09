package com.feilong.lib.org.apache.http.impl.client;

import java.io.Closeable;
import java.net.ProxySelector;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import com.feilong.lib.org.apache.http.ConnectionReuseStrategy;
import com.feilong.lib.org.apache.http.Header;
import com.feilong.lib.org.apache.http.HttpHost;
import com.feilong.lib.org.apache.http.HttpRequestInterceptor;
import com.feilong.lib.org.apache.http.HttpResponseInterceptor;
import com.feilong.lib.org.apache.http.auth.AuthSchemeProvider;
import com.feilong.lib.org.apache.http.client.AuthenticationStrategy;
import com.feilong.lib.org.apache.http.client.BackoffManager;
import com.feilong.lib.org.apache.http.client.ConnectionBackoffStrategy;
import com.feilong.lib.org.apache.http.client.CookieStore;
import com.feilong.lib.org.apache.http.client.CredentialsProvider;
import com.feilong.lib.org.apache.http.client.HttpRequestRetryHandler;
import com.feilong.lib.org.apache.http.client.RedirectStrategy;
import com.feilong.lib.org.apache.http.client.ServiceUnavailableRetryStrategy;
import com.feilong.lib.org.apache.http.client.UserTokenHandler;
import com.feilong.lib.org.apache.http.client.config.AuthSchemes;
import com.feilong.lib.org.apache.http.client.config.RequestConfig;
import com.feilong.lib.org.apache.http.client.entity.InputStreamFactory;
import com.feilong.lib.org.apache.http.client.protocol.RequestAcceptEncoding;
import com.feilong.lib.org.apache.http.client.protocol.RequestAddCookies;
import com.feilong.lib.org.apache.http.client.protocol.RequestAuthCache;
import com.feilong.lib.org.apache.http.client.protocol.RequestClientConnControl;
import com.feilong.lib.org.apache.http.client.protocol.RequestDefaultHeaders;
import com.feilong.lib.org.apache.http.client.protocol.RequestExpectContinue;
import com.feilong.lib.org.apache.http.client.protocol.ResponseContentEncoding;
import com.feilong.lib.org.apache.http.client.protocol.ResponseProcessCookies;
import com.feilong.lib.org.apache.http.config.ConnectionConfig;
import com.feilong.lib.org.apache.http.config.Lookup;
import com.feilong.lib.org.apache.http.config.RegistryBuilder;
import com.feilong.lib.org.apache.http.config.SocketConfig;
import com.feilong.lib.org.apache.http.conn.ConnectionKeepAliveStrategy;
import com.feilong.lib.org.apache.http.conn.DnsResolver;
import com.feilong.lib.org.apache.http.conn.HttpClientConnectionManager;
import com.feilong.lib.org.apache.http.conn.SchemePortResolver;
import com.feilong.lib.org.apache.http.conn.routing.HttpRoutePlanner;
import com.feilong.lib.org.apache.http.conn.socket.ConnectionSocketFactory;
import com.feilong.lib.org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import com.feilong.lib.org.apache.http.conn.socket.PlainConnectionSocketFactory;
import com.feilong.lib.org.apache.http.conn.ssl.DefaultHostnameVerifier;
import com.feilong.lib.org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import com.feilong.lib.org.apache.http.conn.ssl.X509HostnameVerifier;
import com.feilong.lib.org.apache.http.conn.util.PublicSuffixMatcher;
import com.feilong.lib.org.apache.http.conn.util.PublicSuffixMatcherLoader;
import com.feilong.lib.org.apache.http.cookie.CookieSpecProvider;
import com.feilong.lib.org.apache.http.impl.NoConnectionReuseStrategy;
import com.feilong.lib.org.apache.http.impl.auth.BasicSchemeFactory;
import com.feilong.lib.org.apache.http.impl.auth.DigestSchemeFactory;
import com.feilong.lib.org.apache.http.impl.auth.KerberosSchemeFactory;
import com.feilong.lib.org.apache.http.impl.auth.NTLMSchemeFactory;
import com.feilong.lib.org.apache.http.impl.auth.SPNegoSchemeFactory;
import com.feilong.lib.org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import com.feilong.lib.org.apache.http.impl.conn.DefaultRoutePlanner;
import com.feilong.lib.org.apache.http.impl.conn.DefaultSchemePortResolver;
import com.feilong.lib.org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import com.feilong.lib.org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import com.feilong.lib.org.apache.http.impl.execchain.BackoffStrategyExec;
import com.feilong.lib.org.apache.http.impl.execchain.ClientExecChain;
import com.feilong.lib.org.apache.http.impl.execchain.MainClientExec;
import com.feilong.lib.org.apache.http.impl.execchain.ProtocolExec;
import com.feilong.lib.org.apache.http.impl.execchain.RedirectExec;
import com.feilong.lib.org.apache.http.impl.execchain.RetryExec;
import com.feilong.lib.org.apache.http.impl.execchain.ServiceUnavailableRetryExec;
import com.feilong.lib.org.apache.http.protocol.HttpProcessor;
import com.feilong.lib.org.apache.http.protocol.HttpProcessorBuilder;
import com.feilong.lib.org.apache.http.protocol.HttpRequestExecutor;
import com.feilong.lib.org.apache.http.protocol.ImmutableHttpProcessor;
import com.feilong.lib.org.apache.http.protocol.RequestContent;
import com.feilong.lib.org.apache.http.protocol.RequestTargetHost;
import com.feilong.lib.org.apache.http.protocol.RequestUserAgent;
import com.feilong.lib.org.apache.http.ssl.SSLContexts;
import com.feilong.lib.org.apache.http.util.TextUtils;

/**
 * Builder for {@link CloseableHttpClient} instances.
 * <p>
 * When a particular component is not explicitly set this class will
 * use its default implementation. System properties will be taken
 * into account when configuring the default implementations when
 * {@link #useSystemProperties()} method is called prior to calling
 * {@link #build()}.
 * </p>
 * <ul>
 * <li>ssl.TrustManagerFactory.algorithm</li>
 * <li>javax.net.ssl.trustStoreType</li>
 * <li>javax.net.ssl.trustStore</li>
 * <li>javax.net.ssl.trustStoreProvider</li>
 * <li>javax.net.ssl.trustStorePassword</li>
 * <li>ssl.KeyManagerFactory.algorithm</li>
 * <li>javax.net.ssl.keyStoreType</li>
 * <li>javax.net.ssl.keyStore</li>
 * <li>javax.net.ssl.keyStoreProvider</li>
 * <li>javax.net.ssl.keyStorePassword</li>
 * <li>https.protocols</li>
 * <li>https.cipherSuites</li>
 * <li>http.proxyHost</li>
 * <li>http.proxyPort</li>
 * <li>https.proxyHost</li>
 * <li>https.proxyPort</li>
 * <li>http.nonProxyHosts</li>
 * <li>https.proxyUser</li>
 * <li>http.proxyUser</li>
 * <li>https.proxyPassword</li>
 * <li>http.proxyPassword</li>
 * <li>http.keepAlive</li>
 * <li>http.maxConnections</li>
 * <li>http.agent</li>
 * </ul>
 * <p>
 * Please note that some settings used by this class can be mutually
 * exclusive and may not apply when building {@link CloseableHttpClient}
 * instances.
 * </p>
 *
 * @since 4.3
 */
public class HttpClientBuilder{

    /**
     * 伪造的 useragent.
     * 
     * @since 1.5.0
     */
    public static final String                  DEFAULT_USER_AGENT     = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21";

    /** The request exec. */
    private HttpRequestExecutor                 requestExec;

    /** The hostname verifier. */
    private HostnameVerifier                    hostnameVerifier;

    /** The ssl socket factory. */
    private LayeredConnectionSocketFactory      sslSocketFactory;

    /** The ssl context. */
    private SSLContext                          sslContext;

    /** The conn manager. */
    private HttpClientConnectionManager         connManager;

    /** The conn manager shared. */
    private boolean                             connManagerShared;

    /** The scheme port resolver. */
    private SchemePortResolver                  schemePortResolver;

    /** The reuse strategy. */
    private ConnectionReuseStrategy             reuseStrategy;

    /** The keep alive strategy. */
    private ConnectionKeepAliveStrategy         keepAliveStrategy;

    /** The target auth strategy. */
    private AuthenticationStrategy              targetAuthStrategy;

    /** The proxy auth strategy. */
    private AuthenticationStrategy              proxyAuthStrategy;

    /** The user token handler. */
    private UserTokenHandler                    userTokenHandler;

    /** The httpprocessor. */
    private HttpProcessor                       httpprocessor;

    /** The dns resolver. */
    private DnsResolver                         dnsResolver;

    /** The request first. */
    private LinkedList<HttpRequestInterceptor>  requestFirst;

    /** The request last. */
    private LinkedList<HttpRequestInterceptor>  requestLast;

    /** The response first. */
    private LinkedList<HttpResponseInterceptor> responseFirst;

    /** The response last. */
    private LinkedList<HttpResponseInterceptor> responseLast;

    /** The retry handler. */
    private HttpRequestRetryHandler             retryHandler;

    /** The route planner. */
    private HttpRoutePlanner                    routePlanner;

    /** The redirect strategy. */
    private RedirectStrategy                    redirectStrategy;

    /** The connection backoff strategy. */
    private ConnectionBackoffStrategy           connectionBackoffStrategy;

    /** The backoff manager. */
    private BackoffManager                      backoffManager;

    /** The service unavail strategy. */
    private ServiceUnavailableRetryStrategy     serviceUnavailStrategy;

    /** The auth scheme registry. */
    private Lookup<AuthSchemeProvider>          authSchemeRegistry;

    /** The cookie spec registry. */
    private Lookup<CookieSpecProvider>          cookieSpecRegistry;

    /** The content decoder map. */
    private Map<String, InputStreamFactory>     contentDecoderMap;

    /** The cookie store. */
    private CookieStore                         cookieStore;

    /** The credentials provider. */
    private CredentialsProvider                 credentialsProvider;

    /** The user agent. */
    private String                              userAgent;

    /** The proxy. */
    private HttpHost                            proxy;

    /** The default headers. */
    private Collection<? extends Header>        defaultHeaders;

    /** The default socket config. */
    private SocketConfig                        defaultSocketConfig;

    /** The default connection config. */
    private ConnectionConfig                    defaultConnectionConfig;

    /** The default request config. */
    private RequestConfig                       defaultRequestConfig;

    /** The evict expired connections. */
    private boolean                             evictExpiredConnections;

    /** The evict idle connections. */
    private boolean                             evictIdleConnections;

    /** The max idle time. */
    private long                                maxIdleTime;

    /** The max idle time unit. */
    private TimeUnit                            maxIdleTimeUnit;

    /** The system properties. */
    private boolean                             systemProperties;

    /** The redirect handling disabled. */
    private boolean                             redirectHandlingDisabled;

    /** The automatic retries disabled. */
    private boolean                             automaticRetriesDisabled;

    /** The content compression disabled. */
    private boolean                             contentCompressionDisabled;

    /** The cookie management disabled. */
    private boolean                             cookieManagementDisabled;

    /** The auth caching disabled. */
    private boolean                             authCachingDisabled;

    /** The connection state disabled. */
    private boolean                             connectionStateDisabled;

    /** The default user agent disabled. */
    private boolean                             defaultUserAgentDisabled;

    /** The max conn total. */
    private int                                 maxConnTotal           = 0;

    /** The max conn per route. */
    private int                                 maxConnPerRoute        = 0;

    /** The conn time to live. */
    private long                                connTimeToLive         = -1;

    /** The conn time to live time unit. */
    private TimeUnit                            connTimeToLiveTimeUnit = TimeUnit.MILLISECONDS;

    /** The closeables. */
    private List<Closeable>                     closeables;

    /** The public suffix matcher. */
    private PublicSuffixMatcher                 publicSuffixMatcher;

    /**
     * 创建.
     *
     * @return the http client builder
     */
    public static HttpClientBuilder create(){
        return new HttpClientBuilder();
    }

    /**
     * Instantiates a new http client builder.
     */
    protected HttpClientBuilder(){
        super();
    }

    /**
     * Assigns {@link HttpRequestExecutor} instance.
     *
     * @param requestExec
     *            the request exec
     * @return the http client builder
     */
    public final HttpClientBuilder setRequestExecutor(final HttpRequestExecutor requestExec){
        this.requestExec = requestExec;
        return this;
    }

    /**
     * Assigns {@link X509HostnameVerifier} instance.
     * <p>
     * Please note this value can be overridden by the {@link #setConnectionManager(
     * com.feilong.lib.org.apache.http.conn.HttpClientConnectionManager)} and the {@link #setSSLSocketFactory(
     * com.feilong.lib.org.apache.http.conn.socket.LayeredConnectionSocketFactory)} methods.
     * </p>
     *
     * @param hostnameVerifier
     *            the hostname verifier
     * @return the http client builder
     * @deprecated (4.4)
     */
    @Deprecated
    public final HttpClientBuilder setHostnameVerifier(final X509HostnameVerifier hostnameVerifier){
        this.hostnameVerifier = hostnameVerifier;
        return this;
    }

    /**
     * Assigns {@link javax.net.ssl.HostnameVerifier} instance.
     * <p>
     * Please note this value can be overridden by the {@link #setConnectionManager(
     * com.feilong.lib.org.apache.http.conn.HttpClientConnectionManager)} and the {@link #setSSLSocketFactory(
     * com.feilong.lib.org.apache.http.conn.socket.LayeredConnectionSocketFactory)} methods.
     * </p>
     *
     * @param hostnameVerifier
     *            the hostname verifier
     * @return the http client builder
     * @since 4.4
     */
    public final HttpClientBuilder setSSLHostnameVerifier(final HostnameVerifier hostnameVerifier){
        this.hostnameVerifier = hostnameVerifier;
        return this;
    }

    /**
     * Assigns file containing public suffix matcher. Instances of this class can be created
     * with {@link com.feilong.lib.org.apache.http.conn.util.PublicSuffixMatcherLoader}.
     *
     * @param publicSuffixMatcher
     *            the public suffix matcher
     * @return the http client builder
     * @see com.feilong.lib.org.apache.http.conn.util.PublicSuffixMatcher
     * @see com.feilong.lib.org.apache.http.conn.util.PublicSuffixMatcherLoader
     * @since 4.4
     */
    public final HttpClientBuilder setPublicSuffixMatcher(final PublicSuffixMatcher publicSuffixMatcher){
        this.publicSuffixMatcher = publicSuffixMatcher;
        return this;
    }

    /**
     * Assigns {@link SSLContext} instance.
     * <p>
     * Please note this value can be overridden by the {@link #setConnectionManager(
     * com.feilong.lib.org.apache.http.conn.HttpClientConnectionManager)} and the {@link #setSSLSocketFactory(
     * com.feilong.lib.org.apache.http.conn.socket.LayeredConnectionSocketFactory)} methods.
     * </p>
     *
     * @param sslcontext
     *            the sslcontext
     * @return the http client builder
     * @deprecated (4.5) use {@link #setSSLContext(SSLContext)}
     */
    @Deprecated
    public final HttpClientBuilder setSslcontext(final SSLContext sslcontext){
        return setSSLContext(sslcontext);
    }

    /**
     * Assigns {@link SSLContext} instance.
     * <p>
     * Please note this value can be overridden by the {@link #setConnectionManager(
     * com.feilong.lib.org.apache.http.conn.HttpClientConnectionManager)} and the {@link #setSSLSocketFactory(
     * com.feilong.lib.org.apache.http.conn.socket.LayeredConnectionSocketFactory)} methods.
     * </p>
     *
     * @param sslContext
     *            the ssl context
     * @return the http client builder
     */
    public final HttpClientBuilder setSSLContext(final SSLContext sslContext){
        this.sslContext = sslContext;
        return this;
    }

    /**
     * Assigns {@link LayeredConnectionSocketFactory} instance.
     * <p>
     * Please note this value can be overridden by the {@link #setConnectionManager(
     * com.feilong.lib.org.apache.http.conn.HttpClientConnectionManager)} method.
     * </p>
     *
     * @param sslSocketFactory
     *            the ssl socket factory
     * @return the http client builder
     */
    public final HttpClientBuilder setSSLSocketFactory(final LayeredConnectionSocketFactory sslSocketFactory){
        this.sslSocketFactory = sslSocketFactory;
        return this;
    }

    /**
     * Assigns maximum total connection value.
     * <p>
     * Please note this value can be overridden by the {@link #setConnectionManager(
     * com.feilong.lib.org.apache.http.conn.HttpClientConnectionManager)} method.
     * </p>
     *
     * @param maxConnTotal
     *            the max conn total
     * @return the http client builder
     */
    public final HttpClientBuilder setMaxConnTotal(final int maxConnTotal){
        this.maxConnTotal = maxConnTotal;
        return this;
    }

    /**
     * Assigns maximum connection per route value.
     * <p>
     * Please note this value can be overridden by the {@link #setConnectionManager(
     * com.feilong.lib.org.apache.http.conn.HttpClientConnectionManager)} method.
     * </p>
     *
     * @param maxConnPerRoute
     *            the max conn per route
     * @return the http client builder
     */
    public final HttpClientBuilder setMaxConnPerRoute(final int maxConnPerRoute){
        this.maxConnPerRoute = maxConnPerRoute;
        return this;
    }

    /**
     * Assigns default {@link SocketConfig}.
     * <p>
     * Please note this value can be overridden by the {@link #setConnectionManager(
     * com.feilong.lib.org.apache.http.conn.HttpClientConnectionManager)} method.
     * </p>
     *
     * @param config
     *            the config
     * @return the http client builder
     */
    public final HttpClientBuilder setDefaultSocketConfig(final SocketConfig config){
        this.defaultSocketConfig = config;
        return this;
    }

    /**
     * Assigns default {@link ConnectionConfig}.
     * <p>
     * Please note this value can be overridden by the {@link #setConnectionManager(
     * com.feilong.lib.org.apache.http.conn.HttpClientConnectionManager)} method.
     * </p>
     *
     * @param config
     *            the config
     * @return the http client builder
     */
    public final HttpClientBuilder setDefaultConnectionConfig(final ConnectionConfig config){
        this.defaultConnectionConfig = config;
        return this;
    }

    /**
     * Sets maximum time to live for persistent connections
     * <p>
     * Please note this value can be overridden by the {@link #setConnectionManager(
     * com.feilong.lib.org.apache.http.conn.HttpClientConnectionManager)} method.
     * </p>
     *
     * @param connTimeToLive
     *            the conn time to live
     * @param connTimeToLiveTimeUnit
     *            the conn time to live time unit
     * @return the http client builder
     * @since 4.4
     */
    public final HttpClientBuilder setConnectionTimeToLive(final long connTimeToLive,final TimeUnit connTimeToLiveTimeUnit){
        this.connTimeToLive = connTimeToLive;
        this.connTimeToLiveTimeUnit = connTimeToLiveTimeUnit;
        return this;
    }

    /**
     * Assigns {@link HttpClientConnectionManager} instance.
     *
     * @param connManager
     *            the conn manager
     * @return the http client builder
     */
    public final HttpClientBuilder setConnectionManager(final HttpClientConnectionManager connManager){
        this.connManager = connManager;
        return this;
    }

    /**
     * Defines the connection manager is to be shared by multiple
     * client instances.
     * <p>
     * If the connection manager is shared its life-cycle is expected
     * to be managed by the caller and it will not be shut down
     * if the client is closed.
     * </p>
     *
     * @param shared
     *            defines whether or not the connection manager can be shared
     *            by multiple clients.
     * @return the http client builder
     * @since 4.4
     */
    public final HttpClientBuilder setConnectionManagerShared(final boolean shared){
        this.connManagerShared = shared;
        return this;
    }

    /**
     * Assigns {@link ConnectionReuseStrategy} instance.
     *
     * @param reuseStrategy
     *            the reuse strategy
     * @return the http client builder
     */
    public final HttpClientBuilder setConnectionReuseStrategy(final ConnectionReuseStrategy reuseStrategy){
        this.reuseStrategy = reuseStrategy;
        return this;
    }

    /**
     * Assigns {@link ConnectionKeepAliveStrategy} instance.
     *
     * @param keepAliveStrategy
     *            the keep alive strategy
     * @return the http client builder
     */
    public final HttpClientBuilder setKeepAliveStrategy(final ConnectionKeepAliveStrategy keepAliveStrategy){
        this.keepAliveStrategy = keepAliveStrategy;
        return this;
    }

    /**
     * Assigns {@link AuthenticationStrategy} instance for target
     * host authentication.
     *
     * @param targetAuthStrategy
     *            the target auth strategy
     * @return the http client builder
     */
    public final HttpClientBuilder setTargetAuthenticationStrategy(final AuthenticationStrategy targetAuthStrategy){
        this.targetAuthStrategy = targetAuthStrategy;
        return this;
    }

    /**
     * Assigns {@link AuthenticationStrategy} instance for proxy
     * authentication.
     *
     * @param proxyAuthStrategy
     *            the proxy auth strategy
     * @return the http client builder
     */
    public final HttpClientBuilder setProxyAuthenticationStrategy(final AuthenticationStrategy proxyAuthStrategy){
        this.proxyAuthStrategy = proxyAuthStrategy;
        return this;
    }

    /**
     * Assigns {@link UserTokenHandler} instance.
     * <p>
     * Please note this value can be overridden by the {@link #disableConnectionState()}
     * method.
     * </p>
     *
     * @param userTokenHandler
     *            the user token handler
     * @return the http client builder
     */
    public final HttpClientBuilder setUserTokenHandler(final UserTokenHandler userTokenHandler){
        this.userTokenHandler = userTokenHandler;
        return this;
    }

    /**
     * Disables connection state tracking.
     *
     * @return the http client builder
     */
    public final HttpClientBuilder disableConnectionState(){
        connectionStateDisabled = true;
        return this;
    }

    /**
     * Assigns {@link SchemePortResolver} instance.
     *
     * @param schemePortResolver
     *            the scheme port resolver
     * @return the http client builder
     */
    public final HttpClientBuilder setSchemePortResolver(final SchemePortResolver schemePortResolver){
        this.schemePortResolver = schemePortResolver;
        return this;
    }

    /**
     * Assigns {@code User-Agent} value.
     * <p>
     * Please note this value can be overridden by the {@link #setHttpProcessor(
     * com.feilong.lib.org.apache.http.protocol.HttpProcessor)} method.
     * </p>
     *
     * @param userAgent
     *            the user agent
     * @return the http client builder
     */
    public final HttpClientBuilder setUserAgent(final String userAgent){
        this.userAgent = userAgent;
        return this;
    }

    /**
     * Assigns default request header values.
     * <p>
     * Please note this value can be overridden by the {@link #setHttpProcessor(
     * com.feilong.lib.org.apache.http.protocol.HttpProcessor)} method.
     * </p>
     *
     * @param defaultHeaders
     *            the default headers
     * @return the http client builder
     */
    public final HttpClientBuilder setDefaultHeaders(final Collection<? extends Header> defaultHeaders){
        this.defaultHeaders = defaultHeaders;
        return this;
    }

    /**
     * Adds this protocol interceptor to the head of the protocol processing list.
     * <p>
     * Please note this value can be overridden by the {@link #setHttpProcessor(
     * com.feilong.lib.org.apache.http.protocol.HttpProcessor)} method.
     * </p>
     *
     * @param itcp
     *            the itcp
     * @return the http client builder
     */
    public final HttpClientBuilder addInterceptorFirst(final HttpResponseInterceptor itcp){
        if (itcp == null){
            return this;
        }
        if (responseFirst == null){
            responseFirst = new LinkedList<>();
        }
        responseFirst.addFirst(itcp);
        return this;
    }

    /**
     * Adds this protocol interceptor to the tail of the protocol processing list.
     * <p>
     * Please note this value can be overridden by the {@link #setHttpProcessor(
     * com.feilong.lib.org.apache.http.protocol.HttpProcessor)} method.
     * </p>
     *
     * @param itcp
     *            the itcp
     * @return the http client builder
     */
    public final HttpClientBuilder addInterceptorLast(final HttpResponseInterceptor itcp){
        if (itcp == null){
            return this;
        }
        if (responseLast == null){
            responseLast = new LinkedList<>();
        }
        responseLast.addLast(itcp);
        return this;
    }

    /**
     * Adds this protocol interceptor to the head of the protocol processing list.
     * <p>
     * Please note this value can be overridden by the {@link #setHttpProcessor(
     * com.feilong.lib.org.apache.http.protocol.HttpProcessor)} method.
     *
     * @param itcp
     *            the itcp
     * @return the http client builder
     */
    public final HttpClientBuilder addInterceptorFirst(final HttpRequestInterceptor itcp){
        if (itcp == null){
            return this;
        }
        if (requestFirst == null){
            requestFirst = new LinkedList<>();
        }
        requestFirst.addFirst(itcp);
        return this;
    }

    /**
     * Adds this protocol interceptor to the tail of the protocol processing list.
     * <p>
     * Please note this value can be overridden by the {@link #setHttpProcessor(
     * com.feilong.lib.org.apache.http.protocol.HttpProcessor)} method.
     *
     * @param itcp
     *            the itcp
     * @return the http client builder
     */
    public final HttpClientBuilder addInterceptorLast(final HttpRequestInterceptor itcp){
        if (itcp == null){
            return this;
        }
        if (requestLast == null){
            requestLast = new LinkedList<>();
        }
        requestLast.addLast(itcp);
        return this;
    }

    /**
     * Disables state (cookie) management.
     * <p>
     * Please note this value can be overridden by the {@link #setHttpProcessor(
     * com.feilong.lib.org.apache.http.protocol.HttpProcessor)} method.
     *
     * @return the http client builder
     */
    public final HttpClientBuilder disableCookieManagement(){
        this.cookieManagementDisabled = true;
        return this;
    }

    /**
     * Disables automatic content decompression.
     * <p>
     * Please note this value can be overridden by the {@link #setHttpProcessor(
     * com.feilong.lib.org.apache.http.protocol.HttpProcessor)} method.
     *
     * @return the http client builder
     */
    public final HttpClientBuilder disableContentCompression(){
        contentCompressionDisabled = true;
        return this;
    }

    /**
     * Disables authentication scheme caching.
     * <p>
     * Please note this value can be overridden by the {@link #setHttpProcessor(
     * com.feilong.lib.org.apache.http.protocol.HttpProcessor)} method.
     *
     * @return the http client builder
     */
    public final HttpClientBuilder disableAuthCaching(){
        this.authCachingDisabled = true;
        return this;
    }

    /**
     * Assigns {@link HttpProcessor} instance.
     *
     * @param httpprocessor
     *            the httpprocessor
     * @return the http client builder
     */
    public final HttpClientBuilder setHttpProcessor(final HttpProcessor httpprocessor){
        this.httpprocessor = httpprocessor;
        return this;
    }

    /**
     * Assigns {@link DnsResolver} instance.
     * <p>
     * Please note this value can be overridden by the {@link #setConnectionManager(HttpClientConnectionManager)} method.
     *
     * @param dnsResolver
     *            the dns resolver
     * @return the http client builder
     */
    public final HttpClientBuilder setDnsResolver(final DnsResolver dnsResolver){
        this.dnsResolver = dnsResolver;
        return this;
    }

    /**
     * Assigns {@link HttpRequestRetryHandler} instance.
     * <p>
     * Please note this value can be overridden by the {@link #disableAutomaticRetries()}
     * method.
     *
     * @param retryHandler
     *            the retry handler
     * @return the http client builder
     */
    public final HttpClientBuilder setRetryHandler(final HttpRequestRetryHandler retryHandler){
        this.retryHandler = retryHandler;
        return this;
    }

    /**
     * Disables automatic request recovery and re-execution.
     *
     * @return the http client builder
     */
    public final HttpClientBuilder disableAutomaticRetries(){
        automaticRetriesDisabled = true;
        return this;
    }

    /**
     * Assigns default proxy value.
     * <p>
     * Please note this value can be overridden by the {@link #setRoutePlanner(
     * com.feilong.lib.org.apache.http.conn.routing.HttpRoutePlanner)} method.
     *
     * @param proxy
     *            the proxy
     * @return the http client builder
     */
    public final HttpClientBuilder setProxy(final HttpHost proxy){
        this.proxy = proxy;
        return this;
    }

    /**
     * Assigns {@link HttpRoutePlanner} instance.
     *
     * @param routePlanner
     *            the route planner
     * @return the http client builder
     */
    public final HttpClientBuilder setRoutePlanner(final HttpRoutePlanner routePlanner){
        this.routePlanner = routePlanner;
        return this;
    }

    /**
     * Assigns {@link RedirectStrategy} instance.
     * <p>
     * Please note this value can be overridden by the {@link #disableRedirectHandling()}
     * method.
     * </p>
     * `
     *
     * @param redirectStrategy
     *            the redirect strategy
     * @return the http client builder
     */
    public final HttpClientBuilder setRedirectStrategy(final RedirectStrategy redirectStrategy){
        this.redirectStrategy = redirectStrategy;
        return this;
    }

    /**
     * Disables automatic redirect handling.
     *
     * @return the http client builder
     */
    public final HttpClientBuilder disableRedirectHandling(){
        redirectHandlingDisabled = true;
        return this;
    }

    /**
     * Assigns {@link ConnectionBackoffStrategy} instance.
     *
     * @param connectionBackoffStrategy
     *            the connection backoff strategy
     * @return the http client builder
     */
    public final HttpClientBuilder setConnectionBackoffStrategy(final ConnectionBackoffStrategy connectionBackoffStrategy){
        this.connectionBackoffStrategy = connectionBackoffStrategy;
        return this;
    }

    /**
     * Assigns {@link BackoffManager} instance.
     *
     * @param backoffManager
     *            the backoff manager
     * @return the http client builder
     */
    public final HttpClientBuilder setBackoffManager(final BackoffManager backoffManager){
        this.backoffManager = backoffManager;
        return this;
    }

    /**
     * Assigns {@link ServiceUnavailableRetryStrategy} instance.
     *
     * @param serviceUnavailStrategy
     *            the service unavail strategy
     * @return the http client builder
     */
    public final HttpClientBuilder setServiceUnavailableRetryStrategy(final ServiceUnavailableRetryStrategy serviceUnavailStrategy){
        this.serviceUnavailStrategy = serviceUnavailStrategy;
        return this;
    }

    /**
     * Assigns default {@link CookieStore} instance which will be used for
     * request execution if not explicitly set in the client execution context.
     *
     * @param cookieStore
     *            the cookie store
     * @return the http client builder
     */
    public final HttpClientBuilder setDefaultCookieStore(final CookieStore cookieStore){
        this.cookieStore = cookieStore;
        return this;
    }

    /**
     * Assigns default {@link CredentialsProvider} instance which will be used
     * for request execution if not explicitly set in the client execution
     * context.
     *
     * @param credentialsProvider
     *            the credentials provider
     * @return the http client builder
     */
    public final HttpClientBuilder setDefaultCredentialsProvider(final CredentialsProvider credentialsProvider){
        this.credentialsProvider = credentialsProvider;
        return this;
    }

    /**
     * Assigns default {@link com.feilong.lib.org.apache.http.auth.AuthScheme} registry which will
     * be used for request execution if not explicitly set in the client execution
     * context.
     *
     * @param authSchemeRegistry
     *            the auth scheme registry
     * @return the http client builder
     */
    public final HttpClientBuilder setDefaultAuthSchemeRegistry(final Lookup<AuthSchemeProvider> authSchemeRegistry){
        this.authSchemeRegistry = authSchemeRegistry;
        return this;
    }

    /**
     * Assigns default {@link com.feilong.lib.org.apache.http.cookie.CookieSpec} registry which will
     * be used for request execution if not explicitly set in the client execution
     * context.
     *
     * @param cookieSpecRegistry
     *            the cookie spec registry
     * @return the http client builder
     * @see com.feilong.lib.org.apache.http.impl.client.CookieSpecRegistries
     */
    public final HttpClientBuilder setDefaultCookieSpecRegistry(final Lookup<CookieSpecProvider> cookieSpecRegistry){
        this.cookieSpecRegistry = cookieSpecRegistry;
        return this;
    }

    /**
     * Assigns a map of {@link com.feilong.lib.org.apache.http.client.entity.InputStreamFactory}s
     * to be used for automatic content decompression.
     *
     * @param contentDecoderMap
     *            the content decoder map
     * @return the http client builder
     */
    public final HttpClientBuilder setContentDecoderRegistry(final Map<String, InputStreamFactory> contentDecoderMap){
        this.contentDecoderMap = contentDecoderMap;
        return this;
    }

    /**
     * Assigns default {@link RequestConfig} instance which will be used
     * for request execution if not explicitly set in the client execution
     * context.
     *
     * @param config
     *            the config
     * @return the http client builder
     */
    public final HttpClientBuilder setDefaultRequestConfig(final RequestConfig config){
        this.defaultRequestConfig = config;
        return this;
    }

    /**
     * Use system properties when creating and configuring default
     * implementations.
     *
     * @return the http client builder
     */
    public final HttpClientBuilder useSystemProperties(){
        this.systemProperties = true;
        return this;
    }

    /**
     * Makes this instance of HttpClient proactively evict expired connections from the
     * connection pool using a background thread.
     * <p>
     * One MUST explicitly close HttpClient with {@link CloseableHttpClient#close()} in order
     * to stop and release the background thread.
     * <p>
     * Please note this method has no effect if the instance of HttpClient is configuted to
     * use a shared connection manager.
     * <p>
     * Please note this method may not be used when the instance of HttpClient is created
     * inside an EJB container.
     *
     * @return the http client builder
     * @see #setConnectionManagerShared(boolean)
     * @see com.feilong.lib.org.apache.http.conn.HttpClientConnectionManager#closeExpiredConnections()
     * @since 4.4
     */
    public final HttpClientBuilder evictExpiredConnections(){
        evictExpiredConnections = true;
        return this;
    }

    /**
     * Makes this instance of HttpClient proactively evict idle connections from the
     * connection pool using a background thread.
     * <p>
     * One MUST explicitly close HttpClient with {@link CloseableHttpClient#close()} in order
     * to stop and release the background thread.
     * <p>
     * Please note this method has no effect if the instance of HttpClient is configuted to
     * use a shared connection manager.
     * <p>
     * Please note this method may not be used when the instance of HttpClient is created
     * inside an EJB container.
     *
     * @param maxIdleTime
     *            maximum time persistent connections can stay idle while kept alive
     *            in the connection pool. Connections whose inactivity period exceeds this value will
     *            get closed and evicted from the pool.
     * @param maxIdleTimeUnit
     *            time unit for the above parameter.
     * @return the http client builder
     * @see #setConnectionManagerShared(boolean)
     * @see com.feilong.lib.org.apache.http.conn.HttpClientConnectionManager#closeExpiredConnections()
     * @since 4.4
     * @deprecated (4.5) use {@link #evictIdleConnections(long, TimeUnit)}
     */
    @Deprecated
    public final HttpClientBuilder evictIdleConnections(final Long maxIdleTime,final TimeUnit maxIdleTimeUnit){
        return evictIdleConnections(maxIdleTime.longValue(), maxIdleTimeUnit);
    }

    /**
     * Makes this instance of HttpClient proactively evict idle connections from the
     * connection pool using a background thread.
     * <p>
     * One MUST explicitly close HttpClient with {@link CloseableHttpClient#close()} in order
     * to stop and release the background thread.
     * <p>
     * Please note this method has no effect if the instance of HttpClient is configuted to
     * use a shared connection manager.
     * <p>
     * Please note this method may not be used when the instance of HttpClient is created
     * inside an EJB container.
     *
     * @param maxIdleTime
     *            maximum time persistent connections can stay idle while kept alive
     *            in the connection pool. Connections whose inactivity period exceeds this value will
     *            get closed and evicted from the pool.
     * @param maxIdleTimeUnit
     *            time unit for the above parameter.
     * @return the http client builder
     * @see #setConnectionManagerShared(boolean)
     * @see com.feilong.lib.org.apache.http.conn.HttpClientConnectionManager#closeExpiredConnections()
     * @since 4.4
     */
    public final HttpClientBuilder evictIdleConnections(final long maxIdleTime,final TimeUnit maxIdleTimeUnit){
        this.evictIdleConnections = true;
        this.maxIdleTime = maxIdleTime;
        this.maxIdleTimeUnit = maxIdleTimeUnit;
        return this;
    }

    /**
     * Disables the default user agent set by this builder if none has been provided by the user.
     *
     * @return the http client builder
     * @since 4.5.7
     */
    public final HttpClientBuilder disableDefaultUserAgent(){
        this.defaultUserAgentDisabled = true;
        return this;
    }

    /**
     * Produces an instance of {@link ClientExecChain} to be used as a main exec.
     * <p>
     * Default implementation produces an instance of {@link MainClientExec}
     * </p>
     * <p>
     * For internal use.
     * </p>
     *
     * @param requestExec
     *            the request exec
     * @param connManager
     *            the conn manager
     * @param reuseStrategy
     *            the reuse strategy
     * @param keepAliveStrategy
     *            the keep alive strategy
     * @param proxyHttpProcessor
     *            the proxy http processor
     * @param targetAuthStrategy
     *            the target auth strategy
     * @param proxyAuthStrategy
     *            the proxy auth strategy
     * @param userTokenHandler
     *            the user token handler
     * @return the client exec chain
     * @since 4.4
     */
    protected ClientExecChain createMainExec(
                    final HttpRequestExecutor requestExec,
                    final HttpClientConnectionManager connManager,
                    final ConnectionReuseStrategy reuseStrategy,
                    final ConnectionKeepAliveStrategy keepAliveStrategy,
                    final HttpProcessor proxyHttpProcessor,
                    final AuthenticationStrategy targetAuthStrategy,
                    final AuthenticationStrategy proxyAuthStrategy,
                    final UserTokenHandler userTokenHandler){
        return new MainClientExec(
                        requestExec,
                        connManager,
                        reuseStrategy,
                        keepAliveStrategy,
                        proxyHttpProcessor,
                        targetAuthStrategy,
                        proxyAuthStrategy,
                        userTokenHandler);
    }

    /**
     * For internal use.
     *
     * @param mainExec
     *            the main exec
     * @return the client exec chain
     */
    protected ClientExecChain decorateMainExec(final ClientExecChain mainExec){
        return mainExec;
    }

    /**
     * For internal use.
     *
     * @param protocolExec
     *            the protocol exec
     * @return the client exec chain
     */
    protected ClientExecChain decorateProtocolExec(final ClientExecChain protocolExec){
        return protocolExec;
    }

    /**
     * For internal use.
     *
     * @param closeable
     *            the closeable
     */
    protected void addCloseable(final Closeable closeable){
        if (closeable == null){
            return;
        }
        if (closeables == null){
            closeables = new ArrayList<>();
        }
        closeables.add(closeable);
    }

    /**
     * Split.
     *
     * @param s
     *            the s
     * @return the string[]
     */
    private static String[] split(final String s){
        if (TextUtils.isBlank(s)){
            return null;
        }
        return s.split(" *, *");
    }

    /**
     * Builds the.
     *
     * @return the closeable http client
     */
    public CloseableHttpClient build(){
        // Create main request executor
        // We copy the instance fields to avoid changing them, and rename to avoid accidental use of the wrong version
        PublicSuffixMatcher publicSuffixMatcherCopy = this.publicSuffixMatcher;
        if (publicSuffixMatcherCopy == null){
            publicSuffixMatcherCopy = PublicSuffixMatcherLoader.getDefault();
        }

        HttpRequestExecutor requestExecCopy = this.requestExec;
        if (requestExecCopy == null){
            requestExecCopy = new HttpRequestExecutor();
        }
        HttpClientConnectionManager connManagerCopy = this.connManager;
        if (connManagerCopy == null){
            LayeredConnectionSocketFactory sslSocketFactoryCopy = this.sslSocketFactory;
            if (sslSocketFactoryCopy == null){
                final String[] supportedProtocols = systemProperties ? split(System.getProperty("https.protocols")) : null;
                final String[] supportedCipherSuites = systemProperties ? split(System.getProperty("https.cipherSuites")) : null;
                HostnameVerifier hostnameVerifierCopy = this.hostnameVerifier;
                if (hostnameVerifierCopy == null){
                    hostnameVerifierCopy = new DefaultHostnameVerifier(publicSuffixMatcherCopy);
                }
                if (sslContext != null){
                    sslSocketFactoryCopy = new SSLConnectionSocketFactory(
                                    sslContext,
                                    supportedProtocols,
                                    supportedCipherSuites,
                                    hostnameVerifierCopy);
                }else{
                    if (systemProperties){
                        sslSocketFactoryCopy = new SSLConnectionSocketFactory(
                                        (SSLSocketFactory) SSLSocketFactory.getDefault(),
                                        supportedProtocols,
                                        supportedCipherSuites,
                                        hostnameVerifierCopy);
                    }else{
                        sslSocketFactoryCopy = new SSLConnectionSocketFactory(SSLContexts.createDefault(), hostnameVerifierCopy);
                    }
                }
            }
            @SuppressWarnings("resource")
            final PoolingHttpClientConnectionManager poolingmgr = new PoolingHttpClientConnectionManager(
                            RegistryBuilder.<ConnectionSocketFactory> create()
                                            .register("http", PlainConnectionSocketFactory.getSocketFactory())
                                            .register("https", sslSocketFactoryCopy).build(),
                            null,
                            null,
                            dnsResolver,
                            connTimeToLive,
                            connTimeToLiveTimeUnit != null ? connTimeToLiveTimeUnit : TimeUnit.MILLISECONDS);
            if (defaultSocketConfig != null){
                poolingmgr.setDefaultSocketConfig(defaultSocketConfig);
            }
            if (defaultConnectionConfig != null){
                poolingmgr.setDefaultConnectionConfig(defaultConnectionConfig);
            }
            if (systemProperties){
                String s = System.getProperty("http.keepAlive", "true");
                if ("true".equalsIgnoreCase(s)){
                    s = System.getProperty("http.maxConnections", "5");
                    final int max = Integer.parseInt(s);
                    poolingmgr.setDefaultMaxPerRoute(max);
                    poolingmgr.setMaxTotal(2 * max);
                }
            }
            if (maxConnTotal > 0){
                poolingmgr.setMaxTotal(maxConnTotal);
            }
            if (maxConnPerRoute > 0){
                poolingmgr.setDefaultMaxPerRoute(maxConnPerRoute);
            }
            connManagerCopy = poolingmgr;
        }
        ConnectionReuseStrategy reuseStrategyCopy = this.reuseStrategy;
        if (reuseStrategyCopy == null){
            if (systemProperties){
                final String s = System.getProperty("http.keepAlive", "true");
                if ("true".equalsIgnoreCase(s)){
                    reuseStrategyCopy = DefaultClientConnectionReuseStrategy.INSTANCE;
                }else{
                    reuseStrategyCopy = NoConnectionReuseStrategy.INSTANCE;
                }
            }else{
                reuseStrategyCopy = DefaultClientConnectionReuseStrategy.INSTANCE;
            }
        }
        ConnectionKeepAliveStrategy keepAliveStrategyCopy = this.keepAliveStrategy;
        if (keepAliveStrategyCopy == null){
            keepAliveStrategyCopy = DefaultConnectionKeepAliveStrategy.INSTANCE;
        }
        AuthenticationStrategy targetAuthStrategyCopy = this.targetAuthStrategy;
        if (targetAuthStrategyCopy == null){
            targetAuthStrategyCopy = TargetAuthenticationStrategy.INSTANCE;
        }
        AuthenticationStrategy proxyAuthStrategyCopy = this.proxyAuthStrategy;
        if (proxyAuthStrategyCopy == null){
            proxyAuthStrategyCopy = ProxyAuthenticationStrategy.INSTANCE;
        }
        UserTokenHandler userTokenHandlerCopy = this.userTokenHandler;
        if (userTokenHandlerCopy == null){
            if (!connectionStateDisabled){
                userTokenHandlerCopy = DefaultUserTokenHandler.INSTANCE;
            }else{
                userTokenHandlerCopy = NoopUserTokenHandler.INSTANCE;
            }
        }

        String userAgentCopy = this.userAgent;
        if (userAgentCopy == null){
            if (systemProperties){
                userAgentCopy = System.getProperty("http.agent");
            }
            if (userAgentCopy == null && !defaultUserAgentDisabled){
                userAgentCopy = DEFAULT_USER_AGENT;
            }
        }

        ClientExecChain execChain = createMainExec(
                        requestExecCopy,
                        connManagerCopy,
                        reuseStrategyCopy,
                        keepAliveStrategyCopy,
                        new ImmutableHttpProcessor(new RequestTargetHost(), new RequestUserAgent(userAgentCopy)),
                        targetAuthStrategyCopy,
                        proxyAuthStrategyCopy,
                        userTokenHandlerCopy);

        execChain = decorateMainExec(execChain);

        HttpProcessor httpprocessorCopy = this.httpprocessor;
        if (httpprocessorCopy == null){

            final HttpProcessorBuilder b = HttpProcessorBuilder.create();
            if (requestFirst != null){
                for (final HttpRequestInterceptor i : requestFirst){
                    b.addFirst(i);
                }
            }
            if (responseFirst != null){
                for (final HttpResponseInterceptor i : responseFirst){
                    b.addFirst(i);
                }
            }
            b.addAll(
                            new RequestDefaultHeaders(defaultHeaders),
                            new RequestContent(),
                            new RequestTargetHost(),
                            new RequestClientConnControl(),
                            new RequestUserAgent(userAgentCopy),
                            new RequestExpectContinue());
            if (!cookieManagementDisabled){
                b.add(new RequestAddCookies());
            }
            if (!contentCompressionDisabled){
                if (contentDecoderMap != null){
                    final List<String> encodings = new ArrayList<>(contentDecoderMap.keySet());
                    Collections.sort(encodings);
                    b.add(new RequestAcceptEncoding(encodings));
                }else{
                    b.add(new RequestAcceptEncoding());
                }
            }
            if (!authCachingDisabled){
                b.add(new RequestAuthCache());
            }
            if (!cookieManagementDisabled){
                b.add(new ResponseProcessCookies());
            }
            if (!contentCompressionDisabled){
                if (contentDecoderMap != null){
                    final RegistryBuilder<InputStreamFactory> b2 = RegistryBuilder.create();
                    for (final Map.Entry<String, InputStreamFactory> entry : contentDecoderMap.entrySet()){
                        b2.register(entry.getKey(), entry.getValue());
                    }
                    b.add(new ResponseContentEncoding(b2.build()));
                }else{
                    b.add(new ResponseContentEncoding());
                }
            }
            if (requestLast != null){
                for (final HttpRequestInterceptor i : requestLast){
                    b.addLast(i);
                }
            }
            if (responseLast != null){
                for (final HttpResponseInterceptor i : responseLast){
                    b.addLast(i);
                }
            }
            httpprocessorCopy = b.build();
        }
        execChain = new ProtocolExec(execChain, httpprocessorCopy);

        execChain = decorateProtocolExec(execChain);

        // Add request retry executor, if not disabled
        if (!automaticRetriesDisabled){
            HttpRequestRetryHandler retryHandlerCopy = this.retryHandler;
            if (retryHandlerCopy == null){
                retryHandlerCopy = DefaultHttpRequestRetryHandler.INSTANCE;
            }
            execChain = new RetryExec(execChain, retryHandlerCopy);
        }

        HttpRoutePlanner routePlannerCopy = this.routePlanner;
        if (routePlannerCopy == null){
            SchemePortResolver schemePortResolverCopy = this.schemePortResolver;
            if (schemePortResolverCopy == null){
                schemePortResolverCopy = DefaultSchemePortResolver.INSTANCE;
            }
            if (proxy != null){
                routePlannerCopy = new DefaultProxyRoutePlanner(proxy, schemePortResolverCopy);
            }else if (systemProperties){
                routePlannerCopy = new SystemDefaultRoutePlanner(schemePortResolverCopy, ProxySelector.getDefault());
            }else{
                routePlannerCopy = new DefaultRoutePlanner(schemePortResolverCopy);
            }
        }

        // Optionally, add service unavailable retry executor
        final ServiceUnavailableRetryStrategy serviceUnavailStrategyCopy = this.serviceUnavailStrategy;
        if (serviceUnavailStrategyCopy != null){
            execChain = new ServiceUnavailableRetryExec(execChain, serviceUnavailStrategyCopy);
        }

        // Add redirect executor, if not disabled
        if (!redirectHandlingDisabled){
            RedirectStrategy redirectStrategyCopy = this.redirectStrategy;
            if (redirectStrategyCopy == null){
                redirectStrategyCopy = DefaultRedirectStrategy.INSTANCE;
            }
            execChain = new RedirectExec(execChain, routePlannerCopy, redirectStrategyCopy);
        }

        // Optionally, add connection back-off executor
        if (this.backoffManager != null && this.connectionBackoffStrategy != null){
            execChain = new BackoffStrategyExec(execChain, this.connectionBackoffStrategy, this.backoffManager);
        }

        Lookup<AuthSchemeProvider> authSchemeRegistryCopy = this.authSchemeRegistry;
        if (authSchemeRegistryCopy == null){
            authSchemeRegistryCopy = RegistryBuilder.<AuthSchemeProvider> create().register(AuthSchemes.BASIC, new BasicSchemeFactory())
                            .register(AuthSchemes.DIGEST, new DigestSchemeFactory()).register(AuthSchemes.NTLM, new NTLMSchemeFactory())
                            .register(AuthSchemes.SPNEGO, new SPNegoSchemeFactory())
                            .register(AuthSchemes.KERBEROS, new KerberosSchemeFactory()).build();
        }
        Lookup<CookieSpecProvider> cookieSpecRegistryCopy = this.cookieSpecRegistry;
        if (cookieSpecRegistryCopy == null){
            cookieSpecRegistryCopy = CookieSpecRegistries.createDefault(publicSuffixMatcherCopy);
        }

        CookieStore defaultCookieStore = this.cookieStore;
        if (defaultCookieStore == null){
            defaultCookieStore = new BasicCookieStore();
        }

        CredentialsProvider defaultCredentialsProvider = this.credentialsProvider;
        if (defaultCredentialsProvider == null){
            if (systemProperties){
                defaultCredentialsProvider = new SystemDefaultCredentialsProvider();
            }else{
                defaultCredentialsProvider = new BasicCredentialsProvider();
            }
        }

        List<Closeable> closeablesCopy = closeables != null ? new ArrayList<>(closeables) : null;
        if (!this.connManagerShared){
            if (closeablesCopy == null){
                closeablesCopy = new ArrayList<>(1);
            }
            final HttpClientConnectionManager cm = connManagerCopy;

            if (evictExpiredConnections || evictIdleConnections){
                final IdleConnectionEvictor connectionEvictor = new IdleConnectionEvictor(
                                cm,
                                maxIdleTime > 0 ? maxIdleTime : 10,
                                maxIdleTimeUnit != null ? maxIdleTimeUnit : TimeUnit.SECONDS,
                                maxIdleTime,
                                maxIdleTimeUnit);
                closeablesCopy.add(() -> {
                    connectionEvictor.shutdown();
                    try{
                        connectionEvictor.awaitTermination(1L, TimeUnit.SECONDS);
                    }catch (final InterruptedException interrupted){
                        Thread.currentThread().interrupt();
                    }
                });
                connectionEvictor.start();
            }
            closeablesCopy.add(() -> cm.shutdown());
        }

        return new InternalHttpClient(
                        execChain,
                        connManagerCopy,
                        routePlannerCopy,
                        cookieSpecRegistryCopy,
                        authSchemeRegistryCopy,
                        defaultCookieStore,
                        defaultCredentialsProvider,
                        defaultRequestConfig != null ? defaultRequestConfig : RequestConfig.DEFAULT,
                        closeablesCopy);
    }

}
