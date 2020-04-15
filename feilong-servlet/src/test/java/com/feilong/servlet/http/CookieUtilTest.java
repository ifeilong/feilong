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
package com.feilong.servlet.http;

import javax.servlet.http.Cookie;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.bean.BeanUtil;
import com.feilong.core.bean.PropertyUtil;
import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.servlet.http.entity.CookieEntity;

/**
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.5.4
 */
public class CookieUtilTest{

    private static final Logger LOGGER = LoggerFactory.getLogger(CookieUtilTest.class);

    @Test
    public final void test(){
        CookieEntity cookieEntity = new CookieEntity("name", "jinxin");
        cookieEntity.setHttpOnly(false);

        Cookie cookie = new Cookie(cookieEntity.getName(), cookieEntity.getValue());

        PropertyUtil.copyProperties(cookie, cookieEntity //
                        , "httpOnly"//@since Servlet 3.0
        );

        LOGGER.debug(JsonUtil.format(cookie));
    }

    @Test
    public final void test1(){
        CookieEntity cookieEntity = new CookieEntity("name", "jinxin");
        LOGGER.debug("cookieEntity:{}", JsonUtil.format(cookieEntity));

        CookieEntity cloneCookieEntity = BeanUtil.cloneBean(cookieEntity);
        LOGGER.debug("cloneCookieEntity:{}", JsonUtil.format(cloneCookieEntity));

        cloneCookieEntity.setMaxAge(8888);
        cloneCookieEntity.setName("feilong");

        LOGGER.debug("cookieEntity:{}", JsonUtil.format(cookieEntity));
        LOGGER.debug("cloneCookieEntity:{}", JsonUtil.format(cloneCookieEntity));
    }
}
