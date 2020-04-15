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
package com.feilong.core.lang.systemutiltest;

import static com.feilong.core.bean.ConvertUtil.toMap;
import static com.feilong.core.bean.ConvertUtil.toProperties;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertThat;

import java.util.Collections;

import org.junit.Test;

import com.feilong.core.lang.SystemUtil;

public class SetPropertiesFromPropertiesTest{

    @Test
    public void testGetPropertiesMap(){
        SystemUtil.setPropertiesFromProperties(toProperties(toMap("feilong_setPropertiesFromProperties", "1234")));
        assertThat(
                        SystemUtil.getPropertiesMap(),
                        allOf(//
                                        hasEntry("feilong_setPropertiesFromProperties", "1234")
                        //
                        ));
    }

    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testSetPropertiesFromMapTestNull(){
        SystemUtil.setPropertiesFromProperties(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPropertiesFromMapTestEmpty(){
        SystemUtil.setPropertiesFromProperties(toProperties(Collections.<String, String> emptyMap()));
    }
}
