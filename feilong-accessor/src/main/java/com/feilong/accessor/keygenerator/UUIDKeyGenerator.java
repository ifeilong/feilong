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
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.feilong.core.util.RandomUtil;

/**
 * The Class UUIDKeyGenerator.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.5.5
 */
public class UUIDKeyGenerator implements KeyGenerator{

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.framework.accessor.KeyGenerator#generator(java.io.Serializable, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public String generator(Serializable serializable,HttpServletRequest request){
        return UUID.randomUUID().toString() + RandomUtil.createRandomWithLength(2);
    }
}
