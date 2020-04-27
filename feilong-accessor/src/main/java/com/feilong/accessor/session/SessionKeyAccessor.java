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
package com.feilong.accessor.session;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import com.feilong.accessor.KeyAccessor;

/**
 * 基于session的 指定 key 方式的寄存器实现.
 * 
 * <p>
 * <b>场景:</b> 适用于:<br>
 * </p>
 * 
 * <pre>
 * String s = PaymentSecureBuilder.buildS(memberId.toString(), transactionInfo.getAmount(), transactionId);
 * ccpayTokenSessionKeyAccessor.save(<span style="color:red">PaymentConstants.CCPAY_TOKEN_KEY_PREFIX + transactionId</span>, s, request);
 * </pre>
 * 
 * 这种非固定 key,且 key 动态的场景
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.5.4
 */
public class SessionKeyAccessor extends AbstractSessionKeyAccessor implements KeyAccessor{

    /**
     * Save.
     *
     * @param key
     *            the key
     * @param serializable
     *            the serializable
     * @param request
     *            the request
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.accessor.KeyAccessor#save(java.lang.String, java.io.Serializable, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void save(String key,Serializable serializable,HttpServletRequest request){
        super.commonSave(key, serializable, request);
    }
}
