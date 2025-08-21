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

@lombok.extern.slf4j.Slf4j
public class Sm3UtilTest extends AbstractSecurityTest{

    @Test
    public void encode11(){
        String json = "{\"name\":\"Marydon\",\"website\":\"http://www.cnblogs.com/Marydon20170307\"}";
        log.debug(debugSecurityValue(Sm3Util.encode(json)));
    }

    @Test
    public void encode112(){
        String json = "你好";
        log.debug(debugSecurityValue(Sm3Util.encode(json)));
    }

    @Test
    public void encodeUpperCase(){
        assertEquals("207CF410532F92A47DEE245CE9B11FF71F578EBD763EB3BBEA44EBD043D018FB", Sm3Util.encodeUpperCase("123456"));
        assertEquals("1AB21D8355CFA17F8E61194831E81A8F22BEC8C728FEFB747ED035EB5082AA2B", Sm3Util.encodeUpperCase(""));
    }

    @Test
    public void encode(){
        assertEquals("207cf410532f92a47dee245ce9b11ff71f578ebd763eb3bbea44ebd043d018fb", Sm3Util.encode("123456"));
        assertEquals("1ab21d8355cfa17f8e61194831e81a8f22bec8c728fefb747ed035eb5082aa2b", Sm3Util.encode(""));
    }

}
