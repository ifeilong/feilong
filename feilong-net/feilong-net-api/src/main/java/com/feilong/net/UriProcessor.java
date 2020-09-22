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
package com.feilong.net;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.lang.StringUtil.EMPTY;
import static com.feilong.core.lang.StringUtil.SPACE;

import com.feilong.core.lang.StringUtil;

/**
 * url加工器.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.0.10
 */
public class UriProcessor{

    /**
     * 获得 请求的uri地址.
     * 
     * <p>
     * 如果uri是null,将返回empty
     * 如果uri中有空格,将会替换成%20
     * </p>
     *
     * @param uri
     *            the uri
     * @param isTrimUri
     *            the is trim uri
     * @return the 请求的uri地址
     * @see <a href="https://github.com/venusdrogon/feilong-platform/issues/257">增加自动转义-请求参数放在路径中的配置</a>
     * @see <a href="https://github.com/venusdrogon/feilong-net/issues/66">HttpClient uri 中如果有空格会报错</a>
     */
    public static String process(String uri,boolean isTrimUri){
        //这种改造 系统工作量最小
        //since 1.14.0
        if (isNullOrEmpty(uri)){
            return EMPTY;
        }

        //---------------------------------------------------------------

        //since 3.0.10
        if (isTrimUri){
            uri = uri.trim();
        }

        //---------------------------------------------------------------

        //since 1.14.0
        if (uri.contains(SPACE)){
            //W3C标准规定， 当Content-Type为application/x-www-form-urlencoded时，URL中查询参数名和参数值中空格要用加号+替代，
            //所以几乎所有使用该规范的浏览器在表单提交后，URL查询参数中空格都会被编成加号+。

            //而在另一份规范(RFC 2396，定义URI)里, URI里的保留字符都需转义成%HH格式(Section 3.4 Query Component)，因此空格会被编码成%20，加号+本身也作为保留字而被编成%2B，
            //对于某些遵循RFC 2396标准的应用来说，它可能不接受查询字符串中出现加号+，认为它是非法字符。

            //所以一个安全的举措是URL中统一使用%20来编码空格字符。
            uri = StringUtil.replace(uri, SPACE, "%20"); //参见 org.springframework.util.ResourceUtils.toURI(String)
        }
        return uri;
    }
}
