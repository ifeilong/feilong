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
package com.feilong.security.oneway;

import static com.feilong.core.CharsetType.UTF8;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.security.AbstractSecurityTest;

public class MD5UtilTest extends AbstractSecurityTest{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(MD5UtilTest.class);

    @Test
    public void encodeFile() throws IOException{
        String filepath = "/Users/feilong/.gitconfig";
        String encodeFile = MD5Util.encodeFile(filepath);

        File file = new File(filepath);
        FileInputStream fileInputStream = new FileInputStream(file);
        String md5Hex = DigestUtils.md5Hex(fileInputStream);

        assertEquals("cbc5db20a011961d6c72cb4f84b2a9b2", md5Hex);
        assertEquals(encodeFile, md5Hex);

    }

    @Test
    public void encode121(){
        assertEquals(DigestUtils.md5Hex("2284208963"), MD5Util.encode("2284208963"));
    }

    /**
     * Encode11.
     */
    @Test
    public void encode11(){
        LOGGER.debug(debugSecurityValue(DigestUtils.md2Hex("你好")));
        LOGGER.debug(debugSecurityValue(DigestUtils.md5Hex("你好")));
        LOGGER.debug(debugSecurityValue(MD5Util.encode("521000")));
        LOGGER.debug(debugSecurityValue(MD5Util.encode("你好", UTF8)));
    }

    /**
     * Encode12.
     */
    @Test
    public void encode12(){
        assertEquals(
                        "F7468B69D12BB6CE76D6206419A6AC28",
                        MD5Util.encode("1230000000456AAAAAAAAAAAAAAAIDR1120/01/2010 01:01:0112345678901234561234567890123456")
                                        .toUpperCase());
    }
}
