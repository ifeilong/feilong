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
package com.feilong.core.lang.threadutil;

import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.util.CollectionsUtil.newArrayList;
import static com.feilong.core.util.MapUtil.newHashMap;
import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.lang.ThreadUtil;
import com.feilong.core.lang.thread.PartitionPerHandler;
import com.feilong.core.lang.threadutil.entity.EmptyPartitionPerHandler;

public class ExecuteConfigWithParamMapAndPartitionPerHandlerTest extends AbstractExcuteTest{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecuteConfigWithParamMapAndPartitionPerHandlerTest.class);

    //---------------------------------------------------------------

    @Test
    public void testExecute(){
        AtomicInteger atomicInteger = new AtomicInteger(0);

        Map<String, Object> paramsMap1 = newHashMap(1);
        paramsMap1.put("result", atomicInteger);

        ThreadUtil.execute(
                        toList(2, 5, 6, 7),

                        paramsMap1,

                        (perBatchList,partitionThreadEntity,paramsMap) -> {
                            final AtomicInteger atomicInteger2 = (AtomicInteger) paramsMap.get("result");
                            for (Integer value : perBatchList){

                                LOGGER.trace(
                                                "{},BatchNumber:[{}],CurrentListSize:[{}],EachSize:[{}],Name:[{}],TotalListCount:[{}]",
                                                partitionThreadEntity.toString(),
                                                partitionThreadEntity.getBatchNumber(),
                                                partitionThreadEntity.getCurrentListSize(),
                                                partitionThreadEntity.getEachSize(),
                                                partitionThreadEntity.getName(),
                                                partitionThreadEntity.getTotalListCount());
                                atomicInteger2.addAndGet(value);
                            }

                        });

        AtomicInteger result = (AtomicInteger) paramsMap1.get("result");
        assertEquals(20, result.get());

        assertEquals(20, atomicInteger.get());
    }

    @Test
    public void testExecute2(){
        AtomicInteger atomicInteger = new AtomicInteger(0);

        Map<String, Object> paramsMap1 = newHashMap(1);
        paramsMap1.put("result", atomicInteger);

        List<Integer> list = newArrayList();
        for (int i = 0; i < 300000; ++i){
            list.add(i);

        }

        ThreadUtil.execute(list, paramsMap1, (perBatchList,partitionThreadEntity,paramsMap) -> {
            final AtomicInteger atomicInteger2 = (AtomicInteger) paramsMap.get("result");
            for (Integer value : perBatchList){

                LOGGER.trace(
                                "{},BatchNumber:[{}],CurrentListSize:[{}],EachSize:[{}],Name:[{}],TotalListCount:[{}]",
                                partitionThreadEntity.toString(),
                                partitionThreadEntity.getBatchNumber(),
                                partitionThreadEntity.getCurrentListSize(),
                                partitionThreadEntity.getEachSize(),
                                partitionThreadEntity.getName(),
                                partitionThreadEntity.getTotalListCount());
                atomicInteger2.addAndGet(value);
            }

        });

    }

    //---------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testExecuteNullList(){
        ThreadUtil.execute(null, null, EmptyPartitionPerHandler.INSTANCE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecuteEmptyList(){
        ThreadUtil.execute(Collections.<Integer> emptyList(), null, EmptyPartitionPerHandler.INSTANCE);
    }

    //---------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testExecuteNullGroupRunnableBuilder(){
        ThreadUtil.execute(toList(2), null, (PartitionPerHandler<Integer>) null);
    }

}
