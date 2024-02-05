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
package com.feilong.core.lang.systemutil;

import static com.feilong.core.bean.ConvertUtil.toMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;

import java.util.Collections;

import org.junit.Test;

import com.feilong.core.lang.SystemUtil;

public class SetPropertiesFromMapTest{

    @Test
    public void testGetPropertiesMap(){
        SystemUtil.setPropertiesFromMap(toMap("feilongtest_setPropertiesFromMap", "1234"));
        assertThat(
                        SystemUtil.getPropertiesMap(),
                        allOf(//
                                        hasEntry("feilongtest_setPropertiesFromMap", "1234")
                        //
                        ));
    }

    @Test(expected = NullPointerException.class)
    public void testSetPropertiesFromMapTestNull(){
        SystemUtil.setPropertiesFromMap(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPropertiesFromMapTestEmpty(){
        SystemUtil.setPropertiesFromMap(Collections.<String, String> emptyMap());
    }

}
