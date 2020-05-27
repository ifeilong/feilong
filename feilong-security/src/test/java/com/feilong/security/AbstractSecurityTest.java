/**
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

import com.feilong.test.AbstractTest;
import com.feilong.tools.slf4j.Slf4jUtil;

/**
 * The Class BaseSecurityTest.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @version 1.0 Apr 17, 2014 10:27:27 AM
 */
public abstract class AbstractSecurityTest extends AbstractTest{

    /** <code>{@value}</code>. */
    public static final String    testString = "鑫哥爱feilong";

    /** 默认加密解密的key. */
    protected static final String KEY        = "feilong";

    //---------------------------------------------------------------

    /**
     * Debug security value.
     *
     * @param encode
     *            the encode
     * @return the string
     */
    protected String debugSecurityValue(String encode){
        return Slf4jUtil.format(" {} [{}]", encode, encode.length());
    }
}
