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
package com.feilong.json.jsonlib;

import java.util.Calendar;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FormatBeanCalendarTest{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(FormatBeanCalendarTest.class);

    //---------------------------------------------------------------

    @Test
    public void test(){

        BeanWithCalendar beanWithCalendar = new BeanWithCalendar();
        beanWithCalendar.setName("jim");
        beanWithCalendar.setCalendar(Calendar.getInstance());

        LOGGER.debug(JsonUtil.format(beanWithCalendar));
    }

    public class BeanWithCalendar{

        private String   name;

        private Calendar calendar;

        /**
         * @return the name
         */
        public String getName(){
            return name;
        }

        /**
         * @param name
         *            the name to set
         */
        public void setName(String name){
            this.name = name;
        }

        /**
         * @return the calendar
         */
        public Calendar getCalendar(){
            return calendar;
        }

        /**
         * @param calendar
         *            the calendar to set
         */
        public void setCalendar(Calendar calendar){
            this.calendar = calendar;
        }

    }

}
