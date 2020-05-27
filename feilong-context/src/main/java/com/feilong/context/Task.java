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

/**
 * 可以被 quartz 运行的 Task.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @param <T>
 *            the generic type
 * 
 * @see java.util.concurrent.Callable
 * @see java.lang.Runnable
 * 
 * @see java.util.concurrent.Executor
 * @see java.util.TimerTask
 * 
 * @see "org.quartz.Job"
 * @see "org.quartz.StatefulJob"
 * @see "org.springframework.scheduling.TaskScheduler"
 * @see "org.springframework.util.MethodInvoker#setTargetObject(Object)"
 * @see "org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean"
 * @since 1.14.3
 */
public interface Task<T> {

    /**
     * run.
     *
     * @return the t
     */
    T run();

}
