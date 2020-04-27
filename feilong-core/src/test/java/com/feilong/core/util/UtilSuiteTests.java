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
package com.feilong.core.util;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.feilong.core.util.aggregateutiltest.AggregateUtilSuiteTests;
import com.feilong.core.util.closure.ClosureSuiteTests;
import com.feilong.core.util.collectionsutiltest.CollectionsUtilSuiteTests;
import com.feilong.core.util.comparator.ComparatorSuiteTests;
import com.feilong.core.util.enumerationutiltest.EnumerationUtilParameterizedTest;
import com.feilong.core.util.equator.IgnoreCaseEquatorTest;
import com.feilong.core.util.maputiltest.MapUtilSuiteTests;
import com.feilong.core.util.predicate.BeanPredicateUtilSuiteTests;
import com.feilong.core.util.randomutiltest.RandomUtilSuiteTests;
import com.feilong.core.util.regexutiltest.RegexUtilSuiteTests;
import com.feilong.core.util.resourcebundleutiltest.ResourceBundleUtilSuiteTests;
import com.feilong.core.util.sortutiltest.SortUtilSuiteTests;
import com.feilong.core.util.transformer.TransformerSuiteTests;

@RunWith(Suite.class)
@SuiteClasses({ //
                EnumerationUtilParameterizedTest.class,
                IgnoreCaseEquatorTest.class,

                BeanPredicateUtilSuiteTests.class,

                RandomUtilSuiteTests.class,
                ResourceBundleUtilSuiteTests.class,
                AggregateUtilSuiteTests.class,
                RegexUtilSuiteTests.class,
                MapUtilSuiteTests.class,
                CollectionsUtilSuiteTests.class,
                SortUtilSuiteTests.class,

                ComparatorSuiteTests.class,

                TransformerSuiteTests.class,
                ClosureSuiteTests.class,
        //
})
public class UtilSuiteTests{

}
