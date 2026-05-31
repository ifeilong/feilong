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
package com.feilong.core.util.concurrent;

import static com.feilong.core.lang.StringUtil.formatPattern;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.feilong.core.Validate;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 对于ScheduledExecutorService 的封装.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 4.5.3
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ScheduledExecutorServiceUtil{

    /**
     * 启动单线程定时任务.
     * 
     * <p>
     * 适用于“伴随式”场景，例如：一个长时间运行的主任务，需要定期（如每10分钟）报告进度。
     * 执行器创建的线程为守护线程，不会阻止JVM正常退出。
     * </p>
     * <p>
     * 适合执行时间稳定且短的任务
     * </p>
     * 
     * <p>
     * <b>重要：调用者负责在适当时机关闭此执行器，以避免资源泄漏。</b>
     * 通常应在主任务完成后调用 {@link ScheduledExecutorService#shutdown()} 或 {@link ScheduledExecutorService#shutdownNow()}。
     * </p>
     * 
     * @param initialDelay
     *            初始延迟,比如 int initialDelay = 5;// 初始延迟 5 分钟
     * @param period
     *            执行周期,比如 int period = 10;// 间隔 10 分钟
     * @param timeUnit
     *            时间单位,比如 TimeUnit timeUnit = TimeUnit.MINUTES;
     * @param runnable
     * @return 已启动定时任务的 ScheduledExecutorService<br>
     * 
     *         如果 <code>initialDelay{@code <}0</code> ,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>period{@code <}0</code> ,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>timeUnit</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>runnable</code> 是null,抛出 {@link NullPointerException}<br>
     * 
     * @throws IllegalArgumentException
     *             如果 initialDelay &lt; 0 或 period &lt;= 0
     * @throws NullPointerException
     *             如果 timeUnit 或 runnable 为 null
     */
    public static ScheduledExecutorService startSingleThreadScheduledAtFixedRate(
                    int initialDelay,
                    int period,
                    TimeUnit timeUnit,
                    Runnable runnable){
        Validate.isTrue(initialDelay >= 0, "initialDelay must be >= 0, but is %s", initialDelay);
        Validate.isTrue(period > 0, "period must be > 0,but is %s", period);
        Validate.notNull(timeUnit, "timeUnit can't be null!");
        Validate.notNull(runnable, "runnable can't be null!");

        //---------------------------------------------------------------

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(r -> {
            String name = "Scheduled-progress-" + System.nanoTime();
            Thread t = new Thread(r, name);
            t.setDaemon(true); // 守护线程，任务结束自动退出
            return t;
        });

        //---------------------------------------------------------------

        // 包装runnable，捕获异常避免任务静默停止
        Runnable wrappedRunnable = () -> {
            try{
                runnable.run();
            }catch (Throwable e){
                // 使用日志记录异常，防止任务因异常而彻底停止。
                log.error(
                                formatPattern(
                                                "ScheduledProgressTaskExecutionFailed.initialDelay:[{}] period:[{}] timeUnit:[{}]",
                                                initialDelay,
                                                period,
                                                timeUnit),
                                e);
                // 可以选择重新抛出RuntimeException来取消后续调度，或者仅记录。
                // 此处选择仅记录，任务继续。如果希望异常后停止，可抛出异常。
                // throw new RuntimeException(e);
            }
        };
        //ScheduledFuture<?>
        scheduledExecutorService.scheduleAtFixedRate(wrappedRunnable, initialDelay, period, timeUnit);
        return scheduledExecutorService;
    }

}
