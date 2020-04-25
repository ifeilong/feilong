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
package com.feilong.security.symmetric;

import static com.feilong.core.CharsetType.UTF8;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.feilong.security.AbstractSecurityTest;
import com.feilong.security.EncryptionException;

/**
 * The Class DESedeUtilTest.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 1.0 Mar 21, 2011 4:55:43 PM
 */
public class DESedeUtilTest extends AbstractSecurityTest{

    /** The Constant SYMMETRIC_TYPE. */
    private static final SymmetricType SYMMETRIC_TYPE       = SymmetricType.DESede;

    private final SymmetricEncryption  SYMMETRIC_ENCRYPTION = new SymmetricEncryption(SYMMETRIC_TYPE, KEY);

    //---------------------------------------------------------------
    /**
     * Decrypt base64.
     *
     * @throws EncryptionException
     *             the encryption exception
     */
    @Test
    public void decryptBase64(){
        String mingsString = SYMMETRIC_ENCRYPTION.decryptBase64("LdCGo0dplVASWwJrvlHqpw==", UTF8);
        LOGGER.debug(mingsString);
        assertEquals(testString, mingsString);
    }

    /**
     * Encryp base641.
     *
     * @throws NullPointerException
     *             the null pointer exception
     * @throws EncryptionException
     *             the encryption exception
     */
    @Test
    public void encrypBase641(){
        LOGGER.debug(SYMMETRIC_ENCRYPTION.encryptBase64("feilong", UTF8));
    }

    /**
     * Encryp base64.
     *
     * @throws NullPointerException
     *             the null pointer exception
     * @throws EncryptionException
     *             the encryption exception
     */
    @Test
    // @Ignore
    public void encrypBase64(){
        //	assertEquals(expected, actual);
        // oKLAr5N7UK2VzL0kLwnKDA9BoaAU62rV
        //oKLAr5N7UK2VzL0kLwnKDPg/nQZBlrXn
        LOGGER.debug(SYMMETRIC_ENCRYPTION.encryptBase64(testString, UTF8));
    }

    /**
     * Encrypt hex.
     */
    @Test
    public void encryptHex(){
        String keyString = "jinxin";
        String miString = SYMMETRIC_ENCRYPTION.encryptHex(testString, UTF8);
        // String miString = dESUtil.encryptToHexString(testString);
        LOGGER.debug("encryptOriginalToHexString:{}", miString);
        String mingString = SYMMETRIC_ENCRYPTION.decryptHex(miString, UTF8);
        LOGGER.debug(mingString);

        assertEquals(testString, mingString);

        LOGGER.debug("055976934539FAAAD7F0EB5C83FE0D5D".length() + "");
    }

    /**
     * Des3.
     */
    @Test
    public void des3(){
        String original = "F7468B69D12BB6CE76D6206419A6AC28";
        String keyString = "12345678901234561234567890123456";
        String encryptToHexString = SYMMETRIC_ENCRYPTION.encryptHex(original, UTF8);
        LOGGER.debug(encryptToHexString.length() + "");
        LOGGER.debug("BF81501C562D6FEA2FCB905D392D5851".length() + "");
        assertEquals("BF81501C562D6FEA2FCB905D392D5851", encryptToHexString);
    }

}
