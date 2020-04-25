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

public class DESUtilTest extends AbstractSecurityTest{

    /** The Constant SYMMETRIC_TYPE. */
    private static final SymmetricType SYMMETRIC_TYPE       = SymmetricType.DES;

    private final SymmetricEncryption  SYMMETRIC_ENCRYPTION = new SymmetricEncryption(SYMMETRIC_TYPE, KEY);

    //---------------------------------------------------------------
    /**
     * Decrypt base64.
     */
    @Test
    public void decryptBase64(){
        assertEquals(testString, SYMMETRIC_ENCRYPTION.decryptBase64("LdCGo0dplVBHZP+lIOybBPy+v57iXIRX", UTF8));
    }

    /**
     * Encryp base641.
     */
    @Test
    public void encrypBase641(){
        assertEquals("LdCGo0dplVBHZP+lIOybBPy+v57iXIRX", SYMMETRIC_ENCRYPTION.encryptBase64(testString, UTF8));
    }

    /**
     * Encryp base64.
     */
    @Test
    // @Ignore
    public void encrypBase64(){
        // LdCGo0dplVASWwJrvlHqpw==
        LOGGER.debug(SYMMETRIC_ENCRYPTION.encryptBase64(testString, UTF8));
    }

    /**
     * Encrypt hex222.
     *
     * @throws EncryptionException
     *             the encryption exception
     */
    @Test
    // @Ignore
    public void encryptHex222(){
        LOGGER.debug(
                        new SymmetricEncryption(SymmetricType.DES, "jiyoutua", CipherMode.ECB, CipherPadding.NoPadding)
                                        .encryptHex("金总,好喜欢你哟", UTF8));

    }

    /**
     * Encrypt hex.
     */
    @Test
    public void encryptHex(){
        String keyString = "jinxin";
        SymmetricEncryption symmetricEncryption = new SymmetricEncryption(
                        SYMMETRIC_TYPE,
                        keyString,
                        CipherMode.ECB,
                        CipherPadding.ISO10126Padding);

        String miString = symmetricEncryption.encryptHex(testString, UTF8);
        String mingString = symmetricEncryption.decryptHex(miString, UTF8);

        assertEquals(testString, mingString);
    }
}
