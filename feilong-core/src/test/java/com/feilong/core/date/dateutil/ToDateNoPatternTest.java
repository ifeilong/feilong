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
package com.feilong.core.date.dateutil;

import static com.feilong.core.date.DateUtil.toDate;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

import com.feilong.core.DatePattern;
import com.feilong.core.date.DateUtil;

public class ToDateNoPatternTest{

    @Test
    public void testToDateTest(){
        Date date = toDate("2022-09-01");
        assertTrue("2022-09-01 00:00:00".equals(DateUtil.toString(date, DatePattern.COMMON_DATE_AND_TIME)));
    }

    /**
     * Test to date null.
     */
    @Test(expected = NullPointerException.class)
    public void testToDateNull(){
        toDate((String) null);
    }

    /**
     * Test to date empty 1.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToDateEmpty1(){
        toDate(" ");
    }

    //---------------------------------------------------------------

    /**
     * Test to date empty patterns.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToDateEmptyPatterns(){
        toDate("2022-09-01 15:36");
    }

    /**
     * Test to date null pattern element.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToDateNullPatternElement(){
        toDate("2022-09-01 15:36");
    }

    /**
     * Test to date 1.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToDate1(){
        toDate("2022-09-01 15:36 ");
    }

}