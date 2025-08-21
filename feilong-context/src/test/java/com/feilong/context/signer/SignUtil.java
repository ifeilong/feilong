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

import static com.feilong.context.signer.AbstractMapSigner.signLog;
import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.security.symmetric.LogBuilder.hided;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.feilong.core.DefaultRuntimeException;
import com.feilong.core.net.ParamUtil;
import com.feilong.lib.codec.digest.HmacUtils;
import com.feilong.security.oneway.MD5Util;

/**
 * 专门用来生成签名的工具类.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 4.0.8
 */
@lombok.extern.slf4j.Slf4j
public class SignUtil{

    public static String generateSign(Map<String, String> map,String appSecret,String serverAuthenticateStaticKey){
        AtomicInteger counterAtomic = new AtomicInteger(0);

        //按参数名的字典序排序并拼接得到（注意这里的q参数值虽然是中文，但不要做任何编码处理。即生成签名参数sig过程所有参数的值用原始值，不要做任何编码处理）：
        String naturalOrderingQueryString = ParamUtil.toNaturalOrderingQueryString(map);

        //---------------------------------------------------------------
        if (log.isDebugEnabled()){
            log.debug(signLog(counterAtomic, "参数排序结果: {}", naturalOrderingQueryString));
        }
        //---------------------------------------------------------------

        try{
            //将上面的参数字符串进行Base64编码
            byte[] bytes = naturalOrderingQueryString.getBytes(UTF8);
            if (log.isDebugEnabled()){
                log.debug(signLog(counterAtomic, "排序字符串对应bytes结果: {}", bytes));
            }

            // <groupId>commons-codec</groupId>
            // <artifactId>commons-codec</artifactId>
            // <version>1.15</version>

            //byte[] encodeBase64 = org.apache.commons.codec.binary.Base64.encodeBase64(bytes);
            byte[] encodeBase64 = com.feilong.lib.codec.binary.Base64.encodeBase64(bytes);
            if (log.isDebugEnabled()){
                log.debug(
                                signLog(
                                                counterAtomic,
                                                "使用org.apache.commons.codec.binary.Base64.encodeBase64(bytes)将排序字符串对应bytes转Base64编码结果: {}",
                                                encodeBase64));
            }

            String base64EncodedStr = new String(encodeBase64);
            if (log.isDebugEnabled()){
                log.debug(signLog(counterAtomic, "Base64编码转字符串结果: {}", base64EncodedStr));
            }

            //---------------------------------------------------------------
            //进行HMAC-SHA1运算
            //<groupId>commons-codec</groupId>
            //<artifactId>commons-codec</artifactId>
            //<version>1.15</version>

            String sha1Key = appSecret + serverAuthenticateStaticKey;
            //byte[] hmacSha1 = org.apache.commons.codec.digest.HmacUtils.hmacSha1(sha1Key, base64EncodedStr);
            byte[] hmacSha1 = HmacUtils.hmacSha1(sha1Key, base64EncodedStr);
            if (log.isDebugEnabled()){
                log.debug(createLog(counterAtomic, appSecret, serverAuthenticateStaticKey, sha1Key, hmacSha1));
            }

            String encode = MD5Util.encode(hmacSha1);
            if (log.isDebugEnabled()){
                log.debug(signLog(counterAtomic, "md5 最终结果: {}", encode));
            }

            return encode;
        }catch (Exception e){
            log.error("", e);
            throw new DefaultRuntimeException(e);
        }
    }

    private static String createLog(
                    AtomicInteger counterAtomic,
                    String appSecret,
                    String serverAuthenticateStaticKey,
                    String sha1Key,
                    byte[] hmacSha1){
        String pattern = "使用org.apache.commons.codec.digest.HmacUtils.hmacSha1(sha1Key, base64EncodedStr) "//
                        + "(其中sha1Key=appSecret({}) + serverAuthenticateStaticKey({})={}),结果: {}";
        return signLog(counterAtomic, pattern, hided(appSecret), hided(serverAuthenticateStaticKey), hided(sha1Key), hmacSha1);
    }

}
