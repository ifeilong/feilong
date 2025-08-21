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

import com.feilong.servlet.http.SessionUtil;

/**
 * 基于session的寄存器实现.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.5.5
 */
@lombok.extern.slf4j.Slf4j
public abstract class AbstractSessionKeyAccessor{

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
    protected void commonSave(String key,Serializable serializable,HttpServletRequest request){
        SessionUtil.setAttribute(request, key, serializable);

        if (log.isDebugEnabled()){
            log.debug("setAttribute to session,key is:[{}],value is:[{}]", key, format(serializable));
        }
    }

    /**
     * 获得.
     *
     * @param <T>
     *            the generic type
     * @param key
     *            the key
     * @param request
     *            the request
     * @return the t
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.servlet.Accessor#get(java.lang.String, javax.servlet.http.HttpServletRequest)
     */
    public <T extends Serializable> T get(String key,HttpServletRequest request){
        T t = SessionUtil.getAttribute(request, key);

        if (log.isDebugEnabled()){
            log.debug("when key is :[{}],get from session t:[{}]", key, format(t));
        }

        return t;
    }

    /**
     * 删除.
     *
     * @param key
     *            the key
     * @param request
     *            the request
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.servlet.Accessor#remove(java.lang.String, javax.servlet.http.HttpServletRequest)
     */
    public void remove(String key,HttpServletRequest request){
        SessionUtil.removeAttribute(request, key);

        if (log.isDebugEnabled()){
            log.debug("remove session attribute,key:[{}]", key);
        }
    }

    //---------------------------------------------------------------

    /**
     * Format.
     *
     * @param serializable
     *            the serializable
     * @return the string
     * @since 1.10.4
     */
    private static String format(Serializable serializable){
        if (null == serializable){
            return null;
        }
        //TODO
        //return JsonUtil.format(serializable);// 如果是 字符串 这里会报错
        return serializable.toString();
    }
}
