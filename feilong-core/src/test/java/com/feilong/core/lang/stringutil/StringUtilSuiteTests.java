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
package com.feilong.core.lang.stringutil;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * The Class FeiLongStringUtilSuiteTests.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 */
@RunWith(Suite.class)
@SuiteClasses({ //
                FormatParameterizedTest.class,
                TrimAndEqualsIgnoreCaseParameterizedTest.class,

                CleanTest.class,

                GetBytesTest.class,
                GetBytesAndCharsetNameTest.class,

                ReplaceAllParameterizedTest.class,
                ReplaceParameterizedTest.class,
                ReplaceValuesMapTest.class,
                SubstringBeginIndexTest.class,
                SubstringLastTest.class,
                SubstringStartIndexAndLengthParameterizedTest.class,
                SubstringWithoutLastLastLengthTest.class,
                SubstringWithoutLastLastStringTest.class,
                TokenizeToStringArrayTest.class,
                TokenizeToStringArrayWithArgsTest.class,
                TokenizeToArrayContainsParameterizedTest.class,
                TokenizeToArrayContainsDelimitersParameterizedTest.class,

                ToSingleValueMapTest.class,
                ToSingleValueMapConvertTest.class,
                ToMultiValueMapTest.class,
//
})
public class StringUtilSuiteTests{

}
