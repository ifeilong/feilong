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
package com.feilong.security.oneway.sm3;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.feilong.security.AbstractSecurityTest;
import com.feilong.security.oneway.Sm3Util;

public class Sm3EncodeFileTest extends AbstractSecurityTest{

    @Test
    public void encodeFile(){
        String encodeFile = Sm3Util.encodeFile(LOCATION);
        assertEquals("55e12e91650d2fec56ec74e1d3e4ddbfce2ef3a65890c2a19ecf88a307e76a23", encodeFile);
        //assertEquals(encodeFile, DigestUtils.sha256Hex(InputStreamUtil.getInputStream(location)));
    }
    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testSm3UtilTestNull(){
        Sm3Util.encodeFile(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSm3UtilTestEmpty(){
        Sm3Util.encodeFile("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSm3UtilTestBlank(){
        Sm3Util.encodeFile(" ");
    }
}
