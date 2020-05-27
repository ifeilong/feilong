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

/**
 * The Class AESUtilTest.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @version 1.2.2 2015年7月19日 下午6:36:00
 * @since 1.2.2
 */
public class AESUtilTest111 extends AbstractSecurityTest{

    /** The Constant SYMMETRIC_TYPE. */
    private static final SymmetricType       SYMMETRIC_TYPE       = SymmetricType.AES;

    /** The symmetric encryption. */
    private static final SymmetricEncryption SYMMETRIC_ENCRYPTION = new SymmetricEncryption(SYMMETRIC_TYPE, KEY);

    //---------------------------------------------------------------
    /**
     * Decrypt base64.
     */
    @Test
    public void decryptBase64(){
        assertEquals(testString, SYMMETRIC_ENCRYPTION.decryptBase64("NvHLVz3ADOlx3K2dMa8TZjjP5fkAPus2ienTEkOdUX4=", UTF8));
    }

    /**
     * Encryp base64.
     */
    @Test
    public void encrypBase64(){
        assertEquals("NvHLVz3ADOlx3K2dMa8TZjjP5fkAPus2ienTEkOdUX4=", SYMMETRIC_ENCRYPTION.encryptBase64(testString, UTF8));
    }

    //---------------------------------------------------------------

    /**
     * Test encrypt.
     */
    @Test
    
    public void testEncrypt(){
        String SYMMETRIC_TYPE_KEY = "xhf6Z6I3JePpm8pgYa5m6w==";
        SymmetricEncryption symmetricEncryption1 = new SymmetricEncryption(SYMMETRIC_TYPE, SYMMETRIC_TYPE_KEY);

        String plainText = "Base64是一种基于64个可打印字符来表示二进制数据的表示方法.";
        String cipherText = symmetricEncryption1.encryptBase64(plainText, UTF8);
        LOGGER.debug("Cipher:" + cipherText);

        String decipherText = symmetricEncryption1.decryptBase64(cipherText, UTF8);
        LOGGER.debug("Decipher:" + decipherText);
        assert plainText.equals(decipherText) : "Encrypt is not correct";
    }

    //---------------------------------------------------------------

    /**
     * Encrypt hex.
     */
    @Test
    public void testEncryptHex(){
        assertEquals("36F1CB573DC00CE971DCAD9D31AF136638CFE5F9003EEB3689E9D312439D517E", SYMMETRIC_ENCRYPTION.encryptHex(testString, UTF8));
    }

    /**
     * Test decrypt hex.
     */
    @Test
    public void testDecryptHex(){
        assertEquals(testString, SYMMETRIC_ENCRYPTION.decryptHex("36F1CB573DC00CE971DCAD9D31AF136638CFE5F9003EEB3689E9D312439D517E", UTF8));
    }
}
