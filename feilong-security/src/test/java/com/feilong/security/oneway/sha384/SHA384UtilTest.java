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
package com.feilong.security.oneway.sha384;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.feilong.lib.codec.digest.DigestUtils;
import com.feilong.security.AbstractSecurityTest;
import com.feilong.security.oneway.SHA384Util;

public class SHA384UtilTest extends AbstractSecurityTest{

    @Test
    public void encode121(){
        assertEquals(DigestUtils.sha384Hex("2284208963"), SHA384Util.encode("2284208963"));
    }

    @Test
    public void encode12(){
        LOGGER.debug(debugSecurityValue(SHA384Util.encode("2284208963")));
    }

    //---------------------------------------------------------------

    @Test
    public void encodeUpperCase(){
        assertEquals(
                        "0A989EBC4A77B56A6E2BB7B19D995D185CE44090C13E2984B7ECC6D446D4B61EA9991B76A4C2F04B1B4D244841449454",
                        SHA384Util.encodeUpperCase("123456"));
        assertEquals(
                        "38B060A751AC96384CD9327EB1B1E36A21FDB71114BE07434C0CC7BF63F6E1DA274EDEBFE76F65FBD51AD2F14898B95B",
                        SHA384Util.encodeUpperCase(""));
    }

    @Test
    public void encode(){
        assertEquals(
                        "0a989ebc4a77b56a6e2bb7b19d995d185ce44090c13e2984b7ecc6d446d4b61ea9991b76a4c2f04b1b4d244841449454",
                        SHA384Util.encode("123456"));
        assertEquals(
                        "38b060a751ac96384cd9327eb1b1e36a21fdb71114be07434c0cc7bf63f6e1da274edebfe76f65fbd51ad2f14898b95b",
                        SHA384Util.encode(""));
    }

    //---------------------------------------------------------------

}
