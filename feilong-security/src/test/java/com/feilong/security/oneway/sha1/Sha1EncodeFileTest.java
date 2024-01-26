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
package com.feilong.security.oneway.sha1;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.feilong.io.InputStreamUtil;
import com.feilong.lib.codec.digest.DigestUtils;
import com.feilong.security.AbstractSecurityTest;
import com.feilong.security.oneway.SHA1Util;

public class Sha1EncodeFileTest extends AbstractSecurityTest{

    @Test
    public void encodeFile() throws IOException{
        String encodeFile = SHA1Util.encodeFile(LOCATION);
        assertEquals("a94a8fe5ccb19ba61c4c0873d391e987982fbbd3", encodeFile);
        assertEquals(encodeFile, DigestUtils.sha1Hex(InputStreamUtil.getInputStream(LOCATION)));
    }

    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testSHA1UtilTestNull(){
        SHA1Util.encodeFile(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSHA1UtilTestEmpty(){
        SHA1Util.encodeFile("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSHA1UtilTestBlank(){
        SHA1Util.encodeFile(" ");
    }

}
