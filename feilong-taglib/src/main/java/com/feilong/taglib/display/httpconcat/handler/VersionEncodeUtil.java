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
package com.feilong.taglib.display.httpconcat.handler;

import com.feilong.security.oneway.MD5Util;
import com.feilong.security.oneway.SHA1Util;

/**
 * 独立成一个类,避免没有依赖 security 报错.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.11.1
 */
@lombok.extern.slf4j.Slf4j
public final class VersionEncodeUtil{

    /** Don't let anyone instantiate this class. */
    private VersionEncodeUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Encode.
     *
     * @param version
     *            the version
     * @param versionEncode
     *            the version encode
     * @return the string
     */
    static String encode(String version,String versionEncode){
        //后缀
        String suffix = VersionEncodeUtil.class.getName();

        if ("md5".equalsIgnoreCase(versionEncode)){
            return MD5Util.encode(version + suffix);
        }

        if ("sha1".equalsIgnoreCase(versionEncode)){
            return SHA1Util.encode(version + suffix);
        }

        log.warn("versionEncode:[{}] ,not md5 and not sha1", versionEncode);
        return version;
    }
}
