/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
package com.feilong.lib.org.apache.http.impl.cookie;

import com.feilong.lib.org.apache.http.annotation.Contract;
import com.feilong.lib.org.apache.http.annotation.ThreadingBehavior;
import com.feilong.lib.org.apache.http.cookie.ClientCookie;
import com.feilong.lib.org.apache.http.cookie.CommonCookieAttributeHandler;
import com.feilong.lib.org.apache.http.cookie.Cookie;
import com.feilong.lib.org.apache.http.cookie.CookieOrigin;
import com.feilong.lib.org.apache.http.cookie.CookieRestrictionViolationException;
import com.feilong.lib.org.apache.http.cookie.MalformedCookieException;
import com.feilong.lib.org.apache.http.cookie.SetCookie;
import com.feilong.lib.org.apache.http.util.Args;

/**
 *
 * @since 4.0
 */
@Contract(threading = ThreadingBehavior.IMMUTABLE)
public class RFC2109VersionHandler extends AbstractCookieAttributeHandler implements CommonCookieAttributeHandler{

    public RFC2109VersionHandler(){
        super();
    }

    @Override
    public void parse(final SetCookie cookie,final String value) throws MalformedCookieException{
        Args.notNull(cookie, "Cookie");
        if (value == null){
            throw new MalformedCookieException("Missing value for version attribute");
        }
        if (value.trim().isEmpty()){
            throw new MalformedCookieException("Blank value for version attribute");
        }
        try{
            cookie.setVersion(Integer.parseInt(value));
        }catch (final NumberFormatException e){
            throw new MalformedCookieException("Invalid version: " + e.getMessage());
        }
    }

    @Override
    public void validate(final Cookie cookie,final CookieOrigin origin) throws MalformedCookieException{
        Args.notNull(cookie, "Cookie");
        if (cookie.getVersion() < 0){
            throw new CookieRestrictionViolationException("Cookie version may not be negative");
        }
    }

    @Override
    public String getAttributeName(){
        return ClientCookie.VERSION_ATTR;
    }

}
