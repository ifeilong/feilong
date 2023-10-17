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

import static com.feilong.core.util.ResourceBundleUtil.toBean;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.feilong.core.entity.DangaMemCachedNoAliasConfig;
import com.feilong.lib.beanutils.ConvertUtils;
import com.feilong.lib.beanutils.converters.ArrayConverter;
import com.feilong.lib.beanutils.converters.StringConverter;

public class ToBeanBaseNameTest{

    private static final String baseName = "messages/memcachedBean";

    @Test
    public void testToBean(){
        DangaMemCachedNoAliasConfig config = toBean(baseName, DangaMemCachedNoAliasConfig.class);

        //---------------------------------------------------------------

        //        LOGGER.info("" + config);

        //---------------------------------------------------------------

        assertThat(
                        config,
                        allOf(//
                                        hasProperty("serverList", arrayContaining("172.20.3-1.23", "11211", "172.20.31.22", "11211")),
                                        hasProperty("minConnection", is(5)),
                                        hasProperty("minConnection", is(5)),
                                        hasProperty("nagle", is(false))
                        //
                        ));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testToBean1(){
        ArrayConverter arrayConverter = new ArrayConverter(String[].class, new StringConverter(), 2);
        char[] allowedChars = { ':' };
        arrayConverter.setAllowedChars(allowedChars);

        ConvertUtils.register(arrayConverter, String[].class);

        DangaMemCachedNoAliasConfig config = toBean(baseName, DangaMemCachedNoAliasConfig.class);
        assertThat(
                        config,
                        allOf(//
                                        hasProperty("serverList", arrayContaining("172.20.3-1.23:11211", "172.20.31.22:11211")),
                                        hasProperty("minConnection", is(5)),
                                        hasProperty("minConnection", is(5)),
                                        hasProperty("nagle", is(false))
                        //
                        ));

        //        ConvertUtils.deregister(String[].class);
        ConvertUtils.deregister();
    }

    //---------------------------------------------------------------

    /**
     * Test to bean null resource bundle.
     */
    @Test(expected = NullPointerException.class)
    public void testToBeanNullResourceBundle(){
        toBean((String) null, DangaMemCachedNoAliasConfig.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBeanNullResourceBundle1(){
        toBean("", DangaMemCachedNoAliasConfig.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBeanNullResourceBundle2(){
        toBean("  ", DangaMemCachedNoAliasConfig.class);
    }

    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testToBeanNullBeanClass(){

        toBean(baseName, null);
    }
}
