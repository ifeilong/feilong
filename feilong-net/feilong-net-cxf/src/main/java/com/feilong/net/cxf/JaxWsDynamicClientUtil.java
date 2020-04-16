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
package com.feilong.net.cxf;

import static com.feilong.core.util.MapUtil.newLinkedHashMap;

import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.xml.ws.WebServiceException;

import org.apache.commons.lang3.Validate;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.dynamic.DynamicClientFactory;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.json.jsonlib.JsonUtil;

/**
 * 使用{@link JaxWsDynamicClientFactory} 动态调用 WebService服务.
 * 
 * <p>
 * <b>JAX-WS(Java API for XML Web Services)</b>
 * 规范是一组XML web services的JAVA API，JAX-WS允许开发者可以选择RPC-oriented或者message-oriented 来实现自己的web services。
 * </p>
 * 
 * <h3>
 * 对于 {@link <a href=
 * "https://www.mkyong.com/webservices/jax-ws/suncertpathbuilderexception-unable-to-find-valid-certification-path-to-requested-target/">SunCertPathBuilderException:
 * unable to find valid certification path to requested target</a>}
 * 
 * </h3>
 * <blockquote>
 * 参见 {@link <a href=
 * "https://www.mkyong.com/webservices/jax-ws/suncertpathbuilderexception-unable-to-find-valid-certification-path-to-requested-target/">SunCertPathBuilderException:
 * unable to find valid certification path to requested target</a>}
 * </blockquote>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see <a href=
 *      "https://www.mkyong.com/webservices/jax-ws/suncertpathbuilderexception-unable-to-find-valid-certification-path-to-requested-target/">SunCertPathBuilderException:
 *      unable to find valid certification path to requested target</a>
 * @since 3.0.0
 */
public class JaxWsDynamicClientUtil{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(JaxWsDynamicClientUtil.class);

    /** Don't let anyone instantiate this class. */
    private JaxWsDynamicClientUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //------------为了调试方便-------------------
    static{
        setDefaultHostnameVerifierTrue();
    }

    //---------------------------------------------------------------

    /**
     * 如果没有这段代码, 对于 https://203.94.12.226:58443/***_official_ws/OfficialWebservice?wsdl 这么一段 ip+https 的wsdl 的调用, 将会出现 以下异常信息
     * 
     * <pre>
     * 
     * Caused by: org.apache.cxf.service.factory.<span style="color:red">ServiceConstructionException: Failed to create service.</span>
     * at org.apache.cxf.wsdl11.WSDLServiceFactory.<init>(WSDLServiceFactory.java:87)
     * at org.apache.cxf.jaxws.ServiceImpl.initialize(ServiceImpl.java:160)
     * ... 28 more
     * Caused by: javax.wsdl.WSDLException: WSDLException: faultCode=PARSER_ERROR: Problem parsing
     * 'https://203.94.12.226:58443/****_official_ws/OfficialWebservice?wsdl'.: javax.net.ssl.SSLHandshakeException:
     * java.security.cert.CertificateException: No subject alternative names present
     * at com.ibm.wsdl.xml.WSDLReaderImpl.getDocument(WSDLReaderImpl.java:2198)
     * at com.ibm.wsdl.xml.WSDLReaderImpl.readWSDL(WSDLReaderImpl.java:2422)
     * ... 30 more
     * Caused by: javax.net.ssl.SSLHandshakeException: java.security.cert.CertificateException: No subject alternative names present
     * at sun.security.ssl.Alerts.getSSLException(Alerts.java:192)
     * at com.ibm.wsdl.xml.WSDLReaderImpl.getDocument(WSDLReaderImpl.java:2188)
     * ... 35 more
     * Caused by: java.security.cert.CertificateException: <span style="color:red">No subject alternative names present</span>
     * at sun.security.util.HostnameChecker.matchIP(HostnameChecker.java:144)
     * 
     * </pre>
     *
     * @see <a href=
     *      "https://stackoverflow.com/questions/10258101/sslhandshakeexception-no-subject-alternative-names-present">SSLHandshakeException:No
     *      subject alternative names present</a>
     * @since 1.10.3
     */
    public static void setDefaultHostnameVerifierTrue(){
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){

            @Override
            public boolean verify(String hostname,SSLSession session){
                // ip address of the service URL(like.23.28.244.244)
                return true;
            }
        });
    }

    //---------------------------------------------------------------

    /**
     * 调用webservice.
     * 
     * <h3>CXF provides two factory classes for dynamic classes</h3>
     * 
     * <blockquote>
     * <ul>
     * <li>如果 your service is defined in terms of JAX-WS concepts, you should use the {@link JaxWsDynamicClientFactory}.</li>
     * <li>如果 you do not want or need JAX-WS semantics, use the {@link DynamicClientFactory}.</li>
     * </ul>
     * </blockquote>
     *
     * @param <T>
     *            the generic type
     * @param wsdlUrl
     *            the wsdl url,Example: http://ho.gymbomate.com/GymboreeHOServices/HOServices.asmx?wsdl
     * @param operationName
     *            the operation name,Example: GetMemPoints
     * @param params
     *            参数,Example: 15001841317
     * @return 如果 <code>wsdlUrl</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>wsdlUrl</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * 
     *         The return values that matche the parts of the output message of the operation
     * @throws WebServiceException
     *             the web service exception
     */
    @SuppressWarnings("unchecked")
    public static <T> T call(String wsdlUrl,String operationName,Object...params) throws WebServiceException{
        Validate.notBlank(wsdlUrl, "wsdlUrl can't be blank!");
        //---------------------------------------------------------------
        if (LOGGER.isInfoEnabled()){
            Map<String, Object> traceMap = getTraceMapForLog(wsdlUrl, operationName, params);
            LOGGER.info("will call webservice,input infos:{}", JsonUtil.format(traceMap));
        }

        //----------------------------------------------------------------------------
        Client client = createClient(wsdlUrl);

        //-----------------------------------------------------------------------------
        try{
            Object[] obj = client.invoke(operationName, params);
            return (T) obj[0];
        }catch (Exception e){
            throw new WebServiceException(e);
        }
    }

    //---------------------------------------------------------------

    /**
     * @param wsdlUrl
     * @return
     * @since 1.14.0
     */
    static Client createClient(String wsdlUrl){
        // 创建动态客户端
        DynamicClientFactory dynamicClientFactory = JaxWsDynamicClientFactory.newInstance();

        // 创建客户端连接
        Client client = dynamicClientFactory.createClient(wsdlUrl);

        //----------------------------------------------------------------------------

        //封装 TLSClientParameters
        ClientUtil.wrap(client);
        return client;
    }

    //---------------------------------------------------------------

    /**
     * 获得 map for LOGGER.
     *
     * @param wsdlUrl
     *            the wsdl url
     * @param operationName
     *            the operation name
     * @param params
     *            the params
     * @return the map for log
     * @since 1.0.9
     */
    private static Map<String, Object> getTraceMapForLog(String wsdlUrl,String operationName,Object...params){
        Map<String, Object> object = newLinkedHashMap();
        object.put("wsdlUrl", wsdlUrl);
        object.put("operationName", operationName);
        object.put("params", params);
        return object;
    }
}