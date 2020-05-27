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
 * The Class BlowfishTest.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @version 1.0.7 2014年6月5日 下午3:55:06
 * @since 1.0.7
 */
public class BlowfishTest extends AbstractSecurityTest{

    /** The Constant SYMMETRIC_TYPE. */
    private static final SymmetricType SYMMETRIC_TYPE       = SymmetricType.Blowfish;

    private final SymmetricEncryption  SYMMETRIC_ENCRYPTION = new SymmetricEncryption(SYMMETRIC_TYPE, KEY);

    //---------------------------------------------------------------
    /**
     * Blowfish.
     */
    @Test
    public void blowfishEncryptHex(){
        String encryptHex = SYMMETRIC_ENCRYPTION.encryptHex(testString, UTF8);
        LOGGER.debug("SymmetricType.Blowfish:{}", encryptHex);
        assertEquals("055976934539FAAA2439E23AB9F165552F179E4C04C1F7F6", encryptHex);
        // 055976934539FAAA2439E23AB9F165552F179E4C04C1F7F6
    }

    /**
     * Base64 string221.
     * 
     * @throws NullPointerException
     *             the null pointer exception
     * @throws EncryptionException
     *             the encryption exception
     */
    @Test
    public void blowfishDecryptHex(){
        String hexString = "055976934539FAAA2439E23AB9F165552F179E4C04C1F7F6";
        String decryptHex = SYMMETRIC_ENCRYPTION.decryptHex(hexString, UTF8);
        LOGGER.debug(decryptHex);
    }

    /**
     * Encrypt to hex string.
     * 
     * @throws NullPointerException
     *             the null pointer exception
     * @throws EncryptionException
     *             the encryption exception
     */
    @Test
    public void encryptToHexString(){
        String original = testString;
        String base64 = SYMMETRIC_ENCRYPTION.encryptHex(original, UTF8);
        LOGGER.debug(base64);
    }
}
