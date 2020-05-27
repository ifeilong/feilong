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
package com.feilong.accessor.cookie;

import static com.feilong.core.CharsetType.UTF8;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.feilong.core.bean.BeanUtil;
import com.feilong.core.net.URIUtil;
import com.feilong.servlet.http.CookieUtil;
import com.feilong.servlet.http.entity.CookieEntity;

/**
 * 基于{@link Cookie}的寄存器实现.
 * 
 * <h3>使用{@link CookieAccessor}相比较直接使用 {@link Cookie}操作的好处:</h3>
 * <blockquote>
 * <ol>
 * <li>支持 spring DI,{@link Cookie}的相关属性都可以通过配置的形式来实现,修改维护非常方便</li>
 * <li>封装对{@link Cookie}的直接操作,简化开发的工作量</li>
 * </ol>
 * </blockquote>
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see com.feilong.servlet.http.CookieUtil
 * @since 1.5.5
 */
public class CookieAccessor{

    /** The cookie entity. */
    private CookieEntity cookieEntity;

    //---------------------------------------------------------------

    /**
     * 是否将值进行encode操作. URL-encoding
     * 
     * @since 1.10.4
     */
    private boolean      isValueEncoding = false;

    //---------------------------------------------------------------

    /**
     * 将 <code>value</code>保存到cookie中.
     *
     * @param value
     *            the value
     * @param response
     *            the response
     * @see com.feilong.servlet.http.CookieUtil#addCookie(CookieEntity, HttpServletResponse)
     */
    public void save(String value,HttpServletResponse response){
        CookieEntity cloneCookieEntity = BeanUtil.cloneBean(cookieEntity);//不要影响配置的原始数据

        String useValue = isValueEncoding ? URIUtil.encode(value, UTF8) : value;
        cloneCookieEntity.setValue(useValue);

        CookieUtil.addCookie(cloneCookieEntity, response);
    }

    //---------------------------------------------------------------

    /**
     * 获得寄存器中保存的内容.
     *
     * @param request
     *            the request
     * @return 如果取不到{@link Cookie},返回 <code>null</code>;<br>
     *         否则,返回 {@link Cookie#getValue()}
     * @see com.feilong.servlet.http.CookieUtil#getCookieValue(HttpServletRequest, String)
     */
    public String get(HttpServletRequest request){
        String cookieValue = CookieUtil.getCookieValue(request, cookieEntity.getName());
        if (null == cookieValue){
            return null;
        }
        //---------------------------------------------------------------
        //since 1.10.4
        return isValueEncoding ? URIUtil.decode(cookieValue, UTF8) : cookieValue;
    }

    //---------------------------------------------------------------

    /**
     * 删除寄存器保存的内容.
     *
     * @param response
     *            the response
     * @see com.feilong.servlet.http.CookieUtil#deleteCookie(CookieEntity, HttpServletResponse)
     */
    public void remove(HttpServletResponse response){
        CookieEntity cloneCookieEntity = BeanUtil.cloneBean(cookieEntity);//不要影响配置的原始数据
        CookieUtil.deleteCookie(cloneCookieEntity, response);
    }

    //---------------------------------------------------------------

    /**
     * 设置 cookie entity.
     *
     * @param cookieEntity
     *            the cookieEntity to set
     */
    public void setCookieEntity(CookieEntity cookieEntity){
        this.cookieEntity = cookieEntity;
    }

    /**
     * 设置 是否将值进行encode操作.
     *
     * @param isValueEncoding
     *            the isValueEncoding to set
     */
    public void setIsValueEncoding(boolean isValueEncoding){
        this.isValueEncoding = isValueEncoding;
    }

    /**
     * 获得 cookie entity.
     *
     * @return the cookieEntity
     */
    public CookieEntity getCookieEntity(){
        return cookieEntity;
    }

    /**
     * 获得 是否将值进行encode操作.
     *
     * @return the isValueEncoding
     */
    public boolean getIsValueEncoding(){
        return isValueEncoding;
    }
}