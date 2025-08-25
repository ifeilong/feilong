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
package com.feilong.context.log;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * log计步器.
 * 
 * <p>
 * ThreadLocal是用于实现线程级数据隔离的核心工具，它通过为每个线程创建独立的变量副本，解决多线程环境下的数据竞争问题
 * </p>
 * 
 * <h3>ThreadLocal的核心作用​:</h3>
 * 
 * <blockquote>
 * <ol>
 * <li>​
 * <b>线程隔离​</b>
 * <p>
 * ThreadLocal 为每个线程提供独立的变量副本，线程间无法访问彼此的副本，从而避免共享变量引发的线程安全问题（如数据竞争、状态不一致）。<br>
 * <b>​底层原理​：</b>
 * 每个线程内部维护一个 ThreadLocalMap（哈希表结构），以 ThreadLocal实例为键（弱引用），存储线程专属的值（强引用）。通过 get()/set()方法操作当前线程的 ThreadLocalMap
 * </p>
 * </li>
 * 
 * <li>​
 * <b>无锁性能优化​</b>
 * <p>
 * 由于数据天然隔离，无需通过同步（如 synchronized）保证线程安全，减少了锁竞争开销，提升并发性能
 * </p>
 * </li>
 * 
 * <li>​
 * <b>简化上下文传递​</b>
 * <p>
 * 在多层方法调用中（如 Web 请求的 Controller→Service→DAO），可避免显式传递上下文参数（如用户信息），直接通过 ThreadLocal 存取
 * </p>
 * </li>
 * 
 * </ol>
 * 
 * </blockquote>
 * 
 * ThreadLocal 的核心价值在于为每个线程提供独立数据副本，典型应用包括用户会话、数据库连接、工具类复用、日志追踪及事务管理。
 * 
 * <p>
 * 其使用必须遵循 ​​“用完即清理”​​ 原则（try-finally+ remove()），尤其在线程池场景下，否则易引发内存泄漏或数据污染。
 * 
 * 合理使用时，它是简化并发编程的利器；滥用则可能成为系统稳定性隐患
 * </p>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 2024-12-26
 */
public class LogCounterThreadLocal{

    /**
     * 计步器.
     * 
     * 
     * InheritableThreadLocal 子线程通过 get()直接访问父线程设置的值, 适用于 跨父子线程上下文传递（如链路追踪ID）.
     * 
     * <pre>
     * 
     * {
     *     &#64;code
     *     // 1. 定义 InheritableThreadLocal 实例
     *     private static final InheritableThreadLocal<String> context = new InheritableThreadLocal<>();
     * 
     *     // 2. 父线程设置值
     *     context.set("Parent Value");
     * 
     *     // 3. 创建子线程并获取值
     *     new Thread(() -> {
     *         System.out.println("子线程值: " + context.get()); // 输出 "Parent Value"
     *     }).start();
     * 
     *     // 4. 清理防止内存泄漏
     *     context.remove();
     * }
     * </pre>
     */
    private static final ThreadLocal<AtomicInteger> COUNTER_THREAD_LOCAL = new InheritableThreadLocal<>();

    //---------------------------------------------------------------
    /**
     * 开始计步器.
     */
    public static void beginCounter(){
        setCounter(new AtomicInteger(0));
    }

    /**
     * 设置计步器.
     *
     * @param counter
     *            the new counter
     */
    public static void setCounter(AtomicInteger counter){
        COUNTER_THREAD_LOCAL.set(counter);
    }

    //---------------------------------------------------------------

    /**
     * 获取计步器.
     *
     * @return the counter
     */
    public static AtomicInteger getCounter(){
        return COUNTER_THREAD_LOCAL.get();
    }

    /**
     * 删除计步器.
     */
    public static void removeCounter(){
        COUNTER_THREAD_LOCAL.remove();
    }
}