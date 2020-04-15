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

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import org.apache.commons.lang3.Validate;

import com.feilong.io.FileUtil;

/**
 * The Class KeyStoreBuilder.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.0
 */
public class KeyStoreBuilder{

    /** Don't let anyone instantiate this class. */
    private KeyStoreBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Builds the key store.
     *
     * @param signCertPath
     *            the sign cert path
     * @param signCertPassword
     *            the password used to check the integrity of the keystore, the password used to unlock the keystore, or <code>null</code>
     * @param signCertType
     *            the type of keystore.<br>
     *            See the KeyStore section in the <a href= "{@docRoot}/../technotes/guides/security/StandardNames.html#KeyStore"> <br>
     *            Java Cryptography Architecture Standard Algorithm Name Documentation</a> for information about standard keystore types.
     * @return 如果 <code>signCertPath</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>signCertPath</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>signCertPassword</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>signCertPassword</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>signCertType</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>signCertType</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @throws KeyStoreException
     *             the key store exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws NoSuchAlgorithmException
     *             the no such algorithm exception
     * @throws CertificateException
     *             the certificate exception
     */
    public static KeyStore build(String signCertPath,String signCertPassword,String signCertType)
                    throws KeyStoreException,IOException,NoSuchAlgorithmException,CertificateException{
        Validate.notBlank(signCertPath, "signCertPath can't be blank!");
        Validate.notBlank(signCertPassword, "signCertPassword can't be blank!");
        Validate.notBlank(signCertType, "signCertType can't be blank!");

        //---------------------------------------------------------------
        InputStream inputStream = FileUtil.getFileInputStream(signCertPath);
        return build(inputStream, signCertPassword, signCertType);
    }

    //---------------------------------------------------------------

    /**
     * Builds the.
     *
     * @param inputStream
     *            the input stream
     * @param signCertPassword
     *            the password used to check the integrity of the keystore, the password used to unlock the keystore, or <code>null</code>
     * @param signCertType
     *            the type of keystore.<br>
     *            See the KeyStore section in the <a href= "{@docRoot}/../technotes/guides/security/StandardNames.html#KeyStore"> <br>
     *            Java Cryptography Architecture Standard Algorithm Name Documentation</a> for information about standard keystore types.
     * @return 如果 <code>inputStream</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>signCertPassword</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>signCertPassword</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>signCertType</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>signCertType</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @throws KeyStoreException
     *             the key store exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws NoSuchAlgorithmException
     *             the no such algorithm exception
     * @throws CertificateException
     *             the certificate exception
     */
    public static KeyStore build(InputStream inputStream,String signCertPassword,String signCertType)
                    throws KeyStoreException,IOException,NoSuchAlgorithmException,CertificateException{
        Validate.notNull(inputStream, "inputStream can't be blank!");
        Validate.notBlank(signCertPassword, "signCertPassword can't be blank!");
        Validate.notBlank(signCertType, "signCertType can't be blank!");

        //---------------------------------------------------------------

        KeyStore keyStore = KeyStore.getInstance(signCertType);
        keyStore.load(inputStream, signCertPassword.toCharArray());
        return keyStore;
    }
}
