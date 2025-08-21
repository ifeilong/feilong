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

import org.junit.Test;

import com.feilong.lib.codec.digest.DigestUtils;
import com.feilong.security.AbstractSecurityTest;
import com.feilong.security.oneway.SHA512Util;

@lombok.extern.slf4j.Slf4j
public class SHA512UtilTest extends AbstractSecurityTest{

    @Test
    public void encode121(){
        assertEquals(DigestUtils.sha512Hex("2284208963"), SHA512Util.encode("2284208963"));
    }

    @Test
    public void encode12(){
        log.debug(debugSecurityValue(SHA512Util.encode("2284208963")));
    }

    //---------------------------------------------------------------

    @Test
    public void encodeUpperCase(){
        assertEquals(
                        "BA3253876AED6BC22D4A6FF53D8406C6AD864195ED144AB5C87621B6C233B548BAEAE6956DF346EC8C17F5EA10F35EE3CBC514797ED7DDD3145464E2A0BAB413",
                        SHA512Util.encodeUpperCase("123456"));
        assertEquals(
                        "CF83E1357EEFB8BDF1542850D66D8007D620E4050B5715DC83F4A921D36CE9CE47D0D13C5D85F2B0FF8318D2877EEC2F63B931BD47417A81A538327AF927DA3E",
                        SHA512Util.encodeUpperCase(""));
    }

    @Test
    public void encode(){
        assertEquals(
                        "ba3253876aed6bc22d4a6ff53d8406c6ad864195ed144ab5c87621b6c233b548baeae6956df346ec8c17f5ea10f35ee3cbc514797ed7ddd3145464e2a0bab413",
                        SHA512Util.encode("123456"));
        assertEquals(
                        "cf83e1357eefb8bdf1542850d66d8007d620e4050b5715dc83f4a921d36ce9ce47d0d13c5d85f2b0ff8318d2877eec2f63b931bd47417a81a538327af927da3e",
                        SHA512Util.encode(""));
    }

}
