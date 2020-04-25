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
package com.feilong.net.mail.util;

import static com.feilong.core.CharsetType.UTF8;

import java.io.UnsupportedEncodingException;

import javax.mail.internet.InternetAddress;

import org.junit.Test;

import com.feilong.test.AbstractTest;

public class InternetAddressUtilTest extends AbstractTest{

    /**
     * Test.
     *
     * @throws UnsupportedEncodingException
     *             the unsupported encoding exception
     */
    @Test
    public void test() throws UnsupportedEncodingException{
        InternetAddress internetAddress = new InternetAddress("longxia@feilong.cn", "夏龙", UTF8);
        InternetAddress internetAddress1 = new InternetAddress("zhen.yao@feilong.cn", "姚真", UTF8);

        InternetAddress[] internetAddresses = { internetAddress, internetAddress1 };

        LOGGER.debug("\n{}", internetAddress.toUnicodeString());
        LOGGER.debug("\n{}", InternetAddress.toString(internetAddresses, 0));
    }

}
