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
package com.feilong.context;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.date.DateUtil.formatDurationUseBeginTimeMillis;

import java.util.List;

/**
 * 数据 task.
 * 
 * <p>
 * 先进行数据查询, 如果数据是 null 或者 empty 以 info level 日志输出;<br>
 * 如果不是 null 或者 empty ,那么进行调用 {@link #handle(List)} 执行
 * </p>
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @param <T>
 *            the generic type
 * @since 2.0.0
 */
@lombok.extern.slf4j.Slf4j
public abstract class AbstractDataListTask<T extends Data> implements Task<Void>{

    /** 需要被处理的数据查询器. */
    private DataListQuery<T> dataListQuery;

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.context.Task#run()
     */
    @Override
    public Void run(){
        long beginTimeMillis = System.currentTimeMillis();

        String taskName = this.getClass().getName();
        //---------------------------------------------------------------
        //查询需要处理的数据
        List<T> dataList = dataListQuery.query();
        if (isNullOrEmpty(dataList)){

            //如果没有,那么就简单的日志
            if (log.isInfoEnabled()){
                String name = dataListQuery.getClass().getName();
                log.info(
                                "[{}],use dataListQuery:[{}] query no data list,use time: [{}]",
                                taskName,
                                name,
                                formatDurationUseBeginTimeMillis(beginTimeMillis));
            }
            return null;
        }

        //---------------------------------------------------------------

        handle(dataList);

        //---------------------------------------------------------------
        if (log.isInfoEnabled()){
            log.info("[{}],dataList size:[{}],useTime: [{}]", taskName, dataList.size(), formatDurationUseBeginTimeMillis(beginTimeMillis));
        }
        return null;
    }

    //---------------------------------------------------------------

    /**
     * Handle.
     *
     * @param dataList
     *            the data list
     */
    protected abstract void handle(List<T> dataList);

    //---------------------------------------------------------------

    /**
     * 设置 需要被处理的数据查询器.
     *
     * @param dataListQuery
     *            the dataListQuery to set
     */
    public void setDataListQuery(DataListQuery<T> dataListQuery){
        this.dataListQuery = dataListQuery;
    }

}
