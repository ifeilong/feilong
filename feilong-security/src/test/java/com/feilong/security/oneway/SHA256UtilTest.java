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

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.feilong.io.InputStreamUtil;
import com.feilong.lib.codec.digest.DigestUtils;
import com.feilong.security.AbstractSecurityTest;

public class SHA256UtilTest extends AbstractSecurityTest{

    @Test
    public void encodeFile() throws IOException{
        String encodeFile = SHA256Util.encodeFile(LOCATION);
        assertEquals("9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08", encodeFile);
        assertEquals(encodeFile, DigestUtils.sha256Hex(InputStreamUtil.getInputStream(LOCATION)));
    }

    @Test
    public void encode121(){
        assertEquals(DigestUtils.sha256Hex("2284208963"), SHA256Util.encode("2284208963"));
    }

    @Test
    public void encode12(){
        LOGGER.debug(debugSecurityValue(SHA256Util.encode("2284208963")));
    }

    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testSHA256UtilTestNull(){
        SHA256Util.encodeFile(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSHA256UtilTestEmpty(){
        SHA256Util.encodeFile("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSHA256UtilTestBlank(){
        SHA256Util.encodeFile(" ");
    }

}
