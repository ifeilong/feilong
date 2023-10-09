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

import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.date.DateUtil.formatDurationUseBeginTimeMillis;
import static com.feilong.core.util.CollectionsUtil.getPropertyValueList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.store.member.User;

/**
 * The Class GetPropertyValueListTest.
 */
public class GetPropertyValueListPerformanceTest{

    /** The Constant log. */
    private static final Logger     LOGGER = LoggerFactory.getLogger(GetPropertyValueListPerformanceTest.class);

    //---------------------------------------------------------------
    private static final List<User> list   = toList(                                                  //
                    new User(2L),
                    new User(5L),
                    new User(5L));

    //---------------------------------------------------------------

    /**
     * Test get property value list.
     */
    @Test
    public void testGetPropertyValueList(){

        long beginTimeMillis = System.currentTimeMillis();

        for (int i = 0; i < 100000; ++i){

            List<Long> resultList = getPropertyValueList(list, User::getId);

            //        List<Long> resultList = 
            //                        
            //                        
            //                        CollectionsUtil.getPropertyValueList(list, "id");
            assertThat(resultList, contains(2L, 5L, 5L));

            resultList.add(7L);
            resultList.add(8L);

            assertThat(resultList, contains(2L, 5L, 5L, 7L, 8L));

        }
        if (LOGGER.isInfoEnabled()){
            LOGGER.info("useTime: [{}]", formatDurationUseBeginTimeMillis(beginTimeMillis));
        }

    }

    @Test
    public void testGetPropertyValueList1(){

        long beginTimeMillis = System.currentTimeMillis();

        for (int i = 0; i < 100000; ++i){

            List<Long> resultList = getPropertyValueList(list, "id");

            //        List<Long> resultList = 
            //                        
            //                        
            //                        CollectionsUtil.getPropertyValueList(list, "id");
            assertThat(resultList, contains(2L, 5L, 5L));

            resultList.add(7L);
            resultList.add(8L);

            assertThat(resultList, contains(2L, 5L, 5L, 7L, 8L));

        }
        if (LOGGER.isInfoEnabled()){
            LOGGER.info("useTime: [{}]", formatDurationUseBeginTimeMillis(beginTimeMillis));
        }

    }

}
