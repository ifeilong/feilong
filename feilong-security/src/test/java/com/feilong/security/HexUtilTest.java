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

import static com.feilong.core.CharsetType.UTF8;

import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import com.feilong.test.AbstractTest;

/**
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 1.4.0 2015年8月24日 上午10:51:27
 * @since 1.4.0
 */
public class HexUtilTest extends AbstractTest{

    /** <code>{@value}</code>. */
    private static final String TEXT = "jinxin.feilong";

    @Test
    public void toHexStringUpperCase(){
        LOGGER.debug(HexUtil.toHexStringUpperCase(TEXT, UTF8));
        LOGGER.debug(Hex.encodeHexString(TEXT.getBytes()));
    }

    /**
     * To original.
     */
    @Test
    public void toOriginal(){
        String hexStringUpperCase = "5B7B22636F6465223A224B3034383031222C226964223A3730302C226E616D65223A22E697B6E5B09AE6ACBEE992A5E58C99E689A3227D2C7B22636F6465223A2231333433363143222C226964223A35362C226E616D65223A22E58AB2E985B7E688B7E5A496436875636B205461796C6F7220416C6C2053746172204261636B205A6970227D5D";
        byte[] hexBytesToBytes = ByteUtil.hexBytesToBytes(hexStringUpperCase.getBytes());
        LOGGER.debug(new String(hexBytesToBytes));
        LOGGER.debug(HexUtil.toOriginal(hexStringUpperCase, UTF8));
    }

}
