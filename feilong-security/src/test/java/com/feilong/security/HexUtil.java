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

import com.feilong.core.CharsetType;
import com.feilong.core.lang.StringUtil;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * The Class HexUtil.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @version 1.4.0 2015年8月24日 上午10:49:21
 * @see com.feilong.lib.codec.binary.Hex
 * @since 1.4.0
 * @deprecated 不需要使用了
 */
@lombok.extern.slf4j.Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Deprecated
public class HexUtil{

    /**
     * 将原始字符串 转成 大写的HexString 网友gdpglc的思路.
     * 
     * @param original
     *            原始字符串
     * @param charsetName
     *            字符集,you can use {@link CharsetType}
     * @return the string
     * @see ByteUtil#bytesToHexStringUpperCase(byte[])
     * @deprecated will move
     */
    @Deprecated
    public static String toHexStringUpperCase(String original,String charsetName){
        String hexStringUpperCase = ByteUtil.bytesToHexStringUpperCase(StringUtil.getBytes(original, charsetName));
        log.debug("original:[{}],hexStringUpperCase:[{}]", original, hexStringUpperCase);
        return hexStringUpperCase;
    }

    /**
     * 将 转成 大写的HexString原始字符串.
     * 
     * @param hexStringUpperCase
     *            the hex string upper case
     * @param charsetName
     *            指定字符集,you can use {@link CharsetType}
     * @return the string
     * @see ByteUtil#hexBytesToBytes(byte[])
     * @deprecated will move
     */
    @Deprecated
    public static String toOriginal(String hexStringUpperCase,String charsetName){
        byte[] hexBytesToBytes = ByteUtil.hexBytesToBytes(hexStringUpperCase.getBytes());
        String original = StringUtil.newString(hexBytesToBytes, charsetName);
        log.debug("hexStringUpperCase:[{}],original:[{}]", hexStringUpperCase, original);
        return original;
    }
}
