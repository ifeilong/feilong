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

import static com.feilong.core.date.DateUtil.formatElapsedTime;

import org.junit.Rule;
import org.junit.Test;

import com.feilong.test.rule.Repeat;
import com.feilong.test.rule.RepeatRule;

public class FormatElapsedTimeTest{

    @Rule
    public RepeatRule repeatRule = new RepeatRule();

    @Test
    @Repeat(10)
    public void testFormatElapsedTime(){
        long t1 = 1689851445715L;
        System.out.println(formatElapsedTime(t1));
    }

}