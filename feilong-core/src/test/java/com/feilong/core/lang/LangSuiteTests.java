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
package com.feilong.core.lang;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.feilong.core.lang.annotation.AnnotationToStringBuilderSuiteTests;
import com.feilong.core.lang.arrayutiltest.ArrayUtilSuiteTests;
import com.feilong.core.lang.classloaderutiltest.ClassLoaderUtilSuiteTests;
import com.feilong.core.lang.classutiltest.ClassUtilSuiteTests;
import com.feilong.core.lang.enumutiltest.EnumUtilSuiteTests;
import com.feilong.core.lang.numberutiltest.NumberUtilSuiteTests;
import com.feilong.core.lang.objectutiltest.ObjectUtilSuiteTests;
import com.feilong.core.lang.reflect.ReflectSuiteTests;
import com.feilong.core.lang.stringutiltest.StringUtilSuiteTests;
import com.feilong.core.lang.systemutiltest.SystemUtilSuiteTests;
import com.feilong.core.lang.threadutiltest.ThreadUtilSuiteTests;

/**
 * The Class FeiLongLangSuiteTests.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.8.3
 */
@RunWith(Suite.class)
@SuiteClasses({
                ArrayUtilSuiteTests.class,

                ClassLoaderUtilSuiteTests.class,

                EnumUtilSuiteTests.class,

                StringUtilSuiteTests.class,
                ReflectSuiteTests.class,
                NumberUtilSuiteTests.class,
                ObjectUtilSuiteTests.class,
                ClassUtilSuiteTests.class,

                ThreadUtilSuiteTests.class,

                SystemUtilSuiteTests.class,
                AnnotationToStringBuilderSuiteTests.class,
        //
})
public class LangSuiteTests{

}
