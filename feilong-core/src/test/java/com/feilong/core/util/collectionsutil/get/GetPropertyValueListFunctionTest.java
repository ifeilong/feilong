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
package com.feilong.core.util.collectionsutil.get;

import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.util.CollectionsUtil.getPropertyValueList;
import static com.feilong.core.util.CollectionsUtil.size;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.function.Function;

import org.junit.Test;

import com.feilong.store.member.User;

public class GetPropertyValueListFunctionTest{

    private static final List<User> list = toList( //
                    new User(2L),
                    new User(5L),
                    new User(5L));

    //---------------------------------------------------------------

    @Test
    public void testGetPropertyValueList(){
        List<Long> resultList = getPropertyValueList(list, User::getId);

        assertThat(resultList, contains(2L, 5L, 5L));

        resultList.add(7L);
        resultList.add(8L);

        assertThat(resultList, contains(2L, 5L, 5L, 7L, 8L));

    }

    @Test
    public void testGetPropertyValueList1(){
        List<Long> resultList = getPropertyValueList(null, User::getId);
        assertTrue(size(resultList) == 0);

    }

    @Test
    public void testGetPropertyValueListEmptyList(){
        List<Long> resultList = getPropertyValueList(emptyList(), User::getId);
        assertTrue(size(resultList) == 0);

    }

    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testGetPropertyValueListNullFunction(){
        getPropertyValueList(list, (Function) null);
    }

}
