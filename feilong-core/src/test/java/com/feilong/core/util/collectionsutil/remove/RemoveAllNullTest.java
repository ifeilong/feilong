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
package com.feilong.core.util.collectionsutil.remove;

import static com.feilong.core.bean.ConvertUtil.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import java.util.List;

import org.junit.Test;

import com.feilong.core.util.CollectionsUtil;

public class RemoveAllNullTest{

    @Test
    public void testRemoveAllCollection(){
        List<String> list = toList("xinge", null, "feilong2", null, "feilong2");
        List<String> removeList = CollectionsUtil.removeAllNull(list);

        assertThat(removeList, contains("xinge", "feilong2", "feilong2"));
        assertThat(list, contains("xinge", null, "feilong2", null, "feilong2"));
    }

    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testRemoveAllNullObjectCollection(){
        CollectionsUtil.removeAllNull(null);
    }
}
