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

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.context.log.UseTimeLogable;
import com.feilong.lib.lang3.Validate;

/**
 * 带条件的 Task.
 * 
 * <h3>说明:</h3>
 * <blockquote>
 * <ol>
 * <li>如果没有配置 condition,那么无条件执行 {@link Task}</li>
 * <li>如果配置了 condition,但是 {@link Condition#canRun()} 为 false ,那么不会执行 {@link Task}</li>
 * <li>如果配置了 condition,且 {@link Condition#canRun()} 为 true ,那么会执行 {@link Task}</li>
 * </ol>
 * </blockquote>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @param <T>
 *            the generic type
 * @since 1.14.3
 */
public class ConditionableTask<T> implements Task<T>,UseTimeLogable{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConditionableTask.class);

    //---------------------------------------------------------------

    /** 执行条件. */
    private Condition           condition;

    /** Task. */
    private Task<T>             task;

    //---------------------------------------------------------------

    /** Post construct. */
    @PostConstruct
    protected void postConstruct(){
        Validate.notNull(task, "job can't be null!");
    }

    //---------------------------------------------------------------

    /**
     * Run.
     *
     * @return the t
     */
    @Override
    public T run(){
        //如果配置了 condition,但是 {@link condition#canRun()} 为 false ,那么不会执行 {@link Job}
        if (null != condition && !condition.canRun()){
            LOGGER.info("condition:[{}] return can't run,return null.", condition.getClass().getName());
            return null;
        }

        //---------------------------------------------------------------
        //如果没有配置 condition,那么无条件执行 {@link Job}
        String taskName = task.getClass().getName();
        if (null == condition){
            LOGGER.debug("condition is null,will execute task:[{}].", taskName);
        }else{
            LOGGER.debug("condition:[{}] can run,will execute task:[{}].", condition.getClass().getName(), taskName);
        }

        //---------------------------------------------------------------
        T result = task.run();
        LOGGER.debug("task:[{}] run result:[{}]", taskName, result);

        return result;
    }

    //--------------------------------------------------------------- 

    /**
     * 设置 执行条件.
     *
     * @param condition
     *            the condition to set
     */
    public void setCondition(Condition condition){
        this.condition = condition;
    }

    /**
     * 设置 task.
     *
     * @param task
     *            the task to set
     */
    public void setTask(Task<T> task){
        this.task = task;
    }

}
