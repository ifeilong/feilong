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

import java.security.NoSuchAlgorithmException;
import java.security.Provider;

import javax.crypto.KeyGenerator;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.feilong.security.EncryptionException;

/**
 * The Class SymmetricEncryptionSpringTest.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @version 1.0.7 2014-7-2 18:06:19
 */
@lombok.extern.slf4j.Slf4j
@ContextConfiguration(locations = { "classpath:feilong-security.xml" })
public class SymmetricEncryptionSpringTest extends AbstractJUnit4SpringContextTests{

    /** The symmetric encryption. */
    @Autowired(required = false)
    private SymmetricEncryption symmetricEncryption;

    /** <code>{@value}</code>. */
    private static final String testString = "鑫哥爱feilong";

    /**
     * Base64 string.
     */
    @Test
    public void base64String(){
        String original = testString;
        String base64 = symmetricEncryption.encryptBase64(original, UTF8);
        log.debug(base64);
    }

    /**
     * Decrypt base64 string.
     */
    @Test
    public void decryptBase64String(){
        String hexString = "BVl2k0U5+qokOeI6ufFlVS8XnkwEwff2";
        // log.debug(blowfishUtil.encryptToHexString(original));
        // String keyString = blowfishUtil.getKeyString();
        // log.debug(keyString);
        log.debug(symmetricEncryption.decryptBase64(hexString, UTF8));
        // 3B37B7F90CBBD4EFD5502F50F9B407E3
        // 3B37B7F90CBBD4EFD5502F50F9B407E3
        // /x3JicoLOTnZO+Zs3Ha5pg==
        // /x3JicoLOTnZO+Zs3Ha5pg==
    }

    /**
     * Encrypt to hex string.
     */
    @Test
    public void encryptToHexString(){
        String original = testString;
        String base64 = symmetricEncryption.encryptHex(original, UTF8);
        log.debug(base64);
    }

    /**
     * Decrypt hex string.
     * 
     * @throws SecurityException
     *             the security exception
     * @throws EncryptionException
     *             the encryption exception
     */
    @Test
    public void decryptHexString(){
        String hexString = "055976934539FAAA2439E23AB9F165552F179E4C04C1F7F6";
        log.debug(symmetricEncryption.decryptHex(hexString, UTF8));
    }

    @Test
    public void name() throws NoSuchAlgorithmException{
        KeyGenerator kg = KeyGenerator.getInstance("Blowfish");
        // kg.init(561);
        Provider provider = kg.getProvider();
        log.debug(provider.getInfo());
    }
}
