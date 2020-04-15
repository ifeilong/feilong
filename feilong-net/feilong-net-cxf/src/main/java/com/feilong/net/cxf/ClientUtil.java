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

import static com.feilong.core.lang.ObjectUtil.defaultIfNullOrEmpty;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.transport.http.HTTPConduit;

/**
 * The Class ClientUtil.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 3.0.0
 */
public final class ClientUtil{

    /** Don't let anyone instantiate this class. */
    private ClientUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 封装 TLSClientParameters.
     *
     * @param client
     *            the client
     * @see <a href=
     *      "http://cxf.547215.n5.nabble.com/Overriding-TLSClientParameters-after-overriding-endpoint-address-td5727690.html">Overriding
     *      TLSClientParameters after overriding endpoint address</a>
     * @since 1.10.3
     */
    public static void wrap(Client client){
        HTTPConduit httpConduit = (HTTPConduit) client.getConduit();

        TLSClientParameters tslClientParameters = wrap(httpConduit.getTlsClientParameters());

        httpConduit.setTlsClientParameters(tslClientParameters);
    }

    //---------------------------------------------------------------

    /**
     * 如果没有这段代码,对于 以下 wsdl(https://203.94.12.226:58443/***_official_ws/OfficialWebservice?wsdl) 的调用, 将会出现 以下异常
     * 
     * <pre>
     *     javax.xml.ws.WebServiceException: org.apache.cxf.interceptor.Fault: Could not send Message.
     *     at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.main(RemoteTestRunner.java:192)
     *     Caused by: org.apache.cxf.interceptor.Fault: Could not send Message.
     *     at org.apache.cxf.interceptor.MessageSenderInterceptor$MessageSenderEndingInterceptor.handleMessage(MessageSenderInterceptor.java:64)
     *     at org.apache.cxf.phase.PhaseInterceptorChain.doIntercept(PhaseInterceptorChain.java:308)
     *     at org.apache.cxf.endpoint.ClientImpl.invoke(ClientImpl.java:283)
     *     at com.feilong.webservice.cxf.JaxWsDynamicClientUtil.call(JaxWsDynamicClientUtil.java:133)
     *     ... 24 more
     *     Caused by: java.io.IOException: IOException invoking https://203.94.12.226:58443/adi****ficial_ws/OfficialWebservice: The https URL hostname does not match the Common Name (CN) on the server certificate in the client's truststore.  Make sure server certificate is correct, or to disable this check (NOT recommended for production) set the CXF client TLS configuration property "disableCNCheck" to true.
     *     at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
     *     at org.apache.cxf.interceptor.MessageSenderInterceptor$MessageSenderEndingInterceptor.handleMessage(MessageSenderInterceptor.java:62)
     *     ... 32 more
     *     Caused by: java.io.IOException: The https URL hostname does not match the Common Name (CN) on the server certificate in the client's truststore.  Make sure server certificate is correct, or to disable this check (NOT recommended for production) set the CXF client TLS configuration property "disableCNCheck" to true.
     *     at org.apache.cxf.transport.http.HTTPConduit$WrappedOutputStream.onFirstWrite(HTTPConduit.java:1291)
     *     at org.apache.cxf.transport.http.HTTPConduit$WrappedOutputStream.close(HTTPConduit.java:1341)
     *     ... 35 more
     * </pre>
     *
     * @param tlsClientParameters
     *            the tls client parameters
     * @return the TLS client parameters
     * @see <a href=
     *      "http://cxf.547215.n5.nabble.com/Overriding-TLSClientParameters-after-overriding-endpoint-address-td5727690.html">Overriding
     *      TLSClientParameters after overriding endpoint address</a>
     */
    private static TLSClientParameters wrap(TLSClientParameters tlsClientParameters){
        TLSClientParameters tslClientParameters = defaultIfNullOrEmpty(tlsClientParameters, new TLSClientParameters());
        tslClientParameters.setDisableCNCheck(true);
        return tslClientParameters;
    }
}
