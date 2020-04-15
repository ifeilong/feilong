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
package com.feilong.security;

import org.apache.commons.codec.digest.HmacUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class SymmetricEncryptionTest.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 1.0 2011-12-26 上午10:50:59
 */
public class HmacTest2 extends AbstractSecurityTest{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(HmacTest2.class);

    @Test
    public void base64String1(){
        //        a1access_token59CCB23370969AC0932DAFE181323167appid20140716johnnyxiab2c3timestamp1464594701509
        //        123456&
        //        HMAC-SHA1
        String testString = "a1access_token59CCB23370969AC0932DAFE181323167appid20140716johnnyxiab2c3timestamp1464594701509";
        //        LOGGER.debug(
        //
        //                        "SymmetricType.HmacSHA1:{}",
        //                        new SymmetricEncryption(SymmetricType.HmacSHA1, "123456&").encryptBase64(testString, UTF8));
    }

    @Test
    public void base64String2(){
        String testString = "a1access_token59CCB23370969AC0932DAFE181323167appid20140716johnnyxiab2c3timestamp1464594701509";
        LOGGER.debug(HmacUtils.hmacSha1Hex("123456&", testString));
    }
}
