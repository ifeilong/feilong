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
package com.feilong.core.util.maputil;

import static com.feilong.core.util.MapUtil.newHashMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.feilong.core.util.MapUtil;

/**
 * The Class MapUtilPutSumValueTest.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 */
public class PutSumValueTest{

    /**
     * Test put sum value.
     */
    @Test
    public void testPutSumValue(){
        Map<String, Integer> map = newHashMap();
        MapUtil.putSumValue(map, "1000001", 5);
        MapUtil.putSumValue(map, "1000002", 5);
        MapUtil.putSumValue(map, "1000002", 5);

        assertThat(map, allOf(hasEntry("1000001", 5), hasEntry("1000002", 10)));
    }

    /**
     * Test put sum value negative value.
     */
    @Test
    public void testPutSumValueNegativeValue(){
        Map<String, Integer> map = newHashMap();
        MapUtil.putSumValue(map, "1000001", 5);
        MapUtil.putSumValue(map, "1000002", 5);
        MapUtil.putSumValue(map, "1000002", -5);

        assertThat(map, allOf(hasEntry("1000001", 5), hasEntry("1000002", 0)));
    }

    /**
     * Test put sum value null map.
     */
    @Test(expected = NullPointerException.class)
    public void testPutSumValueNullMap(){
        MapUtil.putSumValue((Map<String, Integer>) null, "1000001", 5);
    }

    /**
     * Test put sum value null value.
     */
    @Test(expected = NullPointerException.class)
    public void testPutSumValueNullValue(){
        MapUtil.putSumValue(new HashMap<String, Integer>(), "1000001", null);
    }
}
