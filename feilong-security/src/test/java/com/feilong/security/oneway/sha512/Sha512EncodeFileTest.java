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
package com.feilong.security.oneway.sha512;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.feilong.io.InputStreamUtil;
import com.feilong.lib.codec.digest.DigestUtils;
import com.feilong.security.AbstractSecurityTest;
import com.feilong.security.oneway.SHA512Util;

public class Sha512EncodeFileTest extends AbstractSecurityTest{

    @Test
    public void encodeFile() throws IOException{
        String encodeFile = SHA512Util.encodeFile(LOCATION);
        assertEquals(
                        "ee26b0dd4af7e749aa1a8ee3c10ae9923f618980772e473f8819a5d4940e0db27ac185f8a0e1d5f84f88bc887fd67b143732c304cc5fa9ad8e6f57f50028a8ff",
                        encodeFile);
        assertEquals(encodeFile, DigestUtils.sha512Hex(InputStreamUtil.getInputStream(LOCATION)));
    }
    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testSHA512UtilTestNull(){
        SHA512Util.encodeFile(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSHA512UtilTestEmpty(){
        SHA512Util.encodeFile("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSHA512UtilTestBlank(){
        SHA512Util.encodeFile(" ");
    }

}
