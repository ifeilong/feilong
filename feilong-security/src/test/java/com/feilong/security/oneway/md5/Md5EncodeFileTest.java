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
package com.feilong.security.oneway.md5;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.feilong.io.InputStreamUtil;
import com.feilong.lib.codec.digest.DigestUtils;
import com.feilong.security.AbstractSecurityTest;
import com.feilong.security.oneway.MD5Util;

public class Md5EncodeFileTest extends AbstractSecurityTest{

    @Test
    public void encodeFile() throws IOException{
        String encodeFile = MD5Util.encodeFile(LOCATION);
        assertEquals("098f6bcd4621d373cade4e832627b4f6", encodeFile);
        assertEquals(encodeFile, DigestUtils.md5Hex(InputStreamUtil.getInputStream(LOCATION)));
    }

    @Test(expected = NullPointerException.class)
    public void testMD5UtilTestNull(){
        MD5Util.encodeFile(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSHA1UtilTestEmpty(){
        MD5Util.encodeFile("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMD5UtilTestBlank(){
        MD5Util.encodeFile(" ");
    }
}
