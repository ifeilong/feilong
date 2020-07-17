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

public class SHA384UtilTest extends AbstractSecurityTest{

    @Test
    public void encodeFile() throws IOException{
        String encodeFile = SHA384Util.encodeFile(LOCATION);
        assertEquals("768412320f7b0aa5812fce428dc4706b3cae50e02a64caa16a782249bfe8efc4b7ef1ccb126255d196047dfedf17a0a9", encodeFile);
        assertEquals(encodeFile, DigestUtils.sha384Hex(InputStreamUtil.getInputStream(LOCATION)));
    }

    @Test
    public void encode121(){
        assertEquals(DigestUtils.sha384Hex("2284208963"), SHA384Util.encode("2284208963"));
    }

    @Test
    public void encode12(){
        LOGGER.debug(debugSecurityValue(SHA384Util.encode("2284208963")));
    }

    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testSHA384UtilTestNull(){
        SHA384Util.encodeFile(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSHA384UtilTestEmpty(){
        SHA384Util.encodeFile("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSHA384UtilTestBlank(){
        SHA384Util.encodeFile(" ");
    }

}
