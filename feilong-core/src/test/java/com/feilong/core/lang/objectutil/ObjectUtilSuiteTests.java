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
package com.feilong.core.lang.objectutil;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * The Class ObjectUtilSuiteTests.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 */
@RunWith(Suite.class)
@SuiteClasses({
                IsArrayParameterizedTest.class,
                IsPrimitiveArrayParameterizedTest.class,

                DefaultIfNullOrEmptyParameterizedTest.class,

                DefaultEmptySetIfNullParameterizedTest.class,
                DefaultEmptyListIfNullParameterizedTest.class,
                DefaultEmptyMapIfNullParameterizedTest.class,
                DefaultEmptyStringIfNullParameterizedTest.class,

                DefaultNewArrayListIfNullParameterizedTest.class,
                DefaultNewHashMapIfNullParameterizedTest.class,
                DefaultNewHashSetIfNullParameterizedTest.class,
                DefaultNewLinkedHashMapIfNullParameterizedTest.class,

                DefaultIfNullParameterizedTest.class,
                DefaultIfNullOrLessThanOneParameterizedTest.class,
                DefaultCountIfNullZeroOrGreaterThanParameterizedTest.class,
                DefaultPageNoParameterizedTest.class,
                DefaultZeroParameterizedTest.class,

                EqualsAnyTest.class,
                NewFromTest.class,

                ObjectUtilTest.class, })
public class ObjectUtilSuiteTests{

}
