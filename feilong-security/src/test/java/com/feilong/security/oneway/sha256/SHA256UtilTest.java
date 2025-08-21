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
package com.feilong.security.oneway.sha256;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.feilong.lib.codec.digest.DigestUtils;
import com.feilong.security.AbstractSecurityTest;
import com.feilong.security.oneway.SHA256Util;

@lombok.extern.slf4j.Slf4j
public class SHA256UtilTest extends AbstractSecurityTest{

    @Test
    public void encode121(){
        assertEquals(DigestUtils.sha256Hex("2284208963"), SHA256Util.encode("2284208963"));
    }

    @Test
    public void encode1213(){
        assertEquals(DigestUtils.sha256Hex("123456"), SHA256Util.encode("123456"));
        assertEquals("8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92", SHA256Util.encode("123456"));
    }

    //---------------------------------------------------------------

    @Test
    public void encodeUpperCase(){
        assertEquals("8D969EEF6ECAD3C29A3A629280E686CF0C3F5D5A86AFF3CA12020C923ADC6C92", SHA256Util.encodeUpperCase("123456"));
        assertEquals("E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855", SHA256Util.encodeUpperCase(""));
    }

    @Test
    public void encode(){
        assertEquals("8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92", SHA256Util.encode("123456"));
        assertEquals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", SHA256Util.encode(""));
    }

    //---------------------------------------------------------------

    @Test
    public void encode12(){
        log.debug(debugSecurityValue(SHA256Util.encode("2284208963")));
    }

}
