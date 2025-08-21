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

import com.feilong.core.lang.ThreadUtil;
import com.feilong.core.lang.thread.PartitionPerElementHandler;
import com.feilong.core.lang.threadutil.entity.EmptyPartitionPerElementHandler;

@lombok.extern.slf4j.Slf4j
public class ExecuteUsePerElementWithParamMapAndPartitionPerElementHandlerTest extends AbstractExcuteTest{

    @Test
    public void testExecute(){
        AtomicInteger atomicInteger = new AtomicInteger(0);

        Map<String, Object> paramsMap1 = newHashMap(1);
        paramsMap1.put("result", atomicInteger);

        ThreadUtil.executeUsePerElement(
                        toList(2, 5, 6, 7),

                        paramsMap1,

                        (value,partitionThreadEntity,paramsMap) -> {
                            final AtomicInteger atomicInteger2 = (AtomicInteger) paramsMap.get("result");

                            log.trace(
                                            "{},BatchNumber:[{}],CurrentListSize:[{}],EachSize:[{}],Name:[{}],TotalListCount:[{}]",
                                            partitionThreadEntity.toString(),
                                            partitionThreadEntity.getBatchNumber(),
                                            partitionThreadEntity.getCurrentListSize(),
                                            partitionThreadEntity.getEachSize(),
                                            partitionThreadEntity.getName(),
                                            partitionThreadEntity.getTotalListCount());
                            atomicInteger2.addAndGet(value);

                        });

        AtomicInteger result = (AtomicInteger) paramsMap1.get("result");
        assertEquals(20, result.get());

        assertEquals(20, atomicInteger.get());
    }

    @Test
    public void testExecute1(){
        AtomicInteger atomicInteger = new AtomicInteger(0);

        Map<String, Object> paramsMap1 = newHashMap(1);
        paramsMap1.put("result", atomicInteger);
        paramsMap1.put(ThreadUtil.LOG_KEY_NAME_IN_MAP, "首页数据刷新");

        ThreadUtil.executeUsePerElement(toList(2, 5, 6, 7), paramsMap1, (value,partitionThreadEntity,paramsMap) -> {
            final AtomicInteger atomicInteger2 = (AtomicInteger) paramsMap.get("result");

            log.trace(
                            "{},BatchNumber:[{}],CurrentListSize:[{}],EachSize:[{}],Name:[{}],TotalListCount:[{}]",
                            partitionThreadEntity.toString(),
                            partitionThreadEntity.getBatchNumber(),
                            partitionThreadEntity.getCurrentListSize(),
                            partitionThreadEntity.getEachSize(),
                            partitionThreadEntity.getName(),
                            partitionThreadEntity.getTotalListCount());
            atomicInteger2.addAndGet(value);

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
        for (int i = 0; i <= 100; ++i){
            list.add(i);

        }

        ThreadUtil.executeUsePerElement(list, paramsMap1, (value,partitionThreadEntity,paramsMap) -> {
            final AtomicInteger atomicInteger2 = (AtomicInteger) paramsMap.get("result");

            log.trace(
                            "{},BatchNumber:[{}],CurrentListSize:[{}],EachSize:[{}],Name:[{}],TotalListCount:[{}]",
                            partitionThreadEntity.toString(),
                            partitionThreadEntity.getBatchNumber(),
                            partitionThreadEntity.getCurrentListSize(),
                            partitionThreadEntity.getEachSize(),
                            partitionThreadEntity.getName(),
                            partitionThreadEntity.getTotalListCount());
            atomicInteger2.addAndGet(value);

        });

        assertEquals(5050, atomicInteger.get());
    }

    //---------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testExecuteNullList(){
        ThreadUtil.executeUsePerElement(null, null, EmptyPartitionPerElementHandler.INSTANCE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecuteEmptyList(){
        ThreadUtil.executeUsePerElement(Collections.<Integer> emptyList(), null, EmptyPartitionPerElementHandler.INSTANCE);
    }

    //---------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testExecuteNullGroupRunnableBuilder(){
        ThreadUtil.executeUsePerElement(toList(2), null, (PartitionPerElementHandler<Integer>) null);
    }

}
