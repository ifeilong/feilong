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
import static com.feilong.core.util.ResourceBundleUtil.toMap;
import static java.util.Collections.emptyMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.ResourceBundle;

import org.junit.Test;

/**
 * The Class ResourceBundleUtilToMapTest.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 */
public class ToMapTest{

    /**
     * Test to map.
     */
    @Test
    public void testToMap(){
        Map<String, String> map = toMap(getResourceBundle("messages/memcached"));
        assertThat(map, hasKey("memcached.alivecheck"));
    }

    /**
     * Test to map empty.
     */
    @Test
    public void testToMapEmpty(){
        Map<String, String> map = toMap(getResourceBundle("messages/empty"));
        assertEquals(emptyMap(), map);
    }

    /**
     * Test to map null resource bundle.
     */
    @Test(expected = NullPointerException.class)
    public void testToMapNullResourceBundle(){
        toMap((ResourceBundle) null);
    }
}
