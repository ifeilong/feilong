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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.feilong.json.format.sdk.PropertyFilterTest;

@RunWith(Suite.class)
@SuiteClasses({ //

                FormatBeanIgnoreNullTest.class,

                FormatBeanErrorPropertyTest.class,

                FormatBeanIgnoreNullWithIsIgnoreNullValueElementTest.class,
                FormatBeanIgnoreNullWithIsIgnoreNullValueElementAndIfIgnoreNullValueElementIncludesTest.class,

                FormatBeanSensitiveWordsTest.class,
                FormatBeanSensitiveWordsNoTest.class,

                FormatBeanBigDecimalTest.class,

                FormatBeanFileTest.class,

                //JDK17 运行不通过cannot access class com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl (in module java.xml) because module java.xml does not export com.sun.org.apache.xerces.internal.jaxp.datatype to unnamed module @0x37a71e93
                //                FormatBeanXMLGregorianCalendarTest.class,
                FormatUriTest.class,
                FormatUrlTest.class,

                FormatStringTest.class,
                FormatBeanExcludesTest.class,

                FormatBeanSortBeanPropertyTest.class,

                //---------------------------------------------------------------
                //filter
                PropertyFilterTest.class,
                FormatWithIncludesTest.class,
//
})
public class FormatSuiteTests{

}
