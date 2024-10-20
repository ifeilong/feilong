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
import static com.feilong.core.bean.ConvertUtil.toMap;
import static com.feilong.core.util.CollectionsUtil.newArrayList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.feilong.core.util.CollectionsUtil;

public class GetPropertyValueListBeanWithReturnElementClassTest{

    /**
     * Map.
     */
    @Test
    public void testGetPropertyValueListMap(){
        List<Map<String, String>> list = newArrayList();
        list.add(toMap("key", "1"));
        list.add(toMap("key", "2"));
        list.add(toMap("key", "3"));

        List<Integer> resultList = CollectionsUtil.getPropertyValueList(list, "(key)", Integer.class);
        assertThat(resultList, contains(1, 2, 3));
    }

    /**
     * 集合.
     */
    @Test
    public void testGetPropertyValueListList(){
        List<List<String>> list = newArrayList();
        list.add(toList("小明", "18"));
        list.add(toList("小宏", "19"));
        list.add(toList("小振", "20"));

        List<Long> resultList = CollectionsUtil.getPropertyValueList(list, "[1]", Long.class);
        assertThat(resultList, contains(18L, 19L, 20L));
    }

}
