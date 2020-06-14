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
package com.feilong.core.util.resourcebundleutil;

import static com.feilong.core.util.ResourceBundleUtil.getResourceBundle;
import static com.feilong.core.util.ResourceBundleUtil.toAliasBean;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import com.feilong.core.entity.DangaMemCachedConfig;
import com.feilong.lib.beanutils.ConvertUtils;
import com.feilong.lib.beanutils.converters.ArrayConverter;
import com.feilong.lib.beanutils.converters.StringConverter;

/**
 * The Class ResourceBundleUtilToAliasBeanTest.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 */
public class ToAliasBeanTest{

    @Test
    @SuppressWarnings("unchecked")
    public void testToAliasBean(){
        DangaMemCachedConfig dangaMemCachedConfig = toAliasBean(getResourceBundle("messages.memcached"), DangaMemCachedConfig.class);
        assertThat(
                        dangaMemCachedConfig,
                        allOf(//
                                        hasProperty("serverList", arrayContaining("172.20.3-1.23", "11211", "172.20.31.22", "11211")),
                                        hasProperty("poolName", is("sidsock2")),
                                        hasProperty("expireTime", is(180)),
                                        hasProperty("weight", arrayContaining(2)),
                                        hasProperty("initConnection", is(10)),
                                        hasProperty("minConnection", is(5)),
                                        hasProperty("maxConnection", is(250)),
                                        hasProperty("maintSleep", is(30)),
                                        hasProperty("nagle", is(false)),
                                        hasProperty("socketTo", is(3000)),
                                        hasProperty("aliveCheck", is(false))
                        //
                        ));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testToAliasBean1(){
        ArrayConverter arrayConverter = new ArrayConverter(String[].class, new StringConverter(), 2);
        char[] allowedChars = { ':' };
        arrayConverter.setAllowedChars(allowedChars);

        ConvertUtils.register(arrayConverter, String[].class);

        DangaMemCachedConfig dangaMemCachedConfig = toAliasBean(getResourceBundle("messages.memcached"), DangaMemCachedConfig.class);
        assertThat(
                        dangaMemCachedConfig,
                        allOf(//
                                        hasProperty("serverList", arrayContaining("172.20.3-1.23:11211", "172.20.31.22:11211")),
                                        hasProperty("poolName", is("sidsock2")),
                                        hasProperty("expireTime", is(180)),
                                        hasProperty("weight", arrayContaining(2)),
                                        hasProperty("initConnection", is(10)),
                                        hasProperty("minConnection", is(5)),
                                        hasProperty("maxConnection", is(250)),
                                        hasProperty("maintSleep", is(30)),
                                        hasProperty("nagle", is(false)),
                                        hasProperty("socketTo", is(3000)),
                                        hasProperty("aliveCheck", is(false))
                        //
                        ));
    }

    /**
     * Test to alias bean null resource bundle.
     */
    @Test(expected = NullPointerException.class)
    public void testToAliasBeanNullResourceBundle(){
        toAliasBean(null, DangaMemCachedConfig.class);
    }

    /**
     * Test to alias bean null alias bean class.
     */
    @Test(expected = NullPointerException.class)
    public void testToAliasBeanNullAliasBeanClass(){
        toAliasBean(getResourceBundle("messages.memcached"), null);
    }
}
