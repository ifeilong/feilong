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
package com.feilong.core.util.collectionsutil;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.feilong.core.util.collectionsutil.add.AddAllIgnoreNullTest;
import com.feilong.core.util.collectionsutil.add.AddIfConditionTest;
import com.feilong.core.util.collectionsutil.add.AddIgnoreNullOrEmptyIterableTest;
import com.feilong.core.util.collectionsutil.add.AddIgnoreNullOrEmptyTest;
import com.feilong.core.util.collectionsutil.get.GetPropertyValueListBeanTest;
import com.feilong.core.util.collectionsutil.get.GetPropertyValueListBeanWithReturnElementClassTest;
import com.feilong.core.util.collectionsutil.get.GetPropertyValueListFunctionTest;
import com.feilong.core.util.collectionsutil.get.GetPropertyValueListPropertyNameTest;
import com.feilong.core.util.collectionsutil.get.GetPropertyValueListTest;
import com.feilong.core.util.collectionsutil.get.GetPropertyValueListWithReturnElementClassTest;
import com.feilong.core.util.collectionsutil.get.GetPropertyValueMapFunctionTest;
import com.feilong.core.util.collectionsutil.get.GetPropertyValueMapTest;
import com.feilong.core.util.collectionsutil.get.GetPropertyValueSetTest;
import com.feilong.core.util.collectionsutil.get.GetPropertyValueSetWithReturnElementClassTest;
import com.feilong.core.util.collectionsutil.group.GroupOneLambdaTest;
import com.feilong.core.util.collectionsutil.group.GroupOneTest;
import com.feilong.core.util.collectionsutil.group.GroupWithPropertyNameAndPredicateTest;
import com.feilong.core.util.collectionsutil.group.GroupWithPropertyNameTest;
import com.feilong.core.util.collectionsutil.group.GroupWithTransformerAndPredicateTest;
import com.feilong.core.util.collectionsutil.group.GroupWithTransformerTest;
import com.feilong.core.util.collectionsutil.remove.RemoveAllCollectionTest;
import com.feilong.core.util.collectionsutil.remove.RemoveAllNullTest;
import com.feilong.core.util.collectionsutil.remove.RemoveDuplicateOnePropertyNameTest;
import com.feilong.core.util.collectionsutil.remove.RemoveDuplicatePropertyNamesTest;
import com.feilong.core.util.collectionsutil.remove.RemoveDuplicateTest;
import com.feilong.core.util.collectionsutil.remove.RemoveElementTest;
import com.feilong.core.util.collectionsutil.select.SelectArrayTest;
import com.feilong.core.util.collectionsutil.select.SelectCollectionTest;
import com.feilong.core.util.collectionsutil.select.SelectNotNullOrEmptyStringPredicateTest;
import com.feilong.core.util.collectionsutil.select.SelectPredicateTest;
import com.feilong.core.util.collectionsutil.select.SelectRegexStringPredicateTest;
import com.feilong.core.util.collectionsutil.select.SelectRejectedArrayTest;
import com.feilong.core.util.collectionsutil.select.SelectRejectedCollectionTest;
import com.feilong.core.util.collectionsutil.select.SelectRejectedMapTest;
import com.feilong.core.util.collectionsutil.select.SelectRejectedPredicateTest;
import com.feilong.core.util.collectionsutil.select.SelectWithMapTest;

@RunWith(Suite.class)
@SuiteClasses({

                NewTest.class,

                AddAllIgnoreNullTest.class,
                AddIgnoreNullOrEmptyTest.class,
                AddIgnoreNullOrEmptyIterableTest.class,
                AddIfConditionTest.class,

                IndexOfTest.class,

                ContainsTest.class,
                ContainsAnyTest.class,
                ContainsAnyStringsTest.class,
                ContainsTrimAndIgnoreCaseTest.class,

                CollectIterableTest.class,
                CollectIterableBeanTypeTest.class,
                CollectIteratorTest.class,

                ExistTest.class,
                ExistWithMapTest.class,
                ExistWithPredicateTest.class,

                FindTest.class,
                FindWithMapTest.class,
                FindWithPredicateTest.class,

                GetPropertyValueListTest.class,
                GetPropertyValueListFunctionTest.class,
                GetPropertyValueListBeanTest.class,
                GetPropertyValueListPropertyNameTest.class,

                GetPropertyValueListBeanWithReturnElementClassTest.class,
                GetPropertyValueSetWithReturnElementClassTest.class,
                GetPropertyValueListWithReturnElementClassTest.class,

                GetPropertyValueSetTest.class,
                GetPropertyValueMapTest.class,
                GetPropertyValueMapFunctionTest.class,

                GroupWithPropertyNameTest.class,
                GroupWithPropertyNameAndPredicateTest.class,
                GroupWithTransformerTest.class,
                GroupWithTransformerAndPredicateTest.class,
                GroupOneTest.class,
                GroupOneLambdaTest.class,

                SelectWithMapTest.class,
                SelectPredicateTest.class,
                SelectArrayTest.class,
                SelectCollectionTest.class,

                SelectRejectedMapTest.class,
                SelectRejectedArrayTest.class,
                SelectRejectedCollectionTest.class,
                SelectRejectedPredicateTest.class,

                RemoveDuplicateTest.class,
                RemoveDuplicateOnePropertyNameTest.class,
                RemoveDuplicatePropertyNamesTest.class,
                RemoveElementTest.class,
                RemoveAllNullTest.class,
                RemoveAllCollectionTest.class,

                SplitDuplicatePropertyNamesTest.class,

                ForEachTest.class,

                SelectRegexStringPredicateTest.class,
                SelectNotNullOrEmptyStringPredicateTest.class,

                FirstTest.class,
                LastTest.class,
                SizeTest.class,
//
})
public class CollectionsUtilSuiteTests{

}
