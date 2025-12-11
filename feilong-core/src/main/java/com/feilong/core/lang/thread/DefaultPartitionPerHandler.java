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
package com.feilong.core.lang.thread;

import static com.feilong.core.Validator.isNullOrEmpty;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.feilong.tools.log.LogHelper;
import com.feilong.tools.log.ProcessLogParamEntity;

/**
 * 默认的PartitionPerHandler.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @param <T>
 *            the generic type
 * @since 4.0.6
 */
@lombok.extern.slf4j.Slf4j
public class DefaultPartitionPerHandler<T> implements PartitionPerHandler<T>{

    /** 计数器. */
    private final AtomicInteger                 counter;

    /** 总任务执行开始时间. */
    private final long                          allTimeMillis;

    /** 分区中的每个线程中的每个元素执行. */
    private final PartitionPerElementHandler<T> partitionPerElementHandler;

    //---------------------------------------------------------------
    /**
     * Instantiates a new default partition per handler.
     *
     * @param partitionPerElementHandler
     *            分区中的每个线程中的每个元素执行
     */
    public DefaultPartitionPerHandler(PartitionPerElementHandler<T> partitionPerElementHandler){
        super();
        this.partitionPerElementHandler = partitionPerElementHandler;
        this.counter = new AtomicInteger(0);
        this.allTimeMillis = System.currentTimeMillis();
    }

    //---------------------------------------------------------------

    /**
     * Handle.
     *
     * @param perBatchList
     *            the per batch list
     * @param partitionThreadEntity
     *            the partition thread entity
     * @param paramsMap
     *            the params map
     */
    @Override
    public void handle(List<T> perBatchList,PartitionThreadEntity partitionThreadEntity,Map<String, ?> paramsMap){
        if (isNullOrEmpty(perBatchList)){
            log.warn("{} perBatchListIsNullOrEmpty!", getLogKey(partitionThreadEntity, paramsMap));
            return;
        }

        //---------------------------------------------------------------
        for (T t : perBatchList){
            int current = counter.addAndGet(1);
            long currentTimeMillis = System.currentTimeMillis();
            try{
                partitionPerElementHandler.handle(t, partitionThreadEntity, paramsMap);

                log(t, current, currentTimeMillis, partitionThreadEntity, paramsMap);
            }catch (Throwable e){
                log.error(getLogKey(partitionThreadEntity, paramsMap) + " " + t + " partitionThreadEntity:" + partitionThreadEntity, e);
            }
        }

    }

    //---------------------------------------------------------------

    /**
     * 记录执行日志
     */
    private void log(T t,int current,long currentTimeMillis,PartitionThreadEntity partitionThreadEntity,Map<String, ?> paramsMap){
        if (log.isInfoEnabled()){
            String processLog = LogHelper.getProcessLog(
                            new ProcessLogParamEntity(
                                            current,
                                            partitionThreadEntity.getTotalListCount(),
                                            currentTimeMillis,
                                            allTimeMillis));
            log.info("{} currentElement:[{}] {}", getLogKey(partitionThreadEntity, paramsMap), t, processLog);
        }
    }

    private static String getLogKey(PartitionThreadEntity partitionThreadEntity,Map<String, ?> paramsMap){
        return AbstractPartitionThreadExecutor.getLogKey(paramsMap);
    }

}
