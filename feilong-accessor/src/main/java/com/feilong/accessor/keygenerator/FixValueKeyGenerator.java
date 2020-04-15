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
package com.feilong.accessor.keygenerator;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * 固定值(常用于 {@link com.feilong.accessor.cookie.CookieAccessor}).
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.5.5
 */
public class FixValueKeyGenerator implements KeyGenerator{

    /** The key. */
    private String key;

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.framework.accessor.KeyGenerator#generator(java.io.Serializable, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public String generator(Serializable serializable,HttpServletRequest request){
        return key;
    }

    /**
     * 设置 key.
     *
     * @param key
     *            the key to set
     */
    public void setKey(String key){
        this.key = key;
    }
}
