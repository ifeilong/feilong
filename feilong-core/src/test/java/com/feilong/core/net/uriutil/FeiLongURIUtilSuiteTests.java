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
package com.feilong.core.net.uriutil;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * The Class FeiLongURIUtilSuiteTests.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 */
@RunWith(Suite.class)
@SuiteClasses({ //
                EncodeParameterizedTest.class,
                EncodeDefaultUtf8ParameterizedTest.class,

                EncodeUriTest.class,

                HasQueryTest.class,

                DecodeTest.class,
                DecodeDefaultUtf8Test.class,
                DecodeDefaultUtf8OrDefaultParameterizedTest.class,

                DecodeParameterizedTest.class,
                DecodeDefaultUtf8ParameterizedTest.class,

                CreateTest.class,
                CreateWithCharsetTypeTest.class,
//                
})
public class FeiLongURIUtilSuiteTests{

}
