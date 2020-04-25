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
import static com.feilong.security.symmetric.SymmetricType.AES;
import static com.feilong.security.symmetric.SymmetricType.ARCFOUR;
import static com.feilong.security.symmetric.SymmetricType.Blowfish;
import static com.feilong.security.symmetric.SymmetricType.DES;
import static com.feilong.security.symmetric.SymmetricType.DESede;

import org.junit.Test;

import com.feilong.security.AbstractSecurityTest;

public class SymmetricEncryptionTest extends AbstractSecurityTest{

    @Test
    public void base64String(){
        LOGGER.debug("SymmetricType.ARCFOUR:{}", new SymmetricEncryption(ARCFOUR, KEY).encryptBase64(testString, UTF8));
        LOGGER.debug("SymmetricType.Blowfish:{}", new SymmetricEncryption(Blowfish, KEY).encryptBase64(testString, UTF8));
        LOGGER.debug("SymmetricType.DES:{}", new SymmetricEncryption(DES, KEY).encryptBase64(testString, UTF8));
        LOGGER.debug("SymmetricType.DESede:{}", new SymmetricEncryption(DESede, KEY).encryptBase64(testString, UTF8));
        LOGGER.debug("SymmetricType.AES:{}", new SymmetricEncryption(AES, KEY).encryptBase64(testString, UTF8));
    }

}
