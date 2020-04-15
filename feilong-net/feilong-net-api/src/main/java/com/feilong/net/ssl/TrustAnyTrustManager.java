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
package com.feilong.net.ssl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * 信任所有的证书.
 * 
 * <p>
 * 实现一个{@link X509TrustManager}接口，用于绕过验证，不用修改里面的方法
 * </p>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see "weibo4j.model.MySSLSocketFactory.TrustAnyTrustManager"
 * @see org.apache.commons.net.util.TrustManagerUtils
 * @see javax.net.ssl.HostnameVerifier
 * @deprecated pls use {@link org.apache.commons.net.util.TrustManagerUtils#getAcceptAllTrustManager()}
 */
@Deprecated
public class TrustAnyTrustManager implements X509TrustManager{

    /** Static instance. */
    // the static instance works for all types
    public static final X509TrustManager INSTANCE = new TrustAnyTrustManager();

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see javax.net.ssl.X509TrustManager#getAcceptedIssuers()
     */
    @Override
    public X509Certificate[] getAcceptedIssuers(){
        return new X509Certificate[] {};
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see javax.net.ssl.X509TrustManager#checkClientTrusted(java.security.cert.X509Certificate[], java.lang.String)
     */
    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificate,String authType) throws CertificateException{
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.net.ssl.X509TrustManager#checkServerTrusted(java.security.cert.X509Certificate[], java.lang.String)
     */
    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificate,String authType) throws CertificateException{
    }

}
