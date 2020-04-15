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
package com.feilong.servlet.http;

import static com.feilong.core.CharsetType.UTF8;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.lang.reflect.MethodUtil;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.7.3
 */
public class RequestUtilTest{

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestUtilTest.class);

    /**
     * @since 1.7.3
     * @see com.feilong.servlet.http.RequestUtil#decodeISO88591String(String, String)
     */
    @Test
    public void test(){
        String str = "https://github.com/venusdrogon/feilong-core/search?utf8=%E2%9C%93&q=%E4%B8%AD%E5%9B%BD";
        str = "https://github.com/venusdrogon/feilong-core/search?utf8=%E2%9C%93&q=中国";
        String result = MethodUtil.invokeStaticMethod(RequestUtil.class, "decodeISO88591String", str, UTF8);
        LOGGER.debug(result);
    }
}
