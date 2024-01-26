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

import org.junit.Test;

import com.feilong.security.AbstractSecurityTest;
import com.feilong.security.oneway.MD5Util;

public class Md5EncodeUpperCaseTest extends AbstractSecurityTest{

    @Test(expected = NullPointerException.class)
    public void encodeUpperCase(){
        MD5Util.encodeUpperCase(null);
    }

}
