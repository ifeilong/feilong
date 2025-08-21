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

import static com.feilong.core.CharsetType.UTF8;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.feilong.lib.codec.digest.DigestUtils;
import com.feilong.security.AbstractSecurityTest;
import com.feilong.security.oneway.MD5Util;

@lombok.extern.slf4j.Slf4j
public class Md5EncodeTest extends AbstractSecurityTest{

    @Test
    public void encode121(){
        assertEquals(DigestUtils.md5Hex("2284208963"), MD5Util.encode("2284208963"));
    }

    //---------------------------------------------------------------

    @Test
    public void encode11(){
        log.debug(debugSecurityValue(DigestUtils.md2Hex("你好")));
        log.debug(debugSecurityValue(DigestUtils.md5Hex("你好")));
        log.debug(debugSecurityValue(MD5Util.encode("521000")));
        log.debug(debugSecurityValue(MD5Util.encode("你好", UTF8)));
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

    @Test(expected = NullPointerException.class)
    public void encode(){
        MD5Util.encode((String) null);
    }

}
