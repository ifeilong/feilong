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
import static com.feilong.core.util.MapUtil.newHashMap;
import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.feilong.core.lang.ThreadUtil;
import com.feilong.core.lang.threadutil.entity.EmptyPartitionPerElementHandler;

@lombok.extern.slf4j.Slf4j
public class ExecuteUsePerElementOnlyPartitionPerElementHandlerTest extends AbstractExcuteTest{

    @Test
    public void testExecute(){
        AtomicInteger atomicInteger = new AtomicInteger(0);

        Map<String, Object> paramsMap1 = newHashMap(1);
        paramsMap1.put("result", atomicInteger);
        //        paramsMap1.put(ThreadUtil.LOG_KEY_NAME_IN_MAP, "aaaaa");

        ThreadUtil.executeUsePerElement(
                        toList(2, 5, 6, 7),

                        (element,partitionThreadEntity,paramsMap) -> {
                            AtomicInteger atomicInteger2 = (AtomicInteger) paramsMap1.get("result");
                            log.trace(
                                            "{},BatchNumber:[{}],CurrentListSize:[{}],EachSize:[{}],Name:[{}],TotalListCount:[{}]",
                                            partitionThreadEntity.toString(),
                                            partitionThreadEntity.getBatchNumber(),
                                            partitionThreadEntity.getCurrentListSize(),
                                            partitionThreadEntity.getEachSize(),
                                            partitionThreadEntity.getName(),
                                            partitionThreadEntity.getTotalListCount());
                            atomicInteger2.addAndGet(element);
                        });

        AtomicInteger result = (AtomicInteger) paramsMap1.get("result");
        assertEquals(20, result.get());
        assertEquals(20, atomicInteger.get());
    }

    //---------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testExecuteNullList(){
        ThreadUtil.executeUsePerElement(null, EmptyPartitionPerElementHandler.INSTANCE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecuteEmptyList(){
        ThreadUtil.executeUsePerElement(Collections.<Integer> emptyList(), EmptyPartitionPerElementHandler.INSTANCE);
    }

}
