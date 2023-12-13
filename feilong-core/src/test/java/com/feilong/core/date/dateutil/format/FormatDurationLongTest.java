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
package com.feilong.core.date.dateutil.format;

import static com.feilong.core.bean.ConvertUtil.toLong;
import static com.feilong.core.date.DateUtil.formatDuration;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.feilong.core.TimeInterval;

public class FormatDurationLongTest{

    @Test
    public void testFormatDurationLong(){
        assertEquals("25秒841毫秒", formatDuration(25841));
    }

    @Test
    public void testFormatDurationLong1(){
        assertEquals("365天", formatDuration(toLong(TimeInterval.SECONDS_PER_YEAR) * 1000));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDuration3(){
        formatDuration(-1);
    }

    @Test
    public void testFormatDuration_zero(){
        assertEquals("0", formatDuration(0));
    }
}