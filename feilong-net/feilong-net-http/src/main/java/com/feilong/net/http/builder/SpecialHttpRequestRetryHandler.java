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

import static com.feilong.core.bean.ConvertUtil.toArray;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.apache.http.client.methods.HttpExecutionAware;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.util.Args;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.lang.ClassUtil;
import com.feilong.core.lang.StringUtil;
import com.feilong.lib.lang3.ArrayUtils;

/**
 * 重试策略.
 * 
 * <h3>说明:</h3>
 * <blockquote>
 * 对于 http4默认的重试策略, 参见 https://github.com/ifeilong/feilong/issues/371 , 由于实际场景中, 超时没有重试, 故自定义一个
 * </blockquote>
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see org.apache.http.impl.client.DefaultHttpRequestRetryHandler#retryRequest(IOException, int, HttpContext)
 * @see org.apache.http.impl.execchain.RetryExec#execute(HttpRoute, HttpRequestWrapper, HttpClientContext, HttpExecutionAware)
 * @see org.apache.http.impl.client.DefaultHttpRequestRetryHandler
 * @see org.apache.http.impl.client.StandardHttpRequestRetryHandler
 * @since 3.3.0
 */
public class SpecialHttpRequestRetryHandler extends DefaultHttpRequestRetryHandler{

    /** The Constant log. */
    private static final Logger                         LOGGER            = LoggerFactory.getLogger(SpecialHttpRequestRetryHandler.class);

    //---------------------------------------------------------------
    /** 一旦出现以下的异常就重试. */
    private static final Class<? extends IOException>[] RETRIABLE_CLASSES = toArray(
                    SocketTimeoutException.class,
                    ConnectTimeoutException.class);

    //---------------------------------------------------------------

    /**
     * Create the request retry handler with a retry count of 3, requestSentRetryEnabled false
     * and using the following list of non-retriable IOException classes: <br>
     * <ul>
     * <li>InterruptedIOException</li>
     * <li>UnknownHostException</li>
     * <li>ConnectException</li>
     * <li>SSLException</li>
     * </ul>
     * .
     *
     * @param retryCount
     *            the retry count
     */
    public SpecialHttpRequestRetryHandler(final int retryCount){
        super(retryCount, false);
    }

    //---------------------------------------------------------------

    /**
     * Used {@code retryCount} and {@code requestSentRetryEnabled} to determine
     * if the given method should be retried.
     *
     * @param exception
     *            the exception
     * @param executionCount
     *            the execution count
     * @param context
     *            the context
     * @return true, if successful
     */
    @Override
    public boolean retryRequest(final IOException exception,final int executionCount,final HttpContext context){
        Args.notNull(exception, "Exception parameter");
        Args.notNull(context, "HTTP context");

        //---------------------------------------------------------------

        int retryCount = getRetryCount();
        String exceptionName = exception.getClass().getName();
        Class<? extends IOException>[] retriableClasses = getRetriableClasses();

        //---------------------------------------------------------------

        String commonLogMessage = getCommonLogMessage(executionCount, retryCount, context, exceptionName, retriableClasses);

        if (executionCount > retryCount){
            LOGGER.info("retryRequestMoreCount{},executionCount > retryCount,returnFalse", commonLogMessage);
            // Do not retry if over max retry count
            return false;
        }

        //---------------------------------------------------------------
        LOGGER.debug("retryRequestIn{}", commonLogMessage);

        //---------------------------------------------------------------
        if (ArrayUtils.contains(retriableClasses, exception.getClass())){
            LOGGER.info("retryRequestContains{},returnTrueWillRetryRequest", commonLogMessage);
            return true;
        }
        if (ClassUtil.isInstanceAnyClass(exception, retriableClasses)){
            LOGGER.info("retryRequestInstance{},returnTrueWillRetryRequest", commonLogMessage);
            return true;
        }

        //---------------------------------------------------------------
        return super.retryRequest(exception, executionCount, context);
    }

    private static String getCommonLogMessage(
                    int executionCount,
                    int retryCount,
                    HttpContext context,
                    String exceptionName,
                    Class<? extends IOException>[] retriableClasses){
        //[2/3],[http://test.mapemall.com],exception:[org.apache.http.conn.ConnectTimeoutException],
        //RETRIABLE_CLASSES:[[class java.net.SocketTimeoutException, class org.apache.http.conn.ConnectTimeoutException]],
        //requestInfo:[POST /pay/redirect/doku?PAYMENTCHANNEL=01 HTTP/1.1 [User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21, Content-Length: 17, Content-Type: application/x-www-form-urlencoded; charset=UTF-8, Host: test.mapemall.com, Connection: Keep-Alive, Accept-Encoding: gzip,deflate]]
        return StringUtil.formatPattern(
                        "[{}/{}]],[{}],exception:[{}],RETRIABLE_CLASSES:[{}],requestInfo:[{}]",
                        executionCount,
                        retryCount,

                        context.getAttribute(HttpCoreContext.HTTP_TARGET_HOST),
                        exceptionName,
                        retriableClasses,
                        context.getAttribute(HttpCoreContext.HTTP_REQUEST)
        //
        );
    }

    //---------------------------------------------------------------

    /**
     * 获得 retriable classes.
     *
     * @return the retriable classes
     * @since 3.3.2
     */
    protected Class<? extends IOException>[] getRetriableClasses(){
        return RETRIABLE_CLASSES;
    }

}
