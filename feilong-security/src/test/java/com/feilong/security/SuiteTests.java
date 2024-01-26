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
package com.feilong.security;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.feilong.security.oneway.OnewayEncryptionParameterizedTest;
import com.feilong.security.oneway.SHA1UtilTest;
import com.feilong.security.oneway.SHA256UtilTest;
import com.feilong.security.oneway.SHA384UtilTest;
import com.feilong.security.oneway.SHA512UtilTest;
import com.feilong.security.oneway.md5.Md5EncodeFileTest;
import com.feilong.security.oneway.md5.Md5EncodeParameterizedTest;
import com.feilong.security.oneway.md5.Md5EncodeTest;
import com.feilong.security.oneway.md5.Md5EncodeUpperCaseParameterizedTest;
import com.feilong.security.oneway.md5.Md5EncodeUpperCaseTest;
import com.feilong.security.symmetric.AesUtilTest;

@RunWith(Suite.class)
@SuiteClasses({ //

                Md5EncodeTest.class,
                Md5EncodeFileTest.class,
                Md5EncodeParameterizedTest.class,

                Md5EncodeUpperCaseTest.class,
                Md5EncodeUpperCaseParameterizedTest.class,

                SHA1UtilTest.class,
                SHA256UtilTest.class,
                SHA384UtilTest.class,
                SHA512UtilTest.class,

                OnewayEncryptionParameterizedTest.class,

                //---------------------------------------------------------------

                AesUtilTest.class,
//                
})
public class SuiteTests{

}
