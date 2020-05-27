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

import com.feilong.accessor.AutoKeyAccessor;
import com.feilong.accessor.keygenerator.KeyGenerator;

/**
 * 基于session的寄存器实现.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.5.5
 */
public class SessionAutoKeyAccessor extends AbstractSessionKeyAccessor implements AutoKeyAccessor{

    /** The key generator. */
    private KeyGenerator keyGenerator;

    /**
     * The Constructor.
     */
    public SessionAutoKeyAccessor(){
        //default Constructor
    }

    /**
     * The Constructor.
     *
     * @param keyGenerator
     *            the key generator
     */
    public SessionAutoKeyAccessor(KeyGenerator keyGenerator){
        this.keyGenerator = keyGenerator;
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.framework.accessor.AutoKeyAccessor#save(java.io.Serializable, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public String save(Serializable serializable,HttpServletRequest request){
        String key = keyGenerator.generator(serializable, request);
        super.commonSave(key, serializable, request);
        return key;
    }

    //---------------------------------------------------------------

    /**
     * 设置 key generator.
     *
     * @param keyGenerator
     *            the keyGenerator to set
     */
    public void setKeyGenerator(KeyGenerator keyGenerator){
        this.keyGenerator = keyGenerator;
    }

}
