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
package com.feilong.context.signer;

import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.security.symmetric.LogBuilder.hided;

import java.util.concurrent.atomic.AtomicInteger;

import com.feilong.core.lang.StringUtil;
import com.feilong.lib.codec.digest.HmacUtils;

/**
 * // 合作方实现接口签名生成算法
 * // 有些接口需要合作方按照规范要求实现，比如专辑/声音上下架推送接口，这些接口的签名生成算法如下：
 * //
 * // (1) 将除了sig以外的所有其他请求参数的原始值按照参数名的字典序排序
 * // (2) 将排序后的参数键值对用&拼接，即拼接成key1=val1&key2=val2这样的形式
 * // (3) 将上一步得到的字符串后拼上app_secret，比如app_secret为abc，那么现在就得到key1=val1&key2=val2&app_secret=abc
 * // (4) 对上一步得到的字符串进行MD5运算得到32位小写字符串，即为sig
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 4.0.8
 */
@lombok.extern.slf4j.Slf4j
public class SaltMapSigner extends AbstractMapSigner{

    private final String sha1Key;

    /**
     * @param mapSignConfig
     */
    public SaltMapSigner(MapSignConfig mapSignConfig, String sha1Key){
        super(mapSignConfig);
        this.sha1Key = sha1Key;
    }

    //---------------------------------------------------------------

    @Override
    protected String encodeString(String toBeSignString,AtomicInteger counter){
        //将上面的参数字符串进行Base64编码
        byte[] bytes = StringUtil.getBytes(toBeSignString, UTF8);

        if (log.isDebugEnabled()){
            log.debug(signLog(counter, "排序字符串对应bytes结果: {}", bytes));
        }

        // <groupId>commons-codec</groupId>
        // <artifactId>commons-codec</artifactId>
        // <version>1.15</version>

        //byte[] encodeBase64 = org.apache.commons.codec.binary.Base64.encodeBase64(bytes);
        byte[] encodeBase64 = com.feilong.lib.codec.binary.Base64.encodeBase64(bytes);
        if (log.isDebugEnabled()){
            log.debug(
                            signLog(
                                            counter,
                                            "使用org.apache.commons.codec.binary.Base64.encodeBase64(bytes)将排序字符串对应bytes转Base64编码结果: {}",
                                            encodeBase64));
        }

        String base64EncodedStr = new String(encodeBase64);
        if (log.isDebugEnabled()){
            log.debug(signLog(counter, "Base64编码转字符串结果: {}", base64EncodedStr));
        }

        //---------------------------------------------------------------
        //进行HMAC-SHA1运算
        //<groupId>commons-codec</groupId>
        //<artifactId>commons-codec</artifactId>
        //<version>1.15</version>

        //        String sha1Key = appSecret + serverAuthenticateStaticKey;
        //byte[] hmacSha1 = org.apache.commons.codec.digest.HmacUtils.hmacSha1(sha1Key, base64EncodedStr);
        byte[] hmacSha1 = HmacUtils.hmacSha1(sha1Key, base64EncodedStr);
        if (log.isDebugEnabled()){
            log.debug(
                            signLog(
                                            counter,
                                            "使用org.apache.commons.codec.digest.HmacUtils.hmacSha1(sha1Key, base64EncodedStr) "//
                                                            + "(其中sha1Key={}),结果: {}",

                                            hided(sha1Key),
                                            hmacSha1));
        }
        return encodeBytes(hmacSha1, counter);
    }

}
