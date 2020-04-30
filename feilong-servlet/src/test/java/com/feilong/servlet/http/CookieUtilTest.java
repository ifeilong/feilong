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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

import javax.servlet.http.Cookie;

import org.junit.Test;

import com.feilong.core.bean.BeanUtil;
import com.feilong.core.bean.PropertyUtil;
import com.feilong.servlet.http.entity.CookieEntity;
import com.feilong.test.AbstractTest;

public class CookieUtilTest extends AbstractTest{

    @Test
    public void test(){
        CookieEntity cookieEntity = new CookieEntity("name", "jinxin");
        cookieEntity.setHttpOnly(false);

        Cookie cookie = new Cookie(cookieEntity.getName(), cookieEntity.getValue());

        PropertyUtil.copyProperties(cookie, cookieEntity //
                        , "httpOnly"//@since Servlet 3.0
        );

        assertThat(
                        cookie,
                        allOf(//
                                        hasProperty("name", is("name")),
                                        hasProperty("httpOnly", is(false)),
                                        hasProperty("value", is("jinxin"))));
    }

    @Test
    public void test1(){
        CookieEntity cookieEntity = new CookieEntity("name", "jinxin");

        CookieEntity cloneCookieEntity = BeanUtil.cloneBean(cookieEntity);
        cloneCookieEntity.setMaxAge(8888);
        cloneCookieEntity.setName("feilong");

        assertThat(
                        cloneCookieEntity,
                        allOf(//
                                        hasProperty("name", is("feilong")),
                                        hasProperty("maxAge", is(8888)),
                                        hasProperty("value", is("jinxin"))));

        assertThat(
                        cookieEntity,
                        allOf(//
                                        hasProperty("name", is("name")),
                                        hasProperty("value", is("jinxin"))));

    }
}
