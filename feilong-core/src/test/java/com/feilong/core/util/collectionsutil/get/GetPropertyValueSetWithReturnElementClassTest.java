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
import static java.util.Collections.emptySet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.feilong.core.util.CollectionsUtil;
import com.feilong.store.member.User;

/**
 * The Class CollectionsUtilGetPropertyValueSetTest.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 */
public class GetPropertyValueSetWithReturnElementClassTest{

    /**
     * Test get property value set.
     */
    @Test
    public void testGetPropertyValueSet(){
        List<User> list = toList(//
                        new User(2L),
                        new User(5L),
                        new User(5L));

        Set<Integer> set = CollectionsUtil.getPropertyValueSet(list, "id", Integer.class);
        assertThat(set, contains(2, 5));
    }

    /**
     * Test get property value set null collection.
     */
    @Test
    public void testGetPropertyValueSetNullCollection(){
        assertEquals(emptySet(), CollectionsUtil.getPropertyValueSet(null, "id", Integer.class));
    }

    /**
     * Test get property value set empty collection.
     */
    @Test
    public void testGetPropertyValueSetEmptyCollection(){
        assertEquals(emptySet(), CollectionsUtil.getPropertyValueSet(new ArrayList<>(), "id", Integer.class));
    }

    /**
     * Test get property value set null property name.
     */
    @Test(expected = NullPointerException.class)
    public void testGetPropertyValueSetNullPropertyName(){
        List<User> list = toList(new User(2L), new User(5L), new User(5L));
        CollectionsUtil.getPropertyValueSet(list, null, Integer.class);
    }

    /**
     * Test get property value set empty property name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyValueSetEmptyPropertyName(){
        List<User> list = toList(new User(2L), new User(5L), new User(5L));
        CollectionsUtil.getPropertyValueSet(list, "", Integer.class);
    }

    /**
     * Test get property value set empty property name 1.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyValueSetEmptyPropertyName1(){
        List<User> list = toList(new User(2L), new User(5L), new User(5L));
        CollectionsUtil.getPropertyValueSet(list, " ", Integer.class);
    }

}
