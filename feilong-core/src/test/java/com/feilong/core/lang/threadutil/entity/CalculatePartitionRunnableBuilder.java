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
package com.feilong.core.lang.threadutil.entity;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.feilong.core.lang.thread.PartitionRunnableBuilder;
import com.feilong.core.lang.thread.PartitionThreadEntity;

/**
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.10.4
 */
@lombok.extern.slf4j.Slf4j
public class CalculatePartitionRunnableBuilder implements PartitionRunnableBuilder<Integer>{

    // the static instance works for all types
    public static final CalculatePartitionRunnableBuilder INSTANCE = new CalculatePartitionRunnableBuilder();

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.core.lang.PartitionRunnableBuilder#build(java.util.List, com.feilong.core.lang.PartitionThreadEntity, java.util.Map)
     */
    @Override
    public Runnable build(final List<Integer> perBatchList,final PartitionThreadEntity partitionThreadEntity,Map<String, ?> paramsMap){
        final AtomicInteger atomicInteger = (AtomicInteger) paramsMap.get("result");

        return new Runnable(){

            @Override
            public void run(){
                for (Integer value : perBatchList){

                    log.trace(
                                    "{},BatchNumber:[{}],CurrentListSize:[{}],EachSize:[{}],Name:[{}],TotalListCount:[{}]",
                                    partitionThreadEntity.toString(),
                                    partitionThreadEntity.getBatchNumber(),
                                    partitionThreadEntity.getCurrentListSize(),
                                    partitionThreadEntity.getEachSize(),
                                    partitionThreadEntity.getName(),
                                    partitionThreadEntity.getTotalListCount());
                    atomicInteger.addAndGet(value);
                }
            }
        };
    }
}
