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
package com.feilong.json.format;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.Test;

import com.feilong.json.AbstractJsonTest;
import com.feilong.json.JsonUtil;
import com.feilong.json.entity.BeanWithXMLGregorianCalendar;

public class FormatBeanXMLGregorianCalendarTest extends AbstractJsonTest{

    @Test
    public void test(){
        BeanWithXMLGregorianCalendar bean = new BeanWithXMLGregorianCalendar();
        bean.setName("jim");

        @SuppressWarnings("restriction")
        XMLGregorianCalendar xmlGregorianCalendar = com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl.LEAP_YEAR_DEFAULT;
        bean.setBirthDate(xmlGregorianCalendar);

        String format = JsonUtil.format(bean);
        assertThat(
                        format,
                        allOf(//
                                        containsString("\"birthDate\": \"0400-01-01T00:00:00\""),
                                        containsString("\"name\": \"jim\"")
                        //
                        ));
    }

}
