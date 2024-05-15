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
package com.feilong.core.bean.convertutil;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * The Class FeiLongConvertUtilSuiteTests.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 */
@RunWith(Suite.class)
@SuiteClasses({ //
                ConvertClassTest.class,

                ToArrayCollectionClassTest.class,
                ToArrayStringArrayClassTest.class,
                ToArrayVarargsTest.class,

                ToBigDecimalParameterizedTest.class,
                ToBigDecimalDefaultValueParameterizedTest.class,
                ToBooleanParameterizedTest.class,
                ToBooleanDefaultValueParameterizedTest.class,
                ToEnumerationTest.class,

                ToIntegerDefaultValueParameterizedTest.class,
                ToIntegerParameterizedTest.class,
                ToIntegersParameterizedTest.class,
                ToIntegerListTest.class,
                ToIntegerListParameterizedTest.class,

                ToIteratorTest.class,

                ToListCollectionTest.class,
                ToListEnumerationTest.class,
                ToListVarargsTest.class,
                ToListArrayTest.class,

                ToLocaleTest.class,

                ToLongDefaultValueParameterizedTest.class,
                ToLongParameterizedTest.class,
                ToLongsParameterizedTest.class,
                ToLongListTest.class,
                ToLongListParameterizedTest.class,

                ToStringListTest.class,
                ToStringListParameterizedTest.class,

                ToMapArrayTest.class,
                ToMapCollectionTest.class,
                ToMapKeyValueTest.class,
                ToMapPropertiesTest.class,
                ToMapTargetTypeClassTest.class,
                ToMapValueTargetTypeClassTest.class,
                ToMapTransformerTest.class,

                ToPropertiesTest.class,

                ToStringArrayToStringConfigParameterizedTest.class,
                ToStringCollectionToStringConfigParameterizedTest.class,
                ToStringArrayAndConnectorParameterizedTest.class,
                ToStringCollectionAndConnectorParameterizedTest.class,

                ToStringParameterizedTest.class,
                ToStringsTest.class,

                ToSetVarargsTest.class,

//
})
public class ConvertUtilSuiteTests{

}
