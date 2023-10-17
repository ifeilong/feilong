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

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.lang.StringUtil.EMPTY;

import java.net.SocketTimeoutException;
import java.util.Map;

import javax.net.ssl.SSLException;

import com.feilong.core.lang.ClassUtil;
import com.feilong.core.lang.StringUtil;
import com.feilong.core.lang.SystemUtil;
import com.feilong.core.util.MapUtil;
import com.feilong.json.JsonUtil;
import com.feilong.net.http.ConnectionConfig;
import com.feilong.net.http.HttpLogHelper;
import com.feilong.net.http.HttpRequest;

/**
 * http 请求执行异常 message 构造器.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.14.0
 */
public class HttpRequestExecuterExceptionMessageBuilder{

    /** Don't let anyone instantiate this class. */
    private HttpRequestExecuterExceptionMessageBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Builds the message.
     * 
     * @param httpRequest
     *            the http request
     * @param useConnectionConfig
     *            the use connection config
     * @param e
     *            the e
     *
     * @return the string
     * @since 1.11.4
     * @since 1.14.0 rename from buildMessage
     */
    static String build(HttpRequest httpRequest,ConnectionConfig useConnectionConfig,Exception e){
        String handlerMessage = buildHandlerMessage(e, useConnectionConfig);

        String result = commonMessage(httpRequest, useConnectionConfig);
        if (isNullOrEmpty(handlerMessage)){
            return result;
        }
        //---------------------------------------------------------------
        //带自定义头 handlerMessage
        return StringUtil.formatPattern("[{}],{}", handlerMessage, result);
    }

    //---------------------------------------------------------------

    /**
     * 构造不同异常,特殊友好提示.
     *
     * @param e
     *            the e
     * @param useConnectionConfig
     *            the use connection config
     * @return the string
     * @since 1.14.0
     */
    //XXX 留坑
    private static String buildHandlerMessage(Exception e,ConnectionConfig useConnectionConfig){
        if (ClassUtil.isInstance(e, SocketTimeoutException.class)){ //链接超时
            StringBuilder sb = new StringBuilder();
            sb.append("current ConnectTimeout:").append(useConnectionConfig.getConnectTimeout());
            sb.append(",ReadTimeout:").append(useConnectionConfig.getReadTimeout());
            return sb.toString();
        }

        //---------------------------------------------------------------
        //https://www.cnblogs.com/sunny08/p/8038440.html
        if (ClassUtil.isInstance(e, SSLException.class)){
            return EMPTY;
        }
        return EMPTY;
    }

    //---------------------------------------------------------------

    /**
     * Common message.
     *
     * @param httpRequest
     *            the http request
     * @param useConnectionConfig
     *            the use connection config
     * @return the string
     */
    private static String commonMessage(HttpRequest httpRequest,ConnectionConfig useConnectionConfig){
        Map<String, String> httpPropertiesMap = buildHttpPropertiesMap();

        //---------------------------------------------------------------
        String pattern = "httpRequest:[{}],useConnectionConfig:[{}]";
        String commonResult = StringUtil.formatPattern(
                        pattern,
                        HttpLogHelper.createHttpRequestLog(httpRequest),
                        HttpLogHelper.createConnectionConfigLog(useConnectionConfig));
        if (isNullOrEmpty(httpPropertiesMap)){
            return commonResult;
        }

        //---------------------------------------------------------------
        //带 httpPropertiesMap的
        return StringUtil.formatPattern("{},http system properties:[{}]", commonResult, JsonUtil.toString(httpPropertiesMap));
    }

    //---------------------------------------------------------------

    /**
     * 构造和 http 相关的属性.
     *
     * @return the string
     */
    private static Map<String, String> buildHttpPropertiesMap(){
        Map<String, String> propertiesMap = SystemUtil.getPropertiesMap();
        //"http.nonProxyHosts": "local|*.local|169.254/16|*.169.254/16",
        //"java.protocol.handler.pkgs": "com.sun.net.ssl.internal.www.protocol",
        //"javax.net.ssl.trustStore": SystemUtil.USER_HOME+ "/workspace/feilong/feilong-net/feilong-net-httpclient4/src/test/java/ami***uat.keystore",
        //"javax.net.ssl.trustStorePassword": "ami***t",
        //"socksNonProxyHosts": "local|*.local|169.254/16|*.169.254/16",
        return MapUtil.getSubMap(
                        propertiesMap,
                        "http.nonProxyHosts",
                        "java.protocol.handler.pkgs",
                        "javax.net.ssl.trustStore",
                        "javax.net.ssl.trustStorePassword",
                        "socksNonProxyHosts");
    }
}
