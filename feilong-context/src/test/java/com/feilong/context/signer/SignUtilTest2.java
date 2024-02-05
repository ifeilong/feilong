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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 4.0.8
 */
public class SignUtilTest2{

    private static final Logger LOGGER = LoggerFactory.getLogger(SignUtilTest2.class);
    //
    //    /**
    //     * Generate uppercase MD 5 sign.
    //     *
    //     * @param params
    //     *            the params
    //     * @return 如果 <code>params</code> 是null,抛出 {@link NullPointerException}<br>
    //     *         如果 <code>params</code> 是empty,抛出 {@link IllegalArgumentException}<br>
    //     */
    //    public static String generateUppercaseMD5Sign(Map<String, String> params){
    //        return generateUppercaseMD5Sign(params, null, null);
    //    }
    //
    //    /**
    //     * Generate uppercase MD 5 sign.
    //     *
    //     * @param params
    //     *            the params
    //     * @param appendSecretParamName
    //     *            the append secret param name
    //     * @param appendSecretParamValue
    //     *            the append secret param value
    //     * @return 如果 <code>params</code> 是null,抛出 {@link NullPointerException}<br>
    //     *         如果 <code>params</code> 是empty,抛出 {@link IllegalArgumentException}<br>
    //     */
    //    public static String generateUppercaseMD5Sign(Map<String, String> params,String appendSecretParamName,String appendSecretParamValue){
    //        Validate.notEmpty(params, "params can't be null/empty!");
    //
    //        //---------------------------------------------------------------
    //        AtomicInteger counterAtomic = new AtomicInteger(0);
    //        String toBeSignString = createToBeSignString(params, appendSecretParamName, appendSecretParamValue, counterAtomic);
    //
    //        //---------------------------------------------------------------
    //        String upperCaseMD5Sign = MD5Util.encode(toBeSignString).toUpperCase();
    //        if (LOGGER.isDebugEnabled()){
    //            LOGGER.debug(DefaultMapSigner.signLog(counterAtomic, "md5结果: {}", upperCaseMD5Sign));
    //        }
    //        return upperCaseMD5Sign;
    //    }

}
