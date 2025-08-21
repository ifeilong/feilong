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
package com.feilong.formatter;

import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.date.DateUtil.now;
import static com.feilong.core.lang.StringUtil.EMPTY;
import static com.feilong.formatter.FormatterUtil.formatToSimpleTable;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.feilong.store.member.Person;
import com.feilong.test.AbstractTest;

@lombok.extern.slf4j.Slf4j
public class FormatterBeanWithSortTest extends AbstractTest{

    @Test
    public void testFormatToSimpleTable1(){
        Person person = new Person("feilong", now());
        log.debug(formatToSimpleTable(toList(person), "name", "dateAttr"));
    }

    //---------------------------------------------------------------
    @Test
    public void test(){
        assertEquals(EMPTY, formatToSimpleTable(null, "name"));
    }

}
